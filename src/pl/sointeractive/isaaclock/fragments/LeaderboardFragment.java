package pl.sointeractive.isaaclock.fragments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pl.sointeractive.isaaclock.R;
import pl.sointeractive.isaaclock.activities.UserActivity;
import pl.sointeractive.isaaclock.config.Settings;
import pl.sointeractive.isaaclock.data.Achievement;
import pl.sointeractive.isaaclock.data.App;
import pl.sointeractive.isaaclock.data.LeaderboardPosition;
import pl.sointeractive.isaaclock.data.UserData;
import pl.sointeractive.isaacloud.connection.HttpResponse;
import pl.sointeractive.isaacloud.exceptions.IsaaCloudConnectionException;
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

/**
 * Fragment class for displaying the Leaderboard. Used in the UserActivity and
 * shown in its corresponding Tab. It extends SherlockListFragment, which is a
 * ABS library version of the Android ListFragment class. For detailed
 * information on how the ListFragment handles its data viewing, please check
 * the class documentation provided by Google.
 * 
 * @author Mateusz Renes
 * 
 */
public class LeaderboardFragment extends SherlockListFragment implements
		LoaderManager.LoaderCallbacks<List<LeaderboardPosition>> {

	UserActivity context;
	ArrayList<Achievement> array;
	boolean isLoaded = false;
	LeaderboardAdapter adapter;
	static int userPosition = -1;
	static int userScore = -1;
	static UserData userData;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		context = (UserActivity) getSherlockActivity();
		array = new ArrayList<Achievement>();
		adapter = new LeaderboardAdapter(context);
		setListAdapter(adapter);
		userData = App.loadUserData();
		// Start out with a progress indicator.
		setListShown(false);
		// Set empty text
		setEmptyText(getString(R.string.fragment_leaderboard_empty));
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
			if (userPosition != -1) {
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
			userData = App.loadUserData();
			int userId = userData.getUserId();
			List<LeaderboardPosition> entries = new ArrayList<LeaderboardPosition>();
			// recalculate loeaderboard
			/*
			 * THIS STEP SHOULD NOT BE NECCESSARY. The API should recalculate
			 * the leaderboard periodically, based on the "cron" parameter
			 * defined during leaderboard creation in the Account Center. But
			 * for the purpose of this example we will manually recalculate the
			 * leaderboard, in order to display changes in positions
			 * immediately.
			 */
			try {
				App.getConnector()
						.path("/admin/leaderboards/" + Settings.leaderboardId
								+ "/recalculate").get();
			} catch (IsaaCloudConnectionException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// get leaderboard
			try {
				HttpResponse response = App
						.getConnector()
						.path("/cache/leaderboards/" + Settings.leaderboardId + "/users")
						.withLimit(1000)
						.withFields("firstName", "lastName",
								"gainedAchievements").get();
				JSONArray array = response.getJSONArray();
				for (int i = 0; i < array.length(); i++) {
					entries.add(0,
							new LeaderboardPosition((JSONObject) array.get(i),
									userId));
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return entries;
			} catch (IsaaCloudConnectionException e) {
				e.printStackTrace();
				return entries;
			} catch (IOException e1) {
				e1.printStackTrace();
				return entries;
			}
			// sort leaderboard based on position
			Collections.sort(entries, new Comparator<LeaderboardPosition>() {
				@Override
				public int compare(LeaderboardPosition lhs,
						LeaderboardPosition rhs) {
					return rhs.getUserScore() - lhs.getUserScore();
				}
			});
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
			LeaderboardPosition p = getItem(position);
			TextView textPosition = (TextView) view
					.findViewById(R.id.fragment_leaderboard_position);
			TextView textId = (TextView) view
					.findViewById(R.id.fragment_leaderboard_id);
			TextView textScore = (TextView) view
					.findViewById(R.id.fragment_leaderboard_score);
			ImageView image = (ImageView) view
					.findViewById(R.id.fragment_leaderboard_image);
			textPosition.setText("" + (position + 1));
			textId.setText(p.getUserName());
			textScore.setText("Score: " + p.getUserScore());
			image.setImageDrawable(getResources().getDrawable(
					R.drawable.ic_menu_info_details));
			if (p.isCurrentUserPosition()) {
				view.setBackgroundColor(Color.rgb(0, 150, 150));
			} else {
				view.setBackgroundColor(Color.TRANSPARENT);
			}
			return view;
		}
	}

}
