package pl.sointeractive.isaaclock.data;

public class Achievement {

	private String name, desc, imageUrl;
	private boolean isGained;

	public Achievement(String name, String desc, boolean isGained) {
		this.setName(name);
		this.setDesc(desc);
		this.setGained(isGained);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public boolean isGained() {
		return isGained;
	}

	public void setGained(boolean isGained) {
		this.isGained = isGained;
	}

	public String print() {
		return "Achievement: " + name + " " + desc + " " + isGained;
	}
}
