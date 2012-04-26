package edu.neu.hci.alarm;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import edu.mit.android.wocketsver1.ActivityMonitor.BluetoothSensorService;
import edu.mit.android.wocketsver1.ActivityMonitor.DataStore;
import edu.mit.android.wocketsver1.ActivityMonitor.Defines;
import edu.neu.hci.Global;
import edu.neu.hci.R;
import edu.neu.hci.db.DBAccessHelper;
import edu.neu.hci.summary.SleepSummaryMain;

public class WakeUpActivity extends Activity {
	private Button stop;
	private GSDigitalClock dc;
	private Alarm orignal_alarm;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm);
		stop = (Button) findViewById(R.id.stop);
		dc = (GSDigitalClock) findViewById(R.id.DigitalClock1);
		Bundle bundle = this.getIntent().getExtras();
		orignal_alarm = (Alarm) bundle.get(Global.ALARM);
		dc.set24(false);
	}

	public void onResume() {
		super.onResume();
		dc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				deleteSnooze();
				stopService(new Intent(Alarms.ALARM_ALERT_ACTION));
				saveSnooze();
			}
		});
		stop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				deleteSnooze();
				stopSensor();
				Date date = new Date();
				String goToBedTime = DBAccessHelper.getLastSleepTime(getApplicationContext());
				DBAccessHelper.insertOrUpdateSleepTime(getApplicationContext(), goToBedTime, Global.lastModDateFormat.format(date));
				ArrayList<double[]> sleepData = DBAccessHelper.getSummaryPoints(getApplicationContext());
				if (sleepData != null) {
					int count = 0;
					for (double d : sleepData.get(1))
						if (d < Global.WAKE_THRESHOLD)
							count++;
					DBAccessHelper.insertOrUpdateQuestion(getApplicationContext(), Global.SLEEP_SCORE,
							Math.round(((float) count / sleepData.get(1).length) * 100));
				}
				stopService(new Intent(Alarms.ALARM_ALERT_ACTION));
				Intent i = new Intent();
				i.putExtra("src", "1");
				i.setClass(WakeUpActivity.this, SleepSummaryMain.class);
				startActivity(i);
			}
		});
	}

	private void saveSnooze() {
		Alarm alarm = new Alarm();
		alarm.id = 1;
		alarm.enabled = true;
		Date date = new Date();
		date.setTime(date.getTime() + Global.SNOOZE_TIME);
		alarm.hour = date.getHours();
		alarm.minutes = date.getMinutes();
		alarm.vibrate = true;
		if (orignal_alarm != null)
			alarm.alert = orignal_alarm.alert;
		else
			alarm.alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
		Alarms.addAlarm(getApplicationContext(), alarm);
		Toast toast;
		toast = Toast.makeText(getApplicationContext(), String.format("Snooze for %d minutes", Global.SNOOZE_TIME / 60000), Toast.LENGTH_LONG);
		toast.show();
	}

	private void deleteSnooze() {
		Alarms.deleteAlarm(getApplicationContext(), 1);
	}

	private void stopSensor() {
		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		am.cancel(Global.mAlarmSender);
		Global.mAlarmSender = null;

		DataStore.setRunning(false);

		stopService(new Intent(this, BluetoothSensorService.class));

		// Set all connection states to none
		int size = DataStore.mSensors.size();
		for (int x = 0; x < size; x++) {
			DataStore.mSensors.get(x).mConnectionErrors = Defines.NO_CONNECTION_LIMIT;
		}
	}
}
