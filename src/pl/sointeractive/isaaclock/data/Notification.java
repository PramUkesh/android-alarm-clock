package pl.sointeractive.isaaclock.data;

public class Notification {

	private String data, title, message;
	
	public Notification(String data, String title, String message){
		this.data = data;
		this.title = title;
		this.message = message;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	
}
