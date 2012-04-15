package edu.neu.hci.questionaire;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import edu.neu.hci.R;
import edu.neu.hci.db.DBAccessHelper;
import edu.neu.hci.db.DatabaseDictionary;

public class CaffeineQuestionActivity extends Activity {
	private Button caffeineNextBtn;
	private Button caffeineBackBtn;
	private Button caffeineBtn1;
	private Button caffeineBtn2;
	private Button caffeineBtn3;
	private Button caffeineBtn4;
	private Button caffeineBtn5;
	private Button caffeineBtn6;
	private List<String> l;
	private TextView title;

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
		title = (TextView) findViewById(R.id.caffeineTitle);
	}

	public void onResume() {
		super.onResume();
		l = DBAccessHelper.questionList(getApplicationContext());
		title.setText(String.format("Caffeine (%d/%d)", l.indexOf(CaffeineQuestionActivity.class.getName()) + 1, l.size() - 1));
		setBtn(DBAccessHelper.getQuestion(getApplicationContext(), DatabaseDictionary.CAFFEINE));
		caffeineBtn1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DBAccessHelper.insertOrUpdateQuestion(getApplicationContext(), DatabaseDictionary.CAFFEINE, 0);
				naviNext();
			}
		});
		caffeineBtn2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DBAccessHelper.insertOrUpdateQuestion(getApplicationContext(), DatabaseDictionary.CAFFEINE, 1);
				naviNext();
			}
		});
		caffeineBtn3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DBAccessHelper.insertOrUpdateQuestion(getApplicationContext(), DatabaseDictionary.CAFFEINE, 2);
				naviNext();
			}
		});
		caffeineBtn4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DBAccessHelper.insertOrUpdateQuestion(getApplicationContext(), DatabaseDictionary.CAFFEINE, 3);
				naviNext();
			}
		});
		caffeineBtn5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DBAccessHelper.insertOrUpdateQuestion(getApplicationContext(), DatabaseDictionary.CAFFEINE, 4);
				naviNext();
			}
		});
		caffeineBtn6.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Boolean[] b = DBAccessHelper.getQuestionSetting(getApplicationContext());
				b[0] = false;
				DBAccessHelper.insertOrUpdateQuestionSetting(getApplicationContext(), b);
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
		try {
			i.setClass(CaffeineQuestionActivity.this, Class.forName(l.get(l.indexOf(CaffeineQuestionActivity.class.getName()) + 1)));
			startActivity(i);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void naviBack() {
		// Intent i = new Intent();
		// // Set navigation, first parameter is source, second is target.
		// i.setClass(CaffeineQuestionActivity.this, GoodSleepActivity.class);
		// startActivity(i);
		onBackPressed();
	}

	private void setBtn(int num) {
		switch (num) {
		case 0:
			caffeineBtn1.setPressed(true);
			break;
		case 1:
			caffeineBtn2.setPressed(true);
			break;
		case 2:
			caffeineBtn3.setPressed(true);
			break;
		case 3:
			caffeineBtn4.setPressed(true);
			break;
		case 4:
			caffeineBtn5.setPressed(true);
			break;
		}
	}
}
