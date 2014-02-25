package pl.sointeractive.isaaclock.activities;

import pl.sointeractive.isaaclock.R;
import pl.sointeractive.isaaclock.data.App;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity {

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		
		CheckBoxPreference checkbox = (CheckBoxPreference) findPreference("pref24hTime");
		checkbox.setChecked(App.loadUserData().isUsing24HourTime());
		
	}
}
