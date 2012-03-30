package edu.neu.hci.db;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;

import android.os.Environment;

public class DatabaseDictionary {
	// public final static String STAT_DATABASE_NAME = "city_stat_db";
	public final static int DATABASE_VERSION = 1;
	public static SimpleDateFormat normalDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd");
	public static SimpleDateFormat priorDateFormat = new SimpleDateFormat(
			"MM/dd/yyyy");
	public static SimpleDateFormat lastModDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat exactDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss SSS", Locale.US);
	public static String LAST_MOD_DATE = "LastModDate";

	public static String PACKAGE_NAME = "edu.mit.android.cityver1";
	public static String internalDBPath = "/data"
			+ Environment.getDataDirectory().getAbsolutePath() + "/"
			+ PACKAGE_NAME + "/databases/";
	public static String internalDBFile = "city_statdb_2.sqlite";
	public static String externalDBPath = "/.city/data/statdb/";
	public static String externalDBFile = "city_statdb_2.sqlite";
	public static final String STAT_DATABASE_NAME = "city_statdb_2.sqlite";
	public static String INTERNAL_CP_PathFile = internalDBPath
			+ STAT_DATABASE_NAME;
	public static String EXTERNAL_CP_PathFile = externalDBPath
			+ STAT_DATABASE_NAME;
	public static String internalDBPathFile = INTERNAL_CP_PathFile;
	public static String externalDBPathFile = EXTERNAL_CP_PathFile;
	

	public final static String SYNC_TIME_TABLE = "sync_time";

	public static SimpleDateFormat sqliteFormat = new SimpleDateFormat(
			"yyyy-MM-dd");
	public static SimpleDateFormat serverFormat = new SimpleDateFormat(
			"MM/dd/yyyy");

	public final static String COL_SPLIT = "@c@";
	public final static String ROW_SPLIT = "@r@";
	public final static String PAGE_SPLIT = "@p@";

	public static String[] SyncTables = new String[] { "stats", "stats_ext",
			"imei", "cityschedule", "cityschedule_message", "buddy_user_answer", "buddy_user_quiz"};
	public static final String DAYNUM = "DayNum";
	public static final String LEVEL4FOODENTRYPROMPTINTERVAL = "Leve4FoodEntryPromptInterval";
	public static final String LEVEL4FOODENTRYSINGLEPROMPTTIME = "Level4FoodEntrySinglePromptTime";
	public static final String SSBTRACKERPROMPTINTERVAL = "SSBTrackerPromptInterval";
	public static final String SSBTRACKERSINGLEPROMPTTIME = "SSBTrackerSinglePromptTime";
	public static final String MEATTRACKERPROMPTINTERVAL = "MeatTrackerPromptInterval";
	public static final String MEATTRACKERSINGLEPROMPTTIME = "MeatTrackerSinglePromptTime";
	public static final String FRUITSTRACKERPROMPTINTERVAL = "FruitsTrackerPromptInterval";
	public static final String FRUITSTRACKERSINGLEPROMPTTIME = "FruitsTrackerSinglePromptTime";
	public static final String VEGGIESTRACKERPROMPTINTERVAL = "VeggiesTrackerPromptInterval";
	public static final String VEGGIESTRACKERSINGLEPROMPTTIME = "VeggiesTrackerSinglePromptTime";
	public static final String PATRACKERPROMPTINTERVAL = "PATrackerPromptInterval";
	public static final String PATRACKERSINGLEPROMPTTIME = "PATrackerSinglePromptTime";
	public static final String WEIGHTTRACKERPROMPTINTERVAL = "WeightTrackerPromptInterval";
	public static final String WEIGHTTRACKERSINGLEPROMPTTIME = "WeightTrackerSinglePromptTime";
	public static final String LEVELGOALPROMPTINTERVAL = "Level1GoalPromptInterval";
	public static final String LEVELGOALSINGLEPROMPTTIME = "Level1GoalTrackerSinglePromptTime";
	public static final String LEVEL3BARRIERSPROMPTINTERVAL = "Level3BarriersPromptInterval";
	public static final String LEVEL3BARRIERSSINGLEPROMPTTIME = "Level3BarriersSinglePromptTime";
	public static final String FOODQUIZPROMPTINTERVAL = "FoodQuizPromptInterval";
	public static final String FOODQUIZSINGLEPROMPTTIME = "FoodQuizSinglePromptTime";
	public static final String HEALTHYMEALTRACKERPROMPTINTERVAL = "HealthyMealTrackerPromptInterval";
	public static final String HEALTHYMEALTRACKERSINGLEPROMPTTIME = "HealthyMealTrackerSinglePromptTime";
	public static final String STEPLIVELYPROMPTINTERVAL = "StepLivelyPromptInterval";
	public static final String STEPLIVELYSINGLEPROMPTTIME = "StepLivelySinglePromptTime";
	public static final String WEEKMESSAGEID = "WeekMessageID";
	public static final String LEVEL1GOALAVAILABLE = "Level1GoalAvailable";
	public static final String TUTORIALPLAYERAVAILABLE = "TutorialPlayerAvailable";
	public static final String LEVEL3PAAVAILABLE = "Level3PAAvailable";
	public static final String LEVEL3SSBAVAILABLE = "Level3SSBAvailable";
	public static final String LEVEL3VEGGIESAVAILABLE = "Level3VeggiesAvailable";
	public static final String LEVEL3MEATAVAILABLE = "Level3MeatAvailable";
	public static final String LEVEL3BARRIERSAVAILABLE = "Level3BarriersAvailable";
	public static final String LEVEL3FRUITSAVAILABLE = "Level3FruitsAvailable";
	public static final String LEVEL4FOODENTRYAVAILABLE = "Level4FoodEntryAvailable";
	public static final String FOODQUIZAVAILABLE = "FoodQuizAvailable";
	public static final String HEALTHYMEALTRACKERAVAILABLE = "HealthyMealTrackerAvailable";
	public static final String LEVEL2WEIGHTAVAILABLE = "Level2WeightAvailable";
	public static final String GUIDELINESAVAILABLE = "GuideLinesAvailable";
	public static final String GETHELPAVAILABLE = "GetHelpAvailable";
	public static final String SENDCOMMENTSAVAILABLE = "SendCommentsAvailable";
	public static final String STEPLIVELYAVAILABLE = "StepLivelyAvailable";
	public static final String WEIGHTGRAPHAVAILABLE = "WeightGraphAvailable";
	public static final String FRIDGEBUTLERAVAILABLE = "FridgeButlerAvailable";

	public static String[][] tableParams = new String[][] {
			{
					"imei",
					"Cohort TEXT DEFAULT NULL,"
							+ "ParticipantName TEXT DEFAULT NULL,"
							+ "ParticipantID TEXT DEFAULT NULL,"
							+ "DateOfRV TEXT DEFAULT NULL,"
							+ "UserCondition TEXT DEFAULT NULL,"
							+ "ScaleLot TEXT DEFAULT NULL,"
							+ "StudyPhoneNum TEXT DEFAULT NULL,"
							+ "IMEI TEXT DEFAULT NULL,"
							+ "MSN TEXT DEFAULT NULL,"
							+ "PhoneNum TEXT DEFAULT NULL,"
							+ "Email TEXT DEFAULT NULL,"
							+ "GroupSession TEXT DEFAULT NULL,"
							+ "MonthlyCall TEXT DEFAULT NULL,"
							+ "NextContact TEXT DEFAULT NULL,"
							+ "CurrentWeight TEXT DEFAULT NULL,"
							+ "GoalWeight TEXT DEFAULT NULL,"
							+ "Notes TEXT DEFAULT NULL,"
							+ "LastModDate TEXT DEFAULT NULL,"
							+ "PRIMARY KEY (ParticipantID,IMEI)"},
			{
					"stats",
					"PhoneID TEXT NOT NULL," + "CreateDate TEXT NOT NULL,"
							+ "DaysFromStart INTEGER DEFAULT NULL,"
							+ "UserCondition TEXT DEFAULT NULL,"
							+ "Wallpaper TEXT DEFAULT NULL,"
							+ "NumPrompts TEXT DEFAULT 0,"
							+ "NumPostponed TEXT DEFAULT 0,"
							+ "NumFoodsEntered TEXT DEFAULT 0,"
							+ "TotalCalories TEXT DEFAULT 0,"
							+ "AverageWeight TEXT DEFAULT 0,"
							+ "WeightChange TEXT DEFAULT 0,"
							+ "MinsPhyActivity TEXT DEFAULT 0,"
							+ "LastModDate TEXT DEFAULT NULL,"
							+ "PRIMARY KEY (PhoneID,CreateDate)" },
			{
					"stats_ext",
					"PhoneID TEXT NOT NULL," + "CreateDate TEXT NOT NULL,"
							+ "TutorialViewed TEXT DEFAULT 0,"
							+ "RightNowInCITYViewed TEXT DEFAULT 0,"
							+ "UpdateChecked TEXT DEFAULT 0,"
							+ "NewsViewed TEXT DEFAULT 0,"
							+ "ViewGuidelines TEXT DEFAULT 0,"
							+ "NewGoalSet TEXT DEFAULT 0,"
							+ "GoalsCheckedOff TEXT DEFAULT 0,"
							+ "TotalFeedbackAttempts TEXT DEFAULT 0,"
							+ "Level3BarriersUsedOrNoneClicked TEXT DEFAULT 0,"
							+ "Level3FruitsDoneClicked TEXT DEFAULT 0,"
							+ "Level3MeatDoneClicked TEXT DEFAULT 0,"
							+ "Level3PADoneClicked TEXT DEFAULT 0,"
							+ "Level3SSBDoneClicked TEXT DEFAULT 0,"
							+ "Level3VeggiesDoneClicked TEXT DEFAULT 0,"
							+ "HelpRequestSendAttempts TEXT DEFAULT 0,"
							+ "MainCITYHomeScreenUsed TEXT DEFAULT 0,"
							+ "Level3BarriersUsed TEXT DEFAULT 0,"
							+ "Level3FruitsUsed TEXT DEFAULT 0,"
							+ "Level3MeatUsed TEXT DEFAULT 0,"
							+ "Level3PAUsed TEXT DEFAULT 0,"
							+ "Level3SSBUsed TEXT DEFAULT 0,"
							+ "Level3VeggiesUsed TEXT DEFAULT 0,"
							+ "StepLivelyUsed TEXT DEFAULT 0,"
							+ "LastModDate TEXT DEFAULT NULL,"
							+ "PRIMARY KEY (PhoneID,CreateDate)" },
			{
					"cityschedule",
					"DayNum integer NOT NULL,"
							+ "Level4FoodEntryPromptInterval TEXT DEFAULT '0',"
							+ "Level4FoodEntrySinglePromptTime TEXT DEFAULT NULL,"
							+ "SSBTrackerPromptInterval TEXT DEFAULT '0',"
							+ "SSBTrackerSinglePromptTime TEXT DEFAULT NULL,"
							+ "MeatTrackerPromptInterval TEXT DEFAULT '0',"
							+ "MeatTrackerSinglePromptTime TEXT DEFAULT NULL,"
							+ "FruitsTrackerPromptInterval TEXT DEFAULT '0',"
							+ "FruitsTrackerSinglePromptTime TEXT DEFAULT NULL,"
							+ "VeggiesTrackerPromptInterval TEXT DEFAULT '0',"
							+ "VeggiesTrackerSinglePromptTime TEXT DEFAULT NULL,"
							+ "PATrackerPromptInterval TEXT DEFAULT '0',"
							+ "PATrackerSinglePromptTime TEXT DEFAULT NULL,"
							+ "WeightTrackerPromptInterval TEXT DEFAULT '0',"
							+ "WeightTrackerSinglePromptTime TEXT DEFAULT NULL,"
							+ "Level1GoalPromptInterval TEXT DEFAULT '0',"
							+ "Level1GoalTrackerSinglePromptTime TEXT DEFAULT NULL,"
							+ "Level3BarriersPromptInterval TEXT DEFAULT '0',"
							+ "Level3BarriersSinglePromptTime TEXT  DEFAULT NULL,"
							+ "FoodQuizPromptInterval TEXT DEFAULT '0',"
							+ "FoodQuizSinglePromptTime TEXT DEFAULT NULL,"
							+ "HealthyEatingTrackerPromptInterval TEXT DEFAULT '0',"
							+ "HealthyEatingTrackerSinglePromptTime TEXT DEFAULT NULL,"
							+ "StepLivelyPromptInterval TEXT DEFAULT '0',"
							+ "StepLivelySinglePromptTime TEXT DEFAULT NULL,"
							+ "WeekMessageID TEXT DEFAULT '0',"
							+ "Level1GoalAvailable TEXT DEFAULT '0',"
							+ "TutorialPlayerAvailable TEXT DEFAULT '0',"
							+ "Level3PAAvailable TEXT DEFAULT '0',"
							+ "Level3SSBAvailable TEXT DEFAULT '0',"
							+ "Level3VeggiesAvailable TEXT DEFAULT '0',"
							+ "Level3MeatAvailable TEXT DEFAULT '0',"
							+ "Level3BarriersAvailable TEXT DEFAULT '0',"
							+ "Level3FruitsAvailable TEXT DEFAULT '0',"
							+ "Level4FoodEntryAvailable TEXT DEFAULT '0',"
							+ "FoodQuizAvailable TEXT DEFAULT '0',"
							+ "HealthyEatingTrackerAvailable TEXT DEFAULT '0',"
							+ "Level2WeightAvailable TEXT DEFAULT '0',"
							+ "GuideLinesAvailable TEXT DEFAULT '0',"
							+ "GetHelpAvailable TEXT DEFAULT '0',"
							+ "SendCommentsAvailable TEXT DEFAULT '0',"
							+ "StepLivelyAvailable TEXT DEFAULT '0',"
							+ "WeightGraphAvailable TEXT DEFAULT '0',"
							+ "FridgeButlerAvailable TEXT DEFAULT '0',"
							+ "LastModDate TEXT DEFAULT NULL,"
							+ "PRIMARY KEY (DayNum)" },
			{
					"cityschedule_message",
					"ID integer NOT NULL PRIMARY KEY UNIQUE,"
							+ "WeekMessage TEXT DEFAULT NULL,"
							+ "LastModDate TEXT DEFAULT NULL" },
			{
					"sync_time",
					"PhoneID TEXT NOT NULL," + "TableName TEXT DEFAULT NULL,"
							+ "LastSyncTime TEXT DEFAULT NULL,"
							+ "PRIMARY KEY (PhoneID,TableName)" },
			{
					"logging_time",
					"PhoneID TEXT NOT NULL," + "AppID TEXT NOT NULL,"
							+ "LastLogTime TEXT DEFAULT NULL,"
							+ "PRIMARY KEY (PhoneID,AppID)" },
			{
					"log_message",
					"PhoneID TEXT NOT NULL," + "LogTime TEXT NOT NULL,"
							+ "Module TEXT NOT NULL,"
							+ "LogMessage TEXT DEFAULT NULL,"
							+ "LastModDate TEXT DEFAULT NULL,"
							+ "PRIMARY KEY (PhoneID,LogTime)" },
			
			{
					"buddy_user_answer",
					"ParticipantID TEXT NOT NULL," + "BuddyID EXT NOT NULL,"
							+ "QuizID TEXT NOT NULL,"
							+ "AnswerID TEXT DEFAULT NULL,"
							+ "CustomAnswer TEXT DEFAULT NULL,"				
							+ "IsCurrent TEXT DEFAULT NULL,"							
							+ "LastModDate TEXT DEFAULT NULL,"	
							+ "PRIMARY KEY (ParticipantID,QuizID,BuddyID)"},
			{
					"buddy_user_quiz",
					"QuizID TEXT NOT NULL," + "ParticipantID TEXT NOT NULL,"
							+ "BuddyID TEXT NOT NULL,"
							+ "SuggestedToBuddy TEXT DEFAULT NULL,"							
							+ "LastModDate TEXT DEFAULT NULL,"	
							+ "PRIMARY KEY (QuizID,ParticipantID,BuddyID)"			},
	};

	public static HashMap<String, String[]> getTableCols() {
		HashMap<String, String[]> tableCols = new HashMap<String, String[]>();
		String[] imeiCols = { "Cohort", "ParticipantName", "ParticipantID",
				"DateOfRV", "UserCondition", "ScaleLot", "StudyPhoneNum",
				"IMEI", "MSN", "PhoneNum", "Email", "GroupSession",
				"MonthlyCall", "NextContact", "CurrentWeight", "GoalWeight",
				"Notes", "LastModDate" };
		String[] statsCols = { "PhoneID", "CreateDate", "DaysFromStart",
				"UserCondition", "Wallpaper", "NumPrompts", "NumPostponed",
				"NumFoodsEntered", "TotalCalories", "AverageWeight",
				"WeightChange", "MinsPhyActivity", "LastModDate" };
		String[] stats_extCols = { "PhoneID", "CreateDate", "TutorialViewed",
				"RightNowInCITYViewed", "UpdateChecked", "NewsViewed",
				"ViewGuidelines", "NewGoalSet", "GoalsCheckedOff",
				"TotalFeedbackAttempts", "Level3BarriersUsedOrNoneClicked",
				"Level3FruitsDoneClicked", "Level3MeatDoneClicked",
				"Level3PADoneClicked", "Level3SSBDoneClicked",
				"Level3VeggiesDoneClicked", "HelpRequestSendAttempts",
				"MainCITYHomeScreenUsed", "Level3BarriersUsed",
				"Level3FruitsUsed", "Level3MeatUsed", "Level3PAUsed",
				"Level3SSBUsed", "Level3VeggiesUsed"};
		String[] cityscheduleCols = { "DayNum", "Leve4FoodEntryPromptInterval",
				"Level4FoodEntrySinglePromptTime", "SSBTrackerPromptInterval",
				"SSBTrackerSinglePromptTime", "MeatTrackerPromptInterval",
				"MeatTrackerSinglePromptTime", "FruitsTrackerPromptInterval",
				"FruitsTrackerSinglePromptTime",
				"VeggiesTrackerPromptInterval",
				"VeggiesTrackerSinglePromptTime", "PATrackerPromptInterval",
				"PATrackerSinglePromptTime", "WeightTrackerPromptInterval",
				"WeightTrackerSinglePromptTime", "Level1GoalPromptInterval",
				"Level1GoalTrackerSinglePromptTime",
				"Level3BarriersPromptInterval",
				"Level3BarriersSinglePromptTime", "FoodQuizPromptInterval",
				"FoodQuizSinglePromptTime",
				"HealthyEatingTrackerPromptInterval",
				"HealthyEatingTrackerSinglePromptTime",
				"StepLivelyPromptInterval", "StepLivelySinglePromptTime",
				"WeekMessageID", "Level1GoalAvailable",
				"TutorialPlayerAvailable", "Level3PAAvailable",
				"Level3SSBAvailable", "Level3VeggiesAvailable",
				"Level3MeatAvailable", "Level3BarriersAvailable",
				"Level3FruitsAvailable", "Level4FoodEntryAvailable",
				"FoodQuizAvailable", "HealthyEatingTrackerAvailable",
				"Level2WeightAvailable", "GuideLinesAvailable",
				"GetHelpAvailable", "SendCommentsAvailable",
				"StepLivelyAvailable", "WeightGraphAvailable",
				"FridgeButlerAvailable", "LastModDate" };
		String[] sync_timeCols = { "PhoneID", "TableName", "LastSyncTime" };
		String[] log_messageCols = { "PhoneID", "LogTime", "Module",
				"LogMessage", "LastModDate" };
		String[] cityschedulemsgCols = { "id", "WeekMessage", "LastModDate" };
		String[] schema_updateCols = { "TableName", "UpdateTime" };
		String[] buddy_user_answerCols = {"ParticipantID", "BuddyID", "QuizID", "AnswerID", "CustomAnswer", "IsCurrent", "LastModDate"};
		String[] buddy_user_quizCols = {"QuizID", "ParticipantID", "BuddyID", "SuggestedToBuddy", "LastModDate"};
		tableCols.put("imei", imeiCols);
		tableCols.put("stats", statsCols);
		tableCols.put("stats_ext", stats_extCols);
		tableCols.put("cityschedule", cityscheduleCols);
		tableCols.put("cityschedule_message", cityschedulemsgCols);
		tableCols.put("sync_time", sync_timeCols);
		tableCols.put("log_message", log_messageCols);
		tableCols.put("buddy_user_answer", buddy_user_answerCols);
		tableCols.put("buddy_user_quiz", buddy_user_quizCols);
		return tableCols;
	}

	public static HashMap<String, String[]> getTableKeys() {
		HashMap<String, String[]> tableCols = new HashMap<String, String[]>();
		String[] imeiCols = { "Cohort", "ParticipantName", "ParticipantID",
				"DateOfRV", "UserCondition", "ScaleLot", "StudyPhoneNum",
				"IMEI", "MSN", "PhoneNum", "Email", "GroupSession",
				"MonthlyCall", "NextContact", "CurrentWeight", "GoalWeight",
				"Notes", "LastModDate" };
		String[] statsCols = { "PhoneID", "CreateDate" };
		String[] stats_extCols = { "PhoneID", "CreateDate" };
		String[] cityscheduleCols = { "DayNum" };
		String[] cityschedulemsgCols = { "ID" };
		String[] sync_timeCols = { "PhoneID", "TableName" };
		String[] log_messageCols = { "PhoneID", "LogTime" };
		String[] schema_updateCols = { "TableName" };
		String[] buddy_user_answerCols = {"QuizID", "AnswerID"};
		String[] buddy_user_quizCols = {"QuizID"};
		tableCols.put("imei", imeiCols);
		tableCols.put("stats", statsCols);
		tableCols.put("stats_ext", stats_extCols);
		tableCols.put("cityschedule", cityscheduleCols);
		tableCols.put("cityschedule_message", cityschedulemsgCols);
		tableCols.put("sync_time", sync_timeCols);
		tableCols.put("log_message", log_messageCols);
		tableCols.put("buddy_user_answer", buddy_user_answerCols);
		tableCols.put("buddy_user_quiz", buddy_user_quizCols);
		return tableCols;
	}
}
