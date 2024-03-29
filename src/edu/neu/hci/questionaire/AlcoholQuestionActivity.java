package edu.neu.hci.questionaire;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import edu.neu.hci.R;
import edu.neu.hci.db.DBAccessHelper;
import edu.neu.hci.db.DatabaseDictionary;

public class AlcoholQuestionActivity extends Activity {
	private Button alcoholNextBtn;
	private Button alcoholBackBtn;
	private Button alcoholBtn1;
	private Button alcoholBtn2;
	private Button alcoholBtn3;
	private Button alcoholBtn4;
	private Button alcoholBtn5;
	private Button alcoholBtn6;
	private List<String> l;
	private TextView title;

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
		title = (TextView) findViewById(R.id.alcoholTitle);
	}

	public void onResume() {
		super.onResume();
		l = DBAccessHelper.questionList(getApplicationContext());
		title.setText(String.format("Alcohol (%d/%d)", l.indexOf(AlcoholQuestionActivity.class.getName()) + 1, l.size() - 1));
		setBtn(DBAccessHelper.getQuestion(getApplicationContext(), DatabaseDictionary.ALCOHOL));
		alcoholBtn1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DBAccessHelper.insertOrUpdateQuestion(getApplicationContext(), DatabaseDictionary.ALCOHOL, 0);
				naviNext();
			}
		});
		alcoholBtn2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DBAccessHelper.insertOrUpdateQuestion(getApplicationContext(), DatabaseDictionary.ALCOHOL, 1);
				naviNext();
			}
		});
		alcoholBtn3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DBAccessHelper.insertOrUpdateQuestion(getApplicationContext(), DatabaseDictionary.ALCOHOL, 2);
				naviNext();
			}
		});
		alcoholBtn4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DBAccessHelper.insertOrUpdateQuestion(getApplicationContext(), DatabaseDictionary.ALCOHOL, 3);
				naviNext();
			}
		});
		alcoholBtn5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DBAccessHelper.insertOrUpdateQuestion(getApplicationContext(), DatabaseDictionary.ALCOHOL, 4);
				naviNext();
			}
		});
		alcoholBtn6.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Boolean[] b = DBAccessHelper.getQuestionSetting(getApplicationContext());
				b[1] = false;
				DBAccessHelper.insertOrUpdateQuestionSetting(getApplicationContext(), b);
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
		try {
			i.setClass(AlcoholQuestionActivity.this, Class.forName(l.get(l.indexOf(AlcoholQuestionActivity.class.getName()) + 1)));
			startActivity(i);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void naviBack() {
		// Intent i = new Intent();
		// // Set navigation, first parameter is source, second is target.
		// i.setClass(AlcoholQuestionActivity.this,
		// CaffeineQuestionActivity.class);
		// startActivity(i);
		onBackPressed();
	}

	private void setBtn(int num) {
		switch (num) {
		case 0:
			alcoholBtn1.setPressed(true);
			break;
		case 1:
			alcoholBtn2.setPressed(true);
			break;
		case 2:
			alcoholBtn3.setPressed(true);
			break;
		case 3:
			alcoholBtn4.setPressed(true);
			break;
		case 4:
			alcoholBtn5.setPressed(true);
			break;
		}
	}
}
