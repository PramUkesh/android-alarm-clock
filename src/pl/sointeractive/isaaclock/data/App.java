package pl.sointeractive.isaaclock.data;

import pl.sointeractive.isaacloud.Isaacloud;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * This class represents the running application. It is used mainly for static
 * references to various general-use methods (file management and API
 * communication).
 * 
 * @author Mateusz Renes
 * 
 */
public class App extends Application {

	private static FileManager fileManager;
	private static App obj;
	private static Isaacloud connector;

	@Override
	public void onCreate() {
		super.onCreate();
		obj = this;
		fileManager = new FileManager();

	}

	public static void saveUserData(UserData userData) {
		fileManager.saveUserData(userData, obj);
	}

	public static UserData loadUserData() {
		return fileManager.loadUserData(obj);
	}

	public static void saveLoginData(LoginData data) {
		fileManager.saveLoginData(data, obj);
	}

	public static LoginData loadLoginData() {
		return fileManager.loadLoginData(obj);
	}

	public static App getInstance() {
		return obj;
	}

	public static void resetUserData() {
		fileManager.resetUserData(obj);
	}

	public static Isaacloud getConnector() {
		return connector;
	}

	public static void setConnector(Isaacloud connector) {
		App.connector = connector;
	}
	
	public static boolean isOnline() {
	    ConnectivityManager cm =
	        (ConnectivityManager) obj.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}

}
