package edu.neu.hci.summary;

import edu.neu.hci.GoodSleepActivity;
import edu.neu.hci.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class WakeUpTimeActivity extends Activity {
	private Button wakeUpTimeDoneBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Bundle class with XML layout
		setContentView(R.layout.summary_wake_up_time);
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
