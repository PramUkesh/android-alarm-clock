package pl.sointeractive.isaaclock.data;

import org.json.JSONException;
import org.json.JSONObject;

public class Achievement {

	private String label, desc, imageUrl;
	private boolean isGained;

	public Achievement(String label, String desc, boolean isGained) {
		this.setLabel(label);
		this.setDesc(desc);
		this.setGained(isGained);
	}
	
	public Achievement(JSONObject json, boolean isGained) throws JSONException{
		this.setLabel(json.getString("label"));
		this.setDesc(json.getString("description"));
		this.setGained(isGained);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
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
		return "Achievement: " + label + " " + desc + " " + isGained;
	}
}
