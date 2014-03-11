package pl.sointeractive.isaaclock.data;

import pl.sointeractive.isaacloud.FakeWrapper;
import android.app.Application;

/**
 * This class represents the running application. It is used mainly for static
 * references to various general-use methods.
 * 
 * @author Mateusz Renes
 * 
 */
public class App extends Application {

	private static FileManager fileManager;
	private static App obj;
	private static FakeWrapper wrapper;

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

	public static FakeWrapper getWrapper() {
		return wrapper;
	}

	public static void setWrapper(FakeWrapper wrapper) {
		App.wrapper = wrapper;
	}

}
