package edu.neu.hci.summary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import edu.neu.hci.R;

public class GoToBedTimeActivity extends Activity {
	private Button goToBedTimeDoneBtn;
	private TextView tv;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Bundle class with XML layout
		setContentView(R.layout.summary_go_to_bed_time);
		tv = (TextView) findViewById(R.id.goToBedTimeTV);
		Bundle bundle = this.getIntent().getExtras();
		int i = bundle.getInt("compare");
		if (i == 0)
			tv.setText("Keep a consistent bedtime during all days of the week, including weekends. Last night you slept sooner than your average bedtime.");
		else
			tv.setText("Keep a consistent bedtime during all days of the week, including weekends. Last night you slept later than your average bedtime.");
		
		// Bundle button in code with button in XML layout
		goToBedTimeDoneBtn = (Button) findViewById(R.id.goToBedTimeDoneBtn);
		goToBedTimeDoneBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				// Set navigation, first parameter is source, second is target.
				i.setClass(GoToBedTimeActivity.this, SleepSummaryMain.class);
				startActivity(i);

			}
		});
	}
}
