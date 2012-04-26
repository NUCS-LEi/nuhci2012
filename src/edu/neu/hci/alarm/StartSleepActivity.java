package edu.neu.hci.alarm;

import java.text.ParseException;
import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;
import android.widget.Toast;

import common.wheel.widget.NumericWheelAdapter;
import common.wheel.widget.OnWheelChangedListener;
import common.wheel.widget.OnWheelScrollListener;
import common.wheel.widget.WheelAdapter;
import common.wheel.widget.WheelView;

import edu.mit.android.wocketsver1.ActivityMonitor.BluetoothSensorService;
import edu.mit.android.wocketsver1.ActivityMonitor.DataStore;
import edu.mit.android.wocketsver1.ActivityMonitor.Defines;
import edu.neu.hci.Global;
import edu.neu.hci.R;
import edu.neu.hci.db.DBAccessHelper;
import edu.neu.hci.questionaire.QuestionnaireFeedbackActivity;

public class StartSleepActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {
	private String clockTime;
	private Button done;
	private Button back;
	private AlarmPreference mAlarmPref;
	static final int TIMER_PERIOD = 60;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.start_sleep_layout);
		addPreferencesFromResource(R.xml.alarm_prefs);
		done = (Button) findViewById(R.id.startBtn);
		back = (Button) findViewById(R.id.backBtn);
		mAlarmPref = (AlarmPreference) findPreference("alarm");
		Alarm alarm = Alarms.getAlarm(getContentResolver(), 0);
		if (alarm != null && alarm.alert != null)
			mAlarmPref.setAlert(alarm.alert);
		else
			mAlarmPref.setAlert(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
		mAlarmPref.setOnPreferenceChangeListener(this);
		setClock();
	}

	public void onResume() {
		super.onResume();
		
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				
				i.setClass(StartSleepActivity.this, QuestionnaireFeedbackActivity.class);
				startActivity(i);
			}
		});
		
		
		done.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String wakeUpTime = Global.normalDateFormat.format(new Date()) + " " + clockTime;
				Date d;
				try {
					d = Global.lastModDateFormat.parse(wakeUpTime);
					if (d.getTime() < System.currentTimeMillis())
						d.setTime(d.getTime() + 24 * 3600 * 1000);
					DBAccessHelper.insertOrUpdateSleepTime(getApplicationContext(), Global.lastModDateFormat.format(new Date()),
							Global.lastModDateFormat.format(d));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				Toast toast;
				try {
					toast = Toast.makeText(getApplicationContext(),
							Global.apmDateFormat.format(Global.normalTimeFormat.parse(clockTime)), Toast.LENGTH_LONG);
					toast.show();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				saveAlarm();
				startSensor();
				Date date=new Date();
				int time=date.getHours()*60+date.getMinutes();
				DBAccessHelper.insertOrUpdateQuestion(getApplicationContext(), Global.GO_TO_BED_TIME, time);
				Intent i = new Intent();
				// Set navigation, first parameter is source, second is target.
				i.setClass(StartSleepActivity.this, DuringSleepActivity.class);
				startActivity(i);
			}
		});
		updateStatus();
	}


	private void setClock() {
		try {
			Date clock = Global.lastModDateFormat.parse(DBAccessHelper.getLastSleepTime(getApplicationContext()));
			int hour = clock.getHours();
			int min = clock.getMinutes();
			initMinWheel(R.id.set_minute, min);
			if (hour == 0) {
				initHourWheel(R.id.set_hour, 11);
				initAMPMWheel(R.id.set_am_pm, 0);
			} else if (hour < 12) {
				initHourWheel(R.id.set_hour, hour - 1);
				initAMPMWheel(R.id.set_am_pm, 0);
			} else if (hour == 12) {
				initHourWheel(R.id.set_hour, 11);
				initAMPMWheel(R.id.set_am_pm, 1);
			} else {
				initHourWheel(R.id.set_hour, hour - 13);
				initAMPMWheel(R.id.set_am_pm, 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			initHourWheel(R.id.set_hour, 0);
			initMinWheel(R.id.set_minute, 0);
			initAMPMWheel(R.id.set_am_pm, 0);
		}

	}

	// Wheel scrolled flag
	private boolean wheelScrolled = false;

	// Wheel scrolled listener
	OnWheelScrollListener scrolledListener = new OnWheelScrollListener() {
		public void onScrollingStarted(WheelView wheel) {
			wheelScrolled = true;
		}

		public void onScrollingFinished(WheelView wheel) {
			wheelScrolled = false;
			updateStatus();
		}
	};

	// Wheel changed listener
	private OnWheelChangedListener changedListener = new OnWheelChangedListener() {
		public void onChanged(WheelView wheel, int oldValue, int newValue) {
			if (!wheelScrolled) {
				updateStatus();
			}
		}
	};

	/**
	 * Updates entered PIN status
	 */
	private void updateStatus() {
		String[] s = getAllCode();
		if (s[2].equals("AM") && s[0].equals("12"))
			s[0] = "00";
		if (s[2].equals("PM") && !s[0].equals("12")) {
			s[0] = String.valueOf(Integer.parseInt(s[0]) + 12);
		}
		clockTime = String.format("%s:%s:%s", s[0], s[1], "00");
	}

	/**
	 * Initializes wheel
	 * 
	 * @param id
	 *            the wheel widget Id
	 */

	private void initHourWheel(int id, int hour) {
		WheelView wheel = getWheel(id);
		wheel.setAdapter(new NumericWheelAdapter(01, 12, "%02d"));
		wheel.setCurrentItem(hour);

		wheel.addChangingListener(changedListener);
		wheel.addScrollingListener(scrolledListener);
		wheel.setCyclic(true);
		wheel.setInterpolator(new AnticipateOvershootInterpolator());
	}

	private void initMinWheel(int id, int min) {
		WheelView wheel = getWheel(id);
		wheel.setAdapter(new NumericWheelAdapter(00, 59, "%02d"));
		wheel.setCurrentItem(min);

		wheel.addChangingListener(changedListener);
		wheel.addScrollingListener(scrolledListener);
		wheel.setCyclic(true);
		wheel.setInterpolator(new AnticipateOvershootInterpolator());
	}

	private void initAMPMWheel(int id, int ap) {
		WheelView wheel = getWheel(id);
		wheel.setAdapter(new WheelAdapter() {

			@Override
			public int getMaximumLength() {
				// TODO Auto-generated method stub
				return "AM".length() + 1;
			}

			@Override
			public int getItemsCount() {
				// TODO Auto-generated method stub
				return 2;
			}

			@Override
			public String getItem(int index) {
				// TODO Auto-generated method stub
				if (index == 0)
					return "AM";
				if (index == 1)
					return "PM";
				else
					return "";
			}
		});
		wheel.setCurrentItem(ap);

		wheel.addChangingListener(changedListener);
		wheel.addScrollingListener(scrolledListener);
		wheel.setCyclic(false);
		wheel.setInterpolator(new AnticipateOvershootInterpolator());
	}

	/**
	 * Returns wheel by Id
	 * 
	 * @param id
	 *            the wheel Id
	 * @return the wheel with passed Id
	 */
	private WheelView getWheel(int id) {
		return (WheelView) findViewById(id);
	}

	/**
	 * Tests wheel value
	 * 
	 * @param id
	 *            the wheel Id
	 * @param value
	 *            the value to test
	 * @return true if wheel value is equal to passed value
	 */
	private boolean testWheelValue(int id, int value) {

		return getWheel(id).getCurrentItem() == value;
	}

	/**
	 * Mixes wheel
	 * 
	 * @param id
	 *            the wheel id
	 */
	private void mixWheel(int id) {
		WheelView wheel = getWheel(id);
		wheel.scroll(-25 + (int) (Math.random() * 50), 2000);
	}

	private String[] getAllCode() {
		String[] s = new String[] { getWheel(R.id.set_hour).getCurrentItem() + 1 + "", getWheel(R.id.set_minute).getCurrentItem() + "",
				getWheel(R.id.set_am_pm).getCurrentItem() + "" };
		if (getWheel(R.id.set_hour).getCurrentItem() + 1 < 10)
			s[0] = "0" + s[0];
		if (getWheel(R.id.set_minute).getCurrentItem() < 10)
			s[1] = "0" + s[1];
		if (getWheel(R.id.set_am_pm).getCurrentItem() == 0)
			s[2] = "AM";
		else
			s[2] = "PM";
		return s;
	}

	// Used to post runnables asynchronously.
	private static final Handler sHandler = new Handler();

	public boolean onPreferenceChange(final Preference p, Object newValue) {
		// Asynchronously save the alarm since this method is called _before_
		// the value of the preference has changed.
		android.util.Log.i(Global.TAG, "==onPref called");
		sHandler.post(new Runnable() {
			public void run() {
				saveAlarm();
			}
		});
		return true;
	}

	private int saveAlarm() {
		Alarms.deleteAlarm(getApplicationContext(), 0);
		Alarm alarm = new Alarm();
		alarm.id = 0;
		alarm.enabled = true;
		try {
			alarm.hour = Global.normalTimeFormat.parse(clockTime).getHours();
			alarm.minutes = Global.normalTimeFormat.parse(clockTime).getMinutes();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		alarm.vibrate = true;
		alarm.alert = mAlarmPref.getAlert();
		Alarms.addAlarm(getApplicationContext(), alarm);
		return 0;
	}

	private void startSensor() {
		SharedPreferences pref = getSharedPreferences(Defines.SHARED_PREF_NAME, Context.MODE_PRIVATE);
		if(pref.getInt(Defines.SHARED_PREF_NUM_SENSORS,0)==0){
			Editor edit = getSharedPreferences(Defines.SHARED_PREF_NAME, Context.MODE_PRIVATE).edit();
			edit.putInt(Defines.SHARED_PREF_NUM_SENSORS, 1);
			edit.putString(Defines.SHARED_PREF_SENSOR + "0", Global.DEFAULT_WOCKET_NAME);
			edit.commit();
		}
		if (Global.mAlarmSender == null) {
			Global.mAlarmSender = PendingIntent.getService(this, 0, new Intent(this, BluetoothSensorService.class), 0);
		}
		DataStore.setRunning(true);

		// We want the alarm to go off 60 seconds from now.
		long firstTime = SystemClock.elapsedRealtime();

		// Schedule the alarm!
		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, TIMER_PERIOD * 1000, Global.mAlarmSender);
	}
}
