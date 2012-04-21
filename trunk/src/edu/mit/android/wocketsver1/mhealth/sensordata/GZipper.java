package edu.mit.android.wocketsver1.mhealth.sensordata;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import android.util.Log;

public class GZipper {
	private static final String TAG = "GZipper";
	
	private static final int BUFFER = 2048;
	
	public static void zip(String aFileName) {
		
		String zipFile = aFileName + ".gzip";
		
		long startTime = System.currentTimeMillis();
		try  {
			BufferedInputStream origin = null;
			FileOutputStream dest = new FileOutputStream(zipFile);
			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
			byte data[] = new byte[BUFFER];
			Log.v(TAG, "Adding: " + zipFile); 
			FileInputStream fi = new FileInputStream(aFileName);
			origin = new BufferedInputStream(fi, BUFFER);
			ZipEntry entry = new ZipEntry(aFileName.substring(aFileName.lastIndexOf("/") + 1));
			out.putNextEntry(entry);
			int count;
			while ((count = origin.read(data, 0, BUFFER)) != -1) {
					out.write(data, 0, count);
			}
			origin.close();
			out.close();
			Log.v(TAG, "Compress time: " + (System.currentTimeMillis()-startTime)); 
		} catch(Exception e) {
			e.printStackTrace();     
		}
	}
}
