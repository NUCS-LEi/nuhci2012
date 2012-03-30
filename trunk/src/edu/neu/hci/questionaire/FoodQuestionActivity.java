package edu.neu.hci.questionaire;

import edu.neu.hci.GoodSleepActivity;
import edu.neu.hci.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class FoodQuestionActivity extends Activity {
	private Button foodNextBtn;
	private Button foodBackBtn;
	private Button foodBtn1;
	private Button foodBtn2;
	private Button foodBtn3;
	private Button foodBtn4;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Bundle class with XML layout
		setContentView(R.layout.food_question);
		// Bundle button in code with button in XML layout
		foodBackBtn = (Button) findViewById(R.id.foodBackBtn);
		foodNextBtn = (Button) findViewById(R.id.foodNextBtn);
		foodBtn1 = (Button) findViewById(R.id.foodBtn1);
		foodBtn2 = (Button) findViewById(R.id.foodBtn2);
		foodBtn3 = (Button) findViewById(R.id.foodBtn3);
		foodBtn4 = (Button) findViewById(R.id.foodBtn4);
		foodBtn1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				naviNext();
			}
		});
		foodBtn2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				naviNext();
			}
		});
		foodBtn3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				naviNext();
			}
		});
		foodBtn4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				naviNext();
			}
		});
		foodBackBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				naviBack();
			}
		});
		foodNextBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				naviNext();

			}
		});
	}

	private void naviNext() {
		Intent i = new Intent();
		// Set navigation, first parameter is source, second is target.
		i.setClass(FoodQuestionActivity.this, ActivityQuestionActivity.class);
		startActivity(i);
	}

	private void naviBack() {
		Intent i = new Intent();
		// Set navigation, first parameter is source, second is target.
		i.setClass(FoodQuestionActivity.this, SmokeQuestionActivity.class);
		startActivity(i);
	}
}
