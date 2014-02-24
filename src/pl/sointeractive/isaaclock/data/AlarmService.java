package pl.sointeractive.isaaclock.data;

import java.util.Calendar;

import pl.sointeractive.isaaclock.R;
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

public class AlarmService extends Service {

	final static int RequestCode = 1;
	int snoozeCounter;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("AlarmService", "onStartCommand");
		Bundle extras = intent.getExtras();
		if (extras != null) {
			snoozeCounter = extras.getInt("SNOOZE_COUNTER");
		}
		UserData userData = App.loadUserData();
		UserData.AlarmInfo alarmInfo = userData.getNextAlarmInfo();
		String nextAlarmString = userData.getNextAlarmTime();

		setAlarm(alarmInfo);

		NotificationCompat.Builder noteBuilder = new NotificationCompat.Builder(
				this);
		noteBuilder.setSmallIcon(R.drawable.ic_launcher);
		noteBuilder.setContentTitle(getString(R.string.service_note_title));
		noteBuilder.setContentText(getString(R.string.service_note_message)
				+ "\n" + nextAlarmString);
		Notification note = noteBuilder.build();
		startForeground(9999, note);
		return START_NOT_STICKY;
	}

	private void setAlarm(UserData.AlarmInfo alarmInfo) {

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

			Intent intent = new Intent(getApplicationContext(),
					AlarmReceiver.class);
			intent.putExtra("SNOOZE_COUNTER", snoozeCounter);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(
					getApplicationContext(), RequestCode, intent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			AlarmManager alarmManager = (AlarmManager) getApplicationContext()
					.getSystemService(Context.ALARM_SERVICE);

			long alarmTime = c.getTimeInMillis();
			Log.d("setAlarm", "Alarm set to: " + c.getTime().toString());

			alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);
		}
	}

}
