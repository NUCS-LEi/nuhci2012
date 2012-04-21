/******************************************************************************
 * 
 * @author Kyle Bechtel
 * @date  6/1/11
 * @brief Abstract base class for a Bluetooth sensor.  Maintains variables and functions
 * 			that are common across all types of sensors
 * 
 * 
 *****************************************************************************/

package edu.mit.android.wocketsver1.ActivityMonitor;

import android.content.Context;

public abstract class Sensor {

	enum TYPE
	{
		ZEPHYR,
		POLAR,
		WOCKET
	}
	//The current battery percentage remaining in the sensor (0-100)
	public int mBattery = 0;

	//The sensor's bluetooth device name
	public String mName = "";

	//Flag indicating if the user has chosen to enable and use this sensor.
	// If true, the sensor will be polled each time the Bluetooth service runs.
	public boolean mEnabled = false;

	//The type of this sensor
	public TYPE mType;

	//The bluetooth device address of this sensor
	public String mAddress;

	//The number of attempts to read data from this sensor that have failed since
	//the last successful read
	public int mConnectionErrors;
	
	/**
	 * Reset
	 * 
	 * Resets all necessary fields in this sensor to their default values
	 */
	public abstract void reset();
	
	
	/**
	 * Parse a raw packet read from a sensor and decode its data
	 * 
	 * @param data - the raw bytes read from the Bluetooth device
	 * @param size - the number of raw bytes read
	 */
	public abstract void parsePacket(byte[] data, int size);
	
	/**
	 * Constructor
	 * 
	 * @param type - The type of this sensor
	 * @param name - The BT name of this device
	 * @param address - The BT MAC address of this device
	 */
	public Sensor(TYPE type, String name, String address)
	{
		mType = type;
    	mName = name;
    	mEnabled = false;
    	mAddress = address;
    	mConnectionErrors = Defines.NO_CONNECTION_LIMIT;

	}


	public void parsePacket(Context c, byte[] data, int size) {
		// TODO Auto-generated method stub
		
	}
}
