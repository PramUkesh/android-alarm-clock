package pl.sointeractive.isaaclock.activities;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pl.sointeractive.isaaclock.R;
import pl.sointeractive.isaaclock.config.Settings;
import pl.sointeractive.isaaclock.data.App;
import pl.sointeractive.isaaclock.data.LoginData;
import pl.sointeractive.isaaclock.data.UserData;
import pl.sointeractive.isaacloud.FakeWrapper;
import pl.sointeractive.isaacloud.connection.HttpResponse;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

/**
 * This is the first Activity started by the application. It allows the user to
 * log into their IsaaClock account. Bear in mind that the login flow will
 * change in the near future.
 * 
 * @author Mateusz Renes
 * 
 */
public class LoginActivity extends Activity {
	
	private static final String TAG = "LoginActivity";

	private Button buttonLogin, buttonNewUser, buttonExit;
	private EditText textEmail, textPassword;
	private Context context;
	private LoginData loginData;
	private CheckBox checkbox;
	private ProgressDialog dialog;
	private UserData userData;

	/**
	 * Method called on activity creation.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		context = this;
		// create a new wrapper instance for API connection
		initializeWrapper();
		// find relevant views
		buttonExit = (Button) findViewById(R.id.button_exit);
		buttonNewUser = (Button) findViewById(R.id.button_new_user);
		buttonLogin = (Button) findViewById(R.id.button_login);
		textEmail = (EditText) findViewById(R.id.text_edit_email);
		textPassword = (EditText) findViewById(R.id.text_edit_password);
		checkbox = (CheckBox) findViewById(R.id.activity_login_checkbox);
		// load login data if available
		loginData = App.loadLoginData();
		if (loginData.isRemembered()) {
			checkbox.setChecked(true);
			textEmail.setText(loginData.getEmail());
			textPassword.setText(loginData.getPassword());
		}
		// set button listeners
		setButtonListeners();
	}

	public void setButtonListeners() {
		buttonLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (checkbox.isChecked()) {
					loginData.setRemembered(true);
					loginData.setEmail(textEmail.getEditableText().toString());
					loginData.setPassword(textPassword.getEditableText()
							.toString());
					App.saveLoginData(loginData);
				} else {
					loginData.setRemembered(false);
					loginData.setEmail("");
					loginData.setPassword("");
					App.saveLoginData(loginData);
				}
				if (textEmail.getEditableText().toString().equals("")
						|| textPassword.getEditableText().toString().equals("")) {
					Toast.makeText(context, R.string.error_empty,
							Toast.LENGTH_LONG).show();
				} else {
					userData = App.loadUserData();
					new LoginTask().execute();
				}
				// testWebView();
			}
		});
		buttonNewUser.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				registerNewUser();
			}
		});
		buttonExit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				exit();
			}
		});
	}

	/**
	 * Start RegisterActivity for new account creation.
	 */
	private void registerNewUser() {
		Intent intent = new Intent(context, RegisterActivity.class);
		startActivity(intent);
	}

	private void exit() {
		finish();
	}

	@Override
	public void onBackPressed() {
		finish();
	}

	public void testWebView() {
		Intent intent = new Intent(context, WebViewActivity.class);
		startActivity(intent);
	}

	/**
	 * Create new wrapper instance for communicating with IsaaClock API.
	 */
	public void initializeWrapper() {
		Map<String, String> config = new HashMap<String, String>();
		config.put("memberId", Settings.memberId);
		config.put("appSecret", Settings.appSecret);
		App.setWrapper(new FakeWrapper(App.getInstance()
				.getApplicationContext(), Settings.baseUrl, Settings.oauthUrl,
				Settings.version, config));
	}

	/**
	 * AsyncTask used for logging in. A different login method will be used in
	 * the future release of the app.
	 * 
	 * @author Mateusz Renes
	 * 
	 */
	private class LoginTask extends AsyncTask<Object, Object, Object> {

		boolean success = false;

		@Override
		protected void onPreExecute() {
			Log.d(TAG, "onPreExecute()");
			// lock screen orientation and show progress dialog
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
			dialog = ProgressDialog.show(context, "Logging in", "Please wait");
		}

		@Override
		protected Object doInBackground(Object... params) {
			/*
			 * THIS IS A TEMPORARY SOLUTION. A proper login flow through a
			 * website will be enabled after testing
			 */
			Log.d(TAG, "doInBackground()");
			// connect here
			try {
				String email = textEmail.getEditableText().toString();
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("limit", 1000);
				HttpResponse response = App.getWrapper().getAdminUsers(param);
				Log.d(TAG, response.toString());
				JSONArray array = response.getJSONArray();
				for (int i = 0; i < array.length(); i++) {
					JSONObject json = (JSONObject) array.get(i);
					if (email.equals(json.get("email"))) {
						String userFirstName = json.getString("firstName");
						String userLastName = json.getString("lastName");
						String userEmail = json.getString("email");
						int userId = json.getInt("id");
						// send loaded data to App.UserData
						if (!userData.getEmail().equals(userEmail)) {
							userData.resetData();
						}
						userData.setName(userFirstName + " " + userLastName);
						userData.setEmail(userEmail);
						userData.setUserId(userId);
						App.saveUserData(userData);
						// report user found
						success = true;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			Log.d(TAG, "onPostExecute()");
			// dismiss progress dialog
			dialog.dismiss();
			// if the user was found, start new activity, if not, show error message
			if (success) {
				Intent intent = new Intent(context, UserActivity.class);
				startActivity(intent);
			} else {
				Toast.makeText(context, R.string.error_login, Toast.LENGTH_LONG)
						.show();
			}
			// unlock screen orientation
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
		}

	}

}
