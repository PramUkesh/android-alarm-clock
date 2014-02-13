package pl.sointeractive.isaaclock.activities;

import pl.sointeractive.isaaclock.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {

	Button buttonLogin;
	Button buttonNewUser;
	EditText textEmail, textPassword;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		context = this;

		buttonLogin = (Button) findViewById(R.id.button_login);
		buttonLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				login();
			}
		});
		
		buttonNewUser = (Button) findViewById(R.id.button_new_user);
		buttonNewUser.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				newUser();
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
	
	private void startUserActivity(){
		Intent intent = new Intent(context, UserActivityTabs.class);
		startActivity(intent);
	}
	
	@Override
	public void onBackPressed() {
	   finish();
	}

}
