package edu.neu.hci.db;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;

import android.os.Environment;

public class DatabaseDictionary {
	public final static int DATABASE_VERSION = 1;
	public static String TAG = "GoodSleep";
	
	public static final String CAFFEINE="caffeine";
	public static final String ALCOHOL="alcohol";
	public static final String SMOKE="smoke";
	public static final String FOOD="food";
	public static final String PA="pa";
	public static final String STRESS="stress";
	

	public static final String QUESTION_SETTING_TABLE_NAME = "question_setting";
	public static final String QUESTION_TABLE_NAME = "question";
	public static final String USAGE_LOG_TABLE_NAME = "usage_log";
	public static final String LOCAL_SCHEMA_TABLE_NAME = "schema_update";

	public static SimpleDateFormat normalDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat lastModDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat exactDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS", Locale.US);
	public static String LAST_MOD_DATE = "LastModDate";

	public static String PACKAGE_NAME = "edu.neu.hci";
	public static String internalDBPath = "/data" + Environment.getDataDirectory().getAbsolutePath() + "/" + PACKAGE_NAME + "/databases/";
	public static String internalDBFile = "good_sleep.sqlite";
	public static String externalDBPath = "/.goodsleep/db/";
	public static String externalDBFile = "good_sleep.sqlite";
	public static final String STAT_DATABASE_NAME = "good_sleep.sqlite";
	public static String internalDBPathFile = internalDBPath + STAT_DATABASE_NAME;
	public static String externalDBPathFile = externalDBPath + STAT_DATABASE_NAME;

	public final static String SYNC_TIME_TABLE = "sync_time";

	public static SimpleDateFormat sqliteFormat = new SimpleDateFormat("yyyy-MM-dd");

	public final static String COL_SPLIT = "@c@";
	public final static String ROW_SPLIT = "@r@";
	public final static String PAGE_SPLIT = "@p@";

	public static String[][] tableParams = new String[][] {
			{
					QUESTION_SETTING_TABLE_NAME,
					"PhoneID TEXT NOT NULL," + "Caffeine TEXT DEFAULT NULL," + "Alcohol TEXT DEFAULT NULL," + "Smoke TEXT DEFAULT NULL,"
							+ "Food TEXT DEFAULT NULL," + "PA TEXT DEFAULT NULL," + "Stress TEXT DEFAULT NULL," + "PRIMARY KEY (PhoneID)" },
			{
					QUESTION_TABLE_NAME,
					"PhoneID TEXT NOT NULL," + "CreateDate TEXT NOT NULL," + "QuestionType TEXT NOT NULL," + "QuestionValue TEXT DEFAULT NULL,"
							+ "LastModDate TEXT DEFAULT NULL," + "PRIMARY KEY (CreateDate,QuestionType)" },
			{ USAGE_LOG_TABLE_NAME, "TimeStamp TEXT NOT NULL," + "Activity TEXT DEFAULT NULL," + "PRIMARY KEY (TimeStamp)" } };

	public static HashMap<String, String[]> getTableCols() {
		HashMap<String, String[]> tableCols = new HashMap<String, String[]>();
		String[] question_settingCols = { "PhoneID", "Caffeine", "Alcohol", "Smoke", "Food", "PA", "Stress" };
		String[] questionCols = { "PhoneID", "CreateDate", "QuestionType", "QuestionValue", "LastModDate" };
		String[] usage_logCol = { "TimeStamp", "Activity" };
		tableCols.put(QUESTION_SETTING_TABLE_NAME, question_settingCols);
		tableCols.put(QUESTION_TABLE_NAME, questionCols);
		tableCols.put(USAGE_LOG_TABLE_NAME, usage_logCol);
		return tableCols;
	}

	public static HashMap<String, String[]> getTableKeys() {
		HashMap<String, String[]> tableCols = new HashMap<String, String[]>();
		String[] question_settingCols = { "PhoneID" };
		tableCols.put(QUESTION_SETTING_TABLE_NAME, question_settingCols);
		String[] questionCols = { "CreateDate", "QuestionType" };
		tableCols.put(QUESTION_TABLE_NAME, questionCols);
		String[] usage_logCols = { "TimeStamp" };
		tableCols.put(USAGE_LOG_TABLE_NAME, usage_logCols);
		return tableCols;
	}
}
