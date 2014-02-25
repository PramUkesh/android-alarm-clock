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
	private boolean hasNewNotifications;
	private boolean use24HourTime;

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
		setHasNewNotifications(false);
		setUse24HourTime(false);
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

		int currentDayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
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
					return a.getString();
				} else if (alarmDayOfWeek == currentDayOfWeek) {
					if (alarmHour > currentHour) {
						return a.getString();
					} else if (alarmHour == currentHour) {
						if (alarmMinute > currentMinute) {
							return a.getString();
						}
					}
				}
			}

		}
		return nextAlarmTime;
	}

	public AlarmInfo getNextAlarmInfo() {
		AlarmInfo nextAlarmInfo = new AlarmInfo();
		Calendar c = Calendar.getInstance();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		Alarm firstActiveAlarm = null;

		int currentDayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		int currentHour = c.get(Calendar.HOUR_OF_DAY);
		int currentMinute = c.get(Calendar.MINUTE);

		for (Alarm a : alarmList) {
			if (a.isActive()) {

				int alarmDayOfWeek = a.getDayOfWeekInt();
				int alarmHour = a.getHour();
				int alarmMinute = a.getMinutes();

				if (firstActiveAlarm == null) {
					firstActiveAlarm = a;
					nextAlarmInfo = new AlarmInfo(alarmHour, alarmMinute,
							getDaysBetween(currentDayOfWeek, alarmDayOfWeek));
				}

				if (alarmDayOfWeek > currentDayOfWeek) {
					return new AlarmInfo(alarmHour, alarmMinute,
							getDaysBetween(currentDayOfWeek, alarmDayOfWeek));
				} else if (alarmDayOfWeek == currentDayOfWeek) {
					if (alarmHour > currentHour) {
						return new AlarmInfo(
								alarmHour,
								alarmMinute,
								getDaysBetween(currentDayOfWeek, alarmDayOfWeek));
					} else if (alarmHour == currentHour) {
						if (alarmMinute > currentMinute) {
							return new AlarmInfo(alarmHour, alarmMinute,
									getDaysBetween(currentDayOfWeek,
											alarmDayOfWeek));
						}
					}
				}
			}

		}
		return nextAlarmInfo;

	}

	public int getDaysBetween(int day1, int day2) {
		int counter = 0;
		while (true) {
			if (day1 == day2) {
				return counter;
			} else {
				day1++;
				if (day1 == 8) {
					day1 = 1;
				}
				counter++;
			}
		}
	}

	public boolean hasNewNotifications() {
		return hasNewNotifications;
	}

	public void setHasNewNotifications(boolean hasNewNotifications) {
		this.hasNewNotifications = hasNewNotifications;
	}

	public boolean isUsing24HourTime() {
		return use24HourTime;
	}

	public void setUse24HourTime(boolean use24HourTime) {
		this.use24HourTime = use24HourTime;
	}

	public class AlarmInfo {
		public boolean ACTIVE;
		public int HOUR;
		public int MINUTE;
		public int DAYS_FROM_NOW;

		public AlarmInfo() {
			this.ACTIVE = false;
		}

		public AlarmInfo(int hour, int minute, int days) {
			this.HOUR = hour;
			this.MINUTE = minute;
			this.DAYS_FROM_NOW = days;
			this.ACTIVE = true;
		}

		public boolean isShowingCurrentOrPastTime() {
			Calendar c = Calendar.getInstance();
			if (c.get(Calendar.HOUR_OF_DAY) >= HOUR
					&& c.get(Calendar.MINUTE) >= MINUTE && DAYS_FROM_NOW == 0) {
				return true;
			} else {
				return false;
			}
		}

		public void print() {
			Log.d("AlarmInfo", "Hour: " + HOUR + " Minute: " + MINUTE
					+ " DaysFromNow: " + DAYS_FROM_NOW);
		}
	}

}
