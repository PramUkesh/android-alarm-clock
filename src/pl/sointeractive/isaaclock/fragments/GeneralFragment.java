package pl.sointeractive.isaaclock.fragments;

import pl.sointeractive.isaaclock.R;
import pl.sointeractive.isaaclock.activities.UserActivityTabs;
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

public class GeneralFragment extends SherlockFragment implements
		LoaderManager.LoaderCallbacks<UserData> {

	static UserData userData;
	UserActivityTabs context; 
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		userData = App.loadUserData();
		context = (UserActivityTabs) getActivity();

		// Start out with a progress indicator.
		//setListShown(false);

		// Prepare the loader. Either re-connect with an existing one,
		// or start a new one.
		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_general, container,
				false);
	}

	@Override
	public Loader<UserData> onCreateLoader(int arg0, Bundle arg1) {
		System.out.println("DataListFragment.onCreateLoader");
		return new DataListLoader(getActivity());
	}

	@Override
	public void onLoadFinished(Loader<UserData> arg0, UserData arg1) {
		TextView textName = (TextView) getActivity().findViewById(R.id.fragment_general_user_name);
		TextView textEmail = (TextView) getActivity().findViewById(R.id.fragment_general_user_email);
		TextView textScore = (TextView) getActivity().findViewById(R.id.fragment_general_user_score);
		TextView textAlarm = (TextView) getActivity().findViewById(R.id.fragment_general_next_alarm);
		
		textName.setText(userData.getName());
		textEmail.setText(userData.getEmail());
		textScore.setText(""+userData.getScore());
		textAlarm.setText(userData.getNextAlarm());
		
		Button buttonScore = (Button) getActivity().findViewById(R.id.fragment_general_button_show_score);
		Button buttonAlarm = (Button) getActivity().findViewById(R.id.fragment_general_button_show_alarms);
		
		buttonScore.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				context.getTabHost().setCurrentTabByTag("leaderboard");
			}
		});
		
		buttonAlarm.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				context.getTabHost().setCurrentTabByTag("alarms");
			}
		});
		
		ImageView imageUser = (ImageView) getActivity().findViewById(R.id.fragment_general_image);
		imageUser.setImageDrawable(getResources().getDrawable(
				R.drawable.ic_menu_friendslist));
	}

	@Override
	public void onLoaderReset(Loader<UserData> arg0) {
		// TODO Auto-generated method stub

	}

	public static class DataListLoader extends
			AsyncTaskLoader<UserData> {

		UserData mModel;

		public DataListLoader(Context context) {
			super(context);
		}

		@Override
		public UserData loadInBackground() {
			System.out.println("DataListLoader.loadInBackground");

			// You should perform the heavy task of getting data from
			// Internet or database or other source
			// Here, we are generating some Sample data

			// load user data here
			userData.setName("SomeUserNameExample");
			userData.setEmail("SomeEmailExample@Example.com");
			userData.setScore(666);
			App.saveUserData(userData);
			
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

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
