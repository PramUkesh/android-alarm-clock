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
	EditText textName, textPassword;
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
				Intent intent = new Intent(context, UserActivity.class);
				startActivity(intent);
			}
		});

	}

}
