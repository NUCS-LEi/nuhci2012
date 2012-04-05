package edu.neu.hci;

import edu.neu.hci.questionaire.CaffeineQuestionActivity;
import edu.neu.hci.questionaire.SettingQuestionActivity;
import edu.neu.hci.summary.SleepScoreActivity;
import edu.neu.hci.summary.SleepSummaryMain;
import android.app.Activity;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

// Every display page needs to extends Activity, also register in AndroidManifest.xml, 
// you can copy it like other activities and change name.
public class GoodSleepActivity extends Activity {
	/** Called when the activity is first created. */
	private Button startMySleepTrackingBtn;
	private Button howIsMySleepBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Bundle class with XML layout
		setContentView(R.layout.main);
		// Bundle button in code with button in XML layout
		startMySleepTrackingBtn = (Button) findViewById(R.id.startSleepTrackBtn);
		howIsMySleepBtn = (Button) findViewById(R.id.howIsMySleepBtn);
		// Set button onClickListener
		startMySleepTrackingBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				// Set navigation, first parameter is source, second is target.
				i.setClass(GoodSleepActivity.this, SettingQuestionActivity.class);
				startActivity(i);
			}
		});
		howIsMySleepBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				// Set navigation, first parameter is source, second is target.
				i.setClass(GoodSleepActivity.this, SleepSummaryMain.class);
				startActivity(i);
			}
		});
	}
}