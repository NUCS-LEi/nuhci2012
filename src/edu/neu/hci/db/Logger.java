package edu.neu.hci.db;

public class Logger {
	private String moduleName;
	private String tableName;
	private String[] cols;
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String[] getCols() {
		return cols;
	}
	public void setCols(String[] cols) {
		this.cols = cols;
	}
	public Logger(String moduleName, String tableName, String[] cols) {
		super();
		this.moduleName = moduleName;
		this.tableName = tableName;
		this.cols = cols;
	}
	public Logger() {
		super();
	}
}
