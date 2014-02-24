package pl.sointeractive.isaaclock.activities;

import java.util.Calendar;

import pl.sointeractive.isaaclock.R;
import pl.sointeractive.isaaclock.data.AlarmReceiver;
import pl.sointeractive.isaaclock.data.AlarmService;
import pl.sointeractive.isaaclock.data.App;
import pl.sointeractive.isaaclock.data.UserData;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AlarmActivity extends Activity {

	final static int RequestCode = 1;

	MediaPlayer mp;
	AlertDialog dialog;
	Vibrator vibrator;
	int snoozeCounter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			snoozeCounter = extras.getInt("SNOOZE_COUNTER");
		}
		Log.d("AlarmReceiver", "onCreate() - snoozeCounter = " + snoozeCounter);

		Button buttonOff = (Button) findViewById(R.id.button_alarm_off);
		buttonOff.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				alarmOff();
			}
		});

		Button buttonSnooze = (Button) findViewById(R.id.button_alarm_snooze);
		buttonSnooze.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				alarmSnooze();
			}
		});

		new SoundAlarm().execute();
	}

	private void stopAlarm() {
		mp.stop();
		mp.reset();
		mp.release();
		vibrator.cancel();
	}

	private void alarmOff() {
		stopAlarm();
		Log.d("AlarmReceiver", "alarmOff() - snoozeCounter = " + snoozeCounter);

		// here send info about the alarm

		snoozeCounter = 0;
		resetAlarmService();
		finish();
	}

	private void alarmSnooze() {
		stopAlarm();
		snoozeCounter++;
		Log.d("AlarmReceiver", "alarmSnooze() - snoozeCounter = "
				+ snoozeCounter);

		Calendar c = Calendar.getInstance();
		c.add(Calendar.MINUTE, 1);

		Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
		intent.putExtra("SNOOZE_COUNTER", snoozeCounter);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(
				getApplicationContext(), RequestCode, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarmManager = (AlarmManager) getApplicationContext()
				.getSystemService(Context.ALARM_SERVICE);
		long nextSnoozeTime = c.getTimeInMillis();
		Log.d("setAlarm", "Snooze set to: " + c.getTime().toString());
		alarmManager
				.set(AlarmManager.RTC_WAKEUP, nextSnoozeTime, pendingIntent);
		finish();
	}

	private void resetAlarmService() {
		Log.d("AlarmReceiver", "restart AlarmService");
		Intent intent = new Intent(getApplicationContext(), AlarmService.class);
		stopService(intent);
		startService(intent);
	}

	private class SoundAlarm extends AsyncTask<Object, Object, Object> {

		@Override
		protected Object doInBackground(Object... params) {
			AudioManager audioManager = (AudioManager) App.getInstance()
					.getSystemService(Context.AUDIO_SERVICE);
			int maxVolume = audioManager
					.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
					maxVolume / 2, 0);

			mp = MediaPlayer.create(App.getInstance().getApplicationContext(),
					R.raw.alarm);
			mp.setLooping(true);
			mp.start();

			vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			long[] pattern = { 1000, 1000 };
			vibrator.vibrate(pattern, 0);

			Log.d("AsyncTask", "IsPlaying: " + mp.isPlaying());

			return null;
		}
	}

	@Override
	public void onBackPressed() {
		// do nothing
	}

}
