package pl.sointeractive.isaaclock.fragments;

import java.util.ArrayList;
import java.util.List;

import pl.sointeractive.isaaclock.R;
import pl.sointeractive.isaaclock.activities.UserActivityTabs;
import pl.sointeractive.isaaclock.activities.UserActivityTabs.TabManager;
import pl.sointeractive.isaaclock.data.Achievement;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListFragment;

public class AchievementsFragment extends SherlockListFragment implements
		LoaderManager.LoaderCallbacks<List<Achievement>> {

	UserActivityTabs context;
	ArrayList<Achievement> array;
	boolean isLoaded = false;
	AchievementAdapter adapter;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		context = (UserActivityTabs) getSherlockActivity();
		array = new ArrayList<Achievement>();
		adapter = new AchievementAdapter(context);
		setListAdapter(adapter);

		// Start out with a progress indicator.
		setListShown(false);
		// Set empty text
		setEmptyText(getString(R.string.fragment_achievements_empty));

		// Prepare the loader. Either re-connect with an existing one,
		// or start a new one.
		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public Loader<List<Achievement>> onCreateLoader(int arg0, Bundle arg1) {
		System.out.println("DataListFragment.onCreateLoader");
		return new DataListLoader(getActivity());
	}

	@Override
	public void onLoadFinished(Loader<List<Achievement>> arg0,
			List<Achievement> data) {
		adapter.setData(data);
		System.out.println("DataListFragment.onLoadFinished");
		// The list should now be shown.
		if (isResumed()) {
			setListShown(true);
		} else {
			setListShownNoAnimation(true);
		}
	}

	@Override
	public void onLoaderReset(Loader<List<Achievement>> arg0) {
		adapter.setData(null);
	}

	public void refreshCurrentFragment() {
		Log.d("AlarmsFragment", "refreshCurrentFragment()");
		TabManager tm = context.getTabManager();
		TabHost th = context.getTabHost();
		tm.printTabInfo();
		tm.refreshTab(th.getCurrentTabTag());
	}

	public static class DataListLoader extends
			AsyncTaskLoader<List<Achievement>> {

		List<Achievement> mModels;

		public DataListLoader(Context context) {
			super(context);
		}

		@Override
		public List<Achievement> loadInBackground() {
			System.out.println("DataListLoader.loadInBackground");

			// You should perform the heavy task of getting data from
			// Internet or database or other source
			// Here, we are generating some Sample data

			// Create corresponding array of entries and load with data.
			List<Achievement> entries = new ArrayList<Achievement>(10);
			entries.add(new Achievement("Achievement 0", "Desc 0", true));
			entries.add(new Achievement("Achievement 1", "Desc 1", true));
			entries.add(new Achievement("Achievement 2", "Desc 2", true));
			entries.add(new Achievement("Achievement 3", "Desc 3", true));
			entries.add(new Achievement("Achievement 4", "Desc 4", false));
			entries.add(new Achievement("Achievement 5", "Desc 5", false));
			entries.add(new Achievement("Achievement 6", "Desc 6", false));
			entries.add(new Achievement("Achievement 7", "Desc 7", false));
			entries.add(new Achievement("Achievement 8", "Desc 8", false));
			entries.add(new Achievement("Achievement 9", "Desc 9", false));
			entries.add(new Achievement("Achievement 10", "Desc 10", false));
			entries.add(new Achievement("Achievement 11", "Desc 11", false));
			entries.add(new Achievement("Achievement 12", "Desc 12", false));
			entries.add(new Achievement("Achievement 13", "Desc 13", false));
			entries.add(new Achievement("Achievement 14", "Desc 14", false));
			entries.add(new Achievement("Achievement 15", "Desc 15", false));

			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return entries;
		}

		/**
		 * Called when there is new data to deliver to the client. The super
		 * class will take care of delivering it; the implementation here just
		 * adds a little more logic.
		 */
		@Override
		public void deliverResult(List<Achievement> listOfData) {
			if (isReset()) {
				// An async query came in while the loader is stopped. We
				// don't need the result.
				if (listOfData != null) {
					onReleaseResources(listOfData);
				}
			}
			List<Achievement> oldApps = listOfData;
			mModels = listOfData;

			if (isStarted()) {
				// If the Loader is currently started, we can immediately
				// deliver its results.
				super.deliverResult(listOfData);
			}

			// At this point we can release the resources associated with
			// 'oldApps' if needed; now that the new result is delivered we
			// know that it is no longer in use.
			if (oldApps != null) {
				onReleaseResources(oldApps);
			}
		}

		/**
		 * Handles a request to start the Loader.
		 */
		@Override
		protected void onStartLoading() {
			if (mModels != null) {
				// If we currently have a result available, deliver it
				// immediately.
				deliverResult(mModels);
			}

			if (takeContentChanged() || mModels == null) {
				// If the data has changed since the last time it was loaded
				// or is not currently available, start a load.
				forceLoad();
			}
		}

		/**
		 * Handles a request to stop the Loader.
		 */
		@Override
		protected void onStopLoading() {
			// Attempt to cancel the current load task if possible.
			cancelLoad();
		}

		/**
		 * Handles a request to cancel a load.
		 */
		@Override
		public void onCanceled(List<Achievement> apps) {
			super.onCanceled(apps);

			// At this point we can release the resources associated with 'apps'
			// if needed.
			onReleaseResources(apps);
		}

		/**
		 * Handles a request to completely reset the Loader.
		 */
		@Override
		protected void onReset() {
			super.onReset();

			// Ensure the loader is stopped
			onStopLoading();

			// At this point we can release the resources associated with 'apps'
			// if needed.
			if (mModels != null) {
				onReleaseResources(mModels);
				mModels = null;
			}
		}

		/**
		 * Helper function to take care of releasing resources associated with
		 * an actively loaded data set.
		 */
		protected void onReleaseResources(List<Achievement> apps) {
		}

	}

	private class AchievementAdapter extends ArrayAdapter<Achievement> {
		private final LayoutInflater mInflater;

		public AchievementAdapter(Context context) {
			super(context, R.layout.fragment_achievement_item);
			mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public void setData(List<Achievement> data) {
			clear();
			//array = (ArrayList<Achievement>) data;
			if (data != null) {
				for (Achievement appEntry : data) {
					add(appEntry);
				}
			}
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view;
			if (convertView == null) {
				view = mInflater.inflate(R.layout.fragment_achievement_item,
						parent, false);
			} else {
				view = convertView;
			}

			//Achievement achievement = getItem(position);
			Achievement achievement = getItem(position);
			Log.d("AchievementAdapter",achievement.print());
			TextView textName = (TextView) view
					.findViewById(R.id.fragment_achievement_text_name);
			TextView textDesc = (TextView) view
					.findViewById(R.id.fragment_achievement_text_desc);
			ImageView image = (ImageView) view
					.findViewById(R.id.fragment_achievement_image);
			textName.setText(achievement.getName());
			textDesc.setText(achievement.getDesc());
			image.setImageDrawable(getResources().getDrawable(
					R.drawable.ic_menu_info_details));
			if (!achievement.isGained()) {
				view.setBackgroundColor(Color.GRAY);
			} else {
				view.setBackgroundColor(Color.TRANSPARENT);
			}
			return view;
		}
	}

}
