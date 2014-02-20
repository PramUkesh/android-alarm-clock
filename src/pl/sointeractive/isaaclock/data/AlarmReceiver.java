package pl.sointeractive.isaaclock.data;

import pl.sointeractive.isaaclock.R;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

	AlertDialog dialog;
	MediaPlayer mp;
	Context context;

	@Override
	public void onReceive(final Context context, Intent intent) {
		Toast.makeText(context, "Alarm received!", Toast.LENGTH_LONG).show();
		
		this.context = context;
		
		new AsyncTask<Object, Object, Object>() {
			@Override
			protected Object doInBackground(Object... params) {

				AudioManager audioManager = (AudioManager) App.getInstance()
						.getSystemService(Context.AUDIO_SERVICE);
				int maxVolume = audioManager
						.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume/5,
						0);

				MediaPlayer mp = MediaPlayer.create(App.getInstance()
						.getApplicationContext(), R.raw.load_alarm);
				mp.setLooping(true);
				mp.start();
				
				Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
			    vibrator.vibrate(2000);
			    
				Log.d("AsyncTask", "IsPlaying: " + mp.isPlaying());
				return null;
			}
		}.execute();

		Log.e("AlarmReceiver", "ALARM RECEIVED!!!");
		UserData userData = App.loadUserData();
		App.setAlarm(userData.getNextAlarmInfo());
		
		showAlarmDialog();
	}

	public void showAlarmDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.fragment_alarms_dialog_title);
		builder.setMessage(R.string.alarm_dialog_message);
		builder.setPositiveButton(R.string.alarm_dialog_button_positive,
				new OnPositiveButtonPress());
		builder.setNegativeButton(R.string.alarm_dialog_button_negative,
				new OnNegativeButtonPress());

		dialog = builder.create();
		dialog.show();
	}

	private class OnPositiveButtonPress implements
			DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			mp.stop();
			mp.reset();
			mp.release();
			dialog.dismiss();
		}
	}

	private class OnNegativeButtonPress implements
			DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			mp.stop();
			mp.reset();
			mp.release();
			dialog.dismiss();
		}
	}

}
