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

public class Graph extends Activity {
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
		new aTask().execute(null, null, null);
	}

	public class aTask extends AsyncTask<Void, Void, GraphData> {

		@Override
		protected void onPostExecute(GraphData data) {
			// TODO Auto-generated method stub
			super.onPostExecute(data);
			if (data != null) {
				DrawGraph graph = new DrawGraph();
				Intent intent = graph.execute(Graph.this, data);
				if (intent != null) {
					startActivity(intent);
				}
			}
			Graph.this.finish();
		}

		@Override
		protected GraphData doInBackground(Void... arg0) {
			Context cxt = getApplicationContext();
			List<double[]> dataList = new ArrayList<double[]>();
			boolean[] entries = { true, true, true };
			double[] marker = null;
			// dataList.add(weightDays);
			// dataList.add(weights);
			// if(!calDates.isEmpty()){
			// dataList.add(calDays);
			// entries[0] = true;
			// }
			// else
			// dataList.add(marker);
			// if(!hmtDate.isEmpty()){
			// dataList.add(hmtDays);
			// entries[1] = true;
			// }
			// else
			// dataList.add(marker);
			// if(!stepLivelyDate.isEmpty()){
			// dataList.add(stepLivelyDays);
			// entries[2] = true;
			// }
			// else
			// dataList.add(marker);
			double[] d1 = { 1, 2, 3, 4, 5 };
			double[] d2 = { 2, 4, 6, 8, 10 };
			double[] d3 = { 5, 4, 3, 2, 1 };
			double[] d4 = { 10, 4, 6, 2, 1 };
			double[] d5 = { 1, 1, 1, 1, 1 };
			dataList.add(d1);
			dataList.add(d2);
			dataList.add(d3);
			dataList.add(d4);
			dataList.add(d5);
			GraphData data = new GraphData(dataList, entries);
			return data;
		}
	}

	public class FileFilter implements java.io.FileFilter {
		public boolean accept(File pathname) {
			if (pathname.isFile())
				return true;
			return false;
		}
	}

	public class DirectoryFilter implements java.io.FileFilter {
		public boolean accept(File pathname) {
			if (pathname.isDirectory())
				return true;
			return false;
		}
	}
}
