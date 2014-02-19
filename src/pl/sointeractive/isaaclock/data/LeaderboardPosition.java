package pl.sointeractive.isaaclock.data;

public class LeaderboardPosition {

	private int leaderboardId, position, userId, userScore;
	private boolean isUserPosition = false;;
	
	public LeaderboardPosition(int leaderboardId, int position, int userId, int userScore){
		this.leaderboardId = leaderboardId;
		this.position = position;
		this.userId = userId;
		this.userScore = userScore;
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
}
