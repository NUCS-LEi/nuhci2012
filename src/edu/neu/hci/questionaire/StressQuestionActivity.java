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
import edu.neu.hci.StartSleepActivity;
import edu.neu.hci.db.DBAccessHelper;
import edu.neu.hci.db.DatabaseDictionary;

public class StressQuestionActivity extends Activity {
	private Button stressNextBtn;
	private Button stressBackBtn;
	private Button stressBtn1;
	private Button stressBtn2;
	private Button stressBtn3;
	private Button stressBtn4;
	private List<String> l;
	private TextView title;

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
		title = (TextView) findViewById(R.id.stressTitle);
	}

	public void onResume() {
		super.onResume();
		l = DBAccessHelper.questionList(getApplicationContext());
		title.setText(String.format("Stress (%d/%d)", l.indexOf(StressQuestionActivity.class.getName()) + 1, l.size() - 1));
		setBtn(DBAccessHelper.getQuestion(getApplicationContext(), DatabaseDictionary.STRESS));
		stressBtn1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DBAccessHelper.insertOrUpdateQuestion(getApplicationContext(), DatabaseDictionary.STRESS, 0);
				naviNext();
			}
		});
		stressBtn2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DBAccessHelper.insertOrUpdateQuestion(getApplicationContext(), DatabaseDictionary.STRESS, 1);
				naviNext();
			}
		});
		stressBtn3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DBAccessHelper.insertOrUpdateQuestion(getApplicationContext(), DatabaseDictionary.STRESS, 2);
				naviNext();
			}
		});
		stressBtn4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DBAccessHelper.insertOrUpdateQuestion(getApplicationContext(), DatabaseDictionary.STRESS, 3);
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
		try {
			i.setClass(StressQuestionActivity.this, Class.forName(l.get(l.indexOf(StressQuestionActivity.class.getName()) + 1)));
			startActivity(i);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void naviBack() {
		// Intent i = new Intent();
		// // Set navigation, first parameter is source, second is target.
		// i.setClass(StressQuestionActivity.this,
		// ActivityQuestionActivity.class);
		// startActivity(i);
		onBackPressed();
	}

	private void setBtn(int num) {
		switch (num) {
		case 0:
			stressBtn1.setPressed(true);
			break;
		case 1:
			stressBtn2.setPressed(true);
			break;
		case 2:
			stressBtn3.setPressed(true);
			break;
		case 3:
			stressBtn4.setPressed(true);
			break;
		}
	}
}
