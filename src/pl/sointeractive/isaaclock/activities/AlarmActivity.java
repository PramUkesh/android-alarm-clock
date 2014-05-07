package pl.sointeractive.isaaclock.activities;

import java.io.IOException;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import pl.sointeractive.isaaclock.R;
import pl.sointeractive.isaaclock.alarm.AlarmService;
import pl.sointeractive.isaaclock.alarm.SnoozeReceiver;
import pl.sointeractive.isaaclock.config.Settings;
import pl.sointeractive.isaaclock.data.App;
import pl.sointeractive.isaaclock.data.UserData;
import pl.sointeractive.isaacloud.connection.HttpResponse;
import pl.sointeractive.isaacloud.exceptions.IsaaCloudConnectionException;
import android.app.Activity;
import android.app.AlarmManager;
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

/**
 * This Activity is started when an the AlarmReceiver or SnoozeReceiver detects
 * a valid intent. It allows the use to turn off the alarm or turn on next
 * snooze.
 * 
 * @author Mateusz Renes
 * 
 */
public class AlarmActivity extends Activity {

	private final static int RequestCode = 1;
	private static final String TAG = "AlarmActivity";

	private MediaPlayer mp;
	private Vibrator vibrator;
	private int snoozeCounter;

	/**
	 * Method called on activity creation.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// generate basic view and lock screen orientation
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
		setContentView(R.layout.activity_alarm);
		// get the snoozeCounter value from the intent
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			snoozeCounter = extras.getInt("SNOOZE_COUNTER");
		}
		Log.d(TAG, "onCreate() - snoozeCounter = " + snoozeCounter);
		// set button listeners
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
		// execute alarm sound and vibration async task
		new SoundAlarm().execute();
	}

	/**
	 * Method used to deactivate the alarm after the user used the "Wake up"
	 * button.
	 */
	private void alarmOff() {
		new StopAlarm().execute();
		Log.d(TAG, "alarmOff() - snoozeCounter = " + snoozeCounter);
		new PostEventTask().execute();
		snoozeCounter = 0;
		resetAlarmService();
		finish();
	}

	/**
	 * Method used to deactivate and reschedule the alarm after the user used
	 * the "Snooze" button.
	 */
	private void alarmSnooze() {
		new StopAlarm().execute();
		snoozeCounter++;
		Log.d(TAG, "alarmSnooze() - snoozeCounter = " + snoozeCounter);
		// setup next snooze alarm time
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MINUTE, Settings.snoozeTimeInMinutes);
		long nextSnoozeTime = c.getTimeInMillis();
		// set new snooze alarm
		Intent intent = new Intent(getApplicationContext(),
				SnoozeReceiver.class);
		intent.putExtra("SNOOZE_COUNTER", snoozeCounter);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(
				getApplicationContext(), RequestCode, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarmManager = (AlarmManager) getApplicationContext()
				.getSystemService(Context.ALARM_SERVICE);
		Log.d(TAG, "Snooze set to: " + c.getTime().toString());
		alarmManager
				.set(AlarmManager.RTC_WAKEUP, nextSnoozeTime, pendingIntent);
		finish();
	}

	/**
	 * Cancel any AlarmService tasks and run then again with renewed parameters
	 * (new alarm date and time).
	 */
	private void resetAlarmService() {
		Log.d(TAG, "restart AlarmService");
		Intent intent = new Intent(getApplicationContext(), AlarmService.class);
		stopService(intent);
		startService(intent);
	}

	/**
	 * AsynTask used for starting the alarm sound and vibration.
	 * 
	 * @author Mateusz Renes
	 */
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
			Log.d(TAG, "Is playing: " + mp.isPlaying());
			return null;
		}
	}

	/**
	 * AsynTask used for stopping the alarm sound and vibration.
	 * 
	 * @author Mateusz Renes
	 */
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

	/**
	 * AsynTask used for posting an event to the API after the user finally
	 * decided to wake up.
	 * 
	 * @author Mateusz Renes
	 */
	private class PostEventTask extends AsyncTask<Object, Object, Object> {

		HttpResponse response;
		boolean isError = false;
		UserData userData = App.loadUserData();

		@Override
		protected Object doInBackground(Object... params) {
			Log.d(TAG, "doInBackground()");
			// create the json body post the request
			try {
				// send request and wait for response
				JSONObject body = new JSONObject();
				body.put("wake_up", "woken_up");
				response = App.getConnector().event(userData.getUserId(), "USER", "PRIORITY_HIGH", 1, "NORMAL", body);
			} catch (IsaaCloudConnectionException e) {
				e.printStackTrace();
			} catch (IOException e) {
				isError = true;
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(Object result) {
			Log.d(TAG, "onPostExecute()");
			// check if an error has occured
			if (isError) {
				Log.d(TAG, "onPostExecute() - error detected");
			}
			if (response != null) {
				Log.d(TAG, "onPostExecute() - response: " + response.toString());
			}
		}

	}

	@Override
	public void onBackPressed() {
		// snooze alarm if the user presses the back button
		alarmSnooze();
		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		// unlock screen orientation
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
		super.onDestroy();
	}

}
