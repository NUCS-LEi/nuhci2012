package edu.neu.hci.db;

import java.util.ArrayList;

public class LoggerDictionary {
	public static final String HEALTHY_MEAL_TRACKER_USED = "HealthyMealTrackerUsed";
	public static final String CURRENT_BUDDY_ID="CurrentBuddyId";
	public static final String COMICSPOSITION = "ComicsPosition";
	public static final String BLOGPOSITION = "BlogPosition";
	public static final String MOVIESPOSITION = "MoviesPosition";
	public static String WALLPAPER = "Wallpaper";
	public static String NUM_PROMPTS = "NumPrompts";
	public static String NUM_POSTPONED = "NumPostponed";
	public static String NUM_FOODS_ENTERED = "NumFoodsEntered";
	public static String TOTAL_CALORIES = "TotalCalories";
	public static String AVERAGE_WEIGHT = "AverageWeight";
	public static String WEIGHT_CHANGE = "WeightChange";
	public static String MINS_PHY_ACTIVITY = "MinsPhyActivity";
	public static String TUTORIAL_VIEWED = "TutorialViewed";
	public static String RIGHTNOWINCITY_VIEWED = "RightNowInCITYViewed";
	public static String UPDATE_CHECKED = "UpdateChecked";
	public static String NEWS_VIEWED = "NewsViewed";
	public static String VIEW_GUIDELINES = "ViewGuidelines";
	public static String NEWGOAL_SET = "NewGoalSet";
	public static String GOALS_CHECKEDOFF = "GoalsCheckedOff";
	public static String TOTAL_FEEDBACK_ATTEMPTS = "TotalFeedbackAttempts";
	public static String LEVEL3BARRIERSUSEDORNONECLICK = "Level3BarriersUsedOrNoneClicked";
	public static String LEVEL3FRUITSDONECLICK = "Level3FruitsDoneClicked";
	public static String LEVEL3MEATDONECLICK = "Level3MeatDoneClicked";
	public static String LEVEL3PADONECLICK = "Level3PADoneClicked";
	public static String LEVEL3SSBDONECLICK = "Level3SSBDoneClicked";
	public static String LEVEL3VEGGIESDONECLICK = "Level3VeggiesDoneClicked";
	public static String HELPREQUESTSENDATTEMPTS = "HelpRequestSendAttempts";
	public static String MAINCITYHOMESCREENUSED = "MainCITYHomeScreenUsed";
	public static String LEVEL3BARRIERSUSED = "Level3BarriersUsed";
	public static String LEVEL3FRUITSUSED = "Level3FruitsUsed";
	public static String LEVEL3MEATUSED = "Level3MeatUsed";
	public static String LEVEL3PAUSED = "Level3PAUsed";
	public static String LEVEL3SSBUSED = "Level3SSBUsed";
	public static String LEVEL3VEGGIESUSED = "Level3VeggiesUsed";
	public static String STEPLIVELYUSED = "StepLivelyUsed";
	public static String STEPLIVELYPOINTS = "StepLivelyPoints";
	public static String STEPLIVELYACTIVITIESADDED = "StepLivelyActivitiesAdded";
	public static String PHONETYPE = "PhoneType";
	public static String OSVERSION = "AndroidOSVersion";
	public static String CITYVERSION = "CITYVersion";
	public static String PROFILEVIEWED = "ProfileViewed";
	public static String BUDDYPROFILEVIEWED = "BuddyProfileViewed";

	public static String[] LOG_OF_USEDS = new String[] {
			HELPREQUESTSENDATTEMPTS, MAINCITYHOMESCREENUSED,
			LEVEL3BARRIERSUSED, LEVEL3BARRIERSUSEDORNONECLICK,
			LEVEL3FRUITSUSED, LEVEL3MEATUSED, LEVEL3PAUSED, LEVEL3SSBUSED,
			LEVEL3VEGGIESUSED, STEPLIVELYUSED, HEALTHY_MEAL_TRACKER_USED, PROFILEVIEWED, BUDDYPROFILEVIEWED };

	public static ArrayList<Logger> getModuleLogList() {
		ArrayList<Logger> moduleLogList = new ArrayList<Logger>();
		Logger logger;
		logger = new Logger("Wallpaper", "stats", new String[] { "Wallpaper" });
		moduleLogList.add(logger);
		logger = new Logger("NumPrompts", "stats",
				new String[] { "NumPrompts" });
		moduleLogList.add(logger);
		logger = new Logger("NumPostponed", "stats",
				new String[] { "NumPostponed" });
		moduleLogList.add(logger);
		logger = new Logger("NumFoodsEntered", "stats",
				new String[] { "NumFoodsEntered" });
		moduleLogList.add(logger);
		logger = new Logger("TotalCalories", "stats",
				new String[] { "TotalCalories" });
		moduleLogList.add(logger);
		logger = new Logger("AverageWeight", "stats",
				new String[] { "AverageWeight" });
		moduleLogList.add(logger);
		logger = new Logger("WeightChange", "stats",
				new String[] { "WeightChange" });
		moduleLogList.add(logger);
		logger = new Logger("MinsPhyActivity", "stats",
				new String[] { "MinsPhyActivity" });
		moduleLogList.add(logger);
		logger = new Logger("TutorialViewed", "stats_ext",
				new String[] { "TutorialViewed" });
		moduleLogList.add(logger);
		logger = new Logger("RightNowInCITYViewed", "stats_ext",
				new String[] { "RightNowInCITYViewed" });
		moduleLogList.add(logger);
		logger = new Logger("UpdateChecked", "stats_ext",
				new String[] { "UpdateChecked" });
		moduleLogList.add(logger);
		logger = new Logger("NewsViewed", "stats_ext",
				new String[] { "NewsViewed" });
		moduleLogList.add(logger);
		logger = new Logger("ViewGuidelines", "stats_ext",
				new String[] { "ViewGuidelines" });
		moduleLogList.add(logger);
		logger = new Logger("NewGoalSet", "stats_ext",
				new String[] { "NewGoalSet" });
		moduleLogList.add(logger);
		logger = new Logger("GoalsCheckedOff", "stats_ext",
				new String[] { "GoalsCheckedOff" });
		moduleLogList.add(logger);
		logger = new Logger("TotalFeedbackAttempts", "stats_ext",
				new String[] { "TotalFeedbackAttempts" });
		moduleLogList.add(logger);
		logger = new Logger("Level3BarriersUsedOrNoneClicked", "stats_ext",
				new String[] { "Level3BarriersUsedOrNoneClicked" });
		moduleLogList.add(logger);
		logger = new Logger("Level3FruitsDoneClicked", "stats_ext",
				new String[] { "Level3FruitsDoneClicked" });
		moduleLogList.add(logger);
		logger = new Logger("Level3MeatDoneClicked", "stats_ext",
				new String[] { "Level3MeatDoneClicked" });
		moduleLogList.add(logger);
		logger = new Logger("Level3PADoneClicked", "stats_ext",
				new String[] { "Level3PADoneClicked" });
		moduleLogList.add(logger);
		logger = new Logger("Level3SSBDoneClicked", "stats_ext",
				new String[] { "Level3SSBDoneClicked" });
		moduleLogList.add(logger);
		logger = new Logger("Level3VeggiesDoneClicked", "stats_ext",
				new String[] { "Level3VeggiesDoneClicked" });
		moduleLogList.add(logger);
		logger = new Logger("HelpRequestSendAttempts", "stats_ext",
				new String[] { "HelpRequestSendAttempts" });
		moduleLogList.add(logger);
		logger = new Logger("MainCITYHomeScreenUsed", "stats_ext",
				new String[] { "MainCITYHomeScreenUsed" });
		moduleLogList.add(logger);
		logger = new Logger("Level3BarriersUsed", "stats_ext",
				new String[] { "Level3BarriersUsed" });
		moduleLogList.add(logger);
		logger = new Logger("Level3FruitsUsed", "stats_ext",
				new String[] { "Level3FruitsUsed" });
		moduleLogList.add(logger);
		logger = new Logger("Level3MeatUsed", "stats_ext",
				new String[] { "Level3MeatUsed" });
		moduleLogList.add(logger);
		logger = new Logger("Level3PAUsed", "stats_ext",
				new String[] { "Level3PAUsed" });
		moduleLogList.add(logger);
		logger = new Logger("Level3SSBUsed", "stats_ext",
				new String[] { "Level3SSBUsed" });
		moduleLogList.add(logger);
		logger = new Logger("Level3VeggiesUsed", "stats_ext",
				new String[] { "Level3VeggiesUsed" });
		moduleLogList.add(logger);
		logger = new Logger(LoggerDictionary.STEPLIVELYUSED, "stats_ext",
				new String[] { LoggerDictionary.STEPLIVELYUSED });
		moduleLogList.add(logger);
		logger = new Logger(LoggerDictionary.STEPLIVELYPOINTS, "stats_ext",
				new String[] { LoggerDictionary.STEPLIVELYPOINTS });
		moduleLogList.add(logger);
		logger = new Logger(LoggerDictionary.STEPLIVELYACTIVITIESADDED,
				"stats_ext",
				new String[] { LoggerDictionary.STEPLIVELYACTIVITIESADDED });
		moduleLogList.add(logger);
		logger = new Logger(LoggerDictionary.HEALTHY_MEAL_TRACKER_USED,
				"stats_ext",
				new String[] { LoggerDictionary.HEALTHY_MEAL_TRACKER_USED });
		moduleLogList.add(logger);
		logger = new Logger(LoggerDictionary.CURRENT_BUDDY_ID,
				"stats_ext",
				new String[] { LoggerDictionary.CURRENT_BUDDY_ID });
		moduleLogList.add(logger);
		logger = new Logger(LoggerDictionary.COMICSPOSITION,
				"stats_ext",
				new String[] { LoggerDictionary.COMICSPOSITION });
		moduleLogList.add(logger);
		logger = new Logger(LoggerDictionary.BLOGPOSITION,
				"stats_ext",
				new String[] { LoggerDictionary.BLOGPOSITION });
		moduleLogList.add(logger);
		logger = new Logger(LoggerDictionary.MOVIESPOSITION,
				"stats_ext",
				new String[] { LoggerDictionary.MOVIESPOSITION });
		moduleLogList.add(logger);
		logger = new Logger(LoggerDictionary.PHONETYPE,
				"stats_ext",
				new String[] { LoggerDictionary.PHONETYPE });
		moduleLogList.add(logger);
		logger = new Logger(LoggerDictionary.OSVERSION,
				"stats_ext",
				new String[] { LoggerDictionary.OSVERSION });
		moduleLogList.add(logger);
		logger = new Logger(LoggerDictionary.CITYVERSION,
				"stats_ext",
				new String[] { LoggerDictionary.CITYVERSION });
		moduleLogList.add(logger);
		logger = new Logger(LoggerDictionary.PROFILEVIEWED, "stats_ext",
				new String[] { LoggerDictionary.PROFILEVIEWED });
		moduleLogList.add(logger);
		logger = new Logger(LoggerDictionary.BUDDYPROFILEVIEWED, "stats_ext",
				new String[] { LoggerDictionary.BUDDYPROFILEVIEWED });
		moduleLogList.add(logger);
		return moduleLogList;
	}
}
