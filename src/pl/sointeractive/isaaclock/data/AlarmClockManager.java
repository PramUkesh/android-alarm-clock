package pl.sointeractive.isaaclock.data;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmClockManager {

	final static int RequestCode = 999;

	public void setAlarm(UserData.AlarmInfo alarmInfo, Context context) {
		Log.d("AlarmClockManager", "setAlarm");
		alarmInfo.print();
		if(alarmInfo.ACTIVE){
			Calendar c = Calendar.getInstance();
			c.set(Calendar.HOUR_OF_DAY, alarmInfo.HOUR);
		    c.set(Calendar.MINUTE, alarmInfo.MINUTE);
		    c.add(Calendar.DATE, alarmInfo.DAYS_FROM_NOW);
		    c.set(Calendar.SECOND, 0);
		    c.set(Calendar.MILLISECOND, 0);
		    
			Intent intent = new Intent(context, AlarmReceiver.class);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
					RequestCode, intent, 0);
			AlarmManager alarmManager = (AlarmManager) context
					.getSystemService(Context.ALARM_SERVICE);
			
			long alarmTime = c.getTimeInMillis();
			Log.d("setAlarm","Alarm set to: " + c.getTime().toString());
			
			alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);
		}
	}
	
	
}
