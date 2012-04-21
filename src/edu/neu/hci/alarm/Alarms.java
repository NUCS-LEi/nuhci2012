package edu.neu.hci.alarm;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.provider.Settings;
import android.text.format.DateFormat;
import edu.neu.hci.db.DBContentProvider;
import edu.neu.hci.db.DatabaseDictionary;

public class Alarms {

	// This action triggers the AlarmReceiver as well as the AlarmKlaxon. It
	// is a public action used in the manifest for receiving Alarm broadcasts
	// from the alarm manager.
	public static final String ALARM_ALERT_ACTION = "edu.neu.hci.ALARM_ALERT";

	// A public action sent by AlarmKlaxon when the alarm has stopped sounding
	// for any reason (e.g. because it has been dismissed from
	// AlarmAlertFullScreen,
	// or killed due to an incoming phone call, etc).
	public static final String ALARM_DONE_ACTION = "edu.neu.hci.ALARM_DONE";

	// AlarmAlertFullScreen listens for this broadcast intent, so that other
	// applications
	// can snooze the alarm (after ALARM_ALERT_ACTION and before
	// ALARM_DONE_ACTION).
	public static final String ALARM_SNOOZE_ACTION = "edu.neu.hci.ALARM_SNOOZE";

	// AlarmAlertFullScreen listens for this broadcast intent, so that other
	// applications
	// can dismiss the alarm (after ALARM_ALERT_ACTION and before
	// ALARM_DONE_ACTION).
	public static final String ALARM_DISMISS_ACTION = "edu.neu.hci.ALARM_DISMISS";

	// This is a private action used by the AlarmKlaxon to update the UI to
	// show the alarm has been killed.
	public static final String ALARM_KILLED = "alarm_killed";

	// Extra in the ALARM_KILLED intent to indicate to the user how long the
	// alarm played before being killed.
	public static final String ALARM_KILLED_TIMEOUT = "alarm_killed_timeout";

	// This string is used to indicate a silent alarm in the db.
	public static final String ALARM_ALERT_SILENT = "silent";

	// This intent is sent from the notification when the user cancels the
	// snooze alert.
	public static final String CANCEL_SNOOZE = "cancel_snooze";

	// This string is used when passing an Alarm object through an intent.
	public static final String ALARM_INTENT_EXTRA = "intent.extra.alarm";

	// This extra is the raw Alarm object data. It is used in the
	// AlarmManagerService to avoid a ClassNotFoundException when filling in
	// the Intent extras.
	public static final String ALARM_RAW_DATA = "intent.extra.alarm_raw";

	// This string is used to identify the alarm id passed to SetAlarm from the
	// list of alarms.
	public static final String ALARM_ID = "alarm_id";

	final static String PREF_SNOOZE_ID = "snooze_id";
	final static String PREF_SNOOZE_TIME = "snooze_time";

	private final static String DM12 = "E h:mm aa";
	private final static String DM24 = "E k:mm";

	private final static String M12 = "h:mm aa";
	// Shared with DigitalClock
	final static String M24 = "kk:mm";
	private final static String ALARMCLOCK = "AlarmClock";

	/**
	 * Creates a new Alarm and fills in the given alarm's id.
	 */
	public static long addAlarm(Context context, Alarm alarm) {
		ContentValues values = createContentValues(alarm, alarm.id);
		Uri uri = context.getContentResolver().insert(DBContentProvider.ALARM_CONTENT_URI, values);
		long timeInMillis = calculateAlarm(alarm);
		setNextAlert(context);
		return timeInMillis;
	}

	/**
	 * Removes an existing Alarm. If this alarm is snoozing, disables snooze.
	 * Sets next alert.
	 */
	// public static void deleteAlarm(Context context, int alarmId) {
	// if (alarmId == -1)
	// return;
	//
	// ContentResolver contentResolver = context.getContentResolver();
	// /* If alarm is snoozing, lose it */
	// disableSnoozeAlert(context, alarmId);
	//
	// Uri uri = ContentUris.withAppendedId(DBContentProvider.ALARM_CONTENT_URI,
	// alarmId);
	// contentResolver.delete(uri, "", null);
	//
	// setNextAlert(context);
	// }
	public static void deleteAlarm(Context context, int alarmId) {
		DBContentProvider.execSQL(context, "delete from " + DatabaseDictionary.ALARM_TABLE + " where ID=" + alarmId);
		// setNextAlert(context);
	}

	/**
	 * Queries all alarms
	 * 
	 * @return cursor over all alarms
	 */
	public static Cursor getAlarmsCursor(ContentResolver contentResolver) {
		return contentResolver.query(DBContentProvider.ALARM_CONTENT_URI, DatabaseDictionary.getTableCols().get(DatabaseDictionary.ALARM_TABLE),
				null, null, null);
	}

	public static Cursor getAlarmsCursor(Context c) {
		Cursor cu = DBContentProvider.rawQuery(c, "select * from alarm");
		return DBContentProvider.rawQuery(c, "select * from alarm");
	}

	// Private method to get a more limited set of alarms from the database.
	private static Cursor getFilteredAlarmsCursor(ContentResolver contentResolver) {
		// return contentResolver.query(DBContentProvider.ALARM_CONTENT_URI,
		// Alarm.Columns.ALARM_QUERY_COLUMNS, Alarm.Columns.WHERE_ENABLED, null,
		// null);
		return null;
	}

	private static ContentValues createContentValues(Alarm alarm, int id) {
		ContentValues values = new ContentValues(8);
		// Set the alarm_time value if this alarm does not repeat. This will be
		// used later to disable expire alarms.
		long time = 0;
		time = calculateAlarm(alarm);
		String[] columns = DatabaseDictionary.getTableCols().get(DatabaseDictionary.ALARM_TABLE);
		values.put(columns[0], id);
		values.put(columns[4], alarm.enabled ? 1 : 0);
		values.put(columns[1], alarm.hour);
		values.put(columns[2], alarm.minutes);
		values.put(columns[3], alarm.time);
		values.put(columns[5], alarm.vibrate);
		// A null alert Uri indicates a silent alarm.
		values.put(columns[6], alarm.alert == null ? ALARM_ALERT_SILENT : alarm.alert.toString());

		return values;
	}

	private static void clearSnoozeIfNeeded(Context context, long alarmTime) {
		// If this alarm fires before the next snooze, clear the snooze to
		// enable this alarm.
		SharedPreferences prefs = context.getSharedPreferences(ALARMCLOCK, 0);
		long snoozeTime = prefs.getLong(PREF_SNOOZE_TIME, 0);
		if (alarmTime < snoozeTime) {
			clearSnoozePreference(context, prefs);
		}
	}

	/**
	 * Return an Alarm object representing the alarm id in the database. Returns
	 * null if no alarm exists.
	 */
	public static Alarm getAlarm(ContentResolver contentResolver, int alarmId) {
		Cursor cursor = contentResolver.query(ContentUris.withAppendedId(DBContentProvider.ALARM_CONTENT_URI, alarmId), DatabaseDictionary
				.getTableCols().get(DatabaseDictionary.ALARM_TABLE), null, null, null);
		Alarm alarm = null;
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				alarm = new Alarm(cursor);
			}
			cursor.close();
		}
		return alarm;
	}

	/**
	 * A convenience method to set an alarm in the Alarms content provider.
	 * 
	 * @return Time when the alarm will fire.
	 */
	public static long setAlarm(Context context, Alarm alarm) {
		ContentValues values = createContentValues(alarm, 0);
		ContentResolver resolver = context.getContentResolver();
		resolver.update(ContentUris.withAppendedId(DBContentProvider.ALARM_CONTENT_URI, alarm.id), values, null, null);

		long timeInMillis = calculateAlarm(alarm);

		if (alarm.enabled) {
			// Disable the snooze if we just changed the snoozed alarm. This
			// only does work if the snoozed alarm is the same as the given
			// alarm.
			// TODO: disableSnoozeAlert should have a better name.
			disableSnoozeAlert(context, alarm.id);

			// Disable the snooze if this alarm fires before the snoozed alarm.
			// This works on every alarm since the user most likely intends to
			// have the modified alarm fire next.
			clearSnoozeIfNeeded(context, timeInMillis);
		}

		setNextAlert(context);

		return timeInMillis;
	}

	/**
	 * A convenience method to enable or disable an alarm.
	 * 
	 * @param id
	 *            corresponds to the _id column
	 * @param enabled
	 *            corresponds to the ENABLED column
	 */

	public static void enableAlarm(final Context context, final int id, boolean enabled) {
		enableAlarmInternal(context, id, enabled);
		setNextAlert(context);
	}

	private static void enableAlarmInternal(final Context context, final int id, boolean enabled) {
		enableAlarmInternal(context, getAlarm(context.getContentResolver(), id), enabled);
	}

	private static void enableAlarmInternal(final Context context, final Alarm alarm, boolean enabled) {
		if (alarm == null) {
			return;
		}
		ContentResolver resolver = context.getContentResolver();

		ContentValues values = new ContentValues(2);
		String[] columns = DatabaseDictionary.getTableCols().get(DatabaseDictionary.ALARM_TABLE);
		values.put(columns[5], enabled ? 1 : 0);

		// If we are enabling the alarm, calculate alarm time since the time
		// value in Alarm may be old.
		if (enabled) {
			long time = 0;
			time = calculateAlarm(alarm);
			values.put(columns[4], time);
		} else {
			disableSnoozeAlert(context, alarm.id);
		}

		resolver.update(ContentUris.withAppendedId(DBContentProvider.ALARM_CONTENT_URI, alarm.id), values, null, null);
	}

	public static Alarm calculateNextAlert(final Context context) {
		Alarm alarm = null;
		long minTime = Long.MAX_VALUE;
		long now = System.currentTimeMillis();
		Cursor cursor = getAlarmsCursor(context);
		// Cursor cursor =
		// getFilteredAlarmsCursor(context.getContentResolver());
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				do {
					Alarm a = new Alarm(cursor);
					a.time = calculateAlarm(a);
					if (a.time < minTime) {
						minTime = a.time;
						alarm = a;
					}
				} while (cursor.moveToNext());
			}
			cursor.close();
		}
		return alarm;
	}

	/**
	 * Disables non-repeating alarms that have passed. Called at boot.
	 */
	public static void disableExpiredAlarms(final Context context) {
		Cursor cur = getFilteredAlarmsCursor(context.getContentResolver());
		long now = System.currentTimeMillis();

		if (cur.moveToFirst()) {
			do {
				Alarm alarm = new Alarm(cur);
				// A time of 0 means this alarm repeats. If the time is
				// non-zero, check if the time is before now.
				if (alarm.time != 0 && alarm.time < now) {
					enableAlarmInternal(context, alarm, false);
				}
			} while (cur.moveToNext());
		}
		cur.close();
	}

	/**
	 * Called at system startup, on time/timezone change, and whenever the user
	 * changes alarm settings. Activates snooze if set, otherwise loads all
	 * alarms, activates next alert.
	 */
	public static void setNextAlert(final Context context) {
		if (!enableSnoozeAlert(context)) {
			Alarm alarm = calculateNextAlert(context);
			if (alarm != null) {
				enableAlert(context, alarm, alarm.time);
			} else {
				disableAlert(context);
			}
		}
	}

	/**
	 * Sets alert in AlarmManger and StatusBar. This is what will actually
	 * launch the alert when the alarm triggers.
	 * 
	 * @param alarm
	 *            Alarm.
	 * @param atTimeInMillis
	 *            milliseconds since epoch
	 */
	private static void enableAlert(Context context, final Alarm alarm, final long atTimeInMillis) {
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(ALARM_ALERT_ACTION);
		Parcel out = Parcel.obtain();
		alarm.writeToParcel(out, 0);
		out.setDataPosition(0);
		intent.putExtra(ALARM_RAW_DATA, out.marshall());

		PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		am.set(AlarmManager.RTC_WAKEUP, atTimeInMillis, sender);
		setStatusBarIcon(context, true);
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(atTimeInMillis);
		String timeString = formatDayAndTime(context, c);
		saveNextAlarm(context, timeString);
	}

	/**
	 * Disables alert in AlarmManger and StatusBar.
	 * 
	 * @param id
	 *            Alarm ID.
	 */
	static void disableAlert(Context context) {
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		PendingIntent sender = PendingIntent.getBroadcast(context, 0, new Intent(ALARM_ALERT_ACTION), PendingIntent.FLAG_CANCEL_CURRENT);
		am.cancel(sender);
		setStatusBarIcon(context, false);
		saveNextAlarm(context, "");
	}

	public static void saveSnoozeAlert(final Context context, final int id, final long time) {
		SharedPreferences prefs = context.getSharedPreferences(ALARMCLOCK, 0);
		if (id == -1) {
			clearSnoozePreference(context, prefs);
		} else {
			SharedPreferences.Editor ed = prefs.edit();
			ed.putInt(PREF_SNOOZE_ID, id);
			ed.putLong(PREF_SNOOZE_TIME, time);
			ed.apply();
		}
		// Set the next alert after updating the snooze.
		setNextAlert(context);
	}

	/**
	 * Disable the snooze alert if the given id matches the snooze id.
	 */
	public static void disableSnoozeAlert(final Context context, final int id) {
		SharedPreferences prefs = context.getSharedPreferences(ALARMCLOCK, 0);
		int snoozeId = prefs.getInt(PREF_SNOOZE_ID, -1);
		if (snoozeId == -1) {
			// No snooze set, do nothing.
			return;
		} else if (snoozeId == id) {
			// This is the same id so clear the shared prefs.
			clearSnoozePreference(context, prefs);
		}
	}

	// Helper to remove the snooze preference. Do not use clear because that
	// will erase the clock preferences. Also clear the snooze notification in
	// the window shade.
	private static void clearSnoozePreference(final Context context, final SharedPreferences prefs) {
		final int alarmId = prefs.getInt(PREF_SNOOZE_ID, -1);
		if (alarmId != -1) {
			NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			nm.cancel(alarmId);
		}

		final SharedPreferences.Editor ed = prefs.edit();
		ed.remove(PREF_SNOOZE_ID);
		ed.remove(PREF_SNOOZE_TIME);
		ed.apply();
	};

	/**
	 * If there is a snooze set, enable it in AlarmManager
	 * 
	 * @return true if snooze is set
	 */
	private static boolean enableSnoozeAlert(final Context context) {
		SharedPreferences prefs = context.getSharedPreferences(ALARMCLOCK, 0);

		int id = prefs.getInt(PREF_SNOOZE_ID, -1);
		if (id == -1) {
			return false;
		}
		long time = prefs.getLong(PREF_SNOOZE_TIME, -1);

		// Get the alarm from the db.
		final Alarm alarm = getAlarm(context.getContentResolver(), id);
		if (alarm == null) {
			return false;
		}
		// The time in the database is either 0 (repeating) or a specific time
		// for a non-repeating alarm. Update this value so the AlarmReceiver
		// has the right time to compare.
		alarm.time = time;
		enableAlert(context, alarm, time);
		return true;
	}

	/**
	 * Tells the StatusBar whether the alarm is enabled or disabled
	 */
	private static void setStatusBarIcon(Context context, boolean enabled) {
		Intent alarmChanged = new Intent("android.intent.action.ALARM_CHANGED");
		alarmChanged.putExtra("alarmSet", enabled);
		context.sendBroadcast(alarmChanged);
	}

	private static long calculateAlarm(Alarm alarm) {
		return calculateAlarm(alarm.hour, alarm.minutes).getTimeInMillis();
	}

	/**
	 * Given an alarm in hours and minutes, return a time suitable for setting
	 * in AlarmManager.
	 */
	static Calendar calculateAlarm(int hour, int minute) {

		// start with now
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());

		int nowHour = c.get(Calendar.HOUR_OF_DAY);
		int nowMinute = c.get(Calendar.MINUTE);

		// if alarm is behind current time, advance one day
		if (hour < nowHour || hour == nowHour && minute <= nowMinute) {
			c.add(Calendar.DAY_OF_YEAR, 1);
		}
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, minute);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		return c;
	}

	static String formatTime(final Context context, int hour, int minute) {
		Calendar c = calculateAlarm(hour, minute);
		return formatTime(context, c);
	}

	/* used by AlarmAlert */
	static String formatTime(final Context context, Calendar c) {
		String format = get24HourMode(context) ? M24 : M12;
		return (c == null) ? "" : (String) DateFormat.format(format, c);
	}

	/**
	 * Shows day and time -- used for lock screen
	 */
	private static String formatDayAndTime(final Context context, Calendar c) {
		String format = get24HourMode(context) ? DM24 : DM12;
		return (c == null) ? "" : (String) DateFormat.format(format, c);
	}

	/**
	 * Save time of the next alarm, as a formatted string, into the system
	 * settings so those who care can make use of it.
	 */
	static void saveNextAlarm(final Context context, String timeString) {
		Settings.System.putString(context.getContentResolver(), Settings.System.NEXT_ALARM_FORMATTED, timeString);
	}

	/**
	 * @return true if clock is set to 24-hour mode
	 */
	static boolean get24HourMode(final Context context) {
		return android.text.format.DateFormat.is24HourFormat(context);
	}
}
