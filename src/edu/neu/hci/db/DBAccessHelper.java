package edu.neu.hci.db;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.telephony.TelephonyManager;
import edu.mit.android.wocketsver1.ActivityMonitor.AccelPoint;
import edu.mit.android.wocketsver1.ActivityMonitor.SummaryPoint;
import edu.neu.hci.Global;
import edu.neu.hci.questionaire.ActivityQuestionActivity;
import edu.neu.hci.questionaire.AlcoholQuestionActivity;
import edu.neu.hci.questionaire.CaffeineQuestionActivity;
import edu.neu.hci.questionaire.FoodQuestionActivity;
import edu.neu.hci.questionaire.QuestionnaireFeedbackActivity;
import edu.neu.hci.questionaire.SmokeQuestionActivity;
import edu.neu.hci.questionaire.StressQuestionActivity;

public class DBAccessHelper {
	public static int insertOrUpdateQuestionSetting(Context c, Boolean[] values) {
		if (DatabaseDictionary.getTableCols().get("question_setting").length != values.length + 1)
			return -1;
		ContentValues cv = new ContentValues();
		for (int i = 0; i < values.length; i++) {
			cv.put(DatabaseDictionary.getTableCols().get("question_setting")[i + 1], values[i].toString());
		}
		TelephonyManager tm = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
		Cursor cursor = DBContentProvider.rawQuery(c,
				"select * from " + DatabaseDictionary.QUESTION_SETTING_TABLE + " where PhoneID='" + tm.getDeviceId() + "';");
		if (cursor.getCount() == 0) {
			cv.put("PhoneID", tm.getDeviceId());
			c.getContentResolver().insert(DBContentProvider.QUESTION_SETTING_CONTENT_URI, cv);
		} else
			c.getContentResolver().update(DBContentProvider.QUESTION_SETTING_CONTENT_URI, cv, "PhoneID=?", new String[] { tm.getDeviceId() });
		return 1;
	}

	public static Boolean[] getQuestionSetting(Context c) {
		Cursor cursor = DBContentProvider.rawQuery(c, "select * from " + DatabaseDictionary.QUESTION_SETTING_TABLE);
		if (cursor.getCount() == 0)
			return null;
		else {
			cursor.moveToFirst();
			Boolean s[] = new Boolean[] { Boolean.parseBoolean(cursor.getString(1)), Boolean.parseBoolean(cursor.getString(2)),
					Boolean.parseBoolean(cursor.getString(3)), Boolean.parseBoolean(cursor.getString(4)), Boolean.parseBoolean(cursor.getString(5)),
					Boolean.parseBoolean(cursor.getString(6)) };
			return s;
		}
	}

	public static List<String> questionList(Context c) {
		List<String> l = new ArrayList<String>();
		Boolean[] b = getQuestionSetting(c);
		if (b != null) {
			if (b[0] == true)
				l.add(CaffeineQuestionActivity.class.getName());
			if (b[1] == true)
				l.add(AlcoholQuestionActivity.class.getName());
			if (b[2] == true)
				l.add(SmokeQuestionActivity.class.getName());
			if (b[3] == true)
				l.add(FoodQuestionActivity.class.getName());
			if (b[4] == true)
				l.add(ActivityQuestionActivity.class.getName());
			if (b[5] == true)
				l.add(StressQuestionActivity.class.getName());
			l.add(QuestionnaireFeedbackActivity.class.getName());
			return l;
		} else
			return null;
	}

	public static int logUsage(Context c, String activity) {
		String time = Global.exactDateFormat.format(new Date());
		ContentValues cv = new ContentValues();
		cv.put("TimeStamp", time);
		cv.put("Activity", activity);
		c.getContentResolver().insert(DBContentProvider.USAGE_LOG_CONTENT_URI, cv);
		return 1;
	}

	public static String[] getLastUsage(Context c) {
		Cursor cursor = DBContentProvider.rawQuery(c, "select * from " + DatabaseDictionary.USAGE_LOG_TABLE + " order by TimeStamp desc limit 1;");
		if (cursor.getCount() == 0)
			return null;
		else {
			cursor.moveToFirst();
			String s[] = new String[] { cursor.getString(0), cursor.getString(1) };
			return s;
		}
	}

	public static String getLastUsage(Context c, String activity) {
		Cursor cursor = DBContentProvider.rawQuery(c, "select * from " + DatabaseDictionary.USAGE_LOG_TABLE + " where Activity='" + activity
				+ "' order by TimeStamp desc limit 1;");
		if (cursor.getCount() == 0)
			return null;
		else {
			cursor.moveToFirst();
			return cursor.getString(0);
		}
	}

	public static int insertOrUpdateQuestion(Context c, String type, int value) {
		ContentValues cv = new ContentValues();
		TelephonyManager tm = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
		Date date = new Date();
		if (date.getHours() < 12)
			date.setTime(date.getTime() - 12 * 3600 * 1000);
		String createDate = Global.normalDateFormat.format(date);
		String lastModDate = Global.lastModDateFormat.format(new Date());
		cv.put("PhoneID", tm.getDeviceId());
		cv.put("QuestionValue", value);
		cv.put("LastModDate", lastModDate);
		Cursor cursor = DBContentProvider.rawQuery(c, "select * from " + DatabaseDictionary.QUESTION_TABLE + " where CreateDate='" + createDate
				+ "' and QuestionType='" + type + "';");
		if (cursor.getCount() == 0) {
			cv.put("CreateDate", createDate);
			cv.put("QuestionType", type);
			c.getContentResolver().insert(DBContentProvider.QUESTION_CONTENT_URI, cv);
		} else
			c.getContentResolver().update(DBContentProvider.QUESTION_CONTENT_URI, cv, "CreateDate=? and QuestionType=?",
					new String[] { createDate, type });
		return 1;
	}

	public static int getQuestion(Context c, String type) {
		Date date = new Date();
		if (date.getHours() < 12)
			date.setTime(date.getTime() - 12 * 3600 * 1000);
		String createDate = Global.normalDateFormat.format(date);
		Cursor cursor = DBContentProvider.rawQuery(c, "select QuestionValue from " + DatabaseDictionary.QUESTION_TABLE + " where CreateDate='"
				+ createDate + "' and QuestionType='" + type + "';");
		if (cursor.getCount() == 0)
			return -1;
		else {
			cursor.moveToFirst();
			return cursor.getInt(0);
		}
	}

	public static int insertOrUpdateSleepTime(Context c, String goToBedTime, String wakeUpTime) {
		ContentValues cv = new ContentValues();
		Date date = new Date();
		if (date.getHours() < 12)
			date.setTime(date.getTime() - 12 * 3600 * 1000);
		String createDate = Global.normalDateFormat.format(date);
		String lastModDate = Global.lastModDateFormat.format(new Date());
		cv.put("GoToBedTime", goToBedTime);
		cv.put("WakeUpTime", wakeUpTime);
		cv.put("LastModDate", lastModDate);
		String duration = "";
		try {
			duration = String.valueOf((Global.lastModDateFormat.parse(wakeUpTime).getTime() - Global.lastModDateFormat.parse(goToBedTime).getTime())
					/ (60 * 1000));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		cv.put("SleepDuration", duration);
		Cursor cursor = DBContentProvider.rawQuery(c, "select * from " + DatabaseDictionary.SLEEP_TIME_TABLE + " where CreateDate='" + createDate
				+ "';");
		if (cursor.getCount() == 0) {
			cv.put("CreateDate", createDate);
			c.getContentResolver().insert(DBContentProvider.SLEEP_TIME_CONTENT_URI, cv);
		} else {
			// int r =
			// c.getContentResolver().update(DBContentProvider.SLEEP_TIME_CONTENT_URI,
			// cv, "CreateDate=?", new String[] { createDate });
			DBContentProvider.execSQL(c, "update " + DatabaseDictionary.SLEEP_TIME_TABLE + " set GotoBedTime='" + goToBedTime + "',WakeUpTime='"
					+ wakeUpTime + "',SleepDuration='',LastModDate='" + lastModDate + "' where CreateDate='" + createDate + "'");
		}
		return 1;
	}

	public static String getLastSleepTime(Context c) {
		Cursor cursor = DBContentProvider.rawQuery(c, "select * from " + DatabaseDictionary.SLEEP_TIME_TABLE + " order by CreateDate desc limit 1;");
		if (cursor.getCount() == 0)
			return null;
		else {
			cursor.moveToFirst();
			return cursor.getString(2);
		}
	}

	public static int insertSummaryPoint(Context c, SummaryPoint point) {
		ContentValues cv = new ContentValues();
		if (point == null)
			return 0;
		Date date = new Date();
		date.setTime(point.mWocketRecordedTime.toMillis(true));
		cv.put("WocketRecordedTime", Global.exactDateFormat.format(date));
		date.setTime(point.mPhoneReadTime.toMillis(true));
		cv.put("PhoneReadTime", Global.exactDateFormat.format(date));
		cv.put("Written", point.mWritten);
		cv.put("SeqNum", point.mSeqNum);
		cv.put("Value", point.mValue);
		c.getContentResolver().insert(DBContentProvider.SUMMARY_POINT_URI, cv);
		return 1;
	}

	public static int insertAccelPoint(Context c, AccelPoint point) {
		ContentValues cv = new ContentValues();
		if (point == null)
			return 0;
		Date date = new Date();
		date.setTime(point.mWocketRecordedTime.toMillis(true));
		cv.put("WocketRecordedTime", Global.exactDateFormat.format(date));
		date.setTime(point.mPhoneReadTime.toMillis(true));
		cv.put("PhoneReadTime", Global.exactDateFormat.format(date));
		cv.put("X", point.mX);
		cv.put("Y", point.mY);
		cv.put("Z", point.mZ);
		cv.put("RawData", point.mRawData);
		cv.put("Compressed", point.mCompressed);
		c.getContentResolver().insert(DBContentProvider.ACCEL_POINT_URI, cv);
		return 1;
	}

	public static ArrayList<double[]> getSummaryPoints(Context c) {
		ArrayList<double[]> al = new ArrayList<double[]>();
		Cursor cursor = DBContentProvider.rawQuery(c, "select * from " + DatabaseDictionary.SUMMARY_POINT_TABLE);
		if (cursor.getCount() == 0)
			return null;
		else {
			double[] time = new double[cursor.getCount()];
			double[] value = new double[cursor.getCount()];
			double adjustValue = 0;
			int count = 0;
			int[] count_each = { 0, 0, 0 };
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				// if (count < Global.GROUP_MINUTES) {
				// time[cursor.getPosition()] = cursor.getPosition();
				// if (cursor.getDouble(4) > Global.WAKE_THRESHOLD)
				// count_each[0]++;
				// else if (cursor.getDouble(4) > Global.LIGHT_SLEEP_THRESHOLD)
				// count_each[1]++;
				// else
				// count_each[2]++;
				// } else {
				// android.util.Log.i(Global.TAG, "At " + cursor.getPosition() +
				// " count_each[0]=" + count_each[0] + " count_each[1]="
				// + count_each[1] + " count_each[2]=" + count_each[2]);
				// if (count_each[0] > count_each[1] && count_each[0] >
				// count_each[2])
				// adjustValue = Global.WAKE_THRESHOLD;
				// else if (count_each[1] > count_each[0] && count_each[1] >
				// count_each[2])
				// adjustValue = Global.LIGHT_SLEEP_THRESHOLD;
				// else
				// adjustValue = Global.DEEP_SLEEP;
				// for (int i = cursor.getPosition() - Global.GROUP_MINUTES; i <
				// Global.GROUP_MINUTES; i++) {
				// value[i] = adjustValue;
				// }
				// count = 0;
				// count_each[0] = 0;
				// count_each[1] = 0;
				// count_each[2] = 0;
				// }
				time[cursor.getPosition()] = cursor.getPosition();
				if (cursor.getDouble(4) > Global.WAKE_THRESHOLD)
					adjustValue = Global.WAKE_THRESHOLD;
				else if (cursor.getDouble(4) > Global.LIGHT_SLEEP_THRESHOLD)
					adjustValue = Global.LIGHT_SLEEP_THRESHOLD;
				else
					adjustValue = Global.DEEP_SLEEP;
				value[cursor.getPosition()] = adjustValue;
				cursor.moveToNext();
			}
			al.add(time);
			al.add(value);
			return al;
		}
	}

	public static int getLastNightStatic(Context c, String modularName) {
		Date date = new Date();
		date.setTime(date.getTime() - 24 * 60 * 60 * 1000);
		if (modularName.equals(Global.GO_TO_BED_TIME)) {
			String rawQuery = String.format("select GoToBedTime from sleep_time where CreateDate='%s';", Global.normalDateFormat.format(date));
			Cursor cur = DBContentProvider.rawQuery(c, rawQuery);
			if (cur.getCount() < 1)
				return -1;
			cur.moveToFirst();
			Date d = null;
			try {
				d = Global.lastModDateFormat.parse(cur.getString(0));
			} catch (ParseException e) {
				e.printStackTrace();
				return -1;
			}
			return d.getHours() * 60 + d.getMinutes();
		} else if (modularName.equals(Global.WAKE_UP_TIME)) {
			String rawQuery = String.format("select WakeUpTime from sleep_time where CreateDate='%s';", Global.normalDateFormat.format(date));
			Cursor cur = DBContentProvider.rawQuery(c, rawQuery);
			if (cur.getCount() < 1)
				return -1;
			cur.moveToFirst();
			Date d = null;
			try {
				d = Global.lastModDateFormat.parse(cur.getString(0));
			} catch (ParseException e) {
				e.printStackTrace();
				return -1;
			}
			return d.getHours() * 60 + d.getMinutes();
		} else if (modularName.equals(Global.SLEEP_DURATION)) {
			String rawQuery = String.format("select SleepDuration from sleep_time where CreateDate='%s';", Global.normalDateFormat.format(date));
			Cursor cur = DBContentProvider.rawQuery(c, rawQuery);
			if (cur.getCount() < 1)
				return -1;
			cur.moveToFirst();
			return cur.getInt(0);
		} else {
			String rawQuery = String.format("select QuestionValue from question where CreateDate='%s' and QuestionType='%s';",
					Global.normalDateFormat.format(date), modularName);
			Cursor cur = DBContentProvider.rawQuery(c, rawQuery);
			if (cur.getCount() < 1)
				return -1;
			cur.moveToFirst();
			return cur.getInt(0);
		}
	}

	public static float getAverageStatic(Context c, String modularName) {
		if (modularName.equals(Global.GO_TO_BED_TIME)) {
			String rawQuery = String.format("select GoToBedTime from sleep_time;");
			Cursor cur = DBContentProvider.rawQuery(c, rawQuery);
			if (cur.getCount() < 1)
				return -1;
			cur.moveToFirst();
			Date d = null;
			int total = 0;
			while (!cur.isAfterLast()) {
				try {
					d = Global.lastModDateFormat.parse(cur.getString(0));
					total += d.getHours() * 60 + d.getMinutes();
				} catch (ParseException e) {
					e.printStackTrace();
					continue;
				}
			}
			return total / cur.getCount();
		} else if (modularName.equals(Global.WAKE_UP_TIME)) {
			String rawQuery = String.format("select WakeUpTime from sleep_time;");
			Cursor cur = DBContentProvider.rawQuery(c, rawQuery);
			if (cur.getCount() < 1)
				return -1;
			cur.moveToFirst();
			Date d = null;
			int total = 0;
			while (!cur.isAfterLast()) {
				try {
					d = Global.lastModDateFormat.parse(cur.getString(0));
					total += d.getHours() * 60 + d.getMinutes();
				} catch (ParseException e) {
					e.printStackTrace();
					continue;
				}
			}
			return total / cur.getCount();
		} else if (modularName.equals(Global.SLEEP_DURATION)) {
			String rawQuery = String.format("select avg(SleepDuration) from sleep_time;");
			Cursor cur = DBContentProvider.rawQuery(c, rawQuery);
			if (cur.getCount() < 1)
				return -1;
			cur.moveToFirst();
			return cur.getInt(0);
		} else {
			String rawQuery = String.format("select avg(QuestionValue) from question where QuestionType='%s';", modularName);
			Cursor cur = DBContentProvider.rawQuery(c, rawQuery);
			if (cur.getCount() < 1)
				return -1;
			cur.moveToFirst();
			return cur.getFloat(0);
		}
	}

	public static int getStatic(Context c) {
		String rawQuery = String.format("select count(*) from question union select count(*) from sleep_time;");
		Cursor cur = DBContentProvider.rawQuery(c, rawQuery);
		if (cur.getCount() < 1)
			return -1;
		cur.moveToFirst();
		return cur.getInt(0);
	}
}
