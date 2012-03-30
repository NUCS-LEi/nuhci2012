package edu.neu.hci.questionaire;

import edu.neu.hci.GoodSleepActivity;
import edu.neu.hci.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ActivityQuestionActivity extends Activity {
	private Button activityNextBtn;
	private Button activityBackBtn;
	private Button activityBtn1;
	private Button activityBtn2;
	private Button activityBtn3;
	private Button activityBtn4;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Bundle class with XML layout
		setContentView(R.layout.activity_question);
		// Bundle button in code with button in XML layout
		activityBackBtn = (Button) findViewById(R.id.activityBackBtn);
		activityNextBtn = (Button) findViewById(R.id.activityNextBtn);
		activityBtn1 = (Button) findViewById(R.id.activityBtn1);
		activityBtn2 = (Button) findViewById(R.id.activityBtn2);
		activityBtn3 = (Button) findViewById(R.id.activityBtn3);
		activityBtn4 = (Button) findViewById(R.id.activityBtn4);
		activityBtn1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				naviNext();
			}
		});
		activityBtn2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				naviNext();
			}
		});
		activityBtn3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				naviNext();
			}
		});
		activityBtn4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				naviNext();
			}
		});
		activityBackBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				naviBack();
			}
		});
		activityNextBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				naviNext();

			}
		});
	}

	private void naviNext() {
		Intent i = new Intent();
		// Set navigation, first parameter is source, second is target.
		i.setClass(ActivityQuestionActivity.this, StressQuestionActivity.class);
		startActivity(i);
	}

	private void naviBack() {
		Intent i = new Intent();
		// Set navigation, first parameter is source, second is target.
		i.setClass(ActivityQuestionActivity.this, FoodQuestionActivity.class);
		startActivity(i);
	}
}
