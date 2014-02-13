package pl.sointeractive.isaaclock.data;

import java.io.Serializable;

public class Alarm implements Serializable{
	
	private static final long serialVersionUID = 3779821744568481486L;
	private String day;
	private String time;
	private boolean active;
	
	public Alarm(String day, String time, boolean active){
		setDay(day);
		setTime(time);
		setActive(active);
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	public String print(){
		return "Alarm: "+day+" "+time + " " + active;
	}
}
