package edu.neu.hci.graph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import edu.neu.hci.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.widget.Toast;

public class Graph extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.graph);
	  }

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		new aTask().execute(null,null,null);
	}
	
	public class aTask extends AsyncTask<Void, Void, GraphData>{

		@Override
		protected void onPostExecute(GraphData data) {
			// TODO Auto-generated method stub
			super.onPostExecute(data);
			if(data!=null){
				DrawGraph graph = new DrawGraph();
				Intent intent = graph.execute(Graph.this,data);
				if(intent != null){
					startActivity(intent);				
				}
			}
			Graph.this.finish();
		}

		@Override
		protected GraphData doInBackground(Void... arg0) {
			Context cxt = getApplicationContext();
//			List<String[]> weightEntries = readCSVFile(Constant.path, Constant.fileName);
//			List<Double> weightList = new ArrayList<Double>();
//			List<Calendar> weightDateList = new ArrayList<Calendar>();
//			double[] weights;
//			Calendar[] weightDates;
//			int i = 0;
//			Date d = null;
//			
//			if(DataStorage.getStartDate(Graph.this, null) != null){
//				double startWeight = DataStorage.getStartWeight(Graph.this, 0);
//				weightList.add(startWeight);
//				
//				Calendar startWeightDate = Calendar.getInstance();
//				d = DataStorage.GetDate(DataStorage.getStartDate(Graph.this, null));
//				startWeightDate.setTime(d);
//				weightDateList.add(startWeightDate);
//			}
//				for (Iterator<String[]> iterator = weightEntries.iterator(); iterator.hasNext();i++) {
//					String[] entry = (String[]) iterator.next();
//					if(!entry[0].equalsIgnoreCase("weight")){
//						weightList.add((double)Double.parseDouble(entry[0]));
//						d = DataStorage.GetDate(entry[1]);
//						Calendar weightDate = Calendar.getInstance();
//						weightDate.setTime(d);
//						weightDateList.add(weightDate);
//					}
//				}
//			weights = new double[weightList.size()];
//			weightDates = new Calendar[weightDateList.size()];
//			for (int j = 0; j < weightDates.length; j++) {
//				weights[j] = weightList.get(j);
//				weightDates[j] = weightDateList.get(j);
//			}
//			/**
//			 * get calories data
//			 */
//			List<String[]> calEntries = readCSVFile(Constant.calPath, Constant.calFileName);
//			List<Double> cal = new ArrayList<Double>();
//			List<Calendar> calDates = new ArrayList<Calendar>();
//			double calWADay = 0;double day = 0;	
//			i = 0;
//			int counter = 0;
//			for (Iterator<String[]> iterator = calEntries.iterator(); iterator.hasNext();i++) {
//				String[] entry = (String[]) iterator.next();
//			
//				if(entry[0].equalsIgnoreCase("date")){
//					counter++;
//				}
//				else{	
//					Calendar date = Calendar.getInstance();
//					d = DataStorage.GetDate(entry[0]);
//					date.setTime(d);
//					day = date.get(Calendar.DAY_OF_YEAR);
//					if(i>counter&&i<calEntries.size()-1){
//						if(day == calDates.get(calDates.size()-1).get(Calendar.DAY_OF_YEAR)){
//							calWADay += (double)Double.parseDouble(entry[8]);
//						}
//						else if(day > calDates.get(calDates.size()-1).get(Calendar.DAY_OF_YEAR)){
//							calDates.add(date);
//							cal.add(calWADay);
//							calWADay = (double)Double.parseDouble(entry[8]);
//						}
//					}
//					else if(i == counter){
//						calWADay = (double)Double.parseDouble(entry[8]);
//						calDates.add(date);
//					}
//					else if(i == calEntries.size()-1){
//						if(day > calDates.get(calDates.size()-1).get(Calendar.DAY_OF_YEAR)){
//							cal.add(calWADay);
//							calDates.add(date);
//							calWADay = (double)Double.parseDouble(entry[8]);
//						}
//						cal.add(calWADay);
//					}
//				}
//
//			}
//			for(i = 0; i < cal.size();i++){
//				if(cal.get(i)<1000){
//					cal.remove(i);
//					calDates.remove(i);
//					i--;
//				}
//			}
//			/**
//			 * get Healthy Meal tracker
//			 */
////			Log.v(TAG,"-------reading data for healthy meal tracker--------");
//			List<String[]> hmtEntries = null;	
////			Log.v(TAG, "check database " + CheckDataBase());
//			if(CheckDataBase()){
//				hmtEntries = loadDB(cxt);
//			}
//			else{
//				hmtEntries = readCSVFile(Constant.healthTrackerPath, Constant.healthTrackerName);				
//			}
////			Log.v(TAG,"healthy meal tracker dates are:");
////			for (String[] strings : hmtEntries) {
////				Log.v(TAG,strings[0]);				
////			}
//			List< Calendar > hmtDate = new ArrayList<Calendar>();
//			if(hmtEntries != null){
//				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//
//				for (Iterator<String[]> iterator = hmtEntries.iterator(); iterator
//						.hasNext();) {
//					String[] entry = (String[]) iterator.next();
//					String standard = entry[2];
//					if(standard.equals("Healthy")){
//						Calendar date = Calendar.getInstance();
//						try {
//							d = df.parse(entry[0]);
//						} catch (ParseException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						date.setTime(d);
//						if(!hmtDate.isEmpty()){
//							if(!date.equals(hmtDate.get(hmtDate.size()-1)))
//								hmtDate.add(date);
//						}
//						else
//							hmtDate.add(date);
//					}
//				}
//			}
//
//
//			/**
//			 * get step lively data
//			 */
////			Log.v(TAG,"-------reading data for steplively tracker--------");
//			File file = new File(cxt.getFilesDir() + "/" +Constant.stepLivelyPath);
//			if(!file.exists())
//				Log.v(TAG,"steplively folder not exists");
//			File[] directories = file.listFiles(new DirectoryFilter());
//			List<Calendar> stepLivelyDate = new ArrayList<Calendar>();
//			if(directories != null){
//				for(i = 0; i< directories.length;i++){
//					File dir = directories[i];
//					String date = dir.getName();
//					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//					try {
//						d = df.parse(date);
//					} catch (ParseException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					Calendar calendar = Calendar.getInstance();
//					calendar.setTime(d);
//					stepLivelyDate.add(calendar);
//				}
//			}
//			
//			/**
//			 * uniform the date and data
//			 */
//			double firstYear = weightDates[0].get(Calendar.YEAR);
//			double startDay = weightDates[0].get(Calendar.DAY_OF_YEAR);			
//
//			double hmtDays[] = null;
//			double[] calDays = null;
//			double[] stepLivelyDays = null;
//			double[] weightDays = new double[weightDates.length];
//			if(!calDates.isEmpty())
//				calDays = new double[calDates.size()];
//			if(!hmtDate.isEmpty())
//				hmtDays = new double[hmtDate.size()];
//			if(!stepLivelyDate.isEmpty())
//				stepLivelyDays = new double[stepLivelyDate.size()];
//
//			for(i = 0; i < weightDays.length; i++) 
//				weightDays[i] = weightDates[i].get(Calendar.DAY_OF_YEAR)+365*(weightDates[i].get(Calendar.YEAR)-firstYear)-startDay;
//
//			if(!calDates.isEmpty())
//				for (i = 0; i < calDates.size(); i++) 
//					calDays[i] = calDates.get(i).get(Calendar.DAY_OF_YEAR)+365*(calDates.get(i).get(Calendar.YEAR)-firstYear) - startDay;					
//			if(!hmtDate.isEmpty())
//				for(i = 0; i< hmtDate.size();i++)
//					hmtDays[i] = hmtDate.get(i).get(Calendar.DAY_OF_YEAR)+365*(hmtDate.get(i).get(Calendar.YEAR)-firstYear) - startDay;
//			if(!stepLivelyDate.isEmpty())
//				for(i = 0; i< stepLivelyDate.size();i++)
//					stepLivelyDays[i] = stepLivelyDate.get(i).get(Calendar.DAY_OF_YEAR)+365*(stepLivelyDate.get(i).get(Calendar.YEAR)-firstYear) - startDay;

			/**
			 * encapsulate the data into an object and pass the object to next activity
			 */
			List<double[]> dataList = new ArrayList<double[]>();
			boolean [] entries = {false, false, false};
			double[] marker = null;
//			dataList.add(weightDays);
//			dataList.add(weights);
//			if(!calDates.isEmpty()){
//				dataList.add(calDays);
//				entries[0] = true;
//			}
//			else
//				dataList.add(marker);
//			if(!hmtDate.isEmpty()){
//				dataList.add(hmtDays);
//				entries[1] = true;
//			}
//			else
//				dataList.add(marker);
//			if(!stepLivelyDate.isEmpty()){
//				dataList.add(stepLivelyDays);
//				entries[2] = true;
//			}
//			else
//				dataList.add(marker);
			GraphData data = new GraphData(dataList, entries);
			return data;
		}
	}
public boolean CheckDataBase() {
	SQLiteDatabase checkDB = null;
	try {
		checkDB = SQLiteDatabase.openDatabase(Constant.healthTrackerDBPath, null, SQLiteDatabase.OPEN_READONLY);
		checkDB.close();
	}
	catch (Exception e) {
		System.out.println(e.getMessage());
	}
	return checkDB != null ? true : false;
}

public class FileFilter implements java.io.FileFilter{
	public boolean accept(File pathname){
		if(pathname.isFile())
			return true;
		return false;
	}
}
public class DirectoryFilter implements java.io.FileFilter{
	public boolean accept(File pathname){
		if(pathname.isDirectory())
			return true;
		return false;
	}
}
}

