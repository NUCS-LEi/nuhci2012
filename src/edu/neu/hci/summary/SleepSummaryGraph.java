package edu.neu.hci.summary;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.LineChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import edu.neu.hci.Global;
import edu.neu.hci.GoodSleepActivity;
import edu.neu.hci.R;
import edu.neu.hci.db.DBAccessHelper;
import edu.neu.hci.graph.DrawGraph;
import edu.neu.hci.graph.GraphData;

public class SleepSummaryGraph extends Activity {
	private Button btn;
	private GraphData data;
	private XYMultipleSeriesRenderer render;
	private XYSeries series;
	private GraphicalView gv;
	private XYSeriesRenderer xyRender;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.summary_graph);
		btn = (Button) findViewById(R.id.btnBack);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				i.setClass(getApplicationContext(), GoodSleepActivity.class);
				startActivity(i);
				SleepSummaryGraph.this.finish();
			}
		});
		new aTask().execute(null, null, null);
	}

	public class aTask extends AsyncTask<Void, Void, GraphData> {

		@Override
		protected void onPostExecute(GraphData data) {
			super.onPostExecute(data);
			if (data != null) {
				DrawGraph graph = new DrawGraph();
				LinearLayout layout = (LinearLayout) findViewById(R.id.containerBody);
				gv = graph.getGraphView(getApplicationContext(), data);
				layout.removeAllViews();
				layout.addView(gv, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			}
		}

		@Override
		protected GraphData doInBackground(Void... arg0) {
			List<double[]> dataList = new ArrayList<double[]>();
			boolean[] entries = { true };
			if (DBAccessHelper.getSummaryPoints(getApplicationContext()) != null) {
				dataList.addAll(DBAccessHelper.getSummaryPoints(getApplicationContext()));
				GraphData data = new GraphData(dataList, entries);
				return data;
			} else
				return null;
		}
	}
}
