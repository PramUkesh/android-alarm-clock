package pl.sointeractive.isaaclock.fragments;

import pl.sointeractive.isaaclock.R;
import pl.sointeractive.isaaclock.activities.UserActivity;
import pl.sointeractive.isaaclock.data.App;
import pl.sointeractive.isaaclock.data.UserData;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

/**
 * Fragment class for general user information. Used in the UserActivity an
 * shown in its corresponding Tab. It extends SherlockListFragment, which is a
 * ABS library version of the Android ListFragment class. For detailed
 * information on how the ListFragment handles its data viewing, please check
 * the class documentation provided by Google.
 * 
 * @author Mateusz Renes
 * 
 */
public class GeneralFragment extends SherlockFragment implements
		LoaderManager.LoaderCallbacks<UserData> {

	private static UserData userData;
	private UserActivity context;
	private View view;

	private TextView textName, textEmail, textScore, textAlarm;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		context = (UserActivity) getActivity();
		view.findViewById(R.id.fragment_general_scroll)
				.setVisibility(View.GONE);
		view.findViewById(R.id.fragment_general_progress_bar).setVisibility(
				View.VISIBLE);
		// start loading data
		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_general, container, false);
		textName = (TextView) view
				.findViewById(R.id.fragment_general_user_name);
		textEmail = (TextView) view
				.findViewById(R.id.fragment_general_user_email);
		textScore = (TextView) view
				.findViewById(R.id.fragment_general_user_score);
		textAlarm = (TextView) view
				.findViewById(R.id.fragment_general_next_alarm);
		return view;
	}

	@Override
	public Loader<UserData> onCreateLoader(int arg0, Bundle arg1) {
		System.out.println("DataListFragment.onCreateLoader");
		return new DataListLoader(getActivity());
	}

	@Override
	public void onLoadFinished(Loader<UserData> arg0, UserData arg1) {
		//set text field values
		textName.setText(userData.getName());
		textEmail.setText(userData.getEmail());
		textScore.setText(userData.getLastScore());
		textAlarm.setText(userData.getNextAlarmTime());
		//find buttons 
		Button buttonScore = (Button) getActivity().findViewById(
				R.id.fragment_general_button_show_score);
		Button buttonAlarm = (Button) getActivity().findViewById(
				R.id.fragment_general_button_show_alarms);
		//set button listeners
		buttonScore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				context.getTabHost().setCurrentTabByTag("leaderboard");
			}
		});
		buttonAlarm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				context.getTabHost().setCurrentTabByTag("alarms");
			}
		});
		//set images (currently just fake icons)
		ImageView imageUser = (ImageView) getActivity().findViewById(
				R.id.fragment_general_image);
		imageUser.setImageDrawable(getResources().getDrawable(
				R.drawable.ic_menu_friendslist));
		//show the list
		view.findViewById(R.id.fragment_general_scroll).setVisibility(
				View.VISIBLE);
		//hide progress bar
		view.findViewById(R.id.fragment_general_progress_bar).setVisibility(
				View.GONE);
	}

	@Override
	public void onLoaderReset(Loader<UserData> arg0) {
	}

	public static class DataListLoader extends AsyncTaskLoader<UserData> {
		UserData mModel;

		public DataListLoader(Context context) {
			super(context);
		}

		@Override
		public UserData loadInBackground() {
			System.out.println("DataListLoader.loadInBackground");
			// App.saveUserData(userData);
			// userData =
			userData = App.loadUserData();
			return userData;
		}

		/**
		 * Called when there is new data to deliver to the client. The super
		 * class will take care of delivering it; the implementation here just
		 * adds a little more logic.
		 */
		@Override
		public void deliverResult(UserData data) {
			if (isReset()) {
				// An async query came in while the loader is stopped. We
				// don't need the result.
				if (data != null) {
					onReleaseResources(data);
				}
			}
			UserData oldApps = data;
			mModel = data;

			if (isStarted()) {
				// If the Loader is currently started, we can immediately
				// deliver its results.
				super.deliverResult(data);
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
			if (mModel != null) {
				// If we currently have a result available, deliver it
				// immediately.
				deliverResult(mModel);
			}

			if (takeContentChanged() || mModel == null) {
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
		public void onCanceled(UserData data) {
			super.onCanceled(data);

			// At this point we can release the resources associated with 'apps'
			// if needed.
			onReleaseResources(data);
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
			if (mModel != null) {
				onReleaseResources(mModel);
				mModel = null;
			}
		}

		/**
		 * Helper function to take care of releasing resources associated with
		 * an actively loaded data set.
		 */
		protected void onReleaseResources(UserData apps) {
		}

	}
}
