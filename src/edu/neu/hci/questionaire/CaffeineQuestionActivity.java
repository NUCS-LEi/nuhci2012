package edu.neu.hci.questionaire;

import edu.neu.hci.GoodSleepActivity;
import edu.neu.hci.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class CaffeineQuestionActivity extends Activity {
	private Button caffeineNextBtn;
	private Button caffeineBackBtn;
	private Button caffeineBtn1;
	private Button caffeineBtn2;
	private Button caffeineBtn3;
	private Button caffeineBtn4;
	private Button caffeineBtn5;
	private Button caffeineBtn6;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Bundle class with XML layout
		setContentView(R.layout.caffein_question);
		// Bundle button in code with button in XML layout
		caffeineBackBtn = (Button) findViewById(R.id.caffeineBackBtn);
		caffeineNextBtn = (Button) findViewById(R.id.caffeineNextBtn);
		caffeineBtn1 = (Button) findViewById(R.id.caffeineBtn1);
		caffeineBtn2 = (Button) findViewById(R.id.caffeineBtn2);
		caffeineBtn3 = (Button) findViewById(R.id.caffeineBtn3);
		caffeineBtn4 = (Button) findViewById(R.id.caffeineBtn4);
		caffeineBtn5 = (Button) findViewById(R.id.caffeineBtn5);
		caffeineBtn6 = (Button) findViewById(R.id.caffeineBtn6);
		caffeineBtn1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				naviNext();
			}
		});
		caffeineBtn2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				naviNext();
			}
		});
		caffeineBtn3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				naviNext();
			}
		});
		caffeineBtn4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				naviNext();
			}
		});
		caffeineBtn5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				naviNext();
			}
		});
		caffeineBtn6.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				naviNext();
			}
		});
		caffeineBackBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				naviBack();
			}
		});
		caffeineNextBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				naviNext();
			}
		});
	}

	private void naviNext() {
		Intent i = new Intent();
		// Set navigation, first parameter is source, second is target.
		i.setClass(CaffeineQuestionActivity.this, AlcoholQuestionActivity.class);
		startActivity(i);
	}

	private void naviBack() {
		Intent i = new Intent();
		// Set navigation, first parameter is source, second is target.
		i.setClass(CaffeineQuestionActivity.this, GoodSleepActivity.class);
		startActivity(i);
	}
}
