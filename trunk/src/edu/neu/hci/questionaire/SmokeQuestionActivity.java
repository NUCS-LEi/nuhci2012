package edu.neu.hci.questionaire;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import edu.neu.hci.GoodSleepActivity;
import edu.neu.hci.R;

public class SmokeQuestionActivity extends Activity {
	private Button smokeNextBtn;
	private Button smokeBackBtn;
	private Button smokeBtn1;
	private Button smokeBtn2;
	private Button smokeBtn3;
	private Button smokeBtn4;
	private Button smokeBtn5;
	private Button smokeBtn6;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Bundle class with XML layout
		setContentView(R.layout.smoke_question);
		// Bundle button in code with button in XML layout
		smokeBackBtn = (Button) findViewById(R.id.smokeBackBtn);
		smokeNextBtn = (Button) findViewById(R.id.smokeNextBtn);
		smokeBtn1 = (Button) findViewById(R.id.smokeBtn1);
		smokeBtn2 = (Button) findViewById(R.id.smokeBtn2);
		smokeBtn3 = (Button) findViewById(R.id.smokeBtn3);
		smokeBtn4 = (Button) findViewById(R.id.smokeBtn4);
		smokeBtn5 = (Button) findViewById(R.id.smokeBtn5);
		smokeBtn6 = (Button) findViewById(R.id.smokeBtn6);
		smokeBtn1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				naviNext();
			}
		});
		smokeBtn2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				naviNext();
			}
		});
		smokeBtn3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				naviNext();
			}
		});
		smokeBtn4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				naviNext();
			}
		});
		smokeBtn5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				naviNext();
			}
		});
		smokeBtn6.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				naviNext();
			}
		});
		smokeBackBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				naviBack();
			}
		});
		smokeNextBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				naviNext();

			}
		});
	}
	private void naviNext() {
		Intent i = new Intent();
		// Set navigation, first parameter is source, second is target.
		i.setClass(SmokeQuestionActivity.this, FoodQuestionActivity.class);
		startActivity(i);
	}
	private void naviBack() {
		Intent i = new Intent();
		// Set navigation, first parameter is source, second is target.
		i.setClass(SmokeQuestionActivity.this, AlcoholQuestionActivity.class);
		startActivity(i);
	}
}
