package edu.neu.hci.summary;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import edu.neu.hci.Global;
import edu.neu.hci.GoodSleepActivity;
import edu.neu.hci.R;
import edu.neu.hci.db.DBAccessHelper;

public class SleepSummaryDetail extends Activity {
	private LinearLayout statInfo;
	private LinearLayout statGrid;
	private Button btnBack;
	private TextView title;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.summary_detail);
		title = (TextView) findViewById(R.id.userStatTitle);
		statInfo = (LinearLayout) findViewById(R.id.statInfo);
		statGrid = (LinearLayout) findViewById(R.id.statGrid);

		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(backBtnListener);
		title.setText("Compare to your average:");
	}

	public void onResume() {
		super.onResume();
		refreshGrid();
	}

	private void refreshGrid() {
		String[][] modular = new String[][] { { Global.SLEEP_SCORE, "Sleep Score" }, { Global.SLEEP_DURATION, "Sleep Duration" },
				{ Global.GO_TO_BED_TIME, "Go To Bed Time" }, { Global.WAKE_UP_TIME, "Wake Up Time" }, { Global.CAFFEINE, "Caffeine" },
				{ Global.ALCOHOL, "Alcohol" }, { Global.SMOKE, "Smoke" }, { Global.PA, "Physical Activity" }, { Global.FOOD, "Food" },
				{ Global.STRESS, "Stress" } };
		ArrayList<String[]> summaryDetail = new ArrayList<String[]>();
		for (int i = 0; i < modular.length; i++) {
			summaryDetail.add(modular[i]);
		}
		int maxRows = (summaryDetail != null) ? Math.max(10, summaryDetail.size()) : 10;
		statGrid.removeAllViews();
		statInfo.setVisibility(View.VISIBLE);
		int colorCount = 0;
		for (int i = 0; i < maxRows; i++) {
			int last = DBAccessHelper.getLastNightStatic(getApplicationContext(), summaryDetail.get(i)[0]);
			float avg = DBAccessHelper.getAverageStatic(getApplicationContext(), summaryDetail.get(i)[0]);
			
			if (last == -1)
				continue;
			View v = LayoutInflater.from(this).inflate(R.layout.summary_detail_item, null);

			TextView statName = (TextView) v.findViewById(R.id.statName);
			ImageView img = (ImageView) v.findViewById(R.id.quesImg);
			ImageView statValue1 = (ImageView) v.findViewById(R.id.statValue1);
			ImageView statValue2 = (ImageView) v.findViewById(R.id.statValue2);

			// Create stripes
			int backgroundColor = (colorCount % 2 == 0) ? Color.WHITE : Color.LTGRAY;
			colorCount++;
			v.setBackgroundColor(backgroundColor);
			img.setBackgroundColor(backgroundColor);
			statName.setBackgroundColor(backgroundColor);
			statValue1.setBackgroundColor(backgroundColor);
			statValue2.setBackgroundColor(backgroundColor);

			statName.setText(summaryDetail.get(i)[1]);
			img.setImageResource(R.drawable.question);
			if (last < avg)
				statValue1.setImageResource(R.drawable.down);
			else if (last > avg)
				statValue1.setImageResource(R.drawable.up);
			else
				statValue1.setImageResource(R.drawable.equal);
			if (summaryDetail.get(i)[0].equals(Global.SLEEP_SCORE)) {
				if (last < avg)
					statValue2.setImageResource(R.drawable.t_down);
				else
					statValue2.setImageResource(R.drawable.t_up);
			} else if (summaryDetail.get(i)[0].equals(Global.SLEEP_DURATION)) {
				if (last < avg)
					statValue2.setImageResource(R.drawable.t_down);
				else
					statValue2.setImageResource(R.drawable.t_up);
			} else if (summaryDetail.get(i)[0].equals(Global.GO_TO_BED_TIME)) {
				if (Math.abs(last - avg) > 3600 * 1000)
					statValue2.setImageResource(R.drawable.t_down);
				else
					statValue2.setImageResource(R.drawable.t_up);
			} else if (summaryDetail.get(i)[0].equals(Global.WAKE_UP_TIME)) {
				if (Math.abs(last - avg) > 3600 * 1000)
					statValue2.setImageResource(R.drawable.t_down);
				else
					statValue2.setImageResource(R.drawable.t_up);
			} else if (summaryDetail.get(i)[0].equals(Global.CAFFEINE)) {
				if (last > avg)
					statValue2.setImageResource(R.drawable.t_down);
				else
					statValue2.setImageResource(R.drawable.t_up);
			} else if (summaryDetail.get(i)[0].equals(Global.ALCOHOL)) {
				if (last > avg)
					statValue2.setImageResource(R.drawable.t_down);
				else
					statValue2.setImageResource(R.drawable.t_up);
			} else if (summaryDetail.get(i)[0].equals(Global.FOOD)) {
				if (last > avg)
					statValue2.setImageResource(R.drawable.t_down);
				else
					statValue2.setImageResource(R.drawable.t_up);
			} else if (summaryDetail.get(i)[0].equals(Global.SMOKE)) {
				if (last > avg)
					statValue2.setImageResource(R.drawable.t_down);
				else
					statValue2.setImageResource(R.drawable.t_up);
			} else if (summaryDetail.get(i)[0].equals(Global.PA)) {
				if (last > avg)
					statValue2.setImageResource(R.drawable.t_down);
				else
					statValue2.setImageResource(R.drawable.t_up);
			} else if (summaryDetail.get(i)[0].equals(Global.STRESS)) {
				if (last > avg)
					statValue2.setImageResource(R.drawable.t_down);
				else
					statValue2.setImageResource(R.drawable.t_up);
			}
			v.setTag(summaryDetail.get(i)[0]);
			v.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					v.setDrawingCacheBackgroundColor(Color.GREEN);
					Intent intent = null;
					if (v.getTag().toString().equals(Global.SLEEP_SCORE)) {
						intent = new Intent(SleepSummaryDetail.this, SleepScoreActivity.class);
					}
					if (v.getTag().toString().equals(Global.SLEEP_DURATION)) {
						intent = new Intent(SleepSummaryDetail.this, SleepDurationActivity.class);
					}
					if (v.getTag().toString().equals(Global.GO_TO_BED_TIME)) {
						intent = new Intent(SleepSummaryDetail.this, GoToBedTimeActivity.class);
					}
					if (v.getTag().toString().equals(Global.WAKE_UP_TIME)) {
						intent = new Intent(SleepSummaryDetail.this, WakeUpTimeActivity.class);
					}
					if (v.getTag().toString().equals(Global.CAFFEINE)) {
						intent = new Intent(SleepSummaryDetail.this, CaffeineActivity.class);
					}
					if (v.getTag().toString().equals(Global.ALCOHOL)) {
						intent = new Intent(SleepSummaryDetail.this, AlcoholActivity.class);
					}
					if (v.getTag().toString().equals(Global.SMOKE)) {
						intent = new Intent(SleepSummaryDetail.this, SmokingActivity.class);
					}
					if (v.getTag().toString().equals(Global.PA)) {
						intent = new Intent(SleepSummaryDetail.this, PhysicalActivity.class);
					}
					if (v.getTag().toString().equals(Global.FOOD)) {
						intent = new Intent(SleepSummaryDetail.this, FoodActivity.class);
					}
					if (v.getTag().toString().equals(Global.STRESS)) {
						intent = new Intent(SleepSummaryDetail.this, StressActivity.class);
					}
					if (intent != null)
						startActivity(intent);
				}
			});
			statGrid.addView(v);
		}
		if (colorCount == 0) {
			View v = LayoutInflater.from(this).inflate(R.layout.summary_detail_item, null);
			TextView statName = (TextView) v.findViewById(R.id.statName);
			ImageView img = (ImageView) v.findViewById(R.id.quesImg);
			ImageView statValue1 = (ImageView) v.findViewById(R.id.statValue1);
			ImageView statValue2 = (ImageView) v.findViewById(R.id.statValue2);
			int backgroundColor = Color.WHITE;
			v.setBackgroundColor(backgroundColor);
			img.setBackgroundColor(backgroundColor);
			statName.setBackgroundColor(backgroundColor);
			statValue1.setBackgroundColor(backgroundColor);
			statValue2.setBackgroundColor(backgroundColor);
			statName.setText("No Data Of Last Night Sleep");
			statGrid.addView(v);
		}
	}

	private OnClickListener backBtnListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(SleepSummaryDetail.this, GoodSleepActivity.class);
			startActivity(intent);
		}

	};
}
