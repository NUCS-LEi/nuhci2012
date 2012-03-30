package edu.neu.hci;

import edu.neu.hci.GoodSleepActivity;
import edu.neu.hci.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DuringSleepActivity extends Activity {
	private Button stopTrackingBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Bundle class with XML layout
		setContentView(R.layout.good_night_with_sensor);
		// Bundle button in code with button in XML layout
		stopTrackingBtn = (Button) findViewById(R.id.stopTrackingBtn);
		stopTrackingBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				// Set navigation, first parameter is source, second is target.
				i.setClass(DuringSleepActivity.this, GoodSleepActivity.class);
				startActivity(i);

			}
		});
	}
}
