package pl.sointeractive.isaaclock.data;

public class Alarm {
	private String day;
	private String time;
	private boolean active;
	
	public Alarm(String day, String time, boolean active){
		this.day = day;
		this.time = time;
		this.active = active;
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
}
