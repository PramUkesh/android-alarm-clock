package pl.sointeractive.isaaclock.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

import android.util.Log;
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
		alarmList.add(new Alarm(dayArray[0], 0, 0, false));
		alarmList.add(new Alarm(dayArray[1], 0, 0, false));
		alarmList.add(new Alarm(dayArray[2], 0, 0, false));
		alarmList.add(new Alarm(dayArray[3], 0, 0, false));
		alarmList.add(new Alarm(dayArray[4], 0, 0, false));
		alarmList.add(new Alarm(dayArray[5], 0, 0, false));
		alarmList.add(new Alarm(dayArray[6], 0, 0, false));

		name = "user name";
		email = "user email";
	}

	public ArrayList<Alarm> getAlarms() {
		return alarmList;
	}

	public void setAlarm(int dayIndex, boolean active) {
		int hour = alarmList.get(dayIndex).getHour();
		int minutes = alarmList.get(dayIndex).getMinutes();
		alarmList.set(dayIndex, new Alarm(dayArray[dayIndex], hour, minutes,
				active));
	}

	public void setAlarm(int dayIndex, int hour, int minutes, boolean active) {
		alarmList.set(dayIndex, new Alarm(dayArray[dayIndex], hour, minutes,
				active));
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

	public String getNextAlarmTime() {
		String nextAlarmTime = App.getInstance().getString(
				R.string.alarm_not_set);
		Calendar c = Calendar.getInstance();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		Alarm firstActiveAlarm = null;

		int currentDayOfWeek = c.get(Calendar.DAY_OF_WEEK)-1;
		int currentHour = c.get(Calendar.HOUR_OF_DAY);
		int currentMinute = c.get(Calendar.MINUTE);
		
		for (Alarm a : alarmList) {
			if (a.isActive()) {
				if (firstActiveAlarm == null) {
					firstActiveAlarm = a;
					nextAlarmTime = firstActiveAlarm.getString();
				}

				int alarmDayOfWeek = a.getDayOfWeekInt();
				int alarmHour = a.getHour();
				int alarmMinute = a.getMinutes();

				if (alarmDayOfWeek > currentDayOfWeek) {
					Log.d("AlarmCompare", "Compare day" + alarmDayOfWeek + ">"
							+ currentDayOfWeek + " result true");
					return a.getString();
				} else if (alarmDayOfWeek == currentDayOfWeek) {
					Log.d("AlarmCompare", "Compare day" + alarmDayOfWeek + "=="
							+ currentDayOfWeek + " result true");
					if (alarmHour > currentHour) {
						Log.d("AlarmCompare", "Compare hour" + alarmHour + ">"
								+ currentHour + " result true");
						return a.getString();
					} else if (alarmHour == currentHour) {
						Log.d("AlarmCompare", "Compare hour" + alarmHour + "=="
								+ currentHour + " result true");
						if (alarmMinute > currentMinute) {
							Log.d("AlarmCompare", "Compare minute" + alarmMinute + ">"
									+ currentMinute + " result true");
							return a.getString();
						}
					}
				}
			}

		}
		return nextAlarmTime;
	}

}
