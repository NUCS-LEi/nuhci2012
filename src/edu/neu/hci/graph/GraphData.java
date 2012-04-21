package edu.neu.hci.graph;

import java.util.List;

public class GraphData{
	private List<double[]> dataList;
	private boolean[] entries;
	public boolean[] getEntries() {
		return entries;
	}
	public GraphData(List<double[]> dataList, boolean[] entries) {
		super();
		this.dataList = dataList;
		this.entries = entries;
	}
	public List<double[]> getDataList() {
		return dataList;
	}
}
