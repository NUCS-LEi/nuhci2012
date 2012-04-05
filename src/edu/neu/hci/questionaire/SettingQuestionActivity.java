package edu.neu.hci.questionaire;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import edu.neu.hci.GoodSleepActivity;
import edu.neu.hci.R;

public class SettingQuestionActivity extends Activity {
	private Button setBtn;
	private Button cancelBtn;
	private CheckBox coffeine;
	private CheckBox alcohol;
	private CheckBox smoke;
	private CheckBox food;
	private CheckBox physical;
	private CheckBox stress;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Bundle class with XML layout
		setContentView(R.layout.question_setting);
		// Bundle button in code with button in XML layout
		
		setBtn = (Button) findViewById(R.id.questionSetBtn);
		coffeine=(CheckBox)findViewById(R.id.coffeineCheckBox);
		alcohol=(CheckBox)findViewById(R.id.alcoholCheckBox);
		smoke=(CheckBox)findViewById(R.id.smokeCheckBox);
		food=(CheckBox)findViewById(R.id.foodCheckBox);
		physical=(CheckBox)findViewById(R.id.exerciseCheckBox);
		stress=(CheckBox)findViewById(R.id.stressCheckBox);
		coffeine.setChecked(true);
		alcohol.setChecked(true);
		smoke.setChecked(true);
		food.setChecked(true);
		physical.setChecked(true);
		stress.setChecked(true);
		
		setBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				// Set navigation, first parameter is source, second is target.
				i.setClass(SettingQuestionActivity.this, CaffeineQuestionActivity.class);
				startActivity(i);

			}
		});
	}
}
