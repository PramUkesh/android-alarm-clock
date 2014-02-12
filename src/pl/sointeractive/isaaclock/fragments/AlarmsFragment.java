package pl.sointeractive.isaaclock.fragments;

import java.util.ArrayList;
import java.util.Calendar;

import pl.sointeractive.isaaclock.R;
import pl.sointeractive.isaaclock.activities.UserActivity;
import pl.sointeractive.isaaclock.data.Alarm;
import pl.sointeractive.isaaclock.data.App;
import pl.sointeractive.isaaclock.data.UserData;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockListFragment;

public class AlarmsFragment extends SherlockListFragment {

	UserActivity context;
	AlarmAdapter alarmAdapter;
	ArrayList<Alarm> alarmList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = (UserActivity) getSherlockActivity();

		alarmList = App.loadUserData().getAlarms();
		alarmAdapter = new AlarmAdapter(alarmList);

		setListAdapter(alarmAdapter);
		return inflater.inflate(R.layout.fragment_alarms, container, false);
	}

	public void onAlarmStateChanged(int dayIndex, boolean active) {
		Log.d("onAlarmStateChanged", "1");
		final UserData userData = App.loadUserData();
		Log.d("onAlarmStateChanged", "2");
		userData.setAlarm(dayIndex, active);
		Log.d("onAlarmStateChanged", "3");
		alarmList = userData.getAlarms();
		Log.d("onAlarmStateChanged", "4");
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Log.d("onAlarmStateChanged", "5");
				alarmAdapter.notifyDataSetChanged();
			}
		});
		Log.d("onAlarmStateChanged", "6");
		App.saveUserData(userData);
	}

	public void setTime(final int dayIndex) {
		Log.d("TEST", "touch");
		final UserData userData = App.loadUserData();
		TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
			public void onTimeSet(TimePicker view, int selectedHour,
					int selectedMinute) {
				userData.setAlarm(dayIndex, "" + selectedHour + ":"
						+ selectedMinute, false);
				alarmList = userData.getAlarms();
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						alarmAdapter.notifyDataSetChanged();
					}
				});
				App.saveUserData(userData);
			}
		};

		Calendar c = Calendar.getInstance();

		new TimePickerDialog(context, timePickerListener,
				c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true)
				.show();
	}

	private class AlarmAdapter extends BaseAdapter {

		ArrayList<Alarm> alarmList;

		/* private view holder class */
		private class ViewHolder {
			CheckBox checkbox;
			TextView textDay;
			TextView textTime;
		}

		public AlarmAdapter(ArrayList<Alarm> list) {
			alarmList = list;
		}

		@Override
		public int getCount() {
			return alarmList.size();
		}

		@Override
		public Object getItem(int position) {
			return alarmList.get(position);
		}

		@Override
		public long getItemId(int position) {
			alarmList.indexOf(getItem(position));
			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder = null;

			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.fragment_alarms_item,
						null);
				holder = new ViewHolder();
				holder.checkbox = (CheckBox) convertView
						.findViewById(R.id.checkbox);
				holder.textDay = (TextView) convertView
						.findViewById(R.id.text_day);
				holder.textTime = (TextView) convertView
						.findViewById(R.id.text_time);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			Alarm alarm = (Alarm) getItem(position);
			Log.d("AlarmAdapter.getView()", alarm.print());

			holder.textTime.setText(alarm.getTime());
			holder.textDay.setText(alarm.getDay());
			holder.checkbox.setChecked(alarm.isActive());

			holder.checkbox
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							Log.d("onCheckedChanged",""+isChecked);
							onAlarmStateChanged(position, isChecked);
						}
					});

			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					setTime(position);
				}
			});

			return convertView;
		}

	}
}