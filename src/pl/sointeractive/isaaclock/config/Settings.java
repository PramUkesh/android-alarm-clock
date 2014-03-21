package pl.sointeractive.isaaclock.config;

/**
 * This is a class used for storing app settings. Some of the values here will
 * not be used in the final version of the app.
 * 
 * @author Mateusz Renes
 * 
 */
public class Settings {

	// connection
	public static final String gamificationId = "12";
	public static final String appId = "29";
	public static final String appSecret = "be3af94692dd29ecbde034e160c932d1";

	// alarm settings
	public static final int snoozeTimeInMinutes = 1;

	// leaderboard settings
	public static final int leaderboardId = 6;

	// urls
	public static final String oauthUrl = "https://oauth.isaacloud.com";
	public static final String baseUrl = "https://api.isaacloud.com";
	public static final String loginUrl = "https://oauthdev.isaacloud.com/login";
	public static final String version = "/v1";
	
}
