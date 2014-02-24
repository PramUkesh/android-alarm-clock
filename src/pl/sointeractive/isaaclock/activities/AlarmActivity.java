package pl.sointeractive.isaaclock.activities;

import pl.sointeractive.isaaclock.R;
import pl.sointeractive.isaaclock.data.AlarmService;
import pl.sointeractive.isaaclock.data.App;
import android.app.Activity;
import android.app.AlertDialog;
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

	MediaPlayer mp;
	AlertDialog dialog;
	Vibrator vibrator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm);

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

	private void alarmOff() {
		mp.stop();
		mp.reset();
		mp.release();
		vibrator.cancel();
		resetAlarmService();
		finish();
	}

	private void alarmSnooze() {
		mp.stop();
		mp.reset();
		mp.release();
		vibrator.cancel();
		resetAlarmService();
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
					maxVolume / 5, 0);

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

}
