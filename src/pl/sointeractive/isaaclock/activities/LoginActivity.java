package pl.sointeractive.isaaclock.activities;

import java.util.HashMap;
import java.util.Map;

import pl.sointeractive.isaaclock.R;
import pl.sointeractive.isaaclock.config.Settings;
import pl.sointeractive.isaaclock.data.App;
import pl.sointeractive.isaaclock.data.LoginData;
import pl.sointeractive.isaaclock.data.UserData;
import pl.sointeractive.isaacloud.FakeWrapper;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	Button buttonLogin, buttonNewUser, buttonExit;
	EditText textEmail, textPassword;
	Context context;
	LoginData loginData;
	CheckBox checkbox;
	ProgressDialog dialog;
	UserData userData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		context = this;
		
		initializeWrapper();

		buttonExit = (Button) findViewById(R.id.button_exit);
		buttonNewUser = (Button) findViewById(R.id.button_new_user);
		buttonLogin = (Button) findViewById(R.id.button_login);
		textEmail = (EditText) findViewById(R.id.text_edit_email);
		textPassword = (EditText) findViewById(R.id.text_edit_password);
		checkbox = (CheckBox) findViewById(R.id.activity_login_checkbox);
		
		loginData = App.loadLoginData();
		
		if(loginData.isRemembered()){
			checkbox.setChecked(true);
			textEmail.setText(loginData.getEmail());
			textPassword.setText(loginData.getPassword());
		}
		
		buttonLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(checkbox.isChecked()){
					loginData.setRemembered(true);
					loginData.setEmail(textEmail.getEditableText().toString());
					loginData.setPassword(textPassword.getEditableText().toString());
					App.saveLoginData(loginData);
				} else {
					loginData.setRemembered(false);
					loginData.setEmail("");
					loginData.setPassword("");
					App.saveLoginData(loginData);
				}
				userData = App.loadUserData();
				new LoginTask().execute();
			}
		});
		
		buttonNewUser.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				newUser();
			}
		});
		
		buttonExit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				exit();
			}
		});
	}
	
	private void newUser(){
		Intent intent = new Intent(context, RegisterActivity.class);
		startActivity(intent);
	}
	
	private void exit(){
		finish();
	}
	
	@Override
	public void onBackPressed() {
	   finish();
	}
	
	public void initializeWrapper(){
		Map<String, String> config = new HashMap<String, String>();
		config.put("clientId", Settings.memberId);
		config.put("secret", Settings.appSecret);
		App.setWrapper(new FakeWrapper(App.getInstance().getApplicationContext(),
				"https://api.isaacloud.com", "https://oauth.isaacloud.com",
				"/v1", config));
	}
	
	private class LoginTask extends AsyncTask<Object, Object, Object>{
		
		boolean success = false;
		
		@Override
		protected void onPreExecute () {
			Log.d("LoginTask", "onPreExecute()");
			dialog = ProgressDialog.show(context, "Logging in", "Please wait");
		}

		@Override
		protected Object doInBackground(Object... params) {
			Log.d("LoginTask", "doInBackground()");
			//connect here
			/*
			try {
				success = App.getWrapper().tryLogin();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//we should download these values - THIS IS A TEMPORARY SOLUTION until we create a proper login flow
			String firstName = "John";
			String lastName = "Doe";
			String email = "jdoe2@isaacloud.com";
			int userId = 2;
			
			// send loaded data to App.UserData
			userData.setName(firstName + " " + lastName);
			userData.setEmail(email);
			userData.setUserId(userId);
			App.saveUserData(userData);
			
			success = true;
			
			return null;
		}
		
		@Override
		protected void onPostExecute (Object result) {
			Log.d("LoginTask", "onPostExecute()");
			dialog.dismiss();
			if(success){
				Intent intent = new Intent(context, UserActivityTabs.class);
				startActivity(intent);
			} else {
				Toast.makeText(context, R.string.error_login, Toast.LENGTH_LONG).show();
			}
		}
		
	}

}
