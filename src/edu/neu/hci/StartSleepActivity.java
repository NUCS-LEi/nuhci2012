package edu.neu.hci;

import common.wheel.widget.NumericWheelAdapter;
import common.wheel.widget.OnWheelChangedListener;
import common.wheel.widget.OnWheelScrollListener;
import common.wheel.widget.WheelAdapter;
import common.wheel.widget.WheelView;
import edu.neu.hci.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;
import android.widget.TextView;

public class StartSleepActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.start_sleep_layout);
		initHourWheel(R.id.set_hour);
		initMinWheel(R.id.set_minute);
		initAMPMWheel(R.id.set_am_pm);

		Button done = (Button) findViewById(R.id.startBtn);
		done.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent();
				// Set navigation, first parameter is source, second is target.
				i.setClass(StartSleepActivity.this, DuringSleepActivity.class);
				startActivity(i);
			}
		});

		updateStatus();
	}

	// Wheel scrolled flag
	private boolean wheelScrolled = false;

	// Wheel scrolled listener
	OnWheelScrollListener scrolledListener = new OnWheelScrollListener() {
		public void onScrollingStarted(WheelView wheel) {
			wheelScrolled = true;
		}

		public void onScrollingFinished(WheelView wheel) {
			wheelScrolled = false;
			updateStatus();
		}
	};

	// Wheel changed listener
	private OnWheelChangedListener changedListener = new OnWheelChangedListener() {
		public void onChanged(WheelView wheel, int oldValue, int newValue) {
			if (!wheelScrolled) {
				updateStatus();
			}
		}
	};

	/**
	 * Updates entered PIN status
	 */
	private void updateStatus() {
		// TextView text = (TextView) findViewById(R.id.pwd_status);
		// text.setText(getAllCode());
	}

	/**
	 * Initializes wheel
	 * 
	 * @param id
	 *            the wheel widget Id
	 */
	private void initHourWheel(int id) {
		WheelView wheel = getWheel(id);
		wheel.setAdapter(new NumericWheelAdapter(00, 12, "%02d"));
		wheel.setCurrentItem(00);

		wheel.addChangingListener(changedListener);
		wheel.addScrollingListener(scrolledListener);
		wheel.setCyclic(true);
		wheel.setInterpolator(new AnticipateOvershootInterpolator());
	}

	private void initMinWheel(int id) {
		WheelView wheel = getWheel(id);
		wheel.setAdapter(new NumericWheelAdapter(00, 59, "%02d"));
		wheel.setCurrentItem(00);

		wheel.addChangingListener(changedListener);
		wheel.addScrollingListener(scrolledListener);
		wheel.setCyclic(true);
		wheel.setInterpolator(new AnticipateOvershootInterpolator());
	}

	private void initAMPMWheel(int id) {
		WheelView wheel = getWheel(id);
		wheel.setAdapter(new WheelAdapter() {

			@Override
			public int getMaximumLength() {
				// TODO Auto-generated method stub
				return "AM".length() + 1;
			}

			@Override
			public int getItemsCount() {
				// TODO Auto-generated method stub
				return 2;
			}

			@Override
			public String getItem(int index) {
				// TODO Auto-generated method stub
				if (index == 0)
					return "AM";
				if (index == 1)
					return "PM";
				else
					return "";
			}
		});
		wheel.setCurrentItem(0);

		wheel.addChangingListener(changedListener);
		wheel.addScrollingListener(scrolledListener);
		wheel.setCyclic(false);
		wheel.setInterpolator(new AnticipateOvershootInterpolator());
	}

	/**
	 * Returns wheel by Id
	 * 
	 * @param id
	 *            the wheel Id
	 * @return the wheel with passed Id
	 */
	private WheelView getWheel(int id) {
		return (WheelView) findViewById(id);
	}

	/**
	 * Tests wheel value
	 * 
	 * @param id
	 *            the wheel Id
	 * @param value
	 *            the value to test
	 * @return true if wheel value is equal to passed value
	 */
	private boolean testWheelValue(int id, int value) {

		return getWheel(id).getCurrentItem() == value;
	}

	/**
	 * Mixes wheel
	 * 
	 * @param id
	 *            the wheel id
	 */
	private void mixWheel(int id) {
		WheelView wheel = getWheel(id);
		wheel.scroll(-25 + (int) (Math.random() * 50), 2000);
	}

	private String getAllCode() {
		StringBuilder sb = new StringBuilder();
		return sb.append(getWheel(R.id.set_hour).getCurrentItem() + "").append(getWheel(R.id.set_minute).getCurrentItem() + "")
				.append(getWheel(R.id.set_am_pm).getCurrentItem() + "").toString();
	}
}
