package pl.sointeractive.isaaclock.activities;

import pl.sointeractive.isaaclock.R;
import pl.sointeractive.isaaclock.data.App;
import pl.sointeractive.isaaclock.data.LoginData;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class LoginActivity extends Activity {

	Button buttonLogin, buttonNewUser, buttonExit;
	EditText textName, textPassword;
	Context context;
	LoginData loginData;
	CheckBox checkbox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		context = this;

		buttonExit = (Button) findViewById(R.id.button_exit);
		buttonNewUser = (Button) findViewById(R.id.button_new_user);
		buttonLogin = (Button) findViewById(R.id.button_login);
		textName = (EditText) findViewById(R.id.text_edit_name);
		checkbox = (CheckBox) findViewById(R.id.activity_login_checkbox);
		
		loginData = App.loadLoginData();
		
		if(loginData.isRemembered()){
			checkbox.setChecked(true);
			textName.setText(loginData.getEmail());
		}
		
		buttonLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(checkbox.isChecked()){
					loginData.setRemembered(true);
					loginData.setEmail(textName.getEditableText().toString());
					App.saveLoginData(loginData);
				} else {
					loginData.setRemembered(false);
					App.saveLoginData(loginData);
				}
				login();
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
	
	private void login(){
		startUserActivity();
	}
	
	private void newUser(){
		Intent intent = new Intent(context, RegisterActivity.class);
		startActivity(intent);
	}
	
	private void exit(){
		finish();
	}
	
	private void startUserActivity(){
		Intent intent = new Intent(context, UserActivityTabs.class);
		startActivity(intent);
	}
	
	@Override
	public void onBackPressed() {
	   finish();
	}

}
