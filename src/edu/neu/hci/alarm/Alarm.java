/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.neu.hci.alarm;

import java.util.Calendar;

import android.content.Context;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public final class Alarm implements Parcelable {

	public static final Parcelable.Creator<Alarm> CREATOR = new Parcelable.Creator<Alarm>() {
		public Alarm createFromParcel(Parcel p) {
			return new Alarm(p);
		}

		public Alarm[] newArray(int size) {
			return new Alarm[size];
		}
	};

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel p, int flags) {
		p.writeInt(id);
		p.writeInt(enabled ? 1 : 0);
		p.writeInt(hour);
		p.writeInt(minutes);
		p.writeLong(time);
		p.writeInt(vibrate ? 1 : 0);
		p.writeParcelable(alert, flags);
		p.writeInt(silent ? 1 : 0);
	}

	// ////////////////////////////
	// by end
	// ////////////////////////////

	public int id;
	public boolean enabled;
	public int hour;
	public int minutes;
	public long time;
	public boolean vibrate;
	public Uri alert;
	public boolean silent;

	public Alarm(Cursor c) {
		id = c.getInt(0);
		enabled = c.getInt(4) == 1;
		hour = c.getInt(1);
		minutes = c.getInt(2);
		time = c.getLong(3);
		vibrate = c.getInt(5) == 1;
		String alertString = c.getString(6);
		if (Alarms.ALARM_ALERT_SILENT.equals(alertString)) {
			silent = true;
		} else {
			if (alertString != null && alertString.length() != 0) {
				alert = Uri.parse(alertString);
			}

			// If the database alert is null or it failed to parse, use the
			// default alert.
			if (alert == null) {
				alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
			}
		}
	}

	public Alarm(Parcel p) {
		id = p.readInt();
		enabled = p.readInt() == 1;
		hour = p.readInt();
		minutes = p.readInt();
		time = p.readLong();
		vibrate = p.readInt() == 1;
		alert = (Uri) p.readParcelable(null);
		silent = p.readInt() == 1;
	}

	// Creates a default alarm at the current time.
	public Alarm() {
		id = -1;
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		hour = c.get(Calendar.HOUR_OF_DAY);
		minutes = c.get(Calendar.MINUTE);
		vibrate = true;
		alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
	}

	public String getLabelOrDefault(Context context) {
		return "label";
	}

	/*
	 * Days of week code as a single int. 0x00: no day 0x01: Monday 0x02:
	 * Tuesday 0x04: Wednesday 0x08: Thursday 0x10: Friday 0x20: Saturday 0x40:
	 * Sunday
	 */
	// Bitmask of all repeating days
	private int mDays;

	private boolean isSet(int day) {
		return ((mDays & (1 << day)) > 0);
	}

	public void set(int day, boolean set) {
		if (set) {
			mDays |= (1 << day);
		} else {
			mDays &= ~(1 << day);
		}
	}

	public int getCoded() {
		return mDays;
	}

	// Returns days of week encoded in an array of booleans.
	public boolean[] getBooleanArray() {
		boolean[] ret = new boolean[7];
		for (int i = 0; i < 7; i++) {
			ret[i] = isSet(i);
		}
		return ret;
	}

	public boolean isRepeatSet() {
		return mDays != 0;
	}

	/**
	 * returns number of days from today until next alarm
	 * 
	 * @param c
	 *            must be set to today
	 */
	public int getNextAlarm(Calendar c) {
		if (mDays == 0) {
			return -1;
		}

		int today = (c.get(Calendar.DAY_OF_WEEK) + 5) % 7;

		int day = 0;
		int dayCount = 0;
		for (; dayCount < 7; dayCount++) {
			day = (today + dayCount) % 7;
			if (isSet(day)) {
				break;
			}
		}
		return dayCount;
	}
}
