package edu.neu.hci.summary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import edu.neu.hci.R;

public class StressActivity extends Activity {
	private Button stressDoneBtn;
	private TextView tv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Bundle class with XML layout
		setContentView(R.layout.summary_stress);
		tv = (TextView) findViewById(R.id.stressTV);
		Bundle bundle = this.getIntent().getExtras();
		int i = bundle.getInt("compare");
		if (i == 0)
			tv.setText("Don't attempt to sleep if you're stressing about problems. Yesterday your stress level was lower than your average.");
		else
			tv.setText("Don't attempt to sleep if you're stressing about problems. Yesterday your stress level was higher than your average.");

		// Bundle button in code with button in XML layout
		stressDoneBtn = (Button) findViewById(R.id.stressDoneBtn);
		stressDoneBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				// Set navigation, first parameter is source, second is target.
				i.setClass(StressActivity.this, SleepSummaryMain.class);
				startActivity(i);

			}
		});
	}
}
