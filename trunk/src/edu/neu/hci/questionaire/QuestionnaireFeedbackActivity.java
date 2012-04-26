package edu.neu.hci.questionaire;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import edu.neu.hci.Global;
import edu.neu.hci.R;
import edu.neu.hci.alarm.StartSleepActivity;
import edu.neu.hci.db.DBAccessHelper;

public class QuestionnaireFeedbackActivity extends Activity {
	private Button feedBackModifyBtn;
	private Button feedBackConfirmBtn;
	private TextView tv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Bundle class with XML layout
		setContentView(R.layout.questionnaire_feedback);
		// Bundle button in code with button in XML layout
		feedBackModifyBtn = (Button) findViewById(R.id.feedBackModifyBtn);
		feedBackConfirmBtn = (Button) findViewById(R.id.feedBackConfirmBtn);
		tv = (TextView) findViewById(R.id.feedbackText);
		feedBackModifyBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				naviModify();
			}
		});
		feedBackConfirmBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Date date = new Date();
				Global.QUESTION_CONFIRM.put(Global.normalDateFormat.format(date), true);
				naviConfirm();

			}
		});
	}

	public void onResume() {
		super.onResume();
		Date date = new Date();
		if(Global.QUESTION_CONFIRM.get(Global.normalDateFormat.format(date))==true){
			feedBackConfirmBtn.setText("Done");
			feedBackModifyBtn.setVisibility(View.INVISIBLE);
		}
		int re = 0;
		String s = "";
		String temp = "";
		re = DBAccessHelper.getLastQuestion(getApplicationContext(), Global.CAFFEINE);
		if (re != -1) {
			switch (re) {
			case 0:
				temp = "None";
				break;
			case 1:
				temp = "1-2";
				break;
			case 2:
				temp = "3-4";
				break;
			case 3:
				temp = "5-6";
				break;
			case 4:
				temp = "More";
				break;
			case 5:
				temp = "Never";
				break;
			}
			s += "Your answers are as follows:\nCaffeine: " + temp;
		}
		re = DBAccessHelper.getLastQuestion(getApplicationContext(), Global.ALCOHOL);
		if (re != -1) {
			switch (re) {
			case 0:
				temp = "None";
				break;
			case 1:
				temp = "1-2";
				break;
			case 2:
				temp = "3-4";
				break;
			case 3:
				temp = "5-6";
				break;
			case 4:
				temp = "More";
				break;
			case 5:
				temp = "Never";
				break;
			}
			s += "\nAlcohol: " + temp;
		}
		re = DBAccessHelper.getLastQuestion(getApplicationContext(), Global.SMOKE);
		if (re != -1) {
			switch (re) {
			case 0:
				temp = "None";
				break;
			case 1:
				temp = "Few";
				break;
			case 2:
				temp = "1/2 pack";
				break;
			case 3:
				temp = "1 pack";
				break;
			case 4:
				temp = "More";
				break;
			case 5:
				temp = "Never";
				break;
			}
			s += "\nSmoke: " + temp;
		}
		re = DBAccessHelper.getLastQuestion(getApplicationContext(), Global.FOOD);
		if (re != -1) {
			switch (re) {
			case 0:
				temp = "None";
				break;
			case 1:
				temp = "A Little";
				break;
			case 2:
				temp = "Enough";
				break;
			case 3:
				temp = "To Much";
				break;
			}
			s += "\nFood: " + temp;
		}
		re = DBAccessHelper.getLastQuestion(getApplicationContext(), Global.PA);
		if (re != -1) {
			switch (re) {
			case 0:
				temp = "Poor";
				break;
			case 1:
				temp = "Light";
				break;
			case 2:
				temp = "Moderate";
				break;
			case 3:
				temp = "Vigorous";
				break;
			}
			s += "\nPhysical Activity: " + temp;
		}
		re = DBAccessHelper.getLastQuestion(getApplicationContext(), Global.STRESS);
		if (re != -1) {
			switch (re) {
			case 0:
				temp = "None";
				break;
			case 1:
				temp = "Low";
				break;
			case 2:
				temp = "Moderate";
				break;
			case 3:
				temp = "High";
				break;
			}
			s += "\nStress: " + temp;
		}
		tv.setText(s);
	}

	private void naviConfirm() {
		Intent i = new Intent();
		// Set navigation, first parameter is source, second is target.
		i.setClass(QuestionnaireFeedbackActivity.this, StartSleepActivity.class);
		startActivity(i);
	}

	private void naviModify() {
		Intent i = new Intent();
		// Set navigation, first parameter is source, second is target.
		i.setClass(QuestionnaireFeedbackActivity.this, SettingQuestionActivity.class);
		startActivity(i);
	}
}
