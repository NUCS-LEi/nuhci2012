package edu.neu.hci.alarm;

import java.text.ParseException;

import edu.neu.hci.Global;
import edu.neu.hci.GoodSleepActivity;
import edu.neu.hci.R;
import edu.neu.hci.R.id;
import edu.neu.hci.R.layout;
import edu.neu.hci.db.DBAccessHelper;
import edu.neu.hci.db.DatabaseDictionary;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DuringSleepActivity extends Activity {
	private Button stopTrackingBtn;
	private TextView tv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Bundle class with XML layout
		setContentView(R.layout.good_night_with_sensor);
		// Bundle button in code with button in XML layout
		stopTrackingBtn = (Button) findViewById(R.id.stopTrackingBtn);
		tv = (TextView) findViewById(R.id.textView1);

	}

	public void onResume() {
		super.onResume();
		try {
			tv.setText(String.format("Alarm time:   %s", Global.apmDateFormat.format(Global.lastModDateFormat
					.parse(DBAccessHelper.getLastSleepTime(getApplicationContext())))));
		} catch (ParseException e) {
			e.printStackTrace();
		}
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
