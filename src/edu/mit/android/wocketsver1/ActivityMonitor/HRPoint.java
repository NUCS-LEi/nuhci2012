/******************************************************************************
 * 
 * @author Kyle Bechtel
 * @date  6/1/11
 * @brief Class used to store a single decoded heart rate measurement
 * 
 * 
 *****************************************************************************/

package edu.mit.android.wocketsver1.ActivityMonitor;

import android.text.format.Time;

public class HRPoint {

	//The time the phone read this heart rate value from the sensor
	// Currently matches the same time the sensor recorded the data
	public Time mTime;
	//The actual heart rate
	public int mRate;
	
	/**
	 * Constructor
	 * 
	 * @param rate - the decoded heart rate value
	 */
	public HRPoint( int rate)
	{
		mTime = new Time();
		mTime.setToNow();
		mRate = rate;
	}
}
