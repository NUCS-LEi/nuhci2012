/******************************************************************************
 * 
 * @author Kyle Bechtel
 * @date  6/1/11
 * @brief Class used to store a single decoded activity summary point of data 
 * from an accelerometer sensor.  The accelerometer sensor creates these summaries
 * approximately once a minute that summarize the previous minutes activity levels
 * 
 * 
 *****************************************************************************/


package edu.mit.android.wocketsver1.ActivityMonitor;

import android.text.format.Time;

public class SummaryPoint {

	//The sequence number of this summary
	public int mSeqNum;
	//The activity level value for this summary
	public int mValue;
	
	public Time mPhoneReadTime;
	public Time mWocketRecordedTime;
	//Flag indicating if this point has already been stored to NV storage.
	// True indicates it has and does not need to be written again, false means it hasn't
	public boolean mWritten;
	
	/**
	 * 
	 * @param seqNum - the value indicating the order of this activity summary
	 * @param value - the actual value of the activity summary
	 */
	public SummaryPoint(int seqNum, int value)
	{
		mSeqNum = seqNum;
		mValue = value;
		mPhoneReadTime = new Time();
		mPhoneReadTime.setToNow();
		
		///TODO need to subtract some time here
		mWocketRecordedTime = new Time();
		mWocketRecordedTime.setToNow();
		
		
		mWritten = false;
	}
}
