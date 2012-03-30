package edu.neu.hci.questionaire;

import edu.neu.hci.GoodSleepActivity;
import edu.neu.hci.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AlcoholQuestionActivity extends Activity {
	private Button alcoholNextBtn;
	private Button alcoholBackBtn;
	private Button alcoholBtn1;
	private Button alcoholBtn2;
	private Button alcoholBtn3;
	private Button alcoholBtn4;
	private Button alcoholBtn5;
	private Button alcoholBtn6;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Bundle class with XML layout
		setContentView(R.layout.alcohol_question);
		// Bundle button in code with button in XML layout
		alcoholBackBtn = (Button) findViewById(R.id.alcoholBackBtn);
		alcoholNextBtn = (Button) findViewById(R.id.alcoholNextBtn);
		alcoholBtn1 = (Button) findViewById(R.id.alcoholBtn1);
		alcoholBtn2 = (Button) findViewById(R.id.alcoholBtn2);
		alcoholBtn3 = (Button) findViewById(R.id.alcoholBtn3);
		alcoholBtn4 = (Button) findViewById(R.id.alcoholBtn4);
		alcoholBtn5 = (Button) findViewById(R.id.alcoholBtn5);
		alcoholBtn6 = (Button) findViewById(R.id.alcoholBtn6);
		alcoholBtn1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				naviNext();
			}
		});
		alcoholBtn2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				naviNext();
			}
		});
		alcoholBtn3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				naviNext();
			}
		});
		alcoholBtn4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				naviNext();
			}
		});
		alcoholBtn5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				naviNext();
			}
		});
		alcoholBtn6.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				naviNext();
			}
		});
		alcoholBackBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				naviBack();
			}
		});
		alcoholNextBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				naviNext();

			}
		});
	}
	private void naviNext() {
		Intent i = new Intent();
		// Set navigation, first parameter is source, second is target.
		i.setClass(AlcoholQuestionActivity.this, SmokeQuestionActivity.class);
		startActivity(i);
	}
	private void naviBack() {
		Intent i = new Intent();
		// Set navigation, first parameter is source, second is target.
		i.setClass(AlcoholQuestionActivity.this, CaffeineQuestionActivity.class);
		startActivity(i);
	}
}
