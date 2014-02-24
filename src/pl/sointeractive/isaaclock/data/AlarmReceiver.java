package pl.sointeractive.isaaclock.data;

import pl.sointeractive.isaaclock.activities.AlarmActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

	
	
	Context context;

	@Override
	public void onReceive(final Context context, Intent intent) {
		Toast.makeText(context, "Alarm received!", Toast.LENGTH_LONG).show();

		this.context = context;

		//new SoundAlarm().execute();
		
		Intent newIntent = new Intent(context, AlarmActivity.class);
		newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(newIntent);

		Log.e("AlarmReceiver", "ALARM RECEIVED!!!");
	}

	

}
