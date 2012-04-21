package edu.neu.hci;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import edu.neu.hci.db.DBAccessHelper;

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
			initialDB();
		} else if (bundle == null) {
			goMenu();
		} else {
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
