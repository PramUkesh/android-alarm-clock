package pl.sointeractive.isaaclock.activities;

import java.io.IOException;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import pl.sointeractive.isaaclock.R;
import pl.sointeractive.isaaclock.config.Settings;
import pl.sointeractive.isaaclock.data.AlarmReceiver;
import pl.sointeractive.isaaclock.data.AlarmService;
import pl.sointeractive.isaaclock.data.App;
import pl.sointeractive.isaaclock.data.UserData;
import pl.sointeractive.isaacloud.connection.HttpResponse;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
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
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
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
		buttonSnooze.setBackgroundColor(Color.rgb(0, 150, 150));
		buttonSnooze.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				alarmSnooze();
			}
		});

		new SoundAlarm().execute();
	}

	private void stopAlarm() {
		new StopAlarm().execute();
	}

	private void alarmOff() {
		stopAlarm();
		Log.d("AlarmReceiver", "alarmOff() - snoozeCounter = " + snoozeCounter);

		// here send info about the alarm
		new PostEventTask().execute();
		
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
		c.add(Calendar.MINUTE, Settings.snoozeTimeInMinutes);

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
	
	private class StopAlarm extends AsyncTask<Object, Object, Object> {
		@Override
		protected Object doInBackground(Object... params) {
			mp.stop();
			mp.reset();
			mp.release();
			vibrator.cancel();
			return null;
		}
	}
	
	private class PostEventTask extends AsyncTask<Object, Object, Object>{
		
		HttpResponse response;
		boolean isError = false;
		UserData userData = App.loadUserData();

		@Override
		protected Object doInBackground(Object... params) {
			Log.d("PostEventTask", "doInBackground()");
			
			//userData = App.loadUserData();
			
			JSONObject jsonBody = new JSONObject();
			JSONObject body = new JSONObject();
			try {
				body.put("wake_up", "woken_up");
				jsonBody.put("body", body);
				jsonBody.put("priority", "PRIORITY_HIGH");
				jsonBody.put("sourceId", 1);
				jsonBody.put("subjectId", userData.getUserId());
				jsonBody.put("subjectType", "USER");
				jsonBody.put("type", "NORMAL");
			} catch (JSONException e1) {
				isError = true;
				e1.printStackTrace();
			}
			
			try {
				response = App.getWrapper().postEvent(jsonBody);
			} catch (IOException e) {
				isError = true;
				e.printStackTrace();
			} catch (JSONException e) {
				isError = true;
				e.printStackTrace();
			}
			return null;
		}
		
		protected void onPostExecute (Object result){
			Log.d("PostEventTask", "onPostExecute()");
			if(isError){
				Log.d("PostEventTask", "onPostExecute() - error detected");
				//Toast.makeText(context, R.string.error_no_connection, Toast.LENGTH_LONG).show();
			}
			if(response != null){
				Log.d("PostEventTask", "onPostExecute() - response: " + response.toString());
			}
		}
		
	}

	@Override
	public void onBackPressed() {
		alarmSnooze();
		super.onBackPressed();
	}
	
	@Override
    protected void onDestroy() {
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
		super.onDestroy();
    }

}
