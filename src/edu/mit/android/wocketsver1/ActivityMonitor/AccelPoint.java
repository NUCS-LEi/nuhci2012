/******************************************************************************
 * 
 * @brief Class used to store a single decoded point of data from an accelerometer 
 * sensor.
 * @author Kyle Bechtel
 * @date  6/1/11
 * 
 * 
 *****************************************************************************/
package edu.mit.android.wocketsver1.ActivityMonitor;

import android.text.format.Time;

public class AccelPoint {

	public int mX;
	public int mY;
	public int mZ;
	public byte[] mRawData;
	
	//true indicated the raw data is in compressed format, false uncompressed
	public boolean mCompressed;
	
	//The time the phone read the data from the sensor
	public Time mPhoneReadTime;
	//The approximate time that the sensor recorded the data
	public Time mWocketRecordedTime;
	
	/*
	 * Constructor
	 * 
	 *  @param x - the decoded x value read from the sensor
	 *  @param y - the decoded y value read from the sensor
	 *  @param z - the decoded z value read from the sensor
	 *  @param compressed - flag indicating if the raw bytes for this point were
	 *  		compressed when read from the sensor
	 *  @param raw - an array of the raw bytes read from the sensor, un-decoded
	 * 
	 */
	public AccelPoint( int x,int y,int z, boolean compressed, byte[] raw)
	{
		mPhoneReadTime = new Time();
		mPhoneReadTime.setToNow();
		mX = x;
		mY = y;
		mZ = z;
		mCompressed = compressed;
		
		mRawData = raw;
	}
}
