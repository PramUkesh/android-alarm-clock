package pl.sointeractive.isaaclock.fragments;

import java.util.ArrayList;
import java.util.Calendar;

import pl.sointeractive.isaaclock.R;
import pl.sointeractive.isaaclock.activities.UserActivityTabs;
import pl.sointeractive.isaaclock.activities.UserActivityTabs.TabManager;
import pl.sointeractive.isaaclock.data.Alarm;
import pl.sointeractive.isaaclock.data.AlarmService;
import pl.sointeractive.isaaclock.data.App;
import pl.sointeractive.isaaclock.data.UserData;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TimePicker;

import com.actionbarsherlock.app.SherlockListFragment;

public class AlarmsFragment extends SherlockListFragment {

	UserActivityTabs context;
	AlarmAdapter alarmAdapter;
	ArrayList<Alarm> alarmList;
	UserData userData;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = (UserActivityTabs) getSherlockActivity();

		userData = App.loadUserData();
		alarmList = userData.getAlarms();
		alarmAdapter = new AlarmAdapter(context);

		setListAdapter(alarmAdapter);
		return inflater.inflate(R.layout.fragment_basic_listview, container,
				false);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Log.d("", "ON LIST ITEM CLICK");
		if (alarmList.get(position).isActive()) {
			openOptionsDialog(position);
		} else {
			setTime(position);
		}
	}

	public void deactivateAlarm(final int dayIndex) {
		alarmList.get(dayIndex).setActive(false);
		App.saveUserData(userData);
		refreshCurrentFragment();
		Log.d("setTime", userData.print());
		App.setAlarm(userData.getNextAlarmInfo());

		startAlarmService();
	}

	public void setTime(final int dayIndex) {
		Log.d("TEST", "touch");
		TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
			public void onTimeSet(TimePicker view, int selectedHour,
					int selectedMinute) {
				userData.setAlarm(dayIndex, selectedHour, selectedMinute, true);
				alarmList = userData.getAlarms();
				App.saveUserData(userData);
				refreshCurrentFragment();
				Log.d("setTime", userData.print());
				// App.setAlarm(userData.getNextAlarmInfo());
				// App.startAlarmService();
				startAlarmService();
			}
		};

		Calendar c = Calendar.getInstance();
		c.add(Calendar.MINUTE, 1);

		new TimePickerDialog(context, timePickerListener,
				c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true)
				.show();
	}

	public void refreshCurrentFragment() {
		Log.d("AlarmsFragment", "refreshCurrentFragment()");
		TabManager tm = context.getTabManager();
		TabHost th = context.getTabHost();
		tm.printTabInfo();
		tm.refreshTab(th.getCurrentTabTag());
	}

	public void openOptionsDialog(final int dayIndex) {
		AlertDialog dialog;
		String[] options = {
				getString(R.string.fragment_alarms_dialog_option_change_time),
				getString(R.string.fragment_alarms_dialog_option_deactivate) };
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.fragment_alarms_dialog_title);
		builder.setNeutralButton(R.string.fragment_alarms_dialog_button_cancel,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.setItems(options, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					setTime(dayIndex);
					dialog.dismiss();
					break;
				case 1:
					deactivateAlarm(dayIndex);
					dialog.dismiss();
					break;
				}
			}
		});
		dialog = builder.create();
		dialog.show();

	}

	public void startAlarmService() {
		if (isServiceRunning()) {
			Log.d("AlarmClockManager", "startAlarmService() - restart");
			Intent intent = new Intent(context.getApplicationContext(),
					AlarmService.class);
			context.getApplicationContext().stopService(intent);
			context.getApplicationContext().startService(intent);
		} else {
			Log.d("AlarmClockManager", "startAlarmService() - new service");
			Intent intent = new Intent(context, AlarmService.class);
			context.getApplicationContext().startService(intent);
		}
	}

	private boolean isServiceRunning() {
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (AlarmService.class.getName().equals(
					service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	private class AlarmAdapter extends ArrayAdapter<Alarm> {
		private final LayoutInflater mInflater;

		public AlarmAdapter(Context context) {
			super(context, R.layout.fragment_alarms_item);
			mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			UserData userData = App.loadUserData();
			for (Alarm alarm : userData.getAlarms()) {
				add(alarm);
			}

		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			View view;
			if (convertView == null) {
				view = mInflater.inflate(R.layout.fragment_alarms_item,
						parent, false);
			} else {
				view = convertView;
			}
			boolean isActive = getItem(position).isActive();

			Alarm alarm = (Alarm) getItem(position);
			TextView textDay = (TextView) view
					.findViewById(R.id.text_day);
			TextView textTime = (TextView) view
					.findViewById(R.id.text_time);
			textTime.setText(alarm.getTime());
			textDay.setText(alarm.getDay());
			if (!isActive) {
				view.setBackgroundColor(Color.GRAY);
				textTime.setText(R.string.time_not_set);
			} else {
				view.setBackgroundColor(Color.TRANSPARENT);
				textTime.setText(alarm.getTime());
			}

			return view;
		}
	}

}