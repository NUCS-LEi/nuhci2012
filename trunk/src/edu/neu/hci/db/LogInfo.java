package edu.neu.hci.db;

import java.util.ArrayList;
import java.util.Arrays;

import android.os.Bundle;

public class LogInfo {
	private String tableName;
	private String[][] keySet;
	private String[] colNames;
	private String[] values;
	private String specQueryStatement;
	private String[] additionalQuery;

	public LogInfo(String tableName, String[][] keySet, String[] colNames, String[] values,
			String specQueryStatement, String[] additionalQuery) {
		this.tableName = tableName;
		this.keySet = keySet;
		this.colNames = colNames;
		this.values = values;
		this.specQueryStatement = specQueryStatement;
		this.additionalQuery = additionalQuery;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String[][] getKeySet() {
		return keySet;
	}

	public void setKeySet(String[][] keySet) {
		this.keySet = keySet;
	}

	public String[] getColNames() {
		return colNames;
	}

	public void setColNames(String[] colNames) {
		this.colNames = colNames;
	}

	public String[] getValues() {
		return values;
	}

	public void setValues(String[] values) {
		this.values = values;
	}

	public String getSpecQueryStatement() {
		return specQueryStatement;
	}

	public void setSpecQueryStatement(String specQueryStatement) {
		this.specQueryStatement = specQueryStatement;
	}

	public String[] getAdditionalQuery() {
		return additionalQuery;
	}

	public void setAdditionalQuery(String[] additionalQuery) {
		this.additionalQuery = additionalQuery;
	}
	
	public static boolean bothNullOrEquals(String s1, String s2) {
		return ((s1 == null) && (s2 == null))
				|| (s1.equals(s2));
	}
	
	private static final String KeySetKey = "KeySetKey";
	private static final String KeySetCount = "KeySetCount";
	private static final String AdditionalQueryKey = "AdditionalQueryKey";
	private static final String ColNamesKey = "ColNamesKey";
	private static final String SpecQueryStatementKey = "SpecQueryStatementKey";
	private static final String TableNameKey = "TableNameKey";
	private static final String ValuesKey = "ValuesKey";
	
	public Bundle serializeToBundle() {
		Bundle result = new Bundle();
		result.putString(TableNameKey, this.tableName);
		result.putString(SpecQueryStatementKey, this.specQueryStatement);
		result.putStringArray(ColNamesKey, this.colNames);
		result.putStringArray(ValuesKey, this.values);
		result.putStringArray(AdditionalQueryKey, this.additionalQuery);
		if (this.keySet != null) {
			result.putInt(KeySetCount, this.keySet.length);
			for (int i = 0; i < this.keySet.length; i++) {
				result.putStringArray(KeySetKey + i, this.keySet[i]);
			}
		}
		return result;
	}
	
	public static LogInfo createFromBundle(Bundle b) {
		LogInfo li = new LogInfo(b.getString(TableNameKey), null, b.getStringArray(ColNamesKey),
				b.getStringArray(ValuesKey), b.getString(SpecQueryStatementKey),
				b.getStringArray(AdditionalQueryKey));
		int length = b.getInt(KeySetCount, 0);
		if (length > 0) {
			ArrayList<String[]> keys = new ArrayList<String[]>();
			for (int i = 0; i < length; i++) {
				keys.add(b.getStringArray(KeySetKey + i));
			}
			li.setKeySet(keys.toArray(new String[][] {}));
		}
		return li;
	}
	
	@Override
	public boolean equals(Object o) {
		if ((o == null) || (!(o instanceof LogInfo))) { 
			return false;
		}
		LogInfo li = (LogInfo)o;
		return ((Arrays.deepEquals(this.additionalQuery, li.additionalQuery))
				&& (Arrays.deepEquals(this.colNames, li.colNames))
				&& (Arrays.deepEquals(this.keySet, li.keySet))
				&& (bothNullOrEquals(this.specQueryStatement, li.specQueryStatement))
				&& (bothNullOrEquals(this.tableName, li.tableName))
				&& (Arrays.deepEquals(this.values, li.values)));
	}
}