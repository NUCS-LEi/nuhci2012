package edu.neu.hci;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;

import android.app.PendingIntent;

public class Global {
	public static final String DEFAULT_WOCKET_NAME = "Wocket-D376";
	public static PendingIntent mAlarmSender = null;
	public static String TAG = "GoodSleep";
	public static String ALARM = "edu.neu.hci.ALARM";
	public static long SNOOZE_TIME = 10 * 60 * 1000;
	public static SimpleDateFormat normalDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat lastModDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat exactDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS", Locale.US);
	public static SimpleDateFormat apmDateFormat = new SimpleDateFormat("HH:mm a");
	public static SimpleDateFormat normalTimeFormat = new SimpleDateFormat("HH:mm:ss");
	public static int WAKE_THRESHOLD = 320;
	public static int LIGHT_SLEEP_THRESHOLD = 270;
	public static int DEEP_SLEEP = 240;
	public static int GROUP_MINUTES = 5;
	public static String CAFFEINE = "caffeine";
	public static String ALCOHOL = "alcohol";
	public static String SMOKE = "smoke";
	public static String FOOD = "food";
	public static String PA = "pa";
	public static String STRESS = "stress";
	public static String SLEEP_SCORE = "sleep_score";
	public static String SLEEP_DURATION = "sleep_duration";
	public static String GO_TO_BED_TIME = "go_to_bed_time";
	public static String WAKE_UP_TIME = "wake_up_time";
	public static HashMap<String, Boolean> QUESTION_CONFIRM = new HashMap<String, Boolean>();
}
