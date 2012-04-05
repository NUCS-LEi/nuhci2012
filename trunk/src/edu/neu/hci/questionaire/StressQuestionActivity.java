package edu.neu.hci.questionaire;

import edu.neu.hci.GoodSleepActivity;
import edu.neu.hci.R;
import edu.neu.hci.StartSleepActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class StressQuestionActivity extends Activity {
	private Button stressNextBtn;
	private Button stressBackBtn;
	private Button stressBtn1;
	private Button stressBtn2;
	private Button stressBtn3;
	private Button stressBtn4;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Bundle class with XML layout
		setContentView(R.layout.stress_question);
		// Bundle button in code with button in XML layout
		stressBackBtn = (Button) findViewById(R.id.stressBackBtn);
		stressNextBtn = (Button) findViewById(R.id.stressNextBtn);
		stressBtn1 = (Button) findViewById(R.id.stressBtn1);
		stressBtn2 = (Button) findViewById(R.id.stressBtn2);
		stressBtn3 = (Button) findViewById(R.id.stressBtn3);
		stressBtn4 = (Button) findViewById(R.id.stressBtn4);
		stressBtn1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				naviNext();
			}
		});
		stressBtn2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				naviNext();
			}
		});
		stressBtn3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				naviNext();
			}
		});
		stressBtn4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				naviNext();
			}
		});
		stressBackBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				naviBack();
			}
		});
		stressNextBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				naviNext();

			}
		});
	}

	private void naviNext() {
		Intent i = new Intent();
		// Set navigation, first parameter is source, second is target.

		i.setClass(StressQuestionActivity.this, StartSleepActivity.class);

		startActivity(i);
	}

	private void naviBack() {
		Intent i = new Intent();
		// Set navigation, first parameter is source, second is target.
		i.setClass(StressQuestionActivity.this, ActivityQuestionActivity.class);
		startActivity(i);
	}
}
