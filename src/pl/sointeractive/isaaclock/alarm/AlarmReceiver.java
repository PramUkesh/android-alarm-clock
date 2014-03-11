package pl.sointeractive.isaaclock.alarm;

import pl.sointeractive.isaaclock.activities.AlarmActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * This is a BroadcastReceiver used to detect new alarms. After receiving an
 * appropriate message from the system it starts a new AlarmActivty. A snooze
 * counter is stored and sent together with the new intent.
 * 
 * @author Mateusz Renes
 * 
 */
public class AlarmReceiver extends BroadcastReceiver {

	Context context;
	int snoozeCounter;

	@Override
	public void onReceive(final Context context, Intent intent) {
		this.context = context;

		Bundle extras = intent.getExtras();
		if (extras != null) {
			snoozeCounter = extras.getInt("SNOOZE_COUNTER");
		}

		Intent newIntent = new Intent(context, AlarmActivity.class);
		newIntent.putExtra("SNOOZE_COUNTER", snoozeCounter);
		newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(newIntent);

		Log.e("AlarmReceiver", "ALARM RECEIVED!!!");
	}

}
