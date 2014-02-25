package pl.sointeractive.isaaclock.data;

import java.io.Serializable;

public class Alarm implements Serializable {

	private static final long serialVersionUID = 3779821744568481486L;
	private String day;
	private int hour;
	private int minutes;
	private boolean active;

	public Alarm(String day, int hour, int minutes, boolean active) {
		setDay(day);
		this.setHour(hour);
		this.setMinutes(minutes);
		setActive(active);
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getTime() {
		boolean isUsing24HourTime = App.loadUserData().isUsing24HourTime();
		String time = "";
		if(isUsing24HourTime){
			if (hour < 10) {
				time += "0" + hour;
			} else {
				time += "" + hour;
			}
		} else {
			if (hour < 10) {
				time += "0" + hour;
			} else if (hour < 13){
				time += "" + hour;
			} else {
				time += "0" + (hour-12);
			}
		}
		time += ":";
		if(minutes<10){
			time += "0" + minutes;
		} else {
			time += "" + minutes;
		}
		if(!isUsing24HourTime){
			if(hour<12){
				time += " AM";
			} else {
				time += " PM";
			}
		}
		return time;
	}
	
	public int getDayOfWeekInt(){
		if (day.equals("Mon")) {
			return 1;
		} else if (day.equals("Tue")) {
			return 2;
		} else if (day.equals("Wed")) {
			return 3;
		} else if (day.equals("Thu")) {
			return 4;
		} else if (day.equals("Fri")) {
			return 5;
		} else if (day.equals("Sat")) {
			return 6;
		} else if (day.equals("Sun")) {
			return 7;
		} else {
			return 0;
		}
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String print() {
		return "Alarm: " + day + " " + getTime() + " " + active;
	}

	public String getString() {
		return day + " " + getTime();
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}
}
