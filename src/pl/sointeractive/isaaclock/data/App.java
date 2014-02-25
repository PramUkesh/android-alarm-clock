package pl.sointeractive.isaaclock.data;

import java.util.HashMap;
import java.util.Map;

import pl.sointeractive.isaacloud.FakeWrapper;
import android.app.Application;

public class App extends Application {

	private static FileManager fileManager;
	private static App obj;
	private static FakeWrapper wrapper;
	
	@Override
    public void onCreate() {
        super.onCreate();
        obj = this;
        fileManager = new FileManager();
        
        Map<String, String> config =  new HashMap<String, String>();
        config.put("clientId", "12");
		config.put("secret", "be3af94692dd29ecbde034e160c932d1");
        wrapper = new FakeWrapper("https://api.isaacloud.com","https://oauth.isaacloud.com","v1",config);
    }

	public static void saveUserData(UserData data) {
		fileManager.saveUserData(data, obj);
	}
	
	public static UserData loadUserData(){
		return fileManager.loadUserData(obj);
	}
	
	public static void saveLoginData(LoginData data) {
		fileManager.saveLoginData(data, obj);
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

	public static FakeWrapper getWrapper() {
		return wrapper;
	}

	public static void setWrapper(FakeWrapper wrapper) {
		App.wrapper = wrapper;
	}
	
}
