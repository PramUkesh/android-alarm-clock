package pl.sointeractive.isaaclock.fragments;

import pl.sointeractive.isaaclock.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;

public class GeneralFragment extends SherlockFragment{

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_alarms, container, false);
    }
}
