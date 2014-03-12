package pl.sointeractive.isaaclock.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

import android.util.Log;
import pl.sointeractive.isaaclock.R;

/**
 * Data store class for user information. Apart from collecting user data, it
 * enables some parts of alarms management.
 * 
 * @author Mateusz Renes
 * 
 */
public class UserData implements Serializable {

	private static final long serialVersionUID = 7298108357152407887L;

	private ArrayList<Alarm> alarmList;
	private String name, email;
	private boolean hasNewNotifications;
	private boolean use24HourTime;
	private int userId;
	private Notification lastNotification;
	private String lastScore;

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
		this.lastScore = (App.getInstance().getString(R.string.score_no_data));
		setHasNewNotifications(false);
		setUse24HourTime(false);
	}

	/**
	 * Used for changing alarm activation status without changing its time.
	 * 
	 * @param dayIndex
	 * @param active
	 */
	public void setAlarm(int dayIndex, boolean active) {
		int hour = alarmList.get(dayIndex).getHour();
		int minutes = alarmList.get(dayIndex).getMinutes();
		alarmList.set(dayIndex, new Alarm(dayArray[dayIndex], hour, minutes,
				active));
	}

	/**
	 * Used for changing the alarms time and activation status.
	 * 
	 * @param dayIndex
	 * @param hour
	 * @param minutes
	 * @param active
	 */
	public void setAlarm(int dayIndex, int hour, int minutes, boolean active) {
		alarmList.set(dayIndex, new Alarm(dayArray[dayIndex], hour, minutes,
				active));
	}

	/**
	 * Returns readable info on the alarms.
	 * 
	 * @return
	 */
	public String printAlarms() {
		String result = "Alarms: \n";
		for (int i = 0; i < 7; i++) {
			result += alarmList.get(i).print() + "\n";
		}
		return result;
	}

	/**
	 * Returns a String representation of the next active alarm. If there is no
	 * active alarm detected, the returned String will show this.
	 * 
	 * @return
	 */
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

	/**
	 * Returns AlarmInfo of the next active alarm. If there is no active alarm
	 * detected, the AlarmInfo will have its ACTIVE variable set to false.
	 * 
	 * @return
	 */
	public AlarmInfo getNextAlarmInfo() {
		AlarmInfo nextAlarmInfo = new AlarmInfo();
		Calendar c = Calendar.getInstance();
		// c.setFirstDayOfWeek(Calendar.MONDAY);
		Alarm firstActiveAlarm = null;

		int currentDayOfWeek = c.get(Calendar.DAY_OF_WEEK);
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
		nextAlarmInfo.print();
		return nextAlarmInfo;
	}

	/**
	 * Returns the number of days in between the weekdays passed as arguments.
	 * The day1 and day2 variables must be integers between 1 and 7. The result
	 * is the day difference between these two values. For instance if day1 has
	 * value 6 (Friday) and day2 has value 3 (Tuesday), the returned value will
	 * be 4 (since there is a 4 day difference between current Friday and next
	 * weeks Tuesday).
	 * 
	 * @param day1
	 * @param day2
	 * @return
	 */
	private int getDaysBetween(int day1, int day2) {
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

	/**
	 * This is a helper class for generating and storing alarm information. Used
	 * only for setting alarms in the AlarmActivity and AlarmFragment.
	 * 
	 * @author Mateusz Renes
	 * 
	 */
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

	public void resetData() {
		alarmList.get(0).setActive(false);
		alarmList.get(1).setActive(false);
		alarmList.get(2).setActive(false);
		alarmList.get(3).setActive(false);
		alarmList.get(4).setActive(false);
		alarmList.get(5).setActive(false);
		alarmList.get(6).setActive(false);
		this.lastScore = App.getInstance().getString(R.string.score_no_data);
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

	boolean isActive(int dayIndex) {
		return alarmList.get(dayIndex).isActive();
	}

	public ArrayList<Alarm> getAlarms() {
		return alarmList;
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

	public void setUse24HourTime(boolean use24HourTime) {
		this.use24HourTime = use24HourTime;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Notification getLastNotification() {
		return lastNotification;
	}

	public void setLastNotification(Notification lastNotification) {
		this.lastNotification = lastNotification;
	}

	public String getLastScore() {
		return lastScore;
	}

	public void setLastScore(int score) {
		this.lastScore = "" + score;
	}

	public void resetLastScore() {
		this.lastScore = App.getInstance().getString(R.string.score_no_data);
	}

}
