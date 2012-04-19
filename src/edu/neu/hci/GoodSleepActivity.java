package edu.neu.hci;

import edu.neu.hci.db.DBAccessHelper;
import edu.neu.hci.db.DBContentProvider;
import edu.neu.hci.db.DatabaseDictionary;
import edu.neu.hci.questionaire.CaffeineQuestionActivity;
import edu.neu.hci.questionaire.SettingQuestionActivity;
import edu.neu.hci.summary.SleepScoreActivity;
import edu.neu.hci.summary.SleepSummaryMain;
import android.app.Activity;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

// Every display page needs to extends Activity, also register in AndroidManifest.xml, 
// you can copy it like other activities and change name.
public class GoodSleepActivity extends Activity {
	/** Called when the activity is first created. */
	private Button startMySleepTrackingBtn;
	private Button howIsMySleepBtn;
	private TextView title;
	private ImageView img;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Bundle class with XML layout
		setContentView(R.layout.main);
		// Bundle button in code with button in XML layout
		startMySleepTrackingBtn = (Button) findViewById(R.id.startSleepTrackBtn);
		howIsMySleepBtn = (Button) findViewById(R.id.howIsMySleepBtn);
		title = (TextView) findViewById(R.id.title);
		img = (ImageView) findViewById(R.id.imageView1);

	}

	@Override
	public void onResume() {
		super.onResume();
		title.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				goIntro();
			}
		});
		img.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				goIntro();
			}
		});
		DBAccessHelper.logUsage(getApplicationContext(), GoodSleepActivity.class.getName());
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

	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, "Back");
		menu.add(0, 1, 1, "ExportDB");
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case 0:
			onBackPressed();
			break;
		case 1:
			String s = DBContentProvider.exportLogStatDB();
			android.util.Log.i(DatabaseDictionary.TAG, "Export=" + s);
			break;
		default:
			break;
		}
		return true;
	}

	private void goIntro() {
		Intent i = new Intent();
		i.putExtra("from", true);
		i.setClass(GoodSleepActivity.this, Introduction.class);
		startActivity(i);
	}
}