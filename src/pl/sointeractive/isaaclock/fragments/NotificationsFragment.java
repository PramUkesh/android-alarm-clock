package pl.sointeractive.isaaclock.fragments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pl.sointeractive.isaaclock.R;
import pl.sointeractive.isaaclock.activities.UserActivityTabs;
import pl.sointeractive.isaaclock.data.App;
import pl.sointeractive.isaaclock.data.Notification;
import pl.sointeractive.isaacloud.connection.HttpResponse;
import android.content.Context;
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

public class NotificationsFragment extends SherlockListFragment implements
		LoaderManager.LoaderCallbacks<List<Notification>> {

	UserActivityTabs context;
	ArrayList<Notification> array;
	boolean isLoaded = false;
	NotificationAdapter adapter;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		context = (UserActivityTabs) getSherlockActivity();
		array = new ArrayList<Notification>();
		adapter = new NotificationAdapter(context);
		setListAdapter(adapter);

		// Start out with a progress indicator.
		setListShown(false);
		// Set empty text
		setEmptyText(getString(R.string.fragment_notifications_empty));
		// Prepare the loader. Either re-connect with an existing one,
		// or start a new one.
		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public Loader<List<Notification>> onCreateLoader(int arg0,
			Bundle arg1) {
		System.out.println("DataListFragment.onCreateLoader");
		return new DataListLoader(getActivity());
	}

	@Override
	public void onLoadFinished(Loader<List<Notification>> arg0,
			List<Notification> data) {
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
	public void onLoaderReset(Loader<List<Notification>> arg0) {
		adapter.setData(null);
	}

	public static class DataListLoader extends
			AsyncTaskLoader<List<Notification>> {

		List<Notification> mModels;

		public DataListLoader(Context context) {
			super(context);
		}

		@Override
		public List<Notification> loadInBackground() {
			System.out.println("DataListLoader.loadInBackground");

			List<Notification> entries = new ArrayList<Notification>();
			try {
				HttpResponse response = App.getWrapper().getNotifications();
				JSONArray array = response.getJSONArray();
				for (int i = 0; i < array.length(); i++) {
					entries.add(new Notification((JSONObject) array.get(i)));
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			/*
			int title = 1;
			int message = 1;
			entries.add(new Notification(null, "Title "+title++, "Message "+message++));
			entries.add(new Notification(null, "Title "+title++, "Message "+message++));
			entries.add(new Notification(null, "Title "+title++, "Message "+message++));
			entries.add(new Notification(null, "Title "+title++, "Message "+message++));
			entries.add(new Notification(null, "Title "+title++, "Message "+message++));

			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/

			return entries;
		}

		/**
		 * Called when there is new data to deliver to the client. The super
		 * class will take care of delivering it; the implementation here just
		 * adds a little more logic.
		 */
		@Override
		public void deliverResult(List<Notification> listOfData) {
			if (isReset()) {
				// An async query came in while the loader is stopped. We
				// don't need the result.
				if (listOfData != null) {
					onReleaseResources(listOfData);
				}
			}
			List<Notification> oldApps = listOfData;
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
		public void onCanceled(List<Notification> apps) {
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
		protected void onReleaseResources(List<Notification> apps) {
		}

	}

	private class NotificationAdapter extends ArrayAdapter<Notification> {
		private final LayoutInflater mInflater;

		public NotificationAdapter(Context context) {
			super(context, R.layout.fragment_leaderboard_item);
			mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public void setData(List<Notification> data) {
			clear();
			// array = (ArrayList<Achievement>) data;
			if (data != null) {
				for (Notification appEntry : data) {
					add(appEntry);
				}
			}
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view;
			if (convertView == null) {
				view = mInflater.inflate(R.layout.fragment_notification_item,
						parent, false);
			} else {
				view = convertView;
			}

			// Achievement achievement = getItem(position);
			Notification note = getItem(position);
			TextView textTitle = (TextView) view
					.findViewById(R.id.fragment_notification_title);
			TextView textMessage = (TextView) view
					.findViewById(R.id.fragment_notification_message);
			ImageView image = (ImageView) view
					.findViewById(R.id.fragment_notification_image);
			textTitle.setText(note.getTitle());
			textMessage.setText(note.getMessage());
			image.setImageDrawable(getResources().getDrawable(
					R.drawable.ic_menu_info_details));

			return view;
		}
	}
}