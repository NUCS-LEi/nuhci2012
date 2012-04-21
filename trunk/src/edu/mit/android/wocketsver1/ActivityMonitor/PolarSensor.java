/******************************************************************************
 * 
 * @author Kyle Bechtel
 * @date  6/1/11
 * @brief Class that represents a Polar Heart Rate sensor, extends HeartRateSensor
 * and Sensor
 * 
 * 
 *****************************************************************************/

package edu.mit.android.wocketsver1.ActivityMonitor;

import android.util.Log;

public class PolarSensor extends HeartRateSensor {

	/**
	 * Constructor 
	 * @param name - Bluetooth device name
	 * @param address - Bluetooth device MAC address
	 */
	public PolarSensor(String name, String address) {
		super(TYPE.POLAR, name, address);
	}
	
	

	/**
	 * Decodes the raw bytes to find the heart rate
	 * 
	 * @param data - the raw bytes read from the sensor
	 * @param size - the number of bytes read
	 */
	public void parsePacket(byte[] data, int size)
	{
		///TODO this function does not work properly!  Polar decoding is NOT fully implemented

		short uData[] = new short[16];
		for( int x=0;x<data.length;x++)
		{
			int firstByte = (0x000000FF & ((int)data[x]));			
			uData[x] = (short)firstByte;
		}
		
		
		if( uData[0] != 0xFE)
		{
			Log.i("ActivityMonitor", "Polar - HEADER ERROR");
		}
		
		///TODO this function does not work properly!  Polar decoding is NOT fully implemented
		
		//iSize = data[1]; //size of block including bHeader, always even (8, 10 , 12), different number of RRI
		//iCheck = data[2]; //255-bSize
		//iIndex = data[3]; //index: 0-15 (seconds?), first is 1 //2010-08-05 iBattery changed to iSttus
		//iStatus = data[4]; //status bit 1BBP0001, thus 128+64+16+1=209 beats (P=16) detected (BA=2) , 193 no beats
		int iBeat= (uData[4] >> 4) & 1; 
		int iBattery = (uData[4] >> 5) & 3;
		//iBPM= data[5]; //beats 
		
		Log.i( "ActivityMonitor", "Polar HR: " + uData[5] + "beats " + iBeat + " Battery: " + iBattery);
		
		addPoint( new HRPoint(data[5]));

	}
	
}
