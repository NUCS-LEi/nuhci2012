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

public class SmokeQuestionActivity extends Activity {
	private Button smokeNextBtn;
	private Button smokeBackBtn;
	private Button smokeBtn1;
	private Button smokeBtn2;
	private Button smokeBtn3;
	private Button smokeBtn4;
	private Button smokeBtn5;
	private Button smokeBtn6;
	private List<String> l;
	private TextView title;

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
		title = (TextView) findViewById(R.id.smokeTitle);
	}

	public void onResume() {
		super.onResume();
		l = DBAccessHelper.questionList(getApplicationContext());
		title.setText(String.format("Smoke (%d/%d)", l.indexOf(SmokeQuestionActivity.class.getName()) + 1, l.size() - 1));
		setBtn(DBAccessHelper.getQuestion(getApplicationContext(), DatabaseDictionary.SMOKE));
		smokeBtn1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DBAccessHelper.insertOrUpdateQuestion(getApplicationContext(), DatabaseDictionary.SMOKE, 0);
				naviNext();
			}
		});
		smokeBtn2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DBAccessHelper.insertOrUpdateQuestion(getApplicationContext(), DatabaseDictionary.SMOKE, 1);
				naviNext();
			}
		});
		smokeBtn3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DBAccessHelper.insertOrUpdateQuestion(getApplicationContext(), DatabaseDictionary.SMOKE, 2);
				naviNext();
			}
		});
		smokeBtn4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DBAccessHelper.insertOrUpdateQuestion(getApplicationContext(), DatabaseDictionary.SMOKE, 3);
				naviNext();
			}
		});
		smokeBtn5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DBAccessHelper.insertOrUpdateQuestion(getApplicationContext(), DatabaseDictionary.SMOKE, 4);
				naviNext();
			}
		});
		smokeBtn6.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Boolean[] b = DBAccessHelper.getQuestionSetting(getApplicationContext());
				b[0] = false;
				DBAccessHelper.insertOrUpdateQuestionSetting(getApplicationContext(), b);
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
		try {
			i.setClass(SmokeQuestionActivity.this, Class.forName(l.get(l.indexOf(SmokeQuestionActivity.class.getName()) + 1)));
			startActivity(i);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void naviBack() {
		// Intent i = new Intent();
		// // Set navigation, first parameter is source, second is target.
		// i.setClass(SmokeQuestionActivity.this,
		// AlcoholQuestionActivity.class);
		// startActivity(i);
		onBackPressed();
	}

	private void setBtn(int num) {
		switch (num) {
		case 0:
			smokeBtn1.setPressed(true);
			break;
		case 1:
			smokeBtn2.setPressed(true);
			break;
		case 2:
			smokeBtn3.setPressed(true);
			break;
		case 3:
			smokeBtn4.setPressed(true);
			break;
		case 4:
			smokeBtn5.setPressed(true);
			break;
		}
	}
}
