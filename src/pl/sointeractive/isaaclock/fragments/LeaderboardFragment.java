package pl.sointeractive.isaaclock.fragments;

import java.util.ArrayList;
import java.util.List;

import pl.sointeractive.isaaclock.R;
import pl.sointeractive.isaaclock.activities.UserActivityTabs;
import pl.sointeractive.isaaclock.data.Achievement;
import pl.sointeractive.isaaclock.data.App;
import pl.sointeractive.isaaclock.data.LeaderboardPosition;
import pl.sointeractive.isaaclock.data.UserData;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListFragment;

public class LeaderboardFragment extends SherlockListFragment implements
		LoaderManager.LoaderCallbacks<List<LeaderboardPosition>> {

	UserActivityTabs context;
	ArrayList<Achievement> array;
	boolean isLoaded = false;
	LeaderboardAdapter adapter;
	static int userPosition = -1;
	static int userScore = -1;
	static UserData userData;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		context = (UserActivityTabs) getSherlockActivity();
		array = new ArrayList<Achievement>();
		adapter = new LeaderboardAdapter(context);
		setListAdapter(adapter);
		userData = App.loadUserData();

		// Start out with a progress indicator.
		setListShown(false);

		// Prepare the loader. Either re-connect with an existing one,
		// or start a new one.
		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public Loader<List<LeaderboardPosition>> onCreateLoader(int arg0,
			Bundle arg1) {
		System.out.println("DataListFragment.onCreateLoader");
		return new DataListLoader(getActivity());
	}

	@Override
	public void onLoadFinished(Loader<List<LeaderboardPosition>> arg0,
			List<LeaderboardPosition> data) {
		adapter.setData(data);
		System.out.println("DataListFragment.onLoadFinished");
		// The list should now be shown.
		if (isResumed()) {
			setListShown(true);
			if(userPosition!=-1){
				getListView().smoothScrollToPosition(userPosition);
			}
		} else {
			setListShownNoAnimation(true);
		}
	}

	@Override
	public void onLoaderReset(Loader<List<LeaderboardPosition>> arg0) {
		adapter.setData(null);
	}

	public static class DataListLoader extends
			AsyncTaskLoader<List<LeaderboardPosition>> {

		List<LeaderboardPosition> mModels;

		public DataListLoader(Context context) {
			super(context);
		}

		@Override
		public List<LeaderboardPosition> loadInBackground() {
			System.out.println("DataListLoader.loadInBackground");

			// You should perform the heavy task of getting data from
			// Internet or database or other source
			// Here, we are generating some Sample data

			// Create corresponding array of entries and load with data.
			List<LeaderboardPosition> entries = new ArrayList<LeaderboardPosition>(
					10);
			int leaderboardId = 1;
			int userId = 1;
			int score = 3000;
			int position = 1;
			entries.add(new LeaderboardPosition(leaderboardId, position++,
					userId++, score -= 100));
			entries.add(new LeaderboardPosition(leaderboardId, position++,
					userId++, score -= 100));
			entries.add(new LeaderboardPosition(leaderboardId, position++,
					userId++, score -= 100));
			entries.add(new LeaderboardPosition(leaderboardId, position++,
					userId++, score -= 100));
			entries.add(new LeaderboardPosition(leaderboardId, position++,
					userId++, score -= 100));
			entries.add(new LeaderboardPosition(leaderboardId, position++,
					userId++, score -= 100));
			entries.add(new LeaderboardPosition(leaderboardId, position++,
					userId++, score -= 100));
			entries.add(new LeaderboardPosition(leaderboardId, position++,
					userId++, score -= 100));
			entries.add(new LeaderboardPosition(leaderboardId, position++,
					userId++, score -= 100));
			entries.add(new LeaderboardPosition(leaderboardId, position++,
					userId++, score -= 100));
			entries.add(new LeaderboardPosition(leaderboardId, position++,
					userId++, score -= 100));
			entries.add(new LeaderboardPosition(leaderboardId, position++,
					userId++, score -= 100));
			entries.add(new LeaderboardPosition(leaderboardId, position++,
					userId++, score -= 100));
			entries.add(new LeaderboardPosition(leaderboardId, position++,
					userId++, score -= 100));
			entries.add(new LeaderboardPosition(leaderboardId, position++,
					userId++, score -= 100));
			entries.add(new LeaderboardPosition(leaderboardId, position++,
					userId++, score -= 100));
			entries.add(new LeaderboardPosition(leaderboardId, position++,
					userId++, score -= 100).setIsUserPosition(true));
			userPosition = position-2;
			userData.setLastScore(score+100);
			App.saveUserData(userData);
			entries.add(new LeaderboardPosition(leaderboardId, position++,
					userId++, score -= 100));
			entries.add(new LeaderboardPosition(leaderboardId, position++,
					userId++, score -= 100));
			entries.add(new LeaderboardPosition(leaderboardId, position++,
					userId++, score -= 100));

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
		public void deliverResult(List<LeaderboardPosition> listOfData) {
			if (isReset()) {
				// An async query came in while the loader is stopped. We
				// don't need the result.
				if (listOfData != null) {
					onReleaseResources(listOfData);
				}
			}
			List<LeaderboardPosition> oldApps = listOfData;
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
		public void onCanceled(List<LeaderboardPosition> apps) {
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
		protected void onReleaseResources(List<LeaderboardPosition> apps) {
		}

	}

	private class LeaderboardAdapter extends ArrayAdapter<LeaderboardPosition> {
		private final LayoutInflater mInflater;

		public LeaderboardAdapter(Context context) {
			super(context, R.layout.fragment_leaderboard_item);
			mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public void setData(List<LeaderboardPosition> data) {
			clear();
			// array = (ArrayList<Achievement>) data;
			if (data != null) {
				for (LeaderboardPosition appEntry : data) {
					add(appEntry);
				}
			}
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view;
			if (convertView == null) {
				view = mInflater.inflate(R.layout.fragment_leaderboard_item,
						parent, false);
			} else {
				view = convertView;
			}

			// Achievement achievement = getItem(position);
			LeaderboardPosition p = getItem(position);
			TextView textPosition = (TextView) view
					.findViewById(R.id.fragment_leaderboard_position);
			TextView textId = (TextView) view
					.findViewById(R.id.fragment_leaderboard_id);
			TextView textScore = (TextView) view
					.findViewById(R.id.fragment_leaderboard_score);
			ImageView image = (ImageView) view
					.findViewById(R.id.fragment_leaderboard_image);
			textPosition.setText("" + p.getPosition());
			textId.setText("" + p.getUserId());
			textScore.setText("" + p.getUserScore());
			image.setImageDrawable(getResources().getDrawable(
					R.drawable.ic_menu_info_details));

			if (p.isUserPosition()) {
				view.setBackgroundColor(Color.rgb(0, 150, 150));
			} else {
				view.setBackgroundColor(Color.TRANSPARENT);
			}

			return view;
		}
	}

}
