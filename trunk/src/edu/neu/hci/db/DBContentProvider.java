package edu.neu.hci.db;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.telephony.TelephonyManager;
import edu.neu.hci.Global;
import edu.neu.hci.helper.FileHelper;

public class DBContentProvider extends ContentProvider {

	private static final String LAST_MOD_DATE = "LastModDate";

	private SQLiteOpenHelper dbHelper = null;

	private static final String SYNC_ACTION = "sync_action";
	private static final String RAW_QUERY = "raw_query";
	private static final String EXEC_QUERY = "exec_query";
	private static final String QUERY = "query";

	private static final String AUTHORITY = "edu.neu.hci.db";

	public static final String CONTENT = "content://" + AUTHORITY;

	private static final Uri ALL_CONTENT_URI = Uri.parse(CONTENT + "/*");
	public static final Uri QUESTION_SETTING_CONTENT_URI = Uri.parse(CONTENT + "/" + DatabaseDictionary.QUESTION_SETTING_TABLE);
	public static final Uri QUESTION_CONTENT_URI = Uri.parse(CONTENT + "/" + DatabaseDictionary.QUESTION_TABLE);
	public static final Uri USAGE_LOG_CONTENT_URI = Uri.parse(CONTENT + "/" + DatabaseDictionary.USAGE_LOG_TABLE);
	public static final Uri SLEEP_TIME_CONTENT_URI = Uri.parse(CONTENT + "/" + DatabaseDictionary.SLEEP_TIME_TABLE);
	public static final Uri ALARM_CONTENT_URI = Uri.parse(CONTENT + "/" + DatabaseDictionary.ALARM_TABLE);
	public static final Uri ACCEL_POINT_URI = Uri.parse(CONTENT + "/" + DatabaseDictionary.ACCEL_POINT_TABLE);
	public static final Uri SUMMARY_POINT_URI = Uri.parse(CONTENT + "/" + DatabaseDictionary.SUMMARY_POINT_TABLE);
	private static final Uri LOCAL_SCHEMA_CONTENT_URI = Uri.parse(CONTENT + "/" + DatabaseDictionary.LOCAL_SCHEMA_TABLE);
	private static final Uri RAW_QUERY_CONTENT_URI = Uri.parse(CONTENT + "/" + RAW_QUERY);
	private static final Uri EXEC_QUERY_CONTENT_URI = Uri.parse(CONTENT + "/" + EXEC_QUERY);
	private static final Uri QUERY_CONTENT_URI = Uri.parse(CONTENT + "/" + QUERY);

	private static final String VND_CONTENT_TYPE_PREFIX = "vnd.android.cursor.dir/";
	private static final String VND_CONTENT_ITEM_TYPE_PREFIX = "vnd.android.cursor.item/";
	private static final String CONTENT_TYPE_PREFIX = "vnd.edu.neu.hci.db.";

	private static final String QUESTION_SETTING_CONTENT_TYPE = VND_CONTENT_TYPE_PREFIX + CONTENT_TYPE_PREFIX
			+ DatabaseDictionary.QUESTION_SETTING_TABLE;
	@SuppressWarnings("unused")
	private static final String QUESTION_SETTING_CONTENT_ITEM_TYPE = VND_CONTENT_ITEM_TYPE_PREFIX + CONTENT_TYPE_PREFIX
			+ DatabaseDictionary.QUESTION_SETTING_TABLE;

	private static final String QUESTION_CONTENT_TYPE = VND_CONTENT_TYPE_PREFIX + CONTENT_TYPE_PREFIX + DatabaseDictionary.QUESTION_TABLE;
	@SuppressWarnings("unused")
	private static final String QUESTION_CONTENT_ITEM_TYPE = VND_CONTENT_ITEM_TYPE_PREFIX + CONTENT_TYPE_PREFIX
			+ DatabaseDictionary.QUESTION_TABLE;

	private static final int QUESTION_SETTING_KEY = 100;
	private static final int QUESTION_KEY = 200;
	private static final int LOCAL_SCHEMA_KEY = 400;

	private static final UriMatcher uriMatcher;

	private static final String TAG = "DBContentProvider";

	private static final HashMap<Integer, String> KeyToTableName = new HashMap<Integer, String>();

	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, DatabaseDictionary.QUESTION_SETTING_TABLE, QUESTION_SETTING_KEY);
		uriMatcher.addURI(AUTHORITY, DatabaseDictionary.QUESTION_TABLE, QUESTION_KEY);
		uriMatcher.addURI(AUTHORITY, DatabaseDictionary.LOCAL_SCHEMA_TABLE, LOCAL_SCHEMA_KEY);
		KeyToTableName.put(QUESTION_SETTING_KEY, DatabaseDictionary.QUESTION_SETTING_TABLE);
		KeyToTableName.put(QUESTION_KEY, DatabaseDictionary.QUESTION_TABLE);
		KeyToTableName.put(LOCAL_SCHEMA_KEY, DatabaseDictionary.LOCAL_SCHEMA_TABLE);
	}

	public boolean isSynching = false;

	@Override
	public boolean onCreate() {
		dbHelper = new SQLiteOpenHelper(getContext(), DatabaseDictionary.internalDBFile, null, DatabaseDictionary.DATABASE_VERSION) {
			@Override
			public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			}

			@Override
			public void onCreate(SQLiteDatabase db) {
				// Create all tables on creating DB
				for (String[] s : DatabaseDictionary.tableParams) {
					android.util.Log.i(Global.TAG, "========" + s[0]);
					db.execSQL("CREATE TABLE IF NOT EXISTS " + s[0] + " (" + s[1] + ")");
				}
			}
		};
		return true;
	}

	@Override
	public synchronized int delete(Uri uri, String where, String[] whereArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String tableName = KeyToTableName.get(uriMatcher.match(uri));

		if (tableName == null) {
			return -1;
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return db.delete(tableName, where, whereArgs);
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
		case QUESTION_SETTING_KEY:
			return QUESTION_SETTING_CONTENT_TYPE;
		case QUESTION_KEY:
			return QUESTION_CONTENT_TYPE;
		default:
			return "";
		}
	}

	public static Uri getUriForTableName(String tableName) {
		if (DatabaseDictionary.QUESTION_SETTING_TABLE.equals(tableName)) {
			return QUESTION_SETTING_CONTENT_URI;
		}
		if (DatabaseDictionary.QUESTION_TABLE.equals(tableName)) {
			return QUESTION_CONTENT_URI;
		}
		if (DatabaseDictionary.LOCAL_SCHEMA_TABLE.equals(tableName)) {
			return LOCAL_SCHEMA_CONTENT_URI;
		}
		return null;
	}

	@Override
	public synchronized Uri insert(Uri uri, ContentValues initialValues) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		String tableName = KeyToTableName.get(uriMatcher.match(uri));

		// android.os.Debug.waitForDebugger();

		if (tableName == null) {
			List<String> list = uri.getPathSegments();
			if (list.size() > 0) {
				tableName = list.get(0);
			} else {
				return null;
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
		return null;
	}

	@Override
	public synchronized Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		// android.os.Debug.waitForDebugger();

		SQLiteDatabase db = dbHelper.getWritableDatabase();

		if (RAW_QUERY_CONTENT_URI.equals(uri)) {
			return db.rawQuery(selection, null);
		}

		if (EXEC_QUERY_CONTENT_URI.equals(uri)) {
			db.execSQL(selection);
			return null;
		}

		if (QUERY_CONTENT_URI.equals(uri)) {
			return db.query(selection, null, null, null, null, null, null);
		}

		String tableName = KeyToTableName.get(uriMatcher.match(uri));

		if (tableName == null) {
			return null;
		}

		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(tableName);

		Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);

		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public synchronized int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
		// android.os.Debug.waitForDebugger();

		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int count = -1;
		String tableName = KeyToTableName.get(uriMatcher.match(uri));
		if (tableName == null)
			return -1;
		count = db.update(tableName, values, where, whereArgs);
		if (count == 0) {
			db.insert(tableName, null, values);
			count = 1;
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	public static int updateColumn(Context c, String tableName, ContentValues cv, String[] key, String[] keyValue, String[][] value) {
		// android.os.Debug.waitForDebugger();
		TelephonyManager tm = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
		String phoneID = tm.getDeviceId();
		String date = Global.normalDateFormat.format(new Date());

		Uri uri = getUriForTableName(tableName);

		if ((uri == null) || (phoneID == null) || (date == null) || (cv == null)) {
			return -1;
		}
		String whereCondition = "";

		for (String s : key) {
			whereCondition += String.format("%s=? and ", s);
		}
		for (String s[] : value)
			cv.put(s[0], s[1]);
		cv.put(DatabaseDictionary.LAST_MOD_DATE, lastModDateFormatter().format(new Date()));

		return c.getContentResolver().update(uri, cv, whereCondition, keyValue);
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

	public static String exportLogStatDB() {
		InputStream myInput;
		try {
			myInput = new FileInputStream(DatabaseDictionary.internalDBPathFile);
			FileHelper.createDir(DatabaseDictionary.externalDBPath);
			OutputStream myOutput = FileHelper
					.openFileForWriting(DatabaseDictionary.externalDBPathFile, DatabaseDictionary.externalDBPathFile, false);
			byte[] buffer = new byte[8192];
			int length;
			while ((length = myInput.read(buffer)) > 0) {
				myOutput.write(buffer, 0, length);
			}
			myOutput.flush();
			myInput.close();
			myOutput.close();
			return "Internal database exported to SDcard";
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "Can't find internal database";
		} catch (IOException e) {
			e.printStackTrace();
			return "Can't read internal database or write SD card";
		} catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}
	}
	public static Boolean importLogStatDB(Context context) {
		InputStream myInput;
		try {
			myInput = new FileInputStream(FileHelper.getSDCard().getAbsolutePath() + "/"
					+ DatabaseDictionary.externalDBPathFile);
			OutputStream myOutput = new BufferedOutputStream(new FileOutputStream(
					DatabaseDictionary.internalDBPathFile, false));
			byte[] buffer = new byte[8192];
			int length;
			while ((length = myInput.read(buffer)) > 0) {
				myOutput.write(buffer, 0, length);
			}
			myOutput.flush();
			myInput.close();
			myOutput.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} 
	}

	public static Cursor rawQuery(Context c, String sql) {
		return c.getContentResolver().query(RAW_QUERY_CONTENT_URI, null, sql, null, null);
	}

	public static void execSQL(Context c, String sql) {
		c.getContentResolver().query(EXEC_QUERY_CONTENT_URI, null, sql, null, null);
	}

	public static Cursor query(Context c, String sql) {
		return c.getContentResolver().query(QUERY_CONTENT_URI, null, sql, null, null);
	}

	private static void setLastSyncTime(Context c) {
		// TODO Auto-generated method stub
		SharedPreferences data = c.getSharedPreferences("SyncData", Context.MODE_PRIVATE);
		data.edit().putLong("LastSyncTime", System.currentTimeMillis()).commit();
	}

	public static long getLastSyncTime(Context c) {
		SharedPreferences data = c.getSharedPreferences("SyncData", Context.MODE_PRIVATE);
		return data.getLong("LastSyncTime", -1);
	}

	private static String getComprehensiveNull(Cursor cursor, String column) {
		String result = cursor.getString(cursor.getColumnIndex(column));

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