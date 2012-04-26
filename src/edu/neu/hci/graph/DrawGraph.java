/**
 * Copyright (C) 2009, 2010 SC 4ViewSoft SRL
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.neu.hci.graph;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.LineChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import edu.neu.hci.Global;
import edu.neu.hci.db.DBAccessHelper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;

/**
 * Average temperature demo chart.
 */
public class DrawGraph extends AbstractDemoChart {
	/**
	 * Returns the chart name.
	 * 
	 * @return the chart name
	 */
	public String getName() {
		return "Sleep Graph";
	}

	/**
	 * Returns the chart description.
	 * 
	 * @return the chart description
	 */
	public String getDesc() {
		return "";
	}

	/**
	 * Executes the chart demo.
	 * 
	 * @param context
	 *            the context
	 * @return the built intent
	 */
	public Intent execute(Context context, GraphData data) {
		Intent intent = null;
		// value data
		List<double[]> dataList = data.getDataList();
		double[] time = dataList.get(0);
		double[] value = dataList.get(1);
		double finishDate = time[time.length - 1];
		double upperBound0 = value[0];
		double lowerBound0 = value[0];
		for (int i = 0; i < value.length; i++) {
			upperBound0 = upperBound0 < value[i] ? value[i] : upperBound0;
			lowerBound0 = lowerBound0 > value[i] ? value[i] : lowerBound0;
		}

		List<double[]> x = new ArrayList<double[]>();
		x.add(time);
		List<double[]> values = new ArrayList<double[]>();
		values.add(value);
		List<String> typeList = new ArrayList<String>();
		typeList.add(LineChart.TYPE);
		List<String> titleList = new ArrayList<String>();
		titleList.add("Sleep Phrase");
		List<Integer> colorList = new ArrayList<Integer>();
		colorList.add(Color.YELLOW);
		List<PointStyle> styleList = new ArrayList<PointStyle>();
		styleList.add(PointStyle.POINT);
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer(2);

		String[] types = new String[typeList.size()];
		String[] titles = new String[titleList.size()];
		int[] colors = new int[colorList.size()];
		PointStyle[] styles = new PointStyle[styleList.size()];
		for (int i = 0; i < typeList.size(); i++) {
			titles[i] = titleList.get(i);
			types[i] = typeList.get(i);
			colors[i] = colorList.get(i);
			styles[i] = styleList.get(i);
		}
		setRenderer(renderer, colors, styles);
		int length = renderer.getSeriesRendererCount();
		for (int i = 0; i < length; i++) {
			((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
		}
		setChartSettings(renderer, "Sleep Reported", "Sleep Time", "Phrase", 0, finishDate + 10, lowerBound0 - 10, upperBound0 + 10, Color.LTGRAY,
				Color.LTGRAY);
		renderer.setXLabels(12);
		renderer.setYLabels(10);
		renderer.setShowGrid(true);
		renderer.setXLabelsAlign(Align.CENTER);
		renderer.setYLabelsAlign(Align.CENTER);
		renderer.setZoomButtonsVisible(true);
		renderer.setXAxisMin(0, 0);
		XYMultipleSeriesDataset dataset = buildDataset(titles, x, values);
		intent = ChartFactory.getCombinedXYChartIntent(context, dataset, renderer, types, "Sleep Reported");
		return intent;

	}

	public GraphicalView getGraphView(Context context, GraphData data) {
		GraphicalView gv = null;
		// value data
		List<double[]> dataList = data.getDataList();
		double[] time = dataList.get(0);
		double[] value = dataList.get(1);
		double finishDate = time[time.length - 1];
		double upperBound0 = value[0];
		double lowerBound0 = value[0];
		for (int i = 0; i < value.length; i++) {
			upperBound0 = upperBound0 < value[i] ? value[i] : upperBound0;
			lowerBound0 = lowerBound0 > value[i] ? value[i] : lowerBound0;
		}

		List<double[]> x = new ArrayList<double[]>();
		x.add(time);
		List<double[]> values = new ArrayList<double[]>();
		values.add(value);
		List<String> typeList = new ArrayList<String>();
		typeList.add(LineChart.TYPE);
		List<String> titleList = new ArrayList<String>();
		titleList.add("Sleep Phrase");
		List<Integer> colorList = new ArrayList<Integer>();
		colorList.add(Color.YELLOW);
		List<PointStyle> styleList = new ArrayList<PointStyle>();
		styleList.add(PointStyle.POINT);
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer(2);

		String[] types = new String[typeList.size()];
		String[] titles = new String[titleList.size()];
		int[] colors = new int[colorList.size()];
		PointStyle[] styles = new PointStyle[styleList.size()];
		for (int i = 0; i < typeList.size(); i++) {
			titles[i] = titleList.get(i);
			types[i] = typeList.get(i);
			colors[i] = colorList.get(i);
			styles[i] = styleList.get(i);
		}
		setRenderer(renderer, colors, styles);
		int length = renderer.getSeriesRendererCount();
		for (int i = 0; i < length; i++) {
			((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
		}
		setChartSettings(renderer, "Sleep Report", "Sleep Time", "Phrase", 0, finishDate + 10, lowerBound0 - 10, upperBound0 + 10, Color.LTGRAY,
				Color.LTGRAY);
		renderer.setXLabels(0);
		Date date = null;
		try {
			date = Global.lastModDateFormat.parse(DBAccessHelper.getLastNightSleepTime(context));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < time.length; i++) {
			if (i % 60 == 0) {
				date.setTime(date.getTime() + i * 60 * 1000);
				renderer.addXTextLabel(i, Global.apmDateFormat.format(date));
			}
		}
		renderer.setYLabels(0);
		renderer.addYTextLabel(Global.DEEP_SLEEP, "Deep");
		renderer.addYTextLabel(Global.LIGHT_SLEEP_THRESHOLD, "Light");
		renderer.addYTextLabel(Global.WAKE_THRESHOLD, "Wake");
		renderer.setShowGrid(true);
		renderer.setXLabelsAlign(Align.CENTER);
		renderer.setYLabelsAlign(Align.CENTER);
		renderer.setZoomButtonsVisible(true);
		renderer.setXAxisMin(0, 0);
		XYMultipleSeriesDataset dataset = buildDataset(titles, x, values);
		gv = ChartFactory.getCombinedXYChartView(context, dataset, renderer, types);
		return gv;

	}
}
