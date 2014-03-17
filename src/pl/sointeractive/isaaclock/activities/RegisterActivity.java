package pl.sointeractive.isaaclock.activities;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import pl.sointeractive.isaaclock.R;
import pl.sointeractive.isaaclock.data.App;
import pl.sointeractive.isaaclock.data.UserData;
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
import android.widget.EditText;
import android.widget.Toast;

/**
 * This Activity is started when the user wants to create a new account. User
 * data is loaded from the visible fields and an appropriate request is sent to
 * the API.
 * 
 * @author Mateusz Renes
 * 
 */
public class RegisterActivity extends Activity {

	private static final String TAG = "RegisterActivity";
	
	private Button buttonRegister;
	private EditText textEmail, textPassword, textPasswordRepeat,
			textFirstName, textLastName;
	private Context context;
	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// generate basic view
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		context = this;
		// find views
		textEmail = (EditText) findViewById(R.id.text_edit_email);
		textPassword = (EditText) findViewById(R.id.text_edit_password);
		textPasswordRepeat = (EditText) findViewById(R.id.text_edit_password_repeat);
		textFirstName = (EditText) findViewById(R.id.text_edit_first_name);
		textLastName = (EditText) findViewById(R.id.text_edit_last_name);
		buttonRegister = (Button) findViewById(R.id.button_register);
		// set button listener
		setButtonListeners();
	}

	private void setButtonListeners() {
		buttonRegister.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String pw = textPassword.getEditableText().toString();
				String pw2 = textPasswordRepeat.getEditableText().toString();
				String email = textEmail.getEditableText().toString();
				String firstName = textFirstName.getEditableText().toString();
				String lastName = textLastName.getEditableText().toString();
				if (email.length() > 0 && firstName.length() > 0
						&& lastName.length() > 0) {
					if (pw.equals(pw2)) {
						if (pw.matches("^((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%\\.]).{6,15})$")) {
							new RegisterTask().execute();
						} else {
							Toast.makeText(context, R.string.error_password,
									Toast.LENGTH_LONG).show();
						}
					} else {
						resetPasswordFields();
						Toast.makeText(
								context,
								R.string.activity_register_passwords_dont_match,
								Toast.LENGTH_LONG).show();
					}
				} else {
					resetPasswordFields();
					Toast.makeText(context,
							R.string.activity_register_empty_fields,
							Toast.LENGTH_LONG).show();
				}

			}
		});
	}

	@Override
	public void onBackPressed() {
		finish();
	}

	private void resetPasswordFields() {
		textPassword.getEditableText().clear();
		textPasswordRepeat.getEditableText().clear();
	}

	/**
	 * This AsyncTask is used to communicate with the API. It posts a request to
	 * create a new user and checks for errors. After a successful registration
	 * a new UserActivity is started.
	 * 
	 * @author Mateusz Renes
	 * 
	 */
	private class RegisterTask extends AsyncTask<Object, Object, Object> {

		boolean success = false;

		@Override
		protected void onPreExecute() {
			Log.d(TAG, "onPreExecute()");
			// lock screen orientation
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
			// show progress dialog
			dialog = ProgressDialog.show(context, "Registering account",
					"Please wait");
		}

		@Override
		protected Object doInBackground(Object... params) {
			Log.d(TAG, "doInBackground()");
			JSONObject jsonBody = new JSONObject();
			// generate json
			try {
				jsonBody.put("email", textEmail.getEditableText().toString());
				jsonBody.put("password", textPassword.getEditableText()
						.toString());
				jsonBody.put("firstName", textFirstName.getEditableText()
						.toString());
				jsonBody.put("lastName", textLastName.getEditableText()
						.toString());
				jsonBody.put("status", 1);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			UserData userData = App.loadUserData();
			HttpResponse response = null;
			// send request and retrieve response
			try {
				response = App.getWrapper().postAdminUser(jsonBody, null);
				JSONObject json = response.getJSONObject();
				userData.setUserId(json.getInt("id"));
				userData.setName(json.getString("firstName") + " "
						+ json.getString("lastName"));
				userData.setEmail(json.getString("email"));
				App.saveUserData(userData);
				success = true;
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			Log.d(TAG, "onPostExecute()");
			dialog.dismiss();
			if (success) {
				Intent intent = new Intent(context, UserActivity.class);
				startActivity(intent);
			} else {
				Toast.makeText(context, R.string.error_register,
						Toast.LENGTH_LONG).show();
			}
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
			finish();
		}

	}

}
