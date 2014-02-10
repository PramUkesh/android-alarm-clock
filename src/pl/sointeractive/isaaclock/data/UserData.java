package pl.sointeractive.isaaclock.data;

import java.util.ArrayList;

public class UserData {

	private static ArrayList<Alarm> alarmList;
	private static final String[] dayArray = { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat",
			"Sun" };

	public UserData() {
		alarmList = new ArrayList<Alarm>();
		alarmList.add(new Alarm(dayArray[0], "07:00", false));
		alarmList.add(new Alarm(dayArray[1], "07:00", false));
		alarmList.add(new Alarm(dayArray[2], "07:00", false));
		alarmList.add(new Alarm(dayArray[3], "07:00", false));
		alarmList.add(new Alarm(dayArray[4], "07:00", false));
		alarmList.add(new Alarm(dayArray[5], "07:00", false));
		alarmList.add(new Alarm(dayArray[6], "07:00", false));
	}

	public static ArrayList<Alarm> getAlarms() {
		return alarmList;
	}

	public static void setAlarm(int dayIndex, String time, boolean active){
		alarmList.set(dayIndex, new Alarm(dayArray[dayIndex], time, active));
	}

}
