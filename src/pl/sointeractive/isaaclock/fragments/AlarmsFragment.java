package pl.sointeractive.isaaclock.fragments;

import java.util.ArrayList;
import java.util.Calendar;

import pl.sointeractive.isaaclock.R;
import pl.sointeractive.isaaclock.activities.UserActivity;
import pl.sointeractive.isaaclock.data.Alarm;
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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockListFragment;

public class AlarmsFragment extends SherlockListFragment {

	UserActivity context;
	AlarmAdapter alarmAdapter;

	// ListView listView;
	// ImageButton addButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = (UserActivity) getSherlockActivity();
		// listView = (ListView)
		// context.findViewById(R.id.fragment_alarms_listview);

		// temporary alarmlist creation, will be replaced later
		ArrayList<Alarm> alarmList = new ArrayList<Alarm>();
		alarmList.add(new Alarm("Mon", "07:00", false));
		alarmList.add(new Alarm("Tue", "07:00", false));
		alarmList.add(new Alarm("Wed", "07:00", false));
		alarmList.add(new Alarm("Thu", "07:00", false));
		alarmList.add(new Alarm("Fri", "07:00", false));
		alarmList.add(new Alarm("Sat", "07:00", false));
		alarmList.add(new Alarm("Sun", "07:00", false));
		
		new UserData();

		alarmAdapter = new AlarmAdapter(UserData.getAlarms());

		// listView.setAdapter(alarmAdapter);
		setListAdapter(alarmAdapter);
		return inflater.inflate(R.layout.fragment_alarms, container, false);
	}
	
	public void setTime(final int dayIndex){
		Log.d("TEST", "touch");
		TimePickerDialog.OnTimeSetListener timePickerListener= 
	            new TimePickerDialog.OnTimeSetListener() {
			public void onTimeSet(TimePicker view, int selectedHour,
					int selectedMinute) {
				UserData.setAlarm(dayIndex, ""+selectedHour+":"+selectedMinute, true);
				// set current time into textview
				
	 
			}
		};
		
		Calendar c = Calendar.getInstance();
		
		new TimePickerDialog(context, timePickerListener, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),true).show();
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
		public View getView(final int position, View convertView, ViewGroup parent) {
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

			holder.textTime.setText(alarm.getTime());
			holder.textDay.setText(alarm.getDay());
			holder.checkbox.setChecked(alarm.isActive());
			
			convertView.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					setTime(position);
				}
			});

			return convertView;
		}

	}
}