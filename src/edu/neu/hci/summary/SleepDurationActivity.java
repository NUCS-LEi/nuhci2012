package edu.neu.hci.summary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import edu.neu.hci.R;

public class SleepDurationActivity extends Activity {
	private Button sleepDurationDoneBtn;
	private TextView tv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Bundle class with XML layout
		setContentView(R.layout.summary_sleep_duration);
		// Bundle button in code with button in XML layout
		sleepDurationDoneBtn = (Button) findViewById(R.id.sleepDurationDoneBtn);
		tv = (TextView) findViewById(R.id.sleepDurationTV);
		Bundle bundle = this.getIntent().getExtras();
		int i = bundle.getInt("compare");
		if (i == 0)
			tv.setText("Get a consistent amount of sleep each day, seven days a week. Last night you slept less than your average sleep duration.");
		else
			tv.setText("Get a consistent amount of sleep each day, seven days a week. Last night you slept more than your average sleep duration.");
		sleepDurationDoneBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				// Set navigation, first parameter is source, second is target.
				i.setClass(SleepDurationActivity.this, SleepSummaryMain.class);
				startActivity(i);

			}
		});
	}
}
