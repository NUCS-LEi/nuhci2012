package edu.neu.hci.db;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import edu.neu.hci.helper.FileHelper;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;

public class DbContentProvider extends ContentProvider {

	private static final String SUGGESTED_TO_BUDDY = "SuggestedToBuddy";

	private static final String IS_CURRENT = "IsCurrent";

	private static final String CUSTOM_ANSWER = "CustomAnswer";

	private static final String LAST_MOD_DATE = "LastModDate";

	private static final String ANSWER_ID = "AnswerID";

	private static final String QUIZ_ID = "QuizID";

	private static final String BUDDY_ID = "BuddyID";

	private static final String PARTICIPANT_ID = "ParticipantID";

	private static final String FILTER_FORMATTER_PARTICIPANT_QUIZ_BUDDY = makeFilterFormatter(new String[] { PARTICIPANT_ID, QUIZ_ID, BUDDY_ID });

	private static final String FILTER_FORMATTER_PARTICIPANT_BUDDY = makeFilterFormatter(new String[] { PARTICIPANT_ID, BUDDY_ID });

	private static final String LOG_MESSAGE_COLUMN = "LogMessage";

	private static final String MODULE_COLUMN = "Module";

	private static final String WALLPAPER_VALUE = "Wallpaper";

	@SuppressWarnings("unused")
	private static final String WALLPAPER_COLUMN = "Wallpaper";

	private static final String USER_CONDITION_COLUMN = "UserCondition";

	private static final String DAYS_FROM_START_COLUMN = "DaysFromStart";

	private static final String STAT_LAST_LOGGED_TIME = "LastLoggedTime";

	public static final String CREATE_DATE = "CreateDate";
	
	private static final String LOG_TIME = "LogTime";

	private static final String PHONE_ID = "PhoneID";

	public static final String PHONE_ID_AND_CREATE_DATE = String.format("%s=? and %s=?", PHONE_ID,
			CREATE_DATE);
	
	public static final String PHONE_ID_AND_LOG_TIME = String.format("%s=? and %s=?", PHONE_ID,
			LOG_TIME);
	
	public static final String PARTICIPANT_ID_AND_QUIZ_ID = String.format("%s=? and %s=?", PARTICIPANT_ID,
			QUIZ_ID);
	
	public static final String PARTICIPANT_ID_FILTER = String.format("%s=?", PARTICIPANT_ID);

	public static String makeFilterFormatter(String[] columns) {
		String result = "";
		
		for (int i = 0; i < columns.length; i++) {
			String s = columns[i];
			result += String.format("%s=?", s);
			if (i == (columns.length - 1)) {
				break;
			}
			result += " and ";
		}
		
		return result;
	}
	
	private static final boolean THROW = true;

	private SQLiteOpenHelper dbHelper = null;

	public static final String STATS_TABLE_NAME = "stats";
	public static final String STATS_EXT_TABLE_NAME = "stats_ext";
	public static final String STATS_LOCAL_SCHEMA_TABLE_NAME = "schema_update";
	public static final String LOG_MESSAGE_TABLE_NAME = "log_message";
	private static final String STATS_SYNC_ACTION = "stats_sync_action";
	private static final String TEST_TABLE_NAME = "test";
	private static final String STATS_RAW_QUERY = "stats_raw_query";
	private static final String STATS_EXEC_QUERY = "stats_exec_query";
	private static final String STATS_QUERY = "stats_query";

	private static final String AUTHORITY = "edu.mit.android.cityver1.statdb.logger";

	public static final String CONTENT = "content://" + AUTHORITY;

	private static final Uri ALL_CONTENT_URI = Uri.parse(CONTENT + "/*");
	private static final Uri TEST_CONTENT_URI = Uri.parse(CONTENT + "/" + TEST_TABLE_NAME);
	private static final Uri STATS_CONTENT_URI = Uri.parse(CONTENT + "/" + STATS_TABLE_NAME);
	private static final Uri STATS_EXT_CONTENT_URI = Uri.parse(CONTENT + "/" + STATS_EXT_TABLE_NAME);
	public static final Uri STATS_LOCAL_SCHEMA_CONTENT_URI = Uri.parse(CONTENT + "/" + STATS_LOCAL_SCHEMA_TABLE_NAME);
	public static final Uri LOG_MESSAGE_CONTENT_URI = Uri.parse(CONTENT + "/" + LOG_MESSAGE_TABLE_NAME);

	private static final Uri STATS_SYNC_ACTION_CONTENT_URI = Uri.parse(CONTENT + "/" + STATS_SYNC_ACTION);
	private static final Uri STATS_RAW_QUERY_CONTENT_URI = Uri.parse(CONTENT + "/" + STATS_RAW_QUERY);
	private static final Uri STATS_EXEC_QUERY_CONTENT_URI = Uri.parse(CONTENT + "/" + STATS_EXEC_QUERY);
	private static final Uri STATS_QUERY_CONTENT_URI = Uri.parse(CONTENT + "/" + STATS_QUERY);

	private static final String VND_CONTENT_TYPE_PREFIX = "vnd.android.cursor.dir/";
	private static final String VND_CONTENT_ITEM_TYPE_PREFIX = "vnd.android.cursor.item/";
	private static final String CONTENT_TYPE_PREFIX = "vnd.edu.mit.android.cityver1.statdb.logger.";

	private static final String STATS_CONTENT_TYPE = VND_CONTENT_TYPE_PREFIX + CONTENT_TYPE_PREFIX
			+ STATS_TABLE_NAME;
	@SuppressWarnings("unused")
	private static final String STATS_CONTENT_ITEM_TYPE = VND_CONTENT_ITEM_TYPE_PREFIX
			+ CONTENT_TYPE_PREFIX + STATS_TABLE_NAME;

	private static final String STATS_EXT_CONTENT_TYPE = VND_CONTENT_TYPE_PREFIX
			+ CONTENT_TYPE_PREFIX + STATS_EXT_TABLE_NAME;
	@SuppressWarnings("unused")
	private static final String STATS_EXT_CONTENT_ITEM_TYPE = VND_CONTENT_ITEM_TYPE_PREFIX
			+ CONTENT_TYPE_PREFIX + STATS_EXT_TABLE_NAME;

	private static final int STATS_KEY = 100;
	private static final int STATS_EXT_KEY = 200;
	private static final int TEST_KEY = 300;
	private static final int LOCAL_SCHEMA_KEY = 400;
	private static final int LOG_MESSAGE_KEY = 500;

	private static final UriMatcher uriMatcher;

	private static final String TAG = "StatDbContentProvider";

	private static final HashMap<Integer, String> StatsKeyToTableName = new HashMap<Integer, String>();
	
	private static final HashMap<String, String> TableNameToContentType = new HashMap<String, String>();
	private static final HashMap<String, String> TableNameToContentItemType = new HashMap<String, String>();
	private static final HashMap<String, Uri> TableNameToContentUri = new HashMap<String, Uri>();
	private static final HashMap<String, Integer> TableNameToKey = new HashMap<String, Integer>();

	private static final String BUDDY_USER_ANSWER_TABLE = "buddy_user_answer";
	private static final int BUDDY_USER_ANSWER_KEY = 600; // start at 600
	private static final String BUDDY_USER_QUIZ_TABLE = "buddy_user_quiz";
	private static final int BUDDY_USER_QUIZ_KEY = 700;

	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, STATS_TABLE_NAME, STATS_KEY);
		uriMatcher.addURI(AUTHORITY, STATS_EXT_TABLE_NAME, STATS_EXT_KEY);
		uriMatcher.addURI(AUTHORITY, TEST_TABLE_NAME, TEST_KEY);
		uriMatcher.addURI(AUTHORITY, STATS_LOCAL_SCHEMA_TABLE_NAME, LOCAL_SCHEMA_KEY);
		uriMatcher.addURI(AUTHORITY, LOG_MESSAGE_TABLE_NAME, LOG_MESSAGE_KEY);
		StatsKeyToTableName.put(STATS_KEY, STATS_TABLE_NAME);
		StatsKeyToTableName.put(STATS_EXT_KEY, STATS_EXT_TABLE_NAME);
		StatsKeyToTableName.put(TEST_KEY, TEST_TABLE_NAME);
		StatsKeyToTableName.put(LOCAL_SCHEMA_KEY, STATS_LOCAL_SCHEMA_TABLE_NAME);
		StatsKeyToTableName.put(LOG_MESSAGE_KEY, LOG_MESSAGE_TABLE_NAME);
		
		setupBuddyTables();
	}
	
	private static void setupBuddyTables() {
		TableNameToKey.put(BUDDY_USER_ANSWER_TABLE, BUDDY_USER_ANSWER_KEY);
		TableNameToKey.put(BUDDY_USER_QUIZ_TABLE, BUDDY_USER_QUIZ_KEY);
		
		for (String tableName : TableNameToKey.keySet()) {
			Integer key = TableNameToKey.get(tableName);
			uriMatcher.addURI(AUTHORITY, tableName, key);
			StatsKeyToTableName.put(key, tableName);
			TableNameToContentType.put(tableName, VND_CONTENT_TYPE_PREFIX + CONTENT_TYPE_PREFIX + tableName);
			TableNameToContentItemType.put(tableName, VND_CONTENT_ITEM_TYPE_PREFIX + CONTENT_TYPE_PREFIX + tableName);
			TableNameToContentUri.put(tableName, Uri.parse(CONTENT + "/" + tableName));
		}
	}

	private static final String STAT_DATABASE_NAME = "city_statdb_2.sqlite";
	private static String PACKAGE_NAME = "edu.mit.android.cityver1";
	private static String internalDBPath = "/data"
			+ Environment.getDataDirectory().getAbsolutePath() + "/" + PACKAGE_NAME + "/databases/";
	private static String internalDBFile = STAT_DATABASE_NAME;
	private static String externalDBPath = "/.city/data/statdb/";
	private static String externalDBFile = STAT_DATABASE_NAME;
	private static String internalDBPathFile = internalDBPath + internalDBFile;
	private static String externalDBPathFile = externalDBPath + externalDBFile;
	
	public boolean isSynching = false;

	@Override
	public boolean onCreate() {
		dbHelper = new SQLiteOpenHelper(getContext(), STAT_DATABASE_NAME, null,
				DatabaseDictionary.DATABASE_VERSION) {
			@Override
			public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			}

			@Override
			public void onCreate(SQLiteDatabase db) {
				// Create all tables on creating DB
				for (String[] s : DatabaseDictionary.tableParams)
					db.execSQL("CREATE TABLE IF NOT EXISTS " + s[0] + " (" + s[1] + ")");
			}
		};
		return true;
	}

	@Override
	public synchronized int delete(Uri uri, String where, String[] whereArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String tableName = StatsKeyToTableName.get(uriMatcher.match(uri));

		if (tableName == null) {
			if (THROW) {
				throw new IllegalArgumentException("Unknown URI " + uri);
			}
			else {
				return -1;
			}
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return db.delete(tableName, where, whereArgs);
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
			case STATS_KEY:
				return STATS_CONTENT_TYPE;
			case STATS_EXT_KEY:
				return STATS_EXT_CONTENT_TYPE;
			default:
				if (THROW) {
					throw new IllegalArgumentException("Unknown URI " + uri);
				}
				else {
					return "";
				}
		}
	}

	public static Uri getUriForTableName(String tableName) {
		if (STATS_TABLE_NAME.equals(tableName)) {
			return STATS_CONTENT_URI;
		}
		if (STATS_EXT_TABLE_NAME.equals(tableName)) {
			return STATS_EXT_CONTENT_URI;
		}
		if (STATS_LOCAL_SCHEMA_TABLE_NAME.equals(tableName)) {
			return STATS_LOCAL_SCHEMA_CONTENT_URI;
		}
		if (LOG_MESSAGE_TABLE_NAME.equals(tableName)) {
			return LOG_MESSAGE_CONTENT_URI;
		}
		if (TEST_TABLE_NAME.equals(tableName)) {
			return TEST_CONTENT_URI;
		}
		return null;
	}

	@Override
	public synchronized Uri insert(Uri uri, ContentValues initialValues) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		String tableName = StatsKeyToTableName.get(uriMatcher.match(uri));

		//android.os.Debug.waitForDebugger();

		if (tableName == null) {
			List<String> list = uri.getPathSegments();
			if (list.size() > 0) {
				tableName = list.get(0);
			}
			else {
				if (THROW) {
					throw new IllegalArgumentException("Unknown URI " + uri);
				}
				else {
					return null;
				}
			}
		}

		ContentValues values = new ContentValues();
		if (initialValues != null) {
			values = new ContentValues(initialValues);
		}

		long rowId = db.insert(tableName, null, values);
		if (rowId > 0) {
			Uri contentUri = ContentUris.withAppendedId(uri, rowId);
			getContext().getContentResolver().notifyChange(contentUri, null);
			return contentUri;
		}

		if (THROW) {
			throw new SQLException("Failed to insert row into " + uri);
		}
		else {
			return null;
		}
	}

	@Override
	public synchronized Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
			String sortOrder) {
		//android.os.Debug.waitForDebugger();

		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		if (STATS_RAW_QUERY_CONTENT_URI.equals(uri)) {
			return db.rawQuery(selection, null);
		}
		
		if (STATS_EXEC_QUERY_CONTENT_URI.equals(uri)) {
			db.execSQL(selection);
			return null;
		}
		
		if (STATS_QUERY_CONTENT_URI.equals(uri)) {
			return db.query(selection, null, null, null, null, null, null);
		}
		
		String tableName = StatsKeyToTableName.get(uriMatcher.match(uri));

		if (tableName == null) {
			if (THROW) {
				throw new IllegalArgumentException("Unknown URI " + uri);
			}
			else {
				return null;
			}
		}

		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(tableName);

		Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);

		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public synchronized int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
		//android.os.Debug.waitForDebugger();
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int count = -1;
		String tableName = StatsKeyToTableName.get(uriMatcher.match(uri));

		if (tableName == null) {
			if (THROW) {
				throw new IllegalArgumentException("Unknown URI " + uri);
			}
			else {
				return -1;
			}
		}

		count = db.update(tableName, values, where, whereArgs);
		if (count == 0) {
			db.insert(tableName, null, values);
			count = 1;
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	// don't run if sync database doesn't exist
	public static int updateColumnCount(Context c, String phoneID, String date, String tableName,
			String columnName, int increase) {

		//android.os.Debug.waitForDebugger();
		ContentResolver cr = c.getContentResolver();

		Uri uri = getUriForTableName(tableName);

		if (uri == null) {
			return -1;
		}

		Cursor cursor = cr.query(uri, new String[] { columnName }, PHONE_ID_AND_CREATE_DATE,
				new String[] { phoneID, date }, null);

		int count = 0;

		if (cursor.moveToFirst()) {
			count = cursor.getInt(cursor.getColumnIndex(columnName));
		}

		ContentValues cv =  contentValuesWithConditionAndDaysFromStart(c, tableName, phoneID);
		cv.put(PHONE_ID, phoneID);
		cv.put(CREATE_DATE, date);
		cv.put(DatabaseDictionary.LAST_MOD_DATE, lastModDateFormatter().format(new Date()));
		cv.put(columnName, count + increase);

		return c.getContentResolver().update(uri, cv, PHONE_ID_AND_CREATE_DATE,
				new String[] { phoneID, date });
	}

	// don't run if sync database doesn't exist
	public static int updateColumnCount(Context c, String tableName, String columnName, int increase) {
		TelephonyManager tm = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
		String phoneID = tm.getDeviceId();
		String date = DatabaseDictionary.normalDateFormat.format(new Date());

		return updateColumnCount(c, phoneID, date, tableName, columnName, increase);
	}
	
	// don't run if sync database doesn't exist
	public static int updateColumn(Context c, String tableName, ContentValues cv) {
		//android.os.Debug.waitForDebugger();
		TelephonyManager tm = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
		String phoneID = tm.getDeviceId();
		String date = DatabaseDictionary.normalDateFormat.format(new Date());

		Uri uri = getUriForTableName(tableName);

		if ((uri == null) || (phoneID == null) || (date == null) || (cv == null)) {
			return -1;
		}
		
		cv.putAll(contentValuesWithConditionAndDaysFromStart(c, tableName, phoneID));
		cv.put(PHONE_ID, phoneID);
		cv.put(CREATE_DATE, date);
		cv.put(DatabaseDictionary.LAST_MOD_DATE, lastModDateFormatter().format(new Date()));

		return c.getContentResolver().update(uri, cv, PHONE_ID_AND_CREATE_DATE,
				new String[] { phoneID, date });
	}
	
	// don't run if sync database doesn't exist
	private static int updateLogColumn(Context c, String tableName, ContentValues cv) {
		TelephonyManager tm = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
		String phoneID = tm.getDeviceId();
		String date = DatabaseDictionary.exactDateFormat.format(new Date());

		Uri uri = getUriForTableName(tableName);

		if ((uri == null) || (phoneID == null) || (date == null) || (cv == null)) {
			return -1;
		}
		
		cv.put(PHONE_ID, phoneID);
		cv.put(LOG_TIME, date);
		cv.put(DatabaseDictionary.LAST_MOD_DATE, lastModDateFormatter().format(new Date()));

		return c.getContentResolver().update(uri, cv, PHONE_ID_AND_LOG_TIME,
				new String[] { phoneID, date });
	}
	
	// don't run if sync database doesn't exist
	public static void LogDB(final Context context, final String moduleName, final String logMsg) {
		new Thread() {
			@Override
			public void run() {
				ContentValues cv = new ContentValues();
				cv.put(MODULE_COLUMN, moduleName);
				cv.put(LOG_MESSAGE_COLUMN, logMsg);
				updateLogColumn(context, LOG_MESSAGE_TABLE_NAME, cv);
			}
		}.start();
	}

	private static final String dateFormatString = "yyyy-MM-dd HH:mm:ss.SSSZ";
	private static final String lastModDateFormatString = "yyyy-MM-dd HH:mm:ss";

	// apparently simpledateformat has mutable state which can mess up the
	// timezone, so we'll create a new one every time
	private static SimpleDateFormat dateFormatter() {
		return new SimpleDateFormat(dateFormatString, Locale.US);
	}
	
	public static SimpleDateFormat lastModDateFormatter() {
		SimpleDateFormat result = new SimpleDateFormat(lastModDateFormatString); 
		result.setTimeZone(TimeZone.getTimeZone("GMT"));
		return result;
	}

	static void saveLastUsedTime(Context c, String columnName, Date date) {
		SharedPreferences settings = c.getSharedPreferences(TAG, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(columnName + STAT_LAST_LOGGED_TIME, dateFormatter().format(date));
		editor.commit();
	}

	static Date getLastUsedTime(Context c, String columnName) {
		SharedPreferences settings = c.getSharedPreferences(TAG, 0);
		String s = settings.getString(columnName + STAT_LAST_LOGGED_TIME, null);

		if (s != null) {
			try {
				return dateFormatter().parse(s);
			}
			catch (ParseException e) {
				return null;
			}
		}
		return null;
	}

	// don't run if sync database doesn't exist
	public static void logUse(Context c, String columnName, String increaseString) {
		Logger logger = loggerForColumnName(columnName);
		
		if (logger == null) {
			Log.e(TAG, "logUse: sync database has no such column: " + columnName);
			return;
		}

		int increase = 0;

		try {
			increase = Integer.parseInt(increaseString);
		}
		catch (NumberFormatException e) {
			Log.e(TAG,  "Unexpected non-integer value in logUse: " + increaseString + " for column: " + columnName);
			return;
		}

		// moved to LoggerDatabase
		/*
		GregorianCalendar tenMinutesAgo = new GregorianCalendar();
		tenMinutesAgo.add(GregorianCalendar.MINUTE, -10);
		for (String s : LoggerDictionary.LOG_OF_USEDS) {
			if (s.equals(columnName)) {
				Date lastLogTime = getLastUsedTime(c, columnName);
				if ((lastLogTime == null) || (tenMinutesAgo.getTime().after(lastLogTime))) {
					updateColumnCount(c, logger.getTableName(), logger.getModuleName(), increase);
					saveLastUsedTime(c, columnName, new GregorianCalendar().getTime());
				}
				return;
			}
		}
		*/
		
		Log.i(TAG, "increased value to statdb: " + columnName + ": " + increaseString);
		
		updateColumnCount(c, logger.getTableName(), logger.getModuleName(), increase);
	}

	// don't run if sync database doesn't exist
	public static void logWallpaper(Context c) {
		//android.os.Debug.waitForDebugger();
		logValue(c, LoggerDictionary.WALLPAPER, WALLPAPER_VALUE);
	}

	public static Logger loggerForColumnName(String columnName) {
		Logger logger = null;
		for (Logger l : LoggerDictionary.getModuleLogList()) {
			if (l.getModuleName().equals(columnName)) {
				logger = l;
				break;
			}
		}
		return logger;
	}

	// don't run if sync database doesn't exist
	public static double getAverageWeightForStudy(Context c, double newWeight) {
		Logger logger = loggerForColumnName(LoggerDictionary.AVERAGE_WEIGHT);
		
		if (logger == null) {
			Log.i(TAG, "getAverageWeight: sync database has no such column: " + LoggerDictionary.AVERAGE_WEIGHT);
			return -1;
		}

		Uri uri = getUriForTableName(logger.getTableName());
		
		String date = DatabaseDictionary.normalDateFormat.format(new Date());
		
		String where = String.format("(%s is not null) and (%s != '') and (%s != 'null') and (%s > 0)",
				LoggerDictionary.AVERAGE_WEIGHT, LoggerDictionary.AVERAGE_WEIGHT,
				LoggerDictionary.AVERAGE_WEIGHT, LoggerDictionary.AVERAGE_WEIGHT);

		/*
		String where = String.format("(%s is not null) and (%s is not '') and (%s is not 'null') and (%s > 0)",
				LoggerDictionary.AVERAGE_WEIGHT, LoggerDictionary.AVERAGE_WEIGHT,
				LoggerDictionary.AVERAGE_WEIGHT, LoggerDictionary.AVERAGE_WEIGHT);
				*/
		Cursor cursor = c.getContentResolver().query(uri,
				null, where,
						null,
				CREATE_DATE + " DESC");
		
		double totalWeight = newWeight;
		int count = 1;

		while (cursor.moveToNext()) {
			String string = cursor
					.getString(cursor.getColumnIndex(LoggerDictionary.AVERAGE_WEIGHT));
			if (string == null) {
				continue;
			}
			try {
				double result = Double.parseDouble(string);
				totalWeight += result;
				count++;
			}
			catch (NumberFormatException e) {
				continue;
			}
		}

		if ((totalWeight > 0) && (count > 0)) {
			return totalWeight / count;
		}
		
		return -1;
	}
	
	// don't run if sync database doesn't exist
	public static void logValue(Context c, String columnName, String value) {
		Logger logger = loggerForColumnName(columnName);
		if (logger == null) {
			Log.i(TAG, "logValue: sync database has no such column: " + columnName);
		}
		else {
			Log.i(TAG, "logged value to statdb: " + columnName + ": " + value);
			String tableName = logger.getTableName();
			ContentValues cv = new ContentValues();
			cv.put(columnName, value);
			cv.put(DatabaseDictionary.LAST_MOD_DATE, lastModDateFormatter().format(new Date()));
			updateColumn(c, tableName, cv);
		}
	}
	
	public static String exportLogStatDB() {
		InputStream myInput;
		try {
			myInput = new FileInputStream(internalDBPathFile);
			FileHelper.createDir(externalDBPath);
			OutputStream myOutput = FileHelper.openFileForWriting(externalDBPathFile,
					externalDBPathFile, false);
			byte[] buffer = new byte[8192];
			int length;
			while ((length = myInput.read(buffer)) > 0) {
				myOutput.write(buffer, 0, length);
			}
			myOutput.flush();
			myInput.close();
			myOutput.close();
			return "Internal database exported to SDcard";
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
			return "Can't find internal database";
		}
		catch (IOException e) {
			e.printStackTrace();
			return "Can't read internal database or write SD card";
		}
		catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}
	}
	
	public static Cursor rawQuery(Context c, String sql) {
		return c.getContentResolver().query(STATS_RAW_QUERY_CONTENT_URI, null, sql, null,
				null);
	}
	
	public static void execSQL(Context c, String sql) {
		c.getContentResolver().query(STATS_EXEC_QUERY_CONTENT_URI, null, sql, null,
				null);
	}
	
	public static Cursor query(Context c, String sql) {
		return c.getContentResolver().query(STATS_QUERY_CONTENT_URI, null, sql, null,
				null);
	}
	
	public static void sync(Context c) {
		c.getContentResolver().update(DbContentProvider.STATS_SYNC_ACTION_CONTENT_URI,
				new ContentValues(), null, null);
		setLastSyncTime(c);
	}
	
	
	private static void setLastSyncTime(Context c) {
		// TODO Auto-generated method stub
		SharedPreferences data = c.getSharedPreferences("SyncData", Context.MODE_PRIVATE);
		data.edit().putLong("LastSyncTime", System.currentTimeMillis()).commit();
	}
	public static long getLastSyncTime(Context c){
		SharedPreferences data = c.getSharedPreferences("SyncData", Context.MODE_PRIVATE);
		return data.getLong("LastSyncTime", -1);
	}

	public void test(Context c) {
		ContentValues cv = new ContentValues();
		cv.put(DbContentProvider.PHONE_ID, "blah");
		cv.put(DbContentProvider.CREATE_DATE, "blah2");
		c.getContentResolver().insert(DbContentProvider.STATS_CONTENT_URI, new ContentValues());
		
		c.getContentResolver().insert(DbContentProvider.STATS_SYNC_ACTION_CONTENT_URI, new ContentValues());
	}
	

	private static String getComprehensiveNull(Cursor cursor, String column) {
		String result = cursor.getString(cursor
				.getColumnIndex(column));
		
		if ((result == null) || ("".equals(result)) || ("null".equals(result))) {
			return null;
		}
		return result;
	}
	private static ContentValues contentValuesWithConditionAndDaysFromStart(Context c, String tableName, String phoneID) {
		ContentValues cv = new ContentValues();
		return cv;
	}
}