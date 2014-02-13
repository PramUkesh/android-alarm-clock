package pl.sointeractive.isaaclock.data;

import java.io.Serializable;
import java.util.ArrayList;

public class UserData implements Serializable {

	private static final long serialVersionUID = 7298108357152407887L;

	private ArrayList<Alarm> alarmList;

	private static final String[] dayArray = { "Mon", "Tue", "Wed", "Thu",
			"Fri", "Sat", "Sun" };

	public UserData() {
		alarmList = new ArrayList<Alarm>();
		alarmList.add(new Alarm(dayArray[0], "7:00", false));
		alarmList.add(new Alarm(dayArray[1], "7:00", false));
		alarmList.add(new Alarm(dayArray[2], "7:00", false));
		alarmList.add(new Alarm(dayArray[3], "7:00", false));
		alarmList.add(new Alarm(dayArray[4], "7:00", false));
		alarmList.add(new Alarm(dayArray[5], "7:00", false));
		alarmList.add(new Alarm(dayArray[6], "7:00", false));
	}

	public ArrayList<Alarm> getAlarms() {
		return alarmList;
	}

	public void setAlarm(int dayIndex, boolean active) {
		String time = alarmList.get(dayIndex).getTime();
		alarmList.set(dayIndex, new Alarm(dayArray[dayIndex], time, active));
	}
	
	public void setAlarm(int dayIndex, String time) {
		boolean active = alarmList.get(dayIndex).isActive();
		alarmList.set(dayIndex, new Alarm(dayArray[dayIndex], time, active));
	}

	public void setAlarm(int dayIndex, String time, boolean active) {
		if (dayIndex < 0 || dayIndex > 6) {
			System.out.println("WRONG DAY INDEX");
			return;
		} else {
			alarmList
					.set(dayIndex, new Alarm(dayArray[dayIndex], time, active));
		}
	}

	public String print() {
		String result = "Alarms: \n";
		for (int i = 0; i < 7; i++) {
			result += alarmList.get(i).print() + "\n";
		}
		return result;
	}

}
