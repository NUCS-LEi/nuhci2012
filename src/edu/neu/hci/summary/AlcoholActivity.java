package edu.neu.hci.summary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import edu.neu.hci.R;

public class AlcoholActivity extends Activity {
	private Button alcoholDoneBtn;
	private TextView tv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Bundle class with XML layout
		setContentView(R.layout.summary_alcohol);
		tv = (TextView) findViewById(R.id.alcoholTV);
		Bundle bundle = this.getIntent().getExtras();
		int i = bundle.getInt("compare");
		if (i == 0)
			tv.setText("Avoid excessive consumption of alcohol in the evening. Yesterday you drank less than your average.");
		else
			tv.setText("Avoid excessive consumption of alcohol in the evening. Yesterday you drank more than your average.");

		// Bundle button in code with button in XML layout
		alcoholDoneBtn = (Button) findViewById(R.id.alcoholDoneBtn);
		alcoholDoneBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				// Set navigation, first parameter is source, second is target.
				i.setClass(AlcoholActivity.this, SleepSummaryMain.class);
				startActivity(i);

			}
		});
	}
}
