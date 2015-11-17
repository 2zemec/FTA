package fta.player.com.fta.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fta.player.com.fta.R;

/**
 * Created by Taras on 08.07.2015.
 */
public class MousseFragment extends Fragment {

    public MousseFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.mousse_fragment, container, false);

        return rootView;
    }
}
