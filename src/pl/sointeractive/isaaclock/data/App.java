package pl.sointeractive.isaaclock.data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.app.Application;
import android.content.Context;

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
}
