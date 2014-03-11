package pl.sointeractive.isaaclock.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Data store class for leaderboard items (positions).
 * @author Mateusz Renes
 *
 */
public class LeaderboardPosition {

	private int leaderboardId, position, userId, userScore;
	private String userName;
	private boolean isUserPosition = false;;
	
	public LeaderboardPosition(int leaderboardId, int position, int userId, int userScore){
		this.leaderboardId = leaderboardId;
		this.position = position;
		this.userId = userId;
		this.userScore = userScore;
	}
	
	public LeaderboardPosition(JSONObject json, int userId) throws JSONException{
		this.userId = json.getInt("id");
		String firstName = json.getString("firstName");
		String lastName = json.getString("lastName");
		this.setUserName(firstName + " " + lastName);
		
		userScore=0;
		JSONArray data = json.getJSONArray("gainedAchievements");
		for(int i=0; i<data.length(); i++){
			userScore += data.getJSONObject(i).getInt("amount");
		}
		
		/*
		JSONObject data = json.getJSONArray("leaderboards").getJSONObject(0);
		this.userScore = data.getInt("score");
		this.position = data.getInt("index");
		*/
		if(this.userId == userId){
			UserData userData = App.loadUserData();
			userData.setLastScore(userScore);
			App.saveUserData(userData);
			this.isUserPosition = true;
			
		}
	}

	public int getLeaderboardId() {
		return leaderboardId;
	}

	public void setLeaderboardId(int leaderboardId) {
		this.leaderboardId = leaderboardId;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getUserScore() {
		return userScore;
	}

	public void setUserScore(int userScore) {
		this.userScore = userScore;
	}

	public boolean isUserPosition() {
		return isUserPosition;
	}

	public LeaderboardPosition setIsUserPosition(boolean isUserPosition) {
		this.isUserPosition = isUserPosition;
		return this;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
