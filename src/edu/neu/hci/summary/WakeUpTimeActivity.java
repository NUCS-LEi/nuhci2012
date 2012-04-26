package edu.neu.hci.summary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import edu.neu.hci.R;

public class WakeUpTimeActivity extends Activity {
	private Button wakeUpTimeDoneBtn;
	private TextView tv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Bundle class with XML layout
		setContentView(R.layout.summary_wake_up_time);
		tv = (TextView) findViewById(R.id.wakeUpTimeTV);
		Bundle bundle = this.getIntent().getExtras();
		int i = bundle.getInt("compare");
		if (i == 0)
			tv.setText("Keep a consistent wake up time each day, seven days per week. Today you woke up sooner than your average wake up time.");
		else
			tv.setText("Keep a consistent wake up time each day, seven days per week. Today you woke up later than your average wake up time.");

		// Bundle button in code with button in XML layout
		wakeUpTimeDoneBtn = (Button) findViewById(R.id.wakeUpTimeDoneBtn);
		wakeUpTimeDoneBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				// Set navigation, first parameter is source, second is target.
				i.setClass(WakeUpTimeActivity.this, SleepSummaryMain.class);
				startActivity(i);

			}
		});
	}
}
