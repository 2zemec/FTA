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
public class RZKFragment extends Fragment {

    public RZKFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.rzk_fragment, container, false);

        return rootView;
    }
}