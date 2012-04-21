package edu.neu.hci.graph;

import android.os.Environment;

public class Constant{
	public static final String path = Environment.getExternalStorageDirectory().getAbsolutePath()
			+"/.city/data/level2weight/";
	public static final String fileName = "Level2-WeightTracking.log.csv";
	public static final String calPath = Environment.getExternalStorageDirectory().getAbsolutePath()
			+"/.city/logs/level4foodentry/";
	public static final String calFileName = "SmartListTracker.csv";
	public static final String healthTrackerPath = Environment.getExternalStorageDirectory().getAbsolutePath()
			+"/.city/logs/healthymealtracker/";
	public static final String healthTrackerName = "HealthyMealTracker.csv";
	public static final String PACKAGE_NAME = "edu.mit.android.cityver1";
	public static final String healthTrackerDBPath = "/data" + Environment.getDataDirectory().getAbsolutePath() + "/" + PACKAGE_NAME
			+ "/databases/MyDatabase";
	public static final String healthTrackerDBTableName = "foodtracking";
	public static final String stepLivelyPath = "/.city/data/steplively/";
}
