package pl.sointeractive.isaaclock.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import android.content.Context;

/**
 * This is a helper class for saving and loading files from the application
 * folder.
 * 
 * @author Mateusz Renes
 * 
 */
public class FileManager {
	
	private static final String userDataFileName = "user_data.dat";
	private static final String loginDataFileName = "login_data.dat";

	public UserData loadUserData(App app) {
		UserData data = null;
		//Check if the file exists. If not, create a new one.
		File checkFile = new File(app.getFilesDir(), userDataFileName);
		if (!checkFile.exists()) {
			saveUserData(new UserData(), app);
		}
		//load the file
		try {
			FileInputStream fis = app.openFileInput(userDataFileName);
			ObjectInputStream ois = new ObjectInputStream(fis);
			data = (UserData) ois.readObject();
			ois.close();
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return data;
	}

	public void saveUserData(UserData data, App app) {
		//save the file
		try {
			FileOutputStream fos = app.openFileOutput(userDataFileName,
					Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(data);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public LoginData loadLoginData(App app) {
		LoginData data = null;
		//Check if the file exists. If not, create a new one.
		File checkFile = new File(app.getFilesDir(), loginDataFileName);
		if (!checkFile.exists()) {
			saveLoginData(new LoginData(), app);
		}
		//load the file
		try {
			FileInputStream fis = app.openFileInput(loginDataFileName);
			ObjectInputStream ois = new ObjectInputStream(fis);
			data = (LoginData) ois.readObject();
			ois.close();
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return data;
	}

	public void saveLoginData(LoginData data, App app) {
		//save the file
		try {
			FileOutputStream fos = app.openFileOutput(loginDataFileName,
					Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(data);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void resetUserData(App app) {
		saveUserData(new UserData(), app);
	}
}
