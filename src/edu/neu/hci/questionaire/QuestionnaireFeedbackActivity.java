package edu.neu.hci.questionaire;

import edu.neu.hci.GoodSleepActivity;
import edu.neu.hci.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class QuestionnaireFeedbackActivity extends Activity {
	private Button feedBackModifyBtn;
	private Button feedBackConfirmBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Bundle class with XML layout
		setContentView(R.layout.questionnaire_feedback);
		// Bundle button in code with button in XML layout
		feedBackModifyBtn = (Button) findViewById(R.id.feedBackModifyBtn);
		feedBackConfirmBtn = (Button) findViewById(R.id.feedBackConfirmBtn);
		
		feedBackModifyBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				naviModify();
			}
		});
		feedBackConfirmBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				naviConfirm();

			}
		});
	}

	private void naviConfirm() {
		Intent i = new Intent();
		// Set navigation, first parameter is source, second is target.
		i.setClass(QuestionnaireFeedbackActivity.this, GoodSleepActivity.class);
		startActivity(i);
	}

	private void naviModify() {
		Intent i = new Intent();
		// Set navigation, first parameter is source, second is target.
		i.setClass(QuestionnaireFeedbackActivity.this, CaffeineQuestionActivity.class);
		startActivity(i);
	}
}
