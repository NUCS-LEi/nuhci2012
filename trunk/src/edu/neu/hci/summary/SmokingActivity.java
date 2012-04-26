package edu.neu.hci.summary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import edu.neu.hci.R;

public class SmokingActivity extends Activity {
	private Button smokingDoneBtn;
	private TextView tv;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Bundle class with XML layout
		setContentView(R.layout.summary_smoking);
		tv = (TextView) findViewById(R.id.smokingTV);
		Bundle bundle = this.getIntent().getExtras();
		int i = bundle.getInt("compare");
		if (i == 0)
			tv.setText("Try to avoid smoking, especially during the night when you have trouble sleeping. Yesterday you smoked less than your average." );
		else
			tv.setText("Try to avoid smoking, especially during the night when you have trouble sleeping. Yesterday you smoked more than your average." );
		
		// Bundle button in code with button in XML layout
		smokingDoneBtn = (Button) findViewById(R.id.smokingDoneBtn);
		smokingDoneBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				// Set navigation, first parameter is source, second is target.
				i.setClass(SmokingActivity.this, SleepSummaryMain.class);
				startActivity(i);

			}
		});
	}
}
