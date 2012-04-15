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

public class FoodQuestionActivity extends Activity {
	private Button foodNextBtn;
	private Button foodBackBtn;
	private Button foodBtn1;
	private Button foodBtn2;
	private Button foodBtn3;
	private Button foodBtn4;
	private List<String> l;
	private TextView title;

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
		title = (TextView) findViewById(R.id.foodTitle);
	}

	public void onResume() {
		super.onResume();
		l = DBAccessHelper.questionList(getApplicationContext());
		title.setText(String.format("Food (%d/%d)", l.indexOf(FoodQuestionActivity.class.getName()) + 1, l.size() - 1));
		setBtn(DBAccessHelper.getQuestion(getApplicationContext(), DatabaseDictionary.FOOD));
		foodBtn1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DBAccessHelper.insertOrUpdateQuestion(getApplicationContext(), DatabaseDictionary.FOOD, 0);
				naviNext();
			}
		});
		foodBtn2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DBAccessHelper.insertOrUpdateQuestion(getApplicationContext(), DatabaseDictionary.FOOD, 1);
				naviNext();
			}
		});
		foodBtn3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DBAccessHelper.insertOrUpdateQuestion(getApplicationContext(), DatabaseDictionary.FOOD, 2);
				naviNext();
			}
		});
		foodBtn4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DBAccessHelper.insertOrUpdateQuestion(getApplicationContext(), DatabaseDictionary.FOOD, 3);
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
		try {
			i.setClass(FoodQuestionActivity.this, Class.forName(l.get(l.indexOf(FoodQuestionActivity.class.getName()) + 1)));
			startActivity(i);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void naviBack() {
		// Intent i = new Intent();
		// // Set navigation, first parameter is source, second is target.
		// i.setClass(FoodQuestionActivity.this, SmokeQuestionActivity.class);
		// startActivity(i);
		onBackPressed();
	}
	private void setBtn(int num) {
		switch (num) {
		case 0:
			foodBtn1.setPressed(true);
			break;
		case 1:
			foodBtn2.setPressed(true);
			break;
		case 2:
			foodBtn3.setPressed(true);
			break;
		case 3:
			foodBtn4.setPressed(true);
			break;
		}
	}
}
