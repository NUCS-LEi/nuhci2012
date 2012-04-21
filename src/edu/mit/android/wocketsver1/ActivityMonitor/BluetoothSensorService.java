/******************************************************************************
 * 
 * @brief Service to read from all enabled Bluetooth sensors and pass the data read in
 * 			to the appropriate objects to be decoded.
 * @author Kyle Bechtel
 * @date  6/1/11
 * 
 * 
 *****************************************************************************/

package edu.mit.android.wocketsver1.ActivityMonitor;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.Vibrator;
import android.text.format.Time;
import edu.mit.android.wocketsver1.ActivityMonitor.Sensor.TYPE;
import edu.mit.android.wocketsver1.mhealth.sensordata.DataSaver;
import edu.neu.hci.R;

public class BluetoothSensorService extends Service {
	static final String TAG = "BluetoothSensorService";

	static NotificationManager mNM;
	static PowerManager.WakeLock wl;
	static BluetoothAdapter mBluetoothAdapter;

	boolean readMore = true;
	Sensor currentSensor = null;

	// Defines used for notifications
	static final int READING_NOTIFICATION_ID = 0;
	static final int STILL_NOTIFICATION_ID = 1;
	static final int EMOTION_NOTIFICATION_ID = 2;

	// The last time the user was notified about being still
	static Time mLastStillAlertTime = null;

	// Vibration pattern
	private static final long[] vibrateToneAlive = { 30, 60, 20, 60, 10, 60 };
	private static final long[] vibrateToneWocket = { 40, 40 };
	private static final long[] vibrateToneZephyr = { 10, 10, 10, 10 };
	private static final int WOCKET = 0;
	private static final int ZEPHYR = 1;
	private static final int ALIVE = 2;

	private DataSaver mWocketDataSaver;

	private static Vibrator vb = null;

	public int lastSeqNum = 0;

	private void startVibrationAlert(int type) {
		if (vb == null)
			vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		if (vb != null) {
			if (type == WOCKET)
				vb.vibrate(vibrateToneWocket, -1);
			else if (type == ZEPHYR)
				vb.vibrate(vibrateToneZephyr, -1);
			else if (type == ALIVE)
				vb.vibrate(vibrateToneAlive, -1);
		} else
			android.util.Log.i(TAG, "Couldn't get vibration object.");
	}

	@Override
	// Called when the service is created and needs to be run
	public void onCreate() {

		// Acquire a wake lock so that the CPU will stay awake while we are
		// reading
		// from the sensors
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getString(R.string.app_name));
		wl.acquire();
		if (!wl.isHeld()) {
			writeLogInfoTest("WakeLock not held when should be");
		}

		// writeLogInfoTest("CreateService");

		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		// show the icon in the status bar
//		 showReadingNotification();

		// The data store might not be initialized if the app was closed
		// due to a low memory condition, or some other reason beyond our
		// control.
		// If this happens, we just want to load all our data back from NV and
		// let the
		// service keep running.
		if (!DataStore.getInitialized()) {
			DataStore.init(getApplicationContext());
		}

		mWocketDataSaver = new DataSaver("test", "test", "test", edu.mit.android.wocketsver1.mhealth.sensordata.SensorData.TYPE.WOCKET12BITRAW, 44,
				"junk");

		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		// Start up the thread running the service. Note that we create a
		// separate thread because the service normally runs in the process's
		// main thread, which we don't want to block.
		Thread thr = new Thread(null, mTask, "BluetoothServiceThread");
		thr.start();
	}

	@Override
	// Called when the service has ended
	public void onDestroy() {
		// writeLogInfoTest("DestroyService");
		// Cancel the notification -- we use the same ID that we had used to
		// start it
		mNM.cancel(READING_NOTIFICATION_ID);

		if (!wl.isHeld()) {
			writeLogInfoTest("WakeLock will be released but not held when should be.");
		}

		// Release the wake lock so that the CPU can go back into low power mode
		wl.release();
	}

	private void writeLogInfoTest(String msg) {
		String dirNameTime = "data/summary/";
		String fileNameTime = dirNameTime + "Times.csv";
		File fileTime = new File(DataStore.getContext().getExternalFilesDir(null), fileNameTime);
		Time nowT = new Time();
		Date nowD = new Date();

		try {
			if (fileTime != null) {
				boolean writeHeader = false;
				if (!fileTime.exists()) {
					File directoryTime = new File(DataStore.getContext().getExternalFilesDir(null), dirNameTime);
					directoryTime.mkdirs();
					fileTime.createNewFile();
				}

				FileWriter outTime = new FileWriter(fileTime, true);
				// if (DataStore.mThreadLastRunSystemTime != 0)
				outTime.write(nowD.toString() + "," + (System.currentTimeMillis() - DataStore.mThreadLastRunSystemTime) + "," + msg + "\n");
				outTime.close();
			}
		} catch (Exception e) {

		}
	}

	/**
	 * The function that runs in our worker thread
	 */
	Runnable mTask = new Runnable() {
		public void run() {

			// Debugging only
			// startVibrationAlert(ALIVE);

			DataStore.setThreadLastRunTime(System.currentTimeMillis());
			writeLogInfoTest("");

			SharedPreferences pref = getSharedPreferences("WocketsACPref", MODE_PRIVATE);
			lastSeqNum = pref.getInt("lastSeqNum", 0);

			// Make sure the phone supports Bluetooth
			if (mBluetoothAdapter != null) {

				// /This code checks to see if any of the enabled sensors have
				// made a successful
				// connection in the last 10 minutes. If none of them have, we
				// will toggle
				// the state of bluetooth just to ensure there is no problem in
				// the driver
				// and that everything is OK.
				boolean btProblem = true;
				int numSensors = 0;
				for (int x = 0; x < DataStore.mSensors.size(); x++) {
					if (DataStore.mSensors.get(x).mEnabled) {
						numSensors++;
						if (DataStore.mSensors.get(x).mConnectionErrors < Defines.CONNECTION_ERRORS_BEFORE_RESET) {
							btProblem = false;
							break;
						}
					}
				}

				if (btProblem == true && numSensors > 0) {
					mBluetoothAdapter.disable();

					// We don't want to be constantly toggling the bluetooth
					// state because
					// it appears to break the driver so you can't ever turn it
					// back on.
					// To prevent this, reset the error counts to the minimum no
					// connection level
					for (int x = 0; x < DataStore.mSensors.size(); x++) {
						if (DataStore.mSensors.get(x).mEnabled
								&& DataStore.mSensors.get(x).mConnectionErrors >= Defines.CONNECTION_ERRORS_BEFORE_RESET) {
							DataStore.mSensors.get(x).mConnectionErrors = Defines.NO_CONNECTION_LIMIT;
						}
					}

					int retryCount = 0;
					// Retry for up to 10 seconds
					while (mBluetoothAdapter.getState() != BluetoothAdapter.STATE_OFF && retryCount < 100) {
						synchronized (mBinder) {
							try {
								mBinder.wait(100);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						retryCount++;
					}
				}

				mBluetoothAdapter.enable();

				// Loop through all the paired bluetooth devices on the phone
				// and check to see if
				// any of them match the devices that we want to use and have
				// enabled
				Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
				Iterator<BluetoothDevice> itr = devices.iterator();
				while (itr.hasNext()) {
					BluetoothDevice dev = itr.next();
					currentSensor = null;

					for (int x = 0; x < DataStore.mSensors.size(); x++) {
						if (dev.getName().equals(DataStore.mSensors.get(x).mName) && DataStore.mSensors.get(x).mEnabled) {
							currentSensor = DataStore.mSensors.get(x);
							break;
						}
					}
					if (currentSensor != null) {
						// We've found a device we want to read from
						try {
							if (mBluetoothAdapter.isDiscovering()) {
								mBluetoothAdapter.cancelDiscovery();
							}
							BluetoothSocket sock = null;

							// try to create an insecure RFComm socket. This
							// might not always work
							try {

								Method m = dev.getClass().getMethod("createRfcommSocket", new Class[] { int.class });
								sock = (BluetoothSocket) m.invoke(dev, 1);

							} catch (Exception e) {
								e.printStackTrace();
							}

							// If we couldn't create an insecure socket, just
							// try a regular one
							if (sock == null) {
								// This uses the "well known" SPP UUID to
								// connect to
								sock = dev.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
							}

							int retryCount = 0;
							// Retry for up to 10 seconds, waiting until the
							// bluetooth adapter is
							// actually on and ready to go
							while (mBluetoothAdapter.getState() != BluetoothAdapter.STATE_ON && retryCount < 100) {
								synchronized (mBinder) {
									mBinder.wait(100);
								}
								retryCount++;
							}

							// hold off 30ms just to give time for everything to
							// be ready.
							// This delay might be able to be removed to speed
							// things up a tiny bit
							// and increase battery life
							synchronized (mBinder) {
								mBinder.wait(30);
							}
							sock.connect();

							InputStream in = sock.getInputStream();
							OutputStream out = sock.getOutputStream();

							if (currentSensor.mType == Sensor.TYPE.POLAR) {
								byte[] data = new byte[Defines.MAX_POLAR_PACKET_SIZE];
								int count = in.read(data, 0, Defines.MAX_POLAR_PACKET_SIZE);
								if (count == Defines.MAX_POLAR_PACKET_SIZE) {
									currentSensor.parsePacket(data, count);
									currentSensor.mConnectionErrors = 0;
								}
							} else if (currentSensor.mType == Sensor.TYPE.ZEPHYR) {
								// Vibrate the phone for debugging purposes
								// startVibrationAlert(ZEPHYR);

								byte[] data = new byte[Defines.MAX_ZEPHYR_PACKET_SIZE];

								int count = in.read(data, 0, Defines.MAX_ZEPHYR_PACKET_SIZE);
								while (count < Defines.MAX_ZEPHYR_PACKET_SIZE) {
									count += in.read(data, count, Defines.MAX_ZEPHYR_PACKET_SIZE - count);
								}

								currentSensor.parsePacket(data, count);
								currentSensor.mConnectionErrors = 0;

							} else if (currentSensor.mType == Sensor.TYPE.WOCKET) {
								// startVibrationAlert(WOCKET);

								WocketSensor wocket = (WocketSensor) currentSensor;

								if (!wocket.mInit) {
									out.write(WocketSensor.WOCKET_60_SEC_BURST_PACKET);
									// TODO currently this flag is never set and
									// we write the
									// mode every time the Wocket connects.
									// There was some trouble
									// switching from continuous to burst mode
									// before and this had
									// to be sent often. This may no longer be
									// the case and we
									// will only need to send once

								}

								byte[] seqNum = new byte[4];
								if (wocket.mInit) {
									byte temp;
									temp = (byte) (lastSeqNum >> 8);
									seqNum[0] = (byte) WocketSensor.WOCKET_ACK_PACKET;
									seqNum[1] = (byte) ((byte) (temp >>> 1) & 0x7f);
									seqNum[2] = (byte) ((byte) (temp << 6) & 0x40);
									temp = (byte) (lastSeqNum);
									seqNum[2] |= (byte) ((byte) (temp >> 2) & 0x3f);
									seqNum[3] = (byte) ((byte) (temp << 5) & 0x60);

									/*
									 * out.write(seqNum[0]);
									 * out.write(seqNum[1]);
									 * out.write(seqNum[2]);
									 * out.write(seqNum[3]); //out.flush();
									 * Log.i("ActivityMonitor", "ACK Sent");
									 * android.util.Log.i("ActivityMonitor",
									 * "LastSeqNum :" +
									 * String.valueOf(lastSeqNum));
									 */
								}

								// Get battery percent
								// out.write(WocketSensor.WOCKET_BATTERY_PERCENT_PACKET);

								byte[] data = new byte[Defines.MAX_WOCKET_PACKET_SIZE];
								data[0] = (byte) 0;
								int count = 0;

								try {

									int delay = 0;
									// read until we don't get an 0xFF byte
									while (data[0] != WocketSensor.WOCKET_HEADER_DATA) {
										in.read(data, 0, 1);
										delay++;
										synchronized (mBinder) {
											mBinder.wait(10);
										}
									}

									// while we are reading 0xFF bytes, just
									// through them away
									while (data[0] == WocketSensor.WOCKET_HEADER_DATA) {
										in.read(data, 0, 1);
									}
									// Increment the count, as we have read the
									// first non 0xFF byte
									count++;

									/*
									 * if(wocket.mInit) { out.write(seqNum[0]);
									 * out.write(seqNum[1]);
									 * out.write(seqNum[2]);
									 * out.write(seqNum[3]);
									 * 
									 * Log.i("ActivityMonitor", "ACK Sent");
									 * android.util.Log.i("ActivityMonitor",
									 * "LastSeqNum :" +
									 * String.valueOf(lastSeqNum)); }
									 */

								} catch (Exception e) {
									currentSensor.mConnectionErrors++;
									e.printStackTrace();
								}

								int readTries = 0;

								while ((in.available() > 0 || readTries < 5) && count < Defines.MAX_WOCKET_PACKET_SIZE) {

									if (!wocket.mInit)
										wocket.mInit = true;
									else {
										// send an ack
										out.write(seqNum[0]);
										out.write(seqNum[1]);
										out.write(seqNum[2]);
										out.write(seqNum[3]);
										android.util.Log.i("ActivityMonitor", "LastSeqNum :" + String.valueOf(lastSeqNum));
										byte[] WOCKET_SET_LED_PACKET = { (byte) 0xBC, (byte) 0x03 };
										out.write(WOCKET_SET_LED_PACKET);
										out.write(WOCKET_SET_LED_PACKET);
										out.write(WOCKET_SET_LED_PACKET);
									}

									readTries++;
									count += in.read(data, count, Defines.MAX_WOCKET_PACKET_SIZE - count);
									synchronized (mBinder) {
										mBinder.wait(25);
									}
								}

								currentSensor.parsePacket(data, count);

								lastSeqNum = ((WocketSensor) currentSensor).getLastSeqNum();

								Editor edit = getSharedPreferences("WocketsACPref", MODE_PRIVATE).edit();
								edit.putInt("lastSeqNum", lastSeqNum);
								edit.commit();

								// /////////////////////////////////////////////
								// Debug code that prints out all of the raw
								// data that was read in.
								//
								/*
								 * short uData[] = new short[count]; for( int
								 * x=0;x<count;x++) { int firstByte =
								 * (0x000000FF & ((int)data[x])); uData[x] =
								 * (short)firstByte; }
								 * 
								 * 
								 * Log.i("ActivityMonitor",
								 * ByteArrayToString(uData));
								 */
								//
								// End debug code
								// //////////////////////////////////////////////

								// TODO Fix
								// currentSensor.parsePacket(data, count,
								// mWocketDataSaver);

							}
							// if we made it here, the connection was good, so
							// reset
							// the number of connection errors, and clean up the
							// connection handles
							currentSensor.mConnectionErrors = 0;

							sock.close();
							in.close();
							out.flush();
							out.close();
						} catch (Exception e) {
							e.printStackTrace();
							// If there was some problem, increment the number
							// of connection errors
							currentSensor.mConnectionErrors++;
						}
					}
				}
			}

			// Check if this is a new day and we need to reset the score
			DataStore.checkForScoreReset();

			// Broadcast an Intent to the the UI Activity know that new data is
			// available
			publishDataUpdated();

			// Only alarm if its been more than 30 minutes after last alert
			Time now = new Time();
			now.setToNow();

			Time nextAllowedAlert = new Time();
			if (mLastStillAlertTime != null) {
				nextAllowedAlert.set(mLastStillAlertTime.second, mLastStillAlertTime.minute + Defines.MINUTES_BETWEEN_STILLNESS_ALERT,
						mLastStillAlertTime.hour, mLastStillAlertTime.monthDay, mLastStillAlertTime.month, mLastStillAlertTime.year);

				nextAllowedAlert.normalize(false);
			}

			if (DataStore.mStillnessDuration != 0 && (mLastStillAlertTime == null || now.after(nextAllowedAlert))
					&& checkForStillness(DataStore.mStillnessDuration)) {
				// Show notification
				showStillNotification();

				// Record the current time so that we won't alarm again if the
				// user remains
				// still until a minimum time has passed
				if (mLastStillAlertTime == null) {
					mLastStillAlertTime = new Time();
				}
				mLastStillAlertTime.setToNow();

			}

			// Check if the user has been still, but their heart rate has
			// spiked,
			// show a notification if so
			if (DataStore.mEmotionalEventThreshold != 0 && checkForEmotionalEvent()) {
				showEmotionNotification();
			}

			// Done with our work...stop the service!
			BluetoothSensorService.this.stopSelf();
		}
	};

	/**
	 * convert short array into a string for debugging purposes
	 * 
	 * @param data
	 * @return data in the form of a string
	 */
	String ByteArrayToString(short[] data) {
		String retVal = "";
		for (int x = 0; x < data.length; x++) {
			retVal += data[x] + " ";
		}
		return retVal;
	}

	/**
	 * Sends a broadcast for other activities to respond to once all data has
	 * been read and processed
	 */
	void publishDataUpdated() {
		Intent i = new Intent(Defines.NEW_DATA_READY_BROADCAST_STRING);
		sendBroadcast(i);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	/**
	 * Show a notification while this service is running. Enables a blue
	 * notification LED
	 */
	private void showReadingNotification() {
		CharSequence text = getString(R.string.readingNotification);

		// Set the icon, scrolling text and timestamp
		Notification notification = new Notification(android.R.drawable.stat_sys_warning, text, System.currentTimeMillis());
		notification.ledARGB = 0x0000ff;
		notification.ledOnMS = 1000;
		notification.ledOffMS = 0;
		notification.flags |= Notification.FLAG_SHOW_LIGHTS | Notification.FLAG_ONGOING_EVENT;

		Intent mainIntent = new Intent(this, Main.class);
		mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

		// The PendingIntent to launch our activity if the user selects this
		// notification
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, mainIntent, 0);

		// Set the info for the views that show in the notification panel.
		notification.setLatestEventInfo(getApplicationContext(), getString(R.string.app_name), text, contentIntent);

		// Send the notification.
		mNM.notify(READING_NOTIFICATION_ID, notification);
	}

	/**
	 * Show a notification for the emotional event. No LED is shown, but the
	 * phone will vibrate and play the default notification sound
	 */
	private void showEmotionNotification() {
		CharSequence text = getString(R.string.emotionNotification);

		// Set the icon, scrolling text and timestamp
		Notification notification = new Notification(R.drawable.emotion, text, System.currentTimeMillis());

		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		// Three short vibrations with a longer gap between them
		long[] vibrate = { 0, 100, 200, 100, 200, 100 };
		notification.vibrate = vibrate;

		Intent mainIntent = new Intent(this, Main.class);
		mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

		// The PendingIntent to launch our activity if the user selects this
		// notification
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, mainIntent, 0);

		// Set the info for the views that show in the notification panel.
		notification.setLatestEventInfo(getApplicationContext(), getString(R.string.emotionNotificationDetail), text, contentIntent);

		// Send the notification.
		mNM.notify(EMOTION_NOTIFICATION_ID, notification);
	}

	/**
	 * Show a notification for stillness. No LED is shown, but the phone will
	 * vibrate and play the default notification sound
	 */
	private void showStillNotification() {
		CharSequence text = getString(R.string.inactivityNotification);

		// Set the icon, scrolling text and timestamp
		Notification notification = new Notification(R.drawable.sitting, text, System.currentTimeMillis());

		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		// Three short vibrations with a small gap between them
		long[] vibrate = { 0, 100, 100, 100, 100, 100 };
		notification.vibrate = vibrate;

		Intent mainIntent = new Intent(this, Main.class);
		mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

		// The PendingIntent to launch our activity if the user selects this
		// notification
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, mainIntent, 0);

		// Set the info for the views that show in the notification panel.
		notification.setLatestEventInfo(getApplicationContext(), getString(R.string.inactivityNotificationDetail), text, contentIntent);

		// Send the notification.
		mNM.notify(STILL_NOTIFICATION_ID, notification);
	}

	/**
	 * Checks if there have not been any movements during the specified time
	 * period
	 * 
	 * @param duration
	 *            - the time period to check for movements, given in minutes
	 * @return - true if there hasn't been any movement, false if there has
	 */
	private boolean checkForStillness(int duration) {

		boolean retVal = false;
		int sensorsChecked = 0;
		Time startTime = new Time();
		startTime.setToNow();
		// get the start time at which we want to start looking for movement
		startTime.set(startTime.second, startTime.minute - duration, startTime.hour, startTime.monthDay, startTime.month, startTime.year);
		// the call to normalize will deal with negative minutes and reset the
		// hour and days accordingly
		startTime.normalize(false);

		// make sure we have enough data to measure.
		if (DataStore.getStartRecordingTime() == null || DataStore.getStartRecordingTime().after(startTime)) {
			return false;
		}

		// Look through all of the enabled Wocket sensors that do not have any
		// connection errors.
		// If any single one of them does not have recorded movement above the
		// threshold, return true
		for (int x = 0; x < DataStore.mSensors.size(); x++) {
			if (DataStore.mSensors.get(x).mType == TYPE.WOCKET && DataStore.mSensors.get(x).mEnabled
					&& DataStore.mSensors.get(x).mConnectionErrors == 0) {
				sensorsChecked++;
				boolean idleWocket = true;
				WocketSensor wocket = (WocketSensor) (DataStore.mSensors.get(x));
				int summarySize = wocket.mSummaryPoints.size();
				for (int y = summarySize - 1; y >= 0; y--) {
					if (wocket.mSummaryPoints.get(y).mWocketRecordedTime.after(startTime)
							&& wocket.mSummaryPoints.get(y).mValue > Defines.WOCKET_STILLNESS_MIN) {
						idleWocket = false;
						break;
					}
				}
				if (idleWocket) {
					retVal = true;
					break;
				}
			}
		}

		// If we couldn't find any sensors that were enabled without connection
		// errors,
		// we couldn't have possible detected stillness
		if (sensorsChecked == 0) {
			retVal = false;
		}

		return retVal;

	}

	/**
	 * Check if the user has not recorded any movement during the defined period
	 * for no movement, and the heart rate has increased above the set threshold
	 * value.
	 * 
	 * @return - true if no movement and increase HR, false otherwise
	 */
	private boolean checkForEmotionalEvent() {
		// Find the first enabled heart rate sensor without connection errors.
		// If multiple
		// sensors are enabled, only the first in the list will be used
		HeartRateSensor hrSensor = null;
		for (int x = 0; x < DataStore.mSensors.size(); x++) {
			if ((DataStore.mSensors.get(x).mType == TYPE.POLAR || DataStore.mSensors.get(x).mType == TYPE.ZEPHYR)
					&& DataStore.mSensors.get(x).mEnabled && DataStore.mSensors.get(x).mConnectionErrors == 0) {
				hrSensor = (HeartRateSensor) (DataStore.mSensors.get(x));
				break;
			}
		}

		// If there is no working HR sensor, just return
		if (hrSensor == null) {
			return false;
		}

		// Calculate the threshold for triggering an event based on the current
		// set
		// threshold
		int currentHR = hrSensor.mCurrentRate;
		int trailingAverageHR = hrSensor.mTrailingAvg;
		int thresholdHR = (trailingAverageHR + (int) ((trailingAverageHR * (DataStore.mEmotionalEventThreshold / 100.0))));

		// If the heart rate is above threshold, check if the user has been
		// without movement
		if (currentHR > thresholdHR && checkForStillness(DataStore.mEmotionalEventStillnessDuration)) {
			return true;
		}

		return false;
	}

	/**
	 * This is the object that receives interactions from clients.
	 */
	private final IBinder mBinder = new Binder() {
		@Override
		protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
			return super.onTransact(code, data, reply, flags);
		}
	};
}
