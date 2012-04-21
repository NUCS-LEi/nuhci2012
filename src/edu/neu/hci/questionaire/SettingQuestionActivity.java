package edu.neu.hci.questionaire;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import edu.neu.hci.Global;
import edu.neu.hci.GoodSleepActivity;
import edu.neu.hci.Introduction;
import edu.neu.hci.R;
import edu.neu.hci.db.DBAccessHelper;
import edu.neu.hci.db.DBContentProvider;
import edu.neu.hci.db.DatabaseDictionary;

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
		coffeine = (CheckBox) findViewById(R.id.coffeineCheckBox);
		alcohol = (CheckBox) findViewById(R.id.alcoholCheckBox);
		smoke = (CheckBox) findViewById(R.id.smokeCheckBox);
		food = (CheckBox) findViewById(R.id.foodCheckBox);
		physical = (CheckBox) findViewById(R.id.exerciseCheckBox);
		stress = (CheckBox) findViewById(R.id.stressCheckBox);
	}

	public void onResume() {
		super.onResume();
		if (!checkFirstUsage())
			goNext();
		setCheckBox();
		setBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Boolean[] b = new Boolean[] { coffeine.isChecked(), alcohol.isChecked(), smoke.isChecked(), food.isChecked(), physical.isChecked(),
						stress.isChecked() };
				DBAccessHelper.insertOrUpdateQuestionSetting(getApplicationContext(), b);
				goNext();
			}
		});
	}

	private Boolean checkFirstUsage() {
		String last = DBAccessHelper.getLastUsage(getApplicationContext(), SettingQuestionActivity.class.getName());
		if (last == null) {
			DBAccessHelper.logUsage(getApplicationContext(), SettingQuestionActivity.class.getName());
			return true;
		} else
			try {
				if (Global.exactDateFormat.parse(last) != null
						&& System.currentTimeMillis() - Global.exactDateFormat.parse(last).getTime() > 1000 * 60) {
					DBAccessHelper.logUsage(getApplicationContext(), SettingQuestionActivity.class.getName());
					return true;
				} else {
					return false;
				}
			} catch (ParseException e) {
				e.printStackTrace();
				return true;
			}
	}

	public void goNext() {
		List<String> l = DBAccessHelper.questionList(getApplicationContext());
		Intent i = new Intent();
		try {
			i.setClass(SettingQuestionActivity.this, Class.forName(l.get(0)));
			startActivity(i);
			SettingQuestionActivity.this.finish();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void setCheckBox() {
		Boolean[] b = DBAccessHelper.getQuestionSetting(getApplicationContext());
		if (b != null) {
			coffeine.setChecked(b[0]);
			alcohol.setChecked(b[1]);
			smoke.setChecked(b[2]);
			food.setChecked(b[3]);
			physical.setChecked(b[4]);
			stress.setChecked(b[5]);
		} else {
			coffeine.setChecked(true);
			alcohol.setChecked(true);
			smoke.setChecked(true);
			food.setChecked(true);
			physical.setChecked(true);
			stress.setChecked(true);
		}
	}
}
