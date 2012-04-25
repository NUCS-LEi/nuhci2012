package edu.neu.hci;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import edu.mit.android.wocketsver1.ActivityMonitor.DataStore;
import edu.mit.android.wocketsver1.ActivityMonitor.Defines;
import edu.mit.android.wocketsver1.ActivityMonitor.Sensor;
import edu.neu.hci.db.DBAccessHelper;
import edu.neu.hci.db.DBContentProvider;
import edu.neu.hci.questionaire.SettingQuestionActivity;
import edu.neu.hci.summary.SleepSummaryMain;

// Every display page needs to extends Activity, also register in AndroidManifest.xml, 
// you can copy it like other activities and change name.
public class GoodSleepActivity extends Activity {
	/** Called when the activity is first created. */
	private Button startMySleepTrackingBtn;
	private Button howIsMySleepBtn;
	private TextView title;
	private ImageView img;
	static ArrayAdapter<CharSequence> adapter = null;
	static Sensor currentSensor = null;
	static ArrayList<CharSequence> list = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Bundle class with XML layout
		setContentView(R.layout.main);
		// Bundle button in code with button in XML layout
		startMySleepTrackingBtn = (Button) findViewById(R.id.startSleepTrackBtn);
		howIsMySleepBtn = (Button) findViewById(R.id.howIsMySleepBtn);
		title = (TextView) findViewById(R.id.title);
		img = (ImageView) findViewById(R.id.imageView1);
		list = new ArrayList<CharSequence>();
		adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, list);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (DBAccessHelper.getStatic(getApplicationContext()) == 0)
			howIsMySleepBtn.setVisibility(View.INVISIBLE);
		title.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				goIntro();
			}
		});
		img.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				goIntro();
			}
		});
		DBAccessHelper.logUsage(getApplicationContext(), GoodSleepActivity.class.getName());
		// Set button onClickListener
		startMySleepTrackingBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				// Set navigation, first parameter is source, second is target.
				i.setClass(GoodSleepActivity.this, SettingQuestionActivity.class);
				startActivity(i);
			}
		});
		howIsMySleepBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				// Set navigation, first parameter is source, second is target.
				i.setClass(GoodSleepActivity.this, SleepSummaryMain.class);
				startActivity(i);
			}
		});
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, "ImportDB");
		menu.add(0, 1, 1, "ExportDB");
		menu.add(0, 2, 2, "Sensors");
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case 0:
			DBContentProvider.importLogStatDB(getApplicationContext());
			break;
		case 1:
			DBContentProvider.exportLogStatDB();
			break;
		case 2:
			showDialog(0);
			break;
		default:
			break;
		}
		return true;
	}

	private void goIntro() {
		Intent i = new Intent();
		i.putExtra("from", true);
		i.setClass(GoodSleepActivity.this, Introduction.class);
		startActivity(i);
	}

	private void createFakeData(Context c) {
		int i = 0;
		ContentValues cv = new ContentValues();
		Date date = new Date();
		date.setHours(22);
		date.setMinutes(0);
		date.setSeconds(0);
		date.setTime(date.getTime() - 24 * 60 * 60 * 1000);
		long endTime = date.getTime() + 10 * 60 * 60 * 1000;
		Random r = new Random();
		while (date.getTime() < endTime) {
			if (i % 50 == 0)
				android.util.Log.i(Global.TAG, "Fake data: i=" + i);
			date.setTime(date.getTime() + 60 * 1000);
			cv.put("WocketRecordedTime", Global.exactDateFormat.format(date));
			cv.put("PhoneReadTime", Global.exactDateFormat.format(date));
			cv.put("Written", 0);
			cv.put("SeqNum", i++);
			cv.put("Value", 220 + r.nextInt(150));
			c.getContentResolver().insert(DBContentProvider.SUMMARY_POINT_URI, cv);
		}
	}

	private void clearFakeData(Context c) {
		DBContentProvider.execSQL(c, "delete from summary_point");
	}

	public Dialog onCreateDialog(int dialog) {
		Dialog retVal = null;
		switch (dialog) {

		// Shows a list of all the available supported sensor devices and allows
		// the user to enable or disable them
		case 0: {
			if (BluetoothAdapter.getDefaultAdapter() != null) {
				Set<BluetoothDevice> devices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
				Iterator<BluetoothDevice> itr = devices.iterator();
				while (itr.hasNext()) {
					BluetoothDevice dev = itr.next();
					DataStore.checkAndAddSensor(getApplicationContext(), dev.getName(), dev.getAddress());

				}
			}
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Select Sensors:");
			ArrayList<CharSequence> nameArray = DataStore.getSensorNames(false);
			CharSequence[] names = new CharSequence[nameArray.size()];
			for (int x = 0; x < nameArray.size(); x++) {
				names[x] = nameArray.get(x);
			}
			builder.setCancelable(false);
			builder.setNegativeButton("Add new sensor", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					removeDialog(0);
					Intent intentBluetooth = new Intent();
					intentBluetooth.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
					startActivity(intentBluetooth);
				}
			});
			builder.setNeutralButton("Done", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					removeDialog(0);
					Editor edit = getSharedPreferences(Defines.SHARED_PREF_NAME, Context.MODE_PRIVATE).edit();
					edit.clear();
					edit.putInt(Defines.SHARED_PREF_EMOTION_THRESHOLD, DataStore.mEmotionalEventThreshold);
					edit.putInt(Defines.SHARED_PREF_INACTIVITY_TIME, DataStore.mStillnessDuration);
					edit.putBoolean(Defines.SHARED_PREF_FIRST_RUN, true);
					edit.putInt(Defines.SHARED_PREF_ACTIVITY_SCORE, DataStore.getActivityScore());
					edit.putBoolean(Defines.SHARED_PREF_RUNNNING, DataStore.getRunning());
					if (DataStore.getStartRecordingTime() != null) {
						edit.putString(Defines.SHARED_PREF_START_TIME, DataStore.getStartRecordingTime().format2445());
					}
					for (int x = 0; x < Defines.NUM_DAYS_SCORE_TO_SAVE; x++) {
						edit.putInt(Defines.SHARED_PREF_PREV_SCORE + x, DataStore.mPreviousActivityScores[x]);
					}
					if (DataStore.mActivityScoreDate != null) {
						edit.putString(Defines.SHARED_PREF_SCORE_DATE, DataStore.mActivityScoreDate.format2445());
					}
					ArrayList<CharSequence> enabledNames = DataStore.getSensorNames(true);
					int enabledSize = enabledNames.size();
					edit.putInt(Defines.SHARED_PREF_NUM_SENSORS, enabledSize);

					for (int x = 0; x < enabledSize; x++) {
						edit.putString(Defines.SHARED_PREF_SENSOR + x, enabledNames.get(x).toString());
					}

					edit.commit();
				}
			});

			builder.setMultiChoiceItems(names, DataStore.getSensorStates(), new DialogInterface.OnMultiChoiceClickListener() {
				public void onClick(DialogInterface dialog, int which, boolean isChecked) {

					DataStore.mSensors.get(which).mEnabled = isChecked;

					if (isChecked) {
						adapter.add(DataStore.mSensors.get(which).mName);
					} else {
						adapter.remove(DataStore.mSensors.get(which).mName);
						if (currentSensor == DataStore.mSensors.get(which)) {
							currentSensor = null;
						}
					}

					if (adapter.isEmpty()) {
						adapter.add("Enable a sensor");
						currentSensor = null;
					} else {
						adapter.remove("Enable a sensor");
					}

					if (currentSensor == null) {
						currentSensor = DataStore.getFirstSensor();
					}
				}
			});
			retVal = builder.create();
		}
			break;
		default:
			break;

		}
		return retVal;
	}
}