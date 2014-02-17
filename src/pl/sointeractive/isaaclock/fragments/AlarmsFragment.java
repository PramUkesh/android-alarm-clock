package pl.sointeractive.isaaclock.fragments;

import java.util.ArrayList;
import java.util.Calendar;

import com.actionbarsherlock.app.SherlockListFragment;

import pl.sointeractive.isaaclock.R;
import pl.sointeractive.isaaclock.activities.UserActivityTabs;
import pl.sointeractive.isaaclock.activities.UserActivityTabs.TabManager;
import pl.sointeractive.isaaclock.data.Alarm;
import pl.sointeractive.isaaclock.data.App;
import pl.sointeractive.isaaclock.data.UserData;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TimePicker;


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
		alarmAdapter = new AlarmAdapter(alarmList);

		setListAdapter(alarmAdapter);
		return inflater.inflate(R.layout.fragment_basic_listview, container, false);
	}

	public void onCheckBoxClick(int dayIndex, boolean active) {
		userData.setAlarm(dayIndex, active);
		alarmList = userData.getAlarms();
		App.saveUserData(userData);
		final UserData testUserData = App.loadUserData();
		Log.d("onCheckBoxClick", testUserData.print());
		// refreshCurrentFragment();
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
	
	public void deactivateAlarm(final int dayIndex){
		alarmList.get(dayIndex).setActive(false);
		alarmList.get(dayIndex).setTime(getString(R.string.time_not_set));
		App.saveUserData(userData);
		refreshCurrentFragment();
		Log.d("setTime",userData.print());
	}

	public void setTime(final int dayIndex) {
		Log.d("TEST", "touch");
		TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
			public void onTimeSet(TimePicker view, int selectedHour,
					int selectedMinute) {
				String hour = "" + selectedHour;
				String minute;
				if (selectedMinute < 10) {
					minute = "0" + selectedMinute;
				} else {
					minute = "" + selectedMinute;
				}
				userData.setAlarm(dayIndex, "" + hour + ":" + minute, true);
				alarmList = userData.getAlarms();
				App.saveUserData(userData);
				refreshCurrentFragment();
				Log.d("setTime",userData.print());
			}
		};

		Calendar c = Calendar.getInstance();

		new TimePickerDialog(context, timePickerListener,
				c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false)
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

	private class AlarmAdapter extends BaseAdapter {

		ArrayList<Alarm> alarmList;

		/* private view holder class */
		private class ViewHolder {
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
			
			boolean isActive = alarmList.get(position).isActive();

			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.fragment_alarms_item,
						null);
				holder = new ViewHolder();
				holder.textDay = (TextView) convertView
						.findViewById(R.id.text_day);
				holder.textTime = (TextView) convertView
						.findViewById(R.id.text_time);
				convertView.setTag(holder);
				if(!isActive){
					Log.e("TAG","INACTIVE - COLOR GRAY");
					if(position==0){
						Log.e("TAG","COLOR MONDAY GRAY");
					}
					convertView.setBackgroundColor(Color.GRAY);
				}
				Alarm alarm = (Alarm) getItem(position);
				holder.textTime.setText(alarm.getTime());
				holder.textDay.setText(alarm.getDay());
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if(position==0){
				Log.e("TAG","MONDAY SHOWED");
			}
			return convertView;
		}
	}

}