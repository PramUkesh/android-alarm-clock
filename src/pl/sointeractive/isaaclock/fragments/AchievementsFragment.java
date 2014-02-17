package pl.sointeractive.isaaclock.fragments;

import java.util.ArrayList;

import pl.sointeractive.isaaclock.R;
import pl.sointeractive.isaaclock.activities.UserActivityTabs;
import pl.sointeractive.isaaclock.activities.UserActivityTabs.TabManager;
import pl.sointeractive.isaaclock.data.Achievement;
import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListFragment;

public class AchievementsFragment extends SherlockListFragment {

	UserActivityTabs context;
	ArrayList<Achievement> array;
	boolean isLoaded = false;
	AchievementAdapter adapter;
	
	@Override
	  public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);
	    array = new ArrayList<Achievement>();
		adapter = new AchievementAdapter(array);
	    setListAdapter(adapter);
	  }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = (UserActivityTabs) getSherlockActivity();
		//array = new ArrayList<Achievement>();
		setListAdapter(adapter);
		//new GetAchievementsAsyncTask().execute();
		return inflater.inflate(R.layout.fragment_achievements, container,
				false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		Log.d("AchievemetnsFragment", "onViewCreated");
		if(!isLoaded){
			Log.d("AchievemetnsFragment", "onViewCreated");
			new GetAchievementsAsyncTask().execute();
			isLoaded = true;
		} else {
			
		}
	}

	public void showProgressBar() {
		Log.d("AchievemetnsFragment", "showProgressBar()");
		getView().findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
		getView().findViewById(R.id.list_layout).setVisibility(View.GONE);
	}

	public void hideProgressBar() {
		Log.d("AchievemetnsFragment", "hideProgressBar()");
		getView().findViewById(R.id.progress_bar).setVisibility(View.GONE);
		getView().findViewById(R.id.list_layout).setVisibility(View.VISIBLE);

	}

	public void refreshCurrentFragment() {
		Log.d("AlarmsFragment", "refreshCurrentFragment()");
		TabManager tm = context.getTabManager();
		TabHost th = context.getTabHost();
		tm.printTabInfo();
		tm.refreshTab(th.getCurrentTabTag());
	}

	public ArrayList<Achievement> getAchievementArray() {

		array = new ArrayList<Achievement>();
		array.add(new Achievement("Achievement 1", "Desc 1", true));
		array.add(new Achievement("Achievement 2", "Desc 2", true));
		array.add(new Achievement("Achievement 3", "Desc 3", true));
		array.add(new Achievement("Achievement 4", "Desc 4", true));
		array.add(new Achievement("Achievement 5", "Desc 5", true));
		array.add(new Achievement("Achievement 6", "Desc 6", true));
		array.add(new Achievement("Achievement 7", "Desc 7", true));
		array.add(new Achievement("Achievement 8", "Desc 8", false));
		array.add(new Achievement("Achievement 9", "Desc 9", false));
		array.add(new Achievement("Achievement 10", "Desc 10", false));
		array.add(new Achievement("Achievement 11", "Desc 11", false));
		array.add(new Achievement("Achievement 12", "Desc 12", false));
		array.add(new Achievement("Achievement 13", "Desc 13", false));
		array.add(new Achievement("Achievement 14", "Desc 14", false));
		array.add(new Achievement("Achievement 15", "Desc 15", false));
		return array;
	}

	private class GetAchievementsAsyncTask extends
			AsyncTask<Object, Object, Object> {

		@Override
		protected void onPreExecute() {
			Log.d("AsyncTask", "onPreExecute()");
			showProgressBar();
		}

		@Override
		protected Object doInBackground(Object... params) {
			/*
			 * Here should be the code that does 3 things: 1. Connects to the
			 * API to load the list of user gained achievements (list of id's)
			 * 2. Connects to the API to load the entire achievement list 3.
			 * Returns a JSONArray holding all achievements, with use user
			 * achievements at top.
			 */
			array = new ArrayList<Achievement>();
			array.add(new Achievement("Achievement 1", "Desc 1", true));
			array.add(new Achievement("Achievement 2", "Desc 2", true));
			array.add(new Achievement("Achievement 3", "Desc 3", true));
			array.add(new Achievement("Achievement 4", "Desc 4", true));
			array.add(new Achievement("Achievement 5", "Desc 5", true));
			array.add(new Achievement("Achievement 6", "Desc 6", true));
			array.add(new Achievement("Achievement 7", "Desc 7", true));
			array.add(new Achievement("Achievement 8", "Desc 8", false));
			array.add(new Achievement("Achievement 9", "Desc 9", false));
			array.add(new Achievement("Achievement 10", "Desc 10", false));
			array.add(new Achievement("Achievement 11", "Desc 11", false));
			array.add(new Achievement("Achievement 12", "Desc 12", false));
			array.add(new Achievement("Achievement 13", "Desc 13", false));
			array.add(new Achievement("Achievement 14", "Desc 14", false));
			array.add(new Achievement("Achievement 15", "Desc 15", false));
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			Log.d("AsyncTask", "onPostExecute()");
			hideProgressBar();
			adapter.notifyDataSetChanged();
			refreshCurrentFragment();
		}

	}

	private class AchievementAdapter extends BaseAdapter {

		int size;
		ArrayList<Achievement> arrayList;

		/* private view holder class */
		private class ViewHolder {
			ImageView image;
			TextView textName;
			TextView textDesc;
		}

		public AchievementAdapter(ArrayList<Achievement> array) {
			this.size = array.size();
			this.arrayList = array;
		}

		@Override
		public int getCount() {
			return size;
		}

		@Override
		public Object getItem(int position) {
			return arrayList.get(position);
		}

		@Override
		public long getItemId(int position) {
			arrayList.indexOf(getItem(position));
			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder = null;
			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.fragment_achievement_item, null);
				holder = new ViewHolder();
				holder.textName = (TextView) convertView
						.findViewById(R.id.fragment_achievement_text_name);
				holder.textDesc = (TextView) convertView
						.findViewById(R.id.fragment_achievement_text_desc);
				holder.image = (ImageView) convertView
						.findViewById(R.id.fragment_achievement_image);
				convertView.setTag(holder);
				Achievement achievement = arrayList.get(position);
				holder.textName.setText(achievement.getName());
				holder.textDesc.setText(achievement.getDesc());
				// temporary solution - set fixed image
				holder.image.setImageDrawable(getResources().getDrawable(
						R.drawable.ic_menu_info_details));
				if (!achievement.isGained()) {
					convertView.setBackgroundColor(Color.GRAY);
				}
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			return convertView;
		}
	}

}
