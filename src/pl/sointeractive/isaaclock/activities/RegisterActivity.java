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

public class RegisterActivity  extends Activity {

	Button buttonRegister;
	EditText textName, textPassword, textPasswordRepeat;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		context = this;

		buttonRegister = (Button) findViewById(R.id.button_register);
		buttonRegister.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				register();
			}
		});

	}
	
	private void register(){
		startUserActivity();
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
