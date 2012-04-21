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
import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.LineChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.chart.ScatterChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYValueSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.util.Log;

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
    return "Weight Graph";
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
   * @param context the context
   * @return the built intent
   */
  public Intent execute(Context context,GraphData data) {
	  	Intent intent = null;
		//weight data
		List<double[]> dataList = data.getDataList();
		boolean[] entries = data.getEntries();
		double[] weightDays = dataList.get(0);
		double[] weight = dataList.get(1);		
		double finishDate = weightDays[weightDays.length-1];
		double[] calDays = null;
		
		if(entries[0]){
			calDays = dataList.get(2);
			finishDate = calDays[calDays.length-1]>weightDays[weightDays.length-1]?calDays[calDays.length-1]:weightDays[weightDays.length-1];
		}		
		double[] hmtDays = null;
		if(entries[1])
			hmtDays = dataList.get(3);
		double[] stepLivelyDays = null;
		if(entries[2])
			stepLivelyDays = dataList.get(4);
		
		double upperBound0 = weight[0];
		double lowerBound0 = weight[0];
		for (int i = 0; i < weight.length; i++) {
			upperBound0 = upperBound0<weight[i]?weight[i]:upperBound0;
			lowerBound0 = lowerBound0>weight[i]?weight[i]:lowerBound0;
		}
		
	    List<double[]> x = new ArrayList<double[]>();
	    	x.add(weightDays);
	    List<double[]> values = new ArrayList<double[]>();
	    	values.add(weight);
		List<String> typeList = new ArrayList<String>();
			typeList.add(LineChart.TYPE);
		List<String> titleList = new ArrayList<String>();
			titleList.add("Weight");
	    List<Integer> colorList = new ArrayList<Integer>();
	    	colorList.add(Color.YELLOW);
	    List<PointStyle> styleList = new ArrayList<PointStyle>();
	    	styleList.add(PointStyle.POINT);
	    XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer(2);
	    
	    if(entries[1]){
	    	x.add(hmtDays);
	    	double[] marker = new double[hmtDays.length];
			for(int i = 0; i<hmtDays.length;i++){
				marker[i] = lowerBound0 - 10;
			}
			values.add(marker);
			typeList.add(ScatterChart.TYPE);
			titleList.add("Healthy Meal Tracker Used");
			colorList.add(Color.GREEN);
			styleList.add(PointStyle.CIRCLE);
	    }
    
	    if(entries[2]){
	    	x.add(stepLivelyDays);
	    	double[] marker = new double[stepLivelyDays.length];
			for(int i = 0; i<stepLivelyDays.length;i++){
				marker[i] = lowerBound0 - 25;
			}
			values.add(marker);
			typeList.add(ScatterChart.TYPE);
			titleList.add("StepLively Tracker Used");
			colorList.add(Color.MAGENTA);
			styleList.add(PointStyle.SQUARE);
	    }	    
	    if(entries[0]){
	    	x.add(calDays);
	    	double[] marker = new double[calDays.length];
			for(int i = 0; i<calDays.length;i++){
				marker[i] = lowerBound0 - 40;
			}
			values.add(marker);
			typeList.add(ScatterChart.TYPE);
			titleList.add("Detailed Food Tracker Used (>1000 kcals reported)");
			colorList.add(Color.RED);
			styleList.add(PointStyle.DIAMOND);
	    }

	    String[] types = new String[typeList.size()];
	    String[] titles = new String[titleList.size()];
	    int[] colors = new int[colorList.size()];
	    PointStyle[] styles = new PointStyle[styleList.size()];
	    for(int i = 0; i < typeList.size();i++){
	    	titles[i] = titleList.get(i);
	    	types[i] = typeList.get(i);
	    	colors[i] = colorList.get(i);
	    	styles[i] = styleList.get(i);
	    }
	    setRenderer(renderer,colors,styles);	
	    int length = renderer.getSeriesRendererCount();
	    for (int i = 0; i < length; i++) {
		      ((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
		    }
	    setChartSettings(renderer, "Weight Reported", "Days in CITY", "Weight", 
	    		0, finishDate + 20, lowerBound0-60, upperBound0+20,
	        Color.LTGRAY, Color.LTGRAY);
	    renderer.setXLabels(12);
	    renderer.setYLabels(10);
	    renderer.setShowGrid(true);
	    renderer.setXLabelsAlign(Align.CENTER);
	    renderer.setYLabelsAlign(Align.CENTER);
	    renderer.setZoomButtonsVisible(true);		
	    renderer.setXAxisMin(0,0);
	    XYMultipleSeriesDataset dataset = buildDataset(titles, x, values);
		intent = ChartFactory.getCombinedXYChartIntent(context, dataset, renderer, types,
				"Weight Reported");		
		return intent;
		
		
  }

}
