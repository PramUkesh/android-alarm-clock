package pl.sointeractive.isaaclock.fragments;

import java.util.ArrayList;
import java.util.Calendar;

import pl.sointeractive.isaaclock.R;
import pl.sointeractive.isaaclock.activities.UserActivityTabs;
import pl.sointeractive.isaaclock.activities.UserActivityTabs.TabManager;
import pl.sointeractive.isaaclock.activities.UserActivityViewPager;
import pl.sointeractive.isaaclock.data.Alarm;
import pl.sointeractive.isaaclock.data.App;
import pl.sointeractive.isaaclock.data.UserData;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TimePicker;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockListFragment;

public class AlarmsFragment extends SherlockListFragment {

	UserActivityTabs context;
	AlarmAdapter alarmAdapter;
	ArrayList<Alarm> alarmList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = (UserActivityTabs) getSherlockActivity();

		alarmList = App.loadUserData().getAlarms();
		alarmAdapter = new AlarmAdapter(alarmList);

		setListAdapter(alarmAdapter);
		return inflater.inflate(R.layout.fragment_alarms, container, false);
	}

	public void onCheckBoxClick(int dayIndex, boolean active) {
		final UserData userData = App.loadUserData();
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
			openOptionsDialog();
		} else {
			onTimeTextClick(position);
		}
	}

	public void onTimeTextClick(final int dayIndex) {
		Log.d("TEST", "touch");
		final UserData userData = App.loadUserData();
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

	public void openOptionsDialog() {
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

					dialog.dismiss();
					break;
				case 1:

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
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			Alarm alarm = (Alarm) getItem(position);
			// Log.d("AlarmAdapter.getView()", alarm.print());

			holder.textTime.setText(alarm.getTime());
			holder.textDay.setText(alarm.getDay());

			return convertView;
		}
	}

}