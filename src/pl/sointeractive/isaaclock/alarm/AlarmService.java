package pl.sointeractive.isaaclock.alarm;

import java.util.Calendar;

import pl.sointeractive.isaaclock.R;
import pl.sointeractive.isaaclock.data.App;
import pl.sointeractive.isaaclock.data.UserData;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * This service is used for monitoring alarm activity and setting new alarms for
 * the AlarmManager. The service is run in foreground to ensure that it will be
 * shut down by the OS only is case of extreme lack of resources.
 * 
 * @author Mateusz Renes
 * 
 */
public class AlarmService extends Service {

	private static final String TAG = "AlarmService";

	final static int RequestCode = 1;
	int snoozeCounter;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/**
	 * This method is called after the start() command of the service. It sets a
	 * new alarm based on UserData file and starts the service.
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand");
		// get snooze counter
		Bundle extras = intent.getExtras();
		if (extras != null) {
			snoozeCounter = extras.getInt("SNOOZE_COUNTER");
		}
		// get alarm info
		UserData userData = App.loadUserData();
		UserData.AlarmInfo alarmInfo = userData.getNextAlarmInfo();
		String nextAlarmString = userData.getNextAlarmTime();
		// set alarm
		setAlarm(alarmInfo);
		// build notification
		NotificationCompat.Builder noteBuilder = new NotificationCompat.Builder(
				this);
		noteBuilder.setSmallIcon(R.drawable.ic_launcher);
		noteBuilder.setContentTitle(getString(R.string.service_note_title));
		noteBuilder.setContentText(getString(R.string.service_note_message)
				+ "\n" + nextAlarmString);
		Notification note = noteBuilder.getNotification();
		// start service
		startForeground(1, note);
		return START_NOT_STICKY;
	}

	/**
	 * Sets a new alarm.
	 * 
	 * @param alarmInfo
	 *            Information about the alarm being set.
	 */
	private void setAlarm(UserData.AlarmInfo alarmInfo) {
		// set snooze counter
		Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
		intent.putExtra("SNOOZE_COUNTER", snoozeCounter);
		// create intent
		PendingIntent pendingIntent = PendingIntent.getBroadcast(
				getApplicationContext(), RequestCode, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		// get alarm manager
		AlarmManager alarmManager = (AlarmManager) getApplicationContext()
				.getSystemService(Context.ALARM_SERVICE);
		// cancel any previous alarms if set
		alarmManager.cancel(pendingIntent);
		// if the alarm is active, choose time and date of the alarm and
		// set it in the alarm manager
		if (alarmInfo.ACTIVE) {
			Calendar c = Calendar.getInstance();
			if (alarmInfo.isShowingCurrentOrPastTime()) {
				alarmInfo.DAYS_FROM_NOW += 7;
			}
			c.set(Calendar.HOUR_OF_DAY, alarmInfo.HOUR);
			c.set(Calendar.MINUTE, alarmInfo.MINUTE);
			c.add(Calendar.DATE, alarmInfo.DAYS_FROM_NOW);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			long alarmTime = c.getTimeInMillis();
			Log.d(TAG, "Alarm set to: " + c.getTime().toString());
			alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);

		}
	}

}
