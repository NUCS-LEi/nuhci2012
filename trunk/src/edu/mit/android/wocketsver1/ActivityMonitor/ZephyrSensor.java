/******************************************************************************
 * 
 * @author Kyle Bechtel
 * @date  6/1/11
 * @brief Class that represents a Zephyr HXM Heart Rate sensor, extends HeartRateSensor
 * and Sensor
 * 
 * 
 *****************************************************************************/

package edu.mit.android.wocketsver1.ActivityMonitor;

import android.util.Log;

public class ZephyrSensor extends HeartRateSensor {

	/**
	 * Constructor
	 * 
	 * @param name - The Bluetooth device name
	 * @param address - The Bluetooth device MAC address
	 */
	public ZephyrSensor( String name, String address) {
		super(TYPE.ZEPHYR, name, address);
	}

	/**
	 * Decodes the raw bytes from the Zephyr sensor and pulls out the
	 * heart rate and battery values, then saves them.
	 * 
	 * @param data - the raw bytes read from the Zepyhr sensor
	 * @param size - the number of raw bytes read from the sensor
	 */
	public void parsePacket(byte[] data, int size)
	{		
		int rate = (int)data[12];
		int battery = (int)data[11];

		if( battery > 0 && battery <= 100)
		{
			mBattery = (int)data[11];	
		}
		
		Log.i("ActivityMonitor", "HR: " + rate + " battery: " + battery);
		addPoint( new HRPoint(rate));

		///TODO figure out if we can use more data from the Zepyhr
		/*int v = merge(data[24], data[25]);
			out+= " battery: " + String.valueOf(((double) v / (double) 1000));

			int p = merge(data[18], data[19]);
			out+= " posture: " + String.valueOf(((double) p / (double) 10));

			int r = merge(data[14], data[15]);
			out+= " respiration: " + String.valueOf(Math.abs(((double) r / (double) 10)));

			int t = merge(data[16], data[17]);
			out+= "temp: " + String.valueOf(((double) t / (double) 10));
		 */

	}
}
