package edu.neu.hci.alarm;

import java.text.ParseException;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import edu.mit.android.wocketsver1.ActivityMonitor.BluetoothSensorService;
import edu.mit.android.wocketsver1.ActivityMonitor.DataStore;
import edu.mit.android.wocketsver1.ActivityMonitor.Defines;
import edu.mit.android.wocketsver1.ActivityMonitor.Main;
import edu.neu.hci.Global;
import edu.neu.hci.GoodSleepActivity;
import edu.neu.hci.R;
import edu.neu.hci.db.DBAccessHelper;
import edu.neu.hci.db.DBContentProvider;

public class DuringSleepActivity extends Activity {
	private Button stopTrackingBtn;
	private Button backBtn;
	private TextView tv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.good_night_with_sensor);
		stopTrackingBtn = (Button) findViewById(R.id.stopTrackingBtn);
		backBtn = (Button) findViewById(R.id.backBtn);
		tv=(TextView)findViewById(R.id.textView1);
	}

	@Override
	public void onResume() {
		super.onResume();
		try {
			tv.setText("Alarm time:   "+Global.apmDateFormat.format(Global.lastModDateFormat.parse(DBAccessHelper.getLastSleepTime(getApplicationContext()))));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				stopSensor();
				Intent i = new Intent();
				// Set navigation, first parameter is source, second is target.
				i.setClass(DuringSleepActivity.this, StartSleepActivity.class);
				startActivity(i);
				
			}
		});
		stopTrackingBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//stopSensor();
				Intent i = new Intent();
				// Set navigation, first parameter is source, second is target.
				i.setClass(DuringSleepActivity.this, GoodSleepActivity.class);
				startActivity(i);
			}
		});
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(1, 3, 0, "Back");
		menu.add(1, 4, 1, "ExportDB");
		menu.add(1, 5, 2, "Sensor");
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case 3:
			onBackPressed();
			break;
		case 4:
			DBContentProvider.exportLogStatDB();
			break;
		case 5:
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), Main.class);
			startActivity(intent);
			break;
		default:
			break;
		}
		return true;
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
