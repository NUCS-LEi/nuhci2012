package edu.neu.hci.summary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import edu.neu.hci.R;

public class PhysicalActivity extends Activity {
	private Button physicalActivityDoneBtn;
	private TextView tv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Bundle class with XML layout
		setContentView(R.layout.summary_physical_activity);
		tv = (TextView) findViewById(R.id.physicalActivityTV);
		Bundle bundle = this.getIntent().getExtras();
		int i = bundle.getInt("compare");
		if (i == 0)
			tv.setText("Don't exercise within three hours of bedtime. Yesterday your physical activity was less than your average.");
		else
			tv.setText("Don't exercise within three hours of bedtime. Yesterday your physical activity was more than your average.");

		// Bundle button in code with button in XML layout
		physicalActivityDoneBtn = (Button) findViewById(R.id.physicalActivityDoneBtn);
		physicalActivityDoneBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				// Set navigation, first parameter is source, second is target.
				i.setClass(PhysicalActivity.this, SleepSummaryMain.class);
				startActivity(i);

			}
		});
	}
}
