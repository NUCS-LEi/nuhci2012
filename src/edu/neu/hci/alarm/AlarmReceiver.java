/*
 * Copyright (C) 2007 The Android Open Source Project
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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import edu.neu.hci.Global;

/**
 * Glue class: connects AlarmAlert IntentReceiver to AlarmAlert activity. Passes
 * through Alarm ID.
 */
public class AlarmReceiver extends BroadcastReceiver {

	/**
	 * If the alarm is older than STALE_WINDOW, ignore. It is probably the
	 * result of a time or timezone change
	 */

	@Override
	public void onReceive(Context context, Intent intent) {
		if (Alarms.ALARM_ALERT_ACTION.equals(intent.getAction())) {
			AlarmAlertWakeLock.acquireCpuWakeLock(context);
			Alarm alarm = null;
			final byte[] data = intent.getByteArrayExtra(Alarms.ALARM_RAW_DATA);
			if (data != null) {
				Parcel in = Parcel.obtain();
				in.unmarshall(data, 0, data.length);
				in.setDataPosition(0);
				alarm = Alarm.CREATOR.createFromParcel(in);
			}

			if (alarm == null) {
				Alarms.setNextAlert(context);
				return;
			}
			Intent playAlarm = new Intent(Alarms.ALARM_ALERT_ACTION);
			playAlarm.putExtra(Alarms.ALARM_INTENT_EXTRA, alarm);
			context.startService(playAlarm);
			Intent i = new Intent();
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			i.putExtra(Global.ALARM, alarm);
			i.setClass(context, WakeUpActivity.class);
			context.startActivity(i);
		}
	}
}
