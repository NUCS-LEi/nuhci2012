/******************************************************************************
 * 
 * @author Kyle Bechtel
 * @date  6/1/11
 * @brief Class that models the data and behaviors of a Wocket sensor.
 * 
 * 
 *****************************************************************************/

package edu.mit.android.wocketsver1.ActivityMonitor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.mit.android.wocketsver1.mhealth.sensordata.SensorData;
import edu.mit.android.wocketsver1.mhealth.sensordata.WocketSensorData;
import edu.neu.hci.db.DBAccessHelper;

import android.content.Context;
import android.text.format.Time;
import android.util.Log;

public class WocketSensor extends Sensor {

	// Wocket packet types
	private final static int WOCKET_UNCOMPRESSED = 0;
	// private final static int WOCKET_COMMAND = 1;
	private final static int WOCKET_RESPONSE = 2;
	private final static int WOCKET_COMPRESSED = 3;

	// Wocket response types
	private final static int WOCKET_RESPONSE_BATTERY_LEVEL = 0;
	private final static int WOCKET_RESPONSE_BATTERY_PERCENT = 1;
	private final static int WOCKET_RESPONSE_SLEEP_MODE = 2;
	private final static int WOCKET_RESPONSE_SUMMARY_COUNT = 13;
	private final static int WOCKET_RESPONSE_ACC_RSP = 15;

	// Wocket command values
	public final static int WOCKET_ACK_PACKET = 0xa0 | 27;
	public final static int WOCKET_BATTERY_PERCENT_PACKET = 0xA1;
	public final static byte[] WOCKET_60_SEC_BURST_PACKET = { (byte) 0xBA, (byte) 0x20 };
	public final static byte WOCKET_HEADER_DATA = (byte) -1;

	// Header file used in the Activity Summary CSV file
	private final static String HEADER_STRING = "Phone_Write_Time,AC_DecoderIndex,AC_SeqNum,AC_TimeStamp,AC_Unix_TimeStamp,AC_Value";

	public ArrayList<AccelPoint> mAccelPoints = new ArrayList<AccelPoint>();
	public ArrayList<SummaryPoint> mSummaryPoints = new ArrayList<SummaryPoint>();
	long mBytesReceived;
	// flag to indicate if this Wocket device has been initialized and had its
	// mode bytes
	// written to it
	public boolean mInit;

	// previous x,y,x values that were read from the device
	private int prevX = 0;
	private int prevY = 0;
	private int prevZ = 0;
	private int lastRecivedSN = 0;

	private long lastWrittenTime = 0;

	WocketSensorData[] someWocketSensorData;
	int someWocketSensorDataIndex = 0;

	/**
	 * Constructor
	 * 
	 * @param name
	 *            - the Bluetooth name of the Wocket
	 * @param address
	 *            - the Bluetooth MAC address of the Wocket
	 */
	public WocketSensor(String name, String address) {
		super(TYPE.WOCKET, name, address);
		mBytesReceived = 0;
		mInit = false;
		prevX = 0;
		prevY = 0;
		prevZ = 0;

		// Create storage needed to store raw data before saving
		someWocketSensorData = new WocketSensorData[100];
		Date aDate = new Date();
		for (int i = 0; i < 100; i++)
			someWocketSensorData[i] = new WocketSensorData(SensorData.TYPE.WOCKET12BITRAW, aDate, 0, 0, 0);
		someWocketSensorDataIndex = 0;

		loadNVData();
	}

	private void addWocketSensorDataPoint(Date aTime, int x, int y, int z) {
		someWocketSensorData[someWocketSensorDataIndex].mDateTime = aTime;
		someWocketSensorData[someWocketSensorDataIndex].mX = x;
		someWocketSensorData[someWocketSensorDataIndex].mY = y;
		someWocketSensorData[someWocketSensorDataIndex].mZ = z;
		someWocketSensorDataIndex++;
	}

	/**
	 * Read in the current day's activity summary log file. Add the summary
	 * points that were already written out to the array in memory so that if
	 * the points are read from the Wocket device again, we can safely throw
	 * them out knowing they were already read in.
	 */
	private void loadNVData() {

		Time now = new Time();
		now.setToNow();
		String dirName = "data/summary/" + now.format("%Y-%m-%d") + "/" + now.format("%H") + "/";
		String fileName = dirName + "SummaryAC_" + mName + ".csv";

		if (DataStore.getContext() != null) {
			File file = new File(DataStore.getContext().getExternalFilesDir(null), fileName);

			if (file != null && file.exists()) {
				try {
					BufferedReader in = new BufferedReader(new FileReader(file));

					String nextLine = in.readLine();
					while (nextLine != null) {
						if (!nextLine.contains(HEADER_STRING)) {
							try {
								// Get everything after the first comma
								String firstComma = nextLine.substring(nextLine.indexOf(',') + 1);
								// Get the substring from the first comma, to
								// the next comma
								int seqNum = Integer.parseInt(firstComma.substring(0, firstComma.indexOf(',')));
								int value = Integer.parseInt(nextLine.substring(nextLine.lastIndexOf(',') + 1));

								SummaryPoint point = new SummaryPoint(seqNum, value);
								point.mWritten = true;
								addSummaryPoint(point);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						nextLine = in.readLine();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Checks if the given SummaryPoint matches the sequence number of an
	 * already read Summary point
	 * 
	 * @param point
	 *            - the point to check
	 * @return - True if a match is found and the point is a duplicate, false
	 *         otherwise
	 */
	private boolean containsSummaryPoint(SummaryPoint point) {
		boolean retVal = false;
		for (int x = 0; x < mSummaryPoints.size(); x++) {
			if (mSummaryPoints.get(x).mSeqNum == point.mSeqNum) {
				retVal = true;
				break;
			}
		}
		return retVal;
	}

	/**
	 * Adds the given summary point to the storage array if it is not already
	 * known. Increments the day's activity score if the value of the summary
	 * point is above the defined threshold. Once the size of the storage array
	 * reaches a defined limit, excess points will be removed from the array and
	 * written to a file.
	 * 
	 * @param point
	 *            - the SummaryPoint to add
	 */
	public void addSummaryPoint(SummaryPoint point) {
		// If we already have this point, just dump it
		if (containsSummaryPoint(point)) {
			return;
		}

		mSummaryPoints.add(point);
//		DBAccessHelper.insertSummaryPoint(c, point);

		if (point.mValue > Defines.WOCKET_STILLNESS_MIN && !point.mWritten) {
			DataStore.incrementActivityScore();
		}

		// /TODO - to save battery life (and SD card life), we should probably
		// only write new data
		// to storage periodically instead of on every new point. This is a
		// future enhancement
		if (DataStore.getContext() != null) {
			Time now = new Time();
			now.setToNow();
			// /TODO instead of BT name, use name from sensor.xml
			String dirName = "data/summary/" + now.format("%Y-%m-%d") + "/" + now.format("%H") + "/";
			String fileName = dirName + "SummaryAC_" + mName + ".csv";
			File file = new File(DataStore.getContext().getExternalFilesDir(null), fileName);

			try {
				if (file != null) {
					boolean writeHeader = false;
					if (!file.exists()) {
						writeHeader = true;
						File directory = new File(DataStore.getContext().getExternalFilesDir(null), dirName);
						directory.mkdirs();
						file.createNewFile();
					}

					FileWriter out = new FileWriter(file, true);

					if (writeHeader) {
						out.write(HEADER_STRING + "\n");
					}

					if (!point.mWritten) {
						out.write(point.mPhoneReadTime.format("%Y-%m-%d %H:%M:%S") + "," + point.mSeqNum + "," + point.mSeqNum + "," +
						// create time
								point.mWocketRecordedTime.format("%Y-%m-%d %H:%M:%S") + "," +

								// create MS time
								point.mWocketRecordedTime.toMillis(false) + "," + point.mValue + "\n");
					}

					out.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			// remove excess points from the array
			while (mSummaryPoints.size() > Defines.MAX_WOCKET_SUMMARIES) {
				mSummaryPoints.remove(0);
			}

		}
	}

	/**
	 * Returns the sequence number from the last summary point received by the
	 * phone This is used to send the last sequence number received with an ACK
	 * to the wocket, in order to increment the sequence number on the wocket
	 */
	public int getLastSeqNum() {
		return lastRecivedSN;
	}

	/**
	 * Add a raw acceleration point to the array. Once a limit of points in the
	 * array is reached, older points will be written out to the log file and
	 * removed from the array
	 * 
	 * @param point
	 *            - the new raw point to add.
	 */
	public void addAccelPoint(AccelPoint point) {
		mAccelPoints.add(point);
//		DBAccessHelper.insertAccelPoint(c, point);
		if (mAccelPoints.size() > Defines.MAX_WOCKET_POINTS) {
			if (DataStore.getContext() != null) {
				Time now = new Time();
				now.setToNow();
				// /TODO instead of BT name, use some Wocket format
				// "WocketAccelBytes..."
				String dirName = "data/raw/PLFormat/" + now.format("%Y-%m-%d") + "/" + now.format("%H") + "/";
				String fileName = dirName + mName + ".PLFormat";
				String textFileName = dirName + mName + ".txt";
				File file = new File(DataStore.getContext().getExternalFilesDir(null), fileName);
				File textFile = new File(DataStore.getContext().getExternalFilesDir(null), textFileName);
				try {
					if (file != null) {
						if (!file.exists()) {
							File directory = new File(DataStore.getContext().getExternalFilesDir(null), dirName);
							directory.mkdirs();
							file.createNewFile();
							textFile.createNewFile();
						}

						FileWriter out = new FileWriter(file, true);
						FileOutputStream binaryOut = new FileOutputStream(file, true);

						while (mAccelPoints.size() > 0) {
							// Write the x,y,z values as text to a text file to
							// be human readable
							String output = mAccelPoints.get(0).mPhoneReadTime.format("%H:%M:%S ") + mAccelPoints.get(0).mX + " "
									+ mAccelPoints.get(0).mY + " " + mAccelPoints.get(0).mZ + "\n";
							out.write(output);

							// Write the raw bytes out to be readable by the
							// Merger/Viewer graphing tool
							if (mAccelPoints.get(0).mPhoneReadTime.toMillis(false) < lastWrittenTime) {
								lastWrittenTime = mAccelPoints.get(0).mPhoneReadTime.toMillis(false);
								// Log.("Accelerometer: Save: Data overwritten without saving Accelerometer.cs Save "
								// + this._ID + " " + aUnixTime + " " +
								// lastUnixTime);
							}

							// If the time difference between the current point
							// and the last one
							// written to file is close, just write the
							// difference. Otherwise
							// write out the full 7 byte time stamp (6 bytes
							// data + 1 header)
							if (mAccelPoints.get(0).mPhoneReadTime.toMillis(false) - lastWrittenTime > 254) {
								int sec = (int) (mAccelPoints.get(0).mPhoneReadTime.toMillis(false) / 1000);
								short ms = (short) (mAccelPoints.get(0).mPhoneReadTime.toMillis(false) % 1000);

								// Write 0xFF for a full time stamp
								binaryOut.write(0xFF);
								// write out the time stamp (6 bytes of data)

								// Write 4 bytes for the second value
								binaryOut.write(new byte[] { (byte) (sec & 0xFF), (byte) ((sec >>> 8) & 0xFF), (byte) ((sec >>> 16) & 0xFF),
										(byte) ((sec >>> 24) & 0xFF) }, 0, 4);

								// write 2 bytes for ms value
								binaryOut.write(new byte[] { (byte) (ms & 0xFF), (byte) ((ms >>> 8) & 0xFF) }, 0, 2);
							} else {
								// Just write the difference in time stamps
								binaryOut.write((int) (0xFF & (mAccelPoints.get(0).mPhoneReadTime.toMillis(false) - lastWrittenTime)));
							}

							// write the raw data
							binaryOut.write(mAccelPoints.get(0).mRawData);

							mAccelPoints.remove(0);
						}

						binaryOut.close();
						out.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Reset the default values of this sensor
	 */
	public void reset() {
		mBytesReceived = 0;
		mAccelPoints.clear();
		mInit = false;
		prevX = 0;
		prevY = 0;
		prevZ = 0;
	}

	@Override
	/**
	 * Parse the given data according to the Wocket packet definitions
	 * @param data - the raw data to parse
	 * @param size - the size of the raw data array
	 */
	public void parsePacket(Context c, byte[] data, int size) {
		mBytesReceived += size;

		for (int x = 0; x < size; x++) {
			int Hbit = ((data[x] & 0x80) >> 7);
			if (Hbit == 1) {
				int Tbit = ((data[x] & 0x60) >> 5);
				// Log.e("ActivityMonitor", "T: " + Tbit);

				switch (Tbit) {
				case WOCKET_COMPRESSED: {
					if (x < Defines.MAX_WOCKET_PACKET_SIZE - 2) {
						int xVal = (short) (((data[x] & 0x0f) << 1) | ((data[x + 1] & 0x40) >> 6));
						xVal = ((((short) ((data[x] >> 4) & 0x01)) == 1) ? ((short) (prevX + xVal)) : ((short) (prevX - xVal)));
						int yVal = (short) (data[x + 1] & 0x1f);
						yVal = ((((short) ((data[x + 1] >> 5) & 0x01)) == 1) ? ((short) (prevY + yVal)) : ((short) (prevY - yVal)));
						int zVal = (short) ((data[x + 2] >> 1) & 0x1f);
						zVal = ((((short) ((data[x + 2] >> 6) & 0x01)) == 1) ? ((short) (prevZ + zVal)) : ((short) (prevZ - zVal)));

						// Log.i( "ActivityMonitor", "Compressed " + xVal + " "
						// + yVal + " " + zVal);

						byte[] raw = { data[x], data[x + 1], data[x + 2] };
						addAccelPoint(new AccelPoint(xVal, yVal, zVal, true, raw));

						prevX = xVal;
						prevY = yVal;
						prevZ = zVal;
					}
				}
					break;
				case WOCKET_RESPONSE: {
					// Got a response
					int opCode = (data[x] & 0x1f);
					Log.i("ActivityMonitor", "opcode: " + opCode);
					switch (opCode) {
					case WOCKET_RESPONSE_BATTERY_LEVEL:
						int batteryLevel = 0;
						batteryLevel = ((data[x + 1] & 0x7f) << 3) | ((data[x + 2] & 0x70) >> 4);
						Log.i("ActivityMonitor", "battery level: " + batteryLevel);
						int batteryCalc = (batteryLevel - Defines.WOCKET_LOW_BATTERY_LEVEL);
						if (batteryCalc > 0 && batteryCalc <= 100) {
							mBattery = batteryCalc;
						} else if (batteryCalc > 100) {
							mBattery = 100;
						}

						break;
					case WOCKET_RESPONSE_BATTERY_PERCENT:
						int battery = 0;
						battery = (data[x + 1] & 0x7f);
						Log.i("ActivityMonitor", "battery: " + battery);
						if (battery > 0 && battery <= 100) {
							mBattery = battery;
						}
						break;

					case WOCKET_RESPONSE_SLEEP_MODE:
						int sleep = ((data[x + 1] & 0x7f) << 9) | ((data[x + 2] & 0x7f) << 2) | ((data[x + 3] & 0x60) >> 5);
						Log.i("ActivityMonitor", "sleep : " + sleep);
						break;
					case WOCKET_RESPONSE_SUMMARY_COUNT:
						// read 6 bytes
						int seqNum = ((data[x + 1] & 0x7f) << 9) | ((data[x + 2] & 0x7f) << 2) | ((data[x + 3] >> 5) & 0x03);
						int count = ((data[x + 3] & 0x1f) << 11) | ((data[x + 4] & 0x7f) << 4) | ((data[x + 5] >> 2) & 0x0f);

						Log.i("ActivityMonitor", "Got activity count: " + seqNum + " " + count);

						addSummaryPoint(new SummaryPoint(seqNum, count));
						// if (seqNum > lastRecivedSN){
						lastRecivedSN = seqNum;
						// }

						break;
					case WOCKET_RESPONSE_ACC_RSP:
						int cseqNum = (data[x + 1] & 0xff | data[x + 2] & 0xff);
						Log.i("ActivityMonitor", "Got sseq:" + cseqNum);
					default:
						break;
					}
				}
					break;
				case WOCKET_UNCOMPRESSED: {
					if (x < Defines.MAX_WOCKET_PACKET_SIZE - 4) {
						int xVal = 0, yVal = 0, zVal = 0;
						xVal = ((data[x] & 0x3) << 8) | ((data[x + 1] & 0x7F) << 1) | ((data[x + 2] & 0x40) >> 6);

						yVal = ((data[x + 2] & 0x3f) << 4) | ((data[x + 3] & 0x78) >> 3);

						zVal = ((data[x + 3] & 0x7) << 7) | (data[x + 4] & 0x7F);
						// Log.i("ActivityMonitor",xVal + " " + yVal + " " +
						// zVal);

						byte[] raw = { data[x], data[x + 1], data[x + 2], data[x + 3], data[x + 4] };

						addAccelPoint(new AccelPoint(xVal, yVal, zVal, false, raw));

						prevX = xVal;
						prevY = yVal;
						prevZ = zVal;
					}
				}
					break;
				}
			}
		}
	}

	@Override
	public void parsePacket(byte[] data, int size) {
		// TODO Auto-generated method stub
		
	}
}
