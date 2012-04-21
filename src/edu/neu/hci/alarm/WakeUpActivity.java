package edu.neu.hci.alarm;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import edu.neu.hci.GoodSleepActivity;
import edu.neu.hci.R;
import edu.neu.hci.db.DatabaseDictionary;

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
		orignal_alarm = (Alarm) bundle.get(DatabaseDictionary.ALARM);
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
				stopService(new Intent(Alarms.ALARM_ALERT_ACTION));
				Intent i = new Intent();
				i.setClass(WakeUpActivity.this, GoodSleepActivity.class);
				startActivity(i);
			}
		});
	}

	private void saveSnooze() {
		Alarm alarm = new Alarm();
		alarm.id = 1;
		alarm.enabled = true;
		Date date = new Date();
		date.setTime(date.getTime() + DatabaseDictionary.SNOOZE_TIME);
		alarm.hour = date.getHours();
		alarm.minutes = date.getMinutes();
		alarm.vibrate = true;
		if (orignal_alarm != null)
			alarm.alert = orignal_alarm.alert;
		else
			alarm.alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
		Alarms.addAlarm(getApplicationContext(), alarm);
		Toast toast;
		toast = Toast.makeText(getApplicationContext(), String.format("Snooze for %d minutes", DatabaseDictionary.SNOOZE_TIME / 60000),
				Toast.LENGTH_LONG);
		toast.show();
	}

	private void deleteSnooze() {
		Alarms.deleteAlarm(getApplicationContext(), 1);
	}
}
