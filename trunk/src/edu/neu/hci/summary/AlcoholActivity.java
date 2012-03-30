package edu.neu.hci.summary;

import edu.neu.hci.GoodSleepActivity;
import edu.neu.hci.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AlcoholActivity extends Activity {
	private Button alcoholDoneBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Bundle class with XML layout
		setContentView(R.layout.summary_alcohol);
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
