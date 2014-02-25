package pl.sointeractive.isaaclock.activities;

import pl.sointeractive.isaaclock.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity {

	Button buttonRegister;
	EditText textEmail, textPassword, textPasswordRepeat;
	Context context;
	AlertDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		context = this;

		textEmail = (EditText) findViewById(R.id.text_edit_email);
		textPassword = (EditText) findViewById(R.id.text_edit_password);
		textPasswordRepeat = (EditText) findViewById(R.id.text_edit_password_repeat);

		buttonRegister = (Button) findViewById(R.id.button_register);
		buttonRegister.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String pw = textPassword.getEditableText().toString();
				String pw2 = textPasswordRepeat.getEditableText().toString();
				String email = textEmail.getEditableText().toString();
				if(pw.length()>0 && pw2.length()>0 && email.length()>0){
					if (pw.compareTo(pw2)==0) {
						new RegisterTask().execute();
					} else {
						resetPasswordFields();
						Toast.makeText(context, R.string.activity_register_passwords_dont_match, Toast.LENGTH_LONG).show();
					}
				} else {
					resetPasswordFields();
					Toast.makeText(context, R.string.activity_register_empty_fields, Toast.LENGTH_LONG).show();
				}
				
			}
		});

	}

	@Override
	public void onBackPressed() {
		finish();
	}
	
	private void resetPasswordFields(){
		textPassword.getEditableText().clear();
		textPasswordRepeat.getEditableText().clear();
	}

	private class RegisterTask extends AsyncTask<Object, Object, Object> {

		@Override
		protected void onPreExecute() {
			Log.d("RegisterTask", "onPreExecute()");
			dialog = ProgressDialog.show(context, "Registering account", "Please wait");
		}

		@Override
		protected Object doInBackground(Object... params) {
			Log.d("RegisterTask", "doInBackground()");
			// connect here
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
		protected void onPostExecute(Object result) {
			Log.d("RegisterTask", "onPostExecute()");
			dialog.dismiss();
		}

	}

}
