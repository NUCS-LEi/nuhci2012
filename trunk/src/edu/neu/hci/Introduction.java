package edu.neu.hci;

import edu.neu.hci.GoodSleepActivity;
import edu.neu.hci.R;
import edu.neu.hci.db.DBAccessHelper;
import edu.neu.hci.db.DBContentProvider;
import edu.neu.hci.db.DatabaseDictionary;
import edu.neu.hci.helper.FileHelper;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Introduction extends Activity {
	private Button introductionStartBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Bundle class with XML layout
		setContentView(R.layout.introduction);
		Bundle bundle = this.getIntent().getExtras();

		// Bundle button in code with button in XML layout
		introductionStartBtn = (Button) findViewById(R.id.introductionStartBtn);
		introductionStartBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				goMenu();
			}
		});
		if (DBAccessHelper.getLastUsage(getApplicationContext()) == null) {
			android.util.Log.i(DatabaseDictionary.TAG, "LastUsage==null");
			initialDB();
		} else if (bundle == null) {
			android.util.Log.i(DatabaseDictionary.TAG, "bundle==null");
			goMenu();
		} else {
			android.util.Log.i(DatabaseDictionary.TAG, "else");
			DBAccessHelper.logUsage(getApplicationContext(), Introduction.class.getName());
		}
	}

	private void goMenu() {
		Intent i = new Intent();
		i.setClass(Introduction.this, GoodSleepActivity.class);
		startActivity(i);
		Introduction.this.finish();
	}

	private void initialDB() {
		Boolean[] initialSet = { true, true, true, true, true, true };
		int re = DBAccessHelper.insertOrUpdateQuestionSetting(getApplicationContext(), initialSet);
	}
}
