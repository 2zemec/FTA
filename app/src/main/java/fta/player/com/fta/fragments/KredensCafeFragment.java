package fta.player.com.fta.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import fta.player.com.fta.DownloadImageTask;
import fta.player.com.fta.R;

/**
 * Created by Taras on 08.07.2015.
 */
public class KredensCafeFragment extends Fragment {

    public KredensCafeFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.kredens_fragment, container, false);
        ImageView bgView = (ImageView) rootView.findViewById(R.id.bgView);

        new DownloadImageTask(bgView).execute("http://mjoy.ua/radio/kredens-cafe-radio/image/bg.jpg");
        return rootView;
    }
}
