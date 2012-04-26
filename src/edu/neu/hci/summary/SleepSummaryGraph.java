package edu.neu.hci.summary;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.GraphicalView;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import edu.neu.hci.Global;
import edu.neu.hci.GoodSleepActivity;
import edu.neu.hci.R;
import edu.neu.hci.db.DBAccessHelper;
import edu.neu.hci.graph.DrawGraph;
import edu.neu.hci.graph.GraphData;

public class SleepSummaryGraph extends Activity {
	private Button btn;
	private GraphicalView gv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
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
			LinearLayout layout = (LinearLayout) findViewById(R.id.containerBody);
			layout.removeAllViews();
			if (data != null) {
				Log.i(Global.TAG, "GraphData");
				DrawGraph graph = new DrawGraph();
				gv = graph.getGraphView(getApplicationContext(), data);
				layout.addView(gv, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			} else {
				TextView tv = new TextView(getApplicationContext());
				tv.setText("No Data Of Last Night Sleep");
				layout.addView(tv);
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
