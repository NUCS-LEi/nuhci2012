package edu.neu.hci.summary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import edu.neu.hci.GoodSleepActivity;
import edu.neu.hci.R;

public class SleepSummaryGraph extends Activity {

	private Button btnBack;
	private TextView title;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.summary_graph);

		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(backBtnListener);
	}

	public void onResume() {
		super.onResume();
		refreshGrid();
	}

	private void refreshGrid() {
		
	}

	private OnClickListener backBtnListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(SleepSummaryGraph.this, GoodSleepActivity.class);
			startActivity(intent);
		}

	};
}
