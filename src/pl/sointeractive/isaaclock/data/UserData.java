package pl.sointeractive.isaaclock.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

import pl.sointeractive.isaaclock.R;

public class UserData implements Serializable {

	private static final long serialVersionUID = 7298108357152407887L;

	private ArrayList<Alarm> alarmList;
	private int score;
	private String name, email;
	
	
	private static final String[] dayArray = { "Mon", "Tue", "Wed", "Thu",
			"Fri", "Sat", "Sun" };

	public UserData() {
		alarmList = new ArrayList<Alarm>();
		String default_time = App.getInstance()
				.getString(R.string.time_not_set);
		alarmList.add(new Alarm(dayArray[0], default_time, false));
		alarmList.add(new Alarm(dayArray[1], default_time, false));
		alarmList.add(new Alarm(dayArray[2], default_time, false));
		alarmList.add(new Alarm(dayArray[3], default_time, false));
		alarmList.add(new Alarm(dayArray[4], default_time, false));
		alarmList.add(new Alarm(dayArray[5], default_time, false));
		alarmList.add(new Alarm(dayArray[6], default_time, false));
		
		name = "user name";
		email = "user email";
	}

	public ArrayList<Alarm> getAlarms() {
		return alarmList;
	}

	public void setAlarm(int dayIndex, boolean active) {
		String time = alarmList.get(dayIndex).getTime();
		alarmList.set(dayIndex, new Alarm(dayArray[dayIndex], time, active));
	}

	public void setAlarm(int dayIndex, String time) {
		alarmList.set(dayIndex, new Alarm(dayArray[dayIndex], time, true));
	}

	public void setAlarm(int dayIndex, String time, boolean active) {
		alarmList.set(dayIndex, new Alarm(dayArray[dayIndex], time, active));
	}
	
	

	public String print() {
		String result = "Alarms: \n";
		for (int i = 0; i < 7; i++) {
			result += alarmList.get(i).print() + "\n";
		}
		return result;
	}

	boolean isActive(int dayIndex) {
		return alarmList.get(dayIndex).isActive();
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getNextAlarm(){
		String nextAlarm = App.getInstance()
				.getString(R.string.alarm_not_set);
		Calendar c = Calendar.getInstance();
		int counter=0;
		while(counter<7){
		//	c.compareTo(anotherCalendar)
			counter++;
		}
		
		return nextAlarm;
	}

}
