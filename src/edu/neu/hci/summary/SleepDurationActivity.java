package edu.neu.hci.summary;

import edu.neu.hci.GoodSleepActivity;
import edu.neu.hci.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SleepDurationActivity extends Activity {
	private Button sleepDurationDoneBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Bundle class with XML layout
		setContentView(R.layout.summary_sleep_duration);
		// Bundle button in code with button in XML layout
		sleepDurationDoneBtn = (Button) findViewById(R.id.sleepDurationDoneBtn);
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
