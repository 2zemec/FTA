package com.example.taras.FTA.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.taras.FTA.R;

/**
 * Created by Taras on 08.07.2015.
 */
public class UrbanFragment extends Fragment {

    public UrbanFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.urban_fragment, container, false);

        return rootView;
    }
}
