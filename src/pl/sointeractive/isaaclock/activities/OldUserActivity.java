package pl.sointeractive.isaaclock.activities;

import pl.sointeractive.isaaclock.R;
import pl.sointeractive.isaaclock.fragments.AchievementsFragment;
import pl.sointeractive.isaaclock.fragments.AlarmsFragment;
import pl.sointeractive.isaaclock.fragments.LeaderboardFragment;
import pl.sointeractive.isaaclock.fragments.NotificationsFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;

public class OldUserActivity extends SherlockActivity {
	private Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_old_user);
		setContext(this);

		// ActionBar
		ActionBar actionbar = getSupportActionBar();
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// alarms tab and fragment
		ActionBar.Tab alarmsTab = actionbar.newTab().setText(R.string.activity_user_tab_name_alarms);
		Fragment alarmsFragment = new AlarmsFragment();
		alarmsTab.setTabListener(new MyTabListener(alarmsFragment));
		actionbar.addTab(alarmsTab);

		// achievements tab and fragment
		ActionBar.Tab achievementsTab = actionbar.newTab().setText(R.string.activity_user_tab_name_achievements);
		Fragment achievementsFragment = new AchievementsFragment();
		achievementsTab.setTabListener(new MyTabListener(achievementsFragment));
		actionbar.addTab(achievementsTab);

		// leaderboard tab and fragment
		ActionBar.Tab leaderboardTab = actionbar.newTab().setText(R.string.activity_user_tab_name_leaderboard);
		Fragment leaderboardFragment = new LeaderboardFragment();
		leaderboardTab.setTabListener(new MyTabListener(leaderboardFragment));
		actionbar.addTab(leaderboardTab);

		// notifications tab and fragment
		ActionBar.Tab notificationsTab = actionbar.newTab().setText(R.string.activity_user_tab_name_notifications);
		Fragment notificationsFragment = new NotificationsFragment();
		notificationsTab.setTabListener(new MyTabListener(notificationsFragment));
		actionbar.addTab(notificationsTab);
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	class MyTabListener implements ActionBar.TabListener {
		public Fragment fragment;

		public MyTabListener(Fragment fragment) {
			this.fragment = fragment;
		}

		@Override
		public void onTabSelected(com.actionbarsherlock.app.ActionBar.Tab tab,
				android.support.v4.app.FragmentTransaction ft) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTabUnselected(
				com.actionbarsherlock.app.ActionBar.Tab tab,
				android.support.v4.app.FragmentTransaction ft) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTabReselected(
				com.actionbarsherlock.app.ActionBar.Tab tab,
				android.support.v4.app.FragmentTransaction ft) {
			// TODO Auto-generated method stub
			
		}

		

	}

}
