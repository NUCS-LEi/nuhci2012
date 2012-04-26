package edu.neu.hci.summary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import edu.neu.hci.R;

public class FoodActivity extends Activity {
	private Button foodDoneBtn;
	private TextView tv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Bundle class with XML layout
		setContentView(R.layout.summary_food);
		tv = (TextView) findViewById(R.id.foodTV);
		Bundle bundle = this.getIntent().getExtras();
		int i = bundle.getInt("compare");
		if (i == 0)
			tv.setText("Eat moderately for dinner. Don't go to bed hungry. Don't eat within three hours of bedtime. Last night you ate less than your average.");
		else
			tv.setText("Eat moderately for dinner. Don't go to bed hungry. Don't eat within three hours of bedtime. Last night you ate more than your average.");

		// Bundle button in code with button in XML layout
		foodDoneBtn = (Button) findViewById(R.id.foodDoneBtn);
		foodDoneBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				// Set navigation, first parameter is source, second is target.
				i.setClass(FoodActivity.this, SleepSummaryMain.class);
				startActivity(i);

			}
		});
	}
}
