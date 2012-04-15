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

public class ActivityQuestionActivity extends Activity {
	private Button activityNextBtn;
	private Button activityBackBtn;
	private Button activityBtn1;
	private Button activityBtn2;
	private Button activityBtn3;
	private Button activityBtn4;
	private List<String> l;
	private TextView title;

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
		title = (TextView) findViewById(R.id.activityTitle);
	}

	public void onResume() {
		super.onResume();
		l = DBAccessHelper.questionList(getApplicationContext());
		title.setText(String.format("Activity (%d/%d)", l.indexOf(ActivityQuestionActivity.class.getName()) + 1, l.size() - 1));
		setBtn(DBAccessHelper.getQuestion(getApplicationContext(), DatabaseDictionary.PA));
		activityBtn1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DBAccessHelper.insertOrUpdateQuestion(getApplicationContext(), DatabaseDictionary.PA, 0);
				naviNext();
			}
		});
		activityBtn2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DBAccessHelper.insertOrUpdateQuestion(getApplicationContext(), DatabaseDictionary.PA, 1);
				naviNext();
			}
		});
		activityBtn3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DBAccessHelper.insertOrUpdateQuestion(getApplicationContext(), DatabaseDictionary.PA, 2);
				naviNext();
			}
		});
		activityBtn4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DBAccessHelper.insertOrUpdateQuestion(getApplicationContext(), DatabaseDictionary.PA, 3);
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
		try {
			i.setClass(ActivityQuestionActivity.this, Class.forName(l.get(l.indexOf(ActivityQuestionActivity.class.getName()) + 1)));
			startActivity(i);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void naviBack() {
		// Intent i = new Intent();
		// // Set navigation, first parameter is source, second is target.
		// i.setClass(ActivityQuestionActivity.this,
		// FoodQuestionActivity.class);
		// startActivity(i);
		onBackPressed();
	}
	private void setBtn(int num) {
		switch (num) {
		case 0:
			activityBtn1.setPressed(true);
			break;
		case 1:
			activityBtn2.setPressed(true);
			break;
		case 2:
			activityBtn3.setPressed(true);
			break;
		case 3:
			activityBtn4.setPressed(true);
			break;
		}
	}
}
