package edu.neu.hci.summary;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import edu.neu.hci.GoodSleepActivity;
import edu.neu.hci.R;

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
		String[][] s = new String[][] { { "Sleep Score", " " }, { "Sleep Duration", " " }, { "Go To Bed Time", " " }, { "Wake Up Time", " " },
				{ "Caffeine", " " }, { "Alcohol", " " }, { "Smoke", " " }, { "Physical Activity", " " }, { "Food", " " }, { "Stress", " " } };
		ArrayList<String[]> summaryDetail = new ArrayList<String[]>();
		for (int i = 0; i < s.length; i++) {
			summaryDetail.add(s[i]);
		}
		int maxRows = (summaryDetail != null) ? Math.max(10, summaryDetail.size()) : 10;
		statGrid.removeAllViews();
		statInfo.setVisibility(View.VISIBLE);
		for (int i = 0; i < maxRows; i++) {
			View v = LayoutInflater.from(this).inflate(R.layout.summary_detail_item, null);

			TextView statName = (TextView) v.findViewById(R.id.statName);
			ImageView statValue1 = (ImageView) v.findViewById(R.id.statValue1);
			ImageView statValue2 = (ImageView) v.findViewById(R.id.statValue2);

			// Create stripes
			int backgroundColor = (i % 2 == 0) ? Color.WHITE : Color.LTGRAY;
			statName.setBackgroundColor(backgroundColor);
			statValue1.setBackgroundColor(backgroundColor);
			statValue2.setBackgroundColor(backgroundColor);

			if (summaryDetail == null || summaryDetail.size() <= i) {
				statName.setText("");
				// statValue.setText("");
			} else {
					statName.setText(summaryDetail.get(i)[0]);
					if (i % 2 == 0)
						statValue1.setImageResource(R.drawable.down);
					else
						statValue1.setImageResource(R.drawable.up);
					if (i % 3 == 0)
						statValue2.setImageResource(R.drawable.t_down);
					else
						statValue2.setImageResource(R.drawable.t_up);
				// if (summaryDetail.get(i)[1] == null ||
				// summaryDetail.get(i)[1].equals("") ||
				// summaryDetail.get(i)[1].equals("null"))
				// statValue.setText(" ");
				// else
				// statValue.setText(summaryDetail.get(i)[1]);
			}
			v.setTag(String.valueOf(i));
			v.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					v.setDrawingCacheBackgroundColor(Color.GREEN);
					Intent intent = null;
					if (v.getTag().toString().equals("0")) {
						intent = new Intent(SleepSummaryDetail.this, SleepScoreActivity.class);
					}
					if (v.getTag().toString().equals("1")) {
						intent = new Intent(SleepSummaryDetail.this, SleepDurationActivity.class);
					}
					if (v.getTag().toString().equals("2")) {
						intent = new Intent(SleepSummaryDetail.this, GoToBedTimeActivity.class);
					}
					if (v.getTag().toString().equals("3")) {
						intent = new Intent(SleepSummaryDetail.this, WakeUpTimeActivity.class);
					}
					if (v.getTag().toString().equals("4")) {
						intent = new Intent(SleepSummaryDetail.this, CaffeineActivity.class);
					}
					if (v.getTag().toString().equals("5")) {
						intent = new Intent(SleepSummaryDetail.this, AlcoholActivity.class);
					}
					if (v.getTag().toString().equals("6")) {
						intent = new Intent(SleepSummaryDetail.this, SmokingActivity.class);
					}
					if (v.getTag().toString().equals("7")) {
						intent = new Intent(SleepSummaryDetail.this, PhysicalActivity.class);
					}
					if (v.getTag().toString().equals("8")) {
						intent = new Intent(SleepSummaryDetail.this, FoodActivity.class);
					}
					if (v.getTag().toString().equals("9")) {
						intent = new Intent(SleepSummaryDetail.this, StressActivity.class);
					}
					if (intent != null)
						// android.util.Log.e("DUBUG", "debug:" +
						// v.getTag().toString());
						startActivity(intent);
				}
			});
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
