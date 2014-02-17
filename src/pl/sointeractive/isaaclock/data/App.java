package pl.sointeractive.isaaclock.data;

import android.app.Application;

public class App extends Application {

	private static FileManager fileManager;
	private static App obj;
	
	@Override
    public void onCreate() {
        super.onCreate();
        obj = this;
        fileManager = new FileManager();
    }

	public static void saveUserData(UserData data) {
		fileManager.saveUserData(data, obj);
	}
	
	public static UserData loadUserData(){
		return fileManager.loadUserData(obj);
	}
	
	public void saveLoginData(LoginData data) {
		fileManager.saveLoginData(data, this);
	}
	
	public static LoginData loadLoginData(){
		return fileManager.loadLoginData(obj);
	}
	
	public static App getInstance(){
		return obj;
	}
	
	public static void resetUserData(){
		fileManager.resetUserData(obj);
	}
}
