package pl.sointeractive.isaaclock.activities;

import pl.sointeractive.isaaclock.R;
import pl.sointeractive.isaaclock.data.App;
import pl.sointeractive.isaaclock.data.LoginData;
import android.app.Activity;
import android.app.AlertDialog;
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

public class LoginActivity extends Activity {

	Button buttonLogin, buttonNewUser, buttonExit;
	EditText textName, textPassword;
	Context context;
	LoginData loginData;
	CheckBox checkbox;
	AlertDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		context = this;

		buttonExit = (Button) findViewById(R.id.button_exit);
		buttonNewUser = (Button) findViewById(R.id.button_new_user);
		buttonLogin = (Button) findViewById(R.id.button_login);
		textName = (EditText) findViewById(R.id.text_edit_email);
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
	
	private class LoginTask extends AsyncTask<Object, Object, Object>{
		
		@Override
		protected void onPreExecute () {
			Log.d("LoginTask", "onPreExecute()");
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setView(getLayoutInflater().inflate(R.layout.dialog_progress,
					null));
			builder.setCancelable(false);
			dialog = builder.create();
			dialog.show();
		}

		@Override
		protected Object doInBackground(Object... params) {
			Log.d("LoginTask", "doInBackground()");
			//connect here
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Intent intent = new Intent(context, UserActivityTabs.class);
			startActivity(intent);
			
			return null;
		}
		
		@Override
		protected void onPostExecute (Object result) {
			Log.d("LoginTask", "onPostExecute()");
			dialog.dismiss();
		}
		
	}

}
