package edu.neu.hci.summary;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import edu.neu.hci.GoodSleepActivity;
import edu.neu.hci.R;
import edu.neu.hci.db.DBAccessHelper;
import edu.neu.hci.graph.DrawGraph;
import edu.neu.hci.graph.GraphData;

public class SleepSummaryGraph extends Activity {
	private Button btn;

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
				Intent intent = graph.execute(SleepSummaryGraph.this, data);
				if (intent != null) {
					startActivity(intent);
				}
				SleepSummaryGraph.this.finish();
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
