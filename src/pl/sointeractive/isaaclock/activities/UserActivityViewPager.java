package pl.sointeractive.isaaclock.activities;

import java.util.ArrayList;

import pl.sointeractive.isaaclock.R;
import pl.sointeractive.isaaclock.data.UserData;
import pl.sointeractive.isaaclock.fragments.AchievementsFragment;
import pl.sointeractive.isaaclock.fragments.AlarmsFragment;
import pl.sointeractive.isaaclock.fragments.GeneralFragment;
import pl.sointeractive.isaaclock.fragments.LeaderboardFragment;
import pl.sointeractive.isaaclock.fragments.NotificationsFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabWidget;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

/**
 * Demonstrates combining a TabHost with a ViewPager to implement a tab UI that,
 * switches between tabs and also allows the user to perform horizontal flicks
 * to move between the tabs.
 */

public class UserActivityViewPager extends SherlockFragmentActivity {
	private TabHost mTabHost;
	private ViewPager mViewPager;
	private TabsAdapter mTabsAdapter;
	
	private UserData userData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		userData = new UserData();

		setContentView(R.layout.activity_user_viewpager);
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();

		mViewPager = (ViewPager) findViewById(R.id.pager);

		mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);

		mTabsAdapter.addTab(
				mTabHost.newTabSpec("general").setIndicator(null,
						getResources().getDrawable(R.drawable.ic_menu_cc_am)),
				GeneralFragment.class, null);
		mTabsAdapter.addTab(
				mTabHost.newTabSpec("alarms").setIndicator(
						null,
						getResources().getDrawable(
								R.drawable.ic_menu_recent_history)),
				AlarmsFragment.class, null);
		mTabsAdapter.addTab(
				mTabHost.newTabSpec("achievements").setIndicator(null,
						getResources().getDrawable(R.drawable.ic_menu_star)),
				AchievementsFragment.class, null);
		mTabsAdapter.addTab(
				mTabHost.newTabSpec("leaderboard").setIndicator(
						null,
						getResources().getDrawable(
								R.drawable.ic_menu_friendslist)),
				LeaderboardFragment.class, null);
		mTabsAdapter.addTab(
				mTabHost.newTabSpec("notification").setIndicator(
						null,
						getResources().getDrawable(
								R.drawable.ic_menu_notifications)),
				NotificationsFragment.class, null);

		if (savedInstanceState != null) {
			// mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
			mTabHost.setCurrentTab(0);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(R.string.activity_user_menu_logout);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return true;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("tab", mTabHost.getCurrentTabTag());
	}
	
	@Override
	public void onBackPressed() {
	   finish();
	}

	public UserData getUserData() {
		return userData;
	}

	/**
	 * This is a helper class that implements the management of tabs and all
	 * details of connecting a ViewPager with associated TabHost. It relies on a
	 * trick. Normally a tab host has a simple API for supplying a View or
	 * Intent that each tab will show. This is not sufficient for switching
	 * between pages. So instead we make the content part of the tab host 0dp
	 * high (it is not shown) and the TabsAdapter supplies its own dummy view to
	 * show as the tab content. It listens to changes in tabs, and takes care of
	 * switch to the correct paged in the ViewPager whenever the selected tab
	 * changes.
	 */
	public static class TabsAdapter extends FragmentStatePagerAdapter implements
			TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
		private final Context mContext;
		private final TabHost mTabHost;
		private final ViewPager mViewPager;
		private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

		static final class TabInfo {
			private final String tag;
			private final Class<?> clss;
			private final Bundle args;

			TabInfo(String _tag, Class<?> _class, Bundle _args) {
				tag = _tag;
				clss = _class;
				args = _args;
			}

			public String getTag() {
				return tag;
			}
		}

		static class DummyTabFactory implements TabHost.TabContentFactory {
			private final Context mContext;

			public DummyTabFactory(Context context) {
				mContext = context;
			}

			@Override
			public View createTabContent(String tag) {
				View v = new View(mContext);
				v.setMinimumWidth(0);
				v.setMinimumHeight(0);
				return v;
			}
		}

		public TabsAdapter(FragmentActivity activity, TabHost tabHost,
				ViewPager pager) {
			super(activity.getSupportFragmentManager());
			mContext = activity;
			mTabHost = tabHost;
			mViewPager = pager;
			mTabHost.setOnTabChangedListener(this);
			mViewPager.setAdapter(this);
			mViewPager.setOnPageChangeListener(this);
		}

		public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
			tabSpec.setContent(new DummyTabFactory(mContext));
			String tag = tabSpec.getTag();

			TabInfo info = new TabInfo(tag, clss, args);
			mTabs.add(info);
			mTabHost.addTab(tabSpec);
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return mTabs.size();
		}

		@Override
		public Fragment getItem(int position) {
			TabInfo info = mTabs.get(position);
			return Fragment.instantiate(mContext, info.clss.getName(),
					info.args);
		}

		@Override
		public void onTabChanged(String tabId) {
			int position = mTabHost.getCurrentTab();
			mViewPager.setCurrentItem(position);
		}

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
		}

		@Override
		public void onPageSelected(int position) {
			// Unfortunately when TabHost changes the current tab, it kindly
			// also takes care of putting focus on it when not in touch mode.
			// The jerk.
			// This hack tries to prevent this from pulling focus out of our
			// ViewPager.
			TabWidget widget = mTabHost.getTabWidget();
			int oldFocusability = widget.getDescendantFocusability();
			widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
			mTabHost.setCurrentTab(position);
			widget.setDescendantFocusability(oldFocusability);
		}

		@Override
		public void onPageScrollStateChanged(int state) {
		}
	}
}
