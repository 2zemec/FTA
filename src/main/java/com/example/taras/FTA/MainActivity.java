package com.example.taras.FTA;

import android.app.Activity;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private boolean isPlaying = false;
    private URLLoader urlLoader;
    private Timer timer = new Timer();
    private String ds = "radio-z-kryjivky";
    private String listSource = "http://mjoy.ua/radio/station/rzk/playlist.json";
    public MediaPlayer mp = new MediaPlayer();

    private MediaPlayer.OnPreparedListener onBufComplete = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            //final Button button = (Button) findViewById(R.id.playBtn);
            //button.setEnabled(true);
        }
    };

    private TimerTask ts = new TimerTask() {
        @Override
        public void run() {
            try{
                loadTrackList();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    };

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        mp.setOnPreparedListener(onBufComplete);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        stopPlaying();
        switch (number) {
            case 1: {
                mTitle = getString(R.string.title_section1);
                ds = getString(R.string.radio1);
                listSource = getString(R.string.radioS1);
                stopPlaying();
                break;
            }
            case 2: {
                mTitle = getString(R.string.title_section2);
                ds = getString(R.string.radio2);
                listSource = getString(R.string.radioS2);
                stopPlaying();
                break;
            }
            case 3: {
                mTitle = getString(R.string.title_section3);
                ds = getString(R.string.radio3);
                listSource = getString(R.string.radioS3);
                stopPlaying();
                break;
            }
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.urban_fragment, container, false);

            final TextView trackTitleTxt = (TextView)rootView.findViewById(R.id.trackTitle);
            trackTitleTxt.setVisibility(View.GONE);

            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    public void playStream(View v){

        final TextView trackTitleTxt = (TextView) findViewById(R.id.trackTitle);
        trackTitleTxt.setVisibility(View.VISIBLE);
        final Button button = (Button) findViewById(R.id.playBtn);
        //button.setEnabled(false);
        if(!isPlaying) {
            button.setText("Stop");
            try {
                mp.setDataSource("http://stream.mjoy.ua:8000/"+ds);
                mp.prepare();
                mp.start();
                isPlaying = true;
                timer = new Timer();
                ts = new TimerTask() {
                    @Override
                    public void run() {
                        try{
                            loadTrackList();
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                };
                timer.schedule(ts, 0, 5000);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            stopPlaying();
        }
    }

    public void stopPlaying()
    {
        final Button button = (Button) findViewById(R.id.playBtn);
        isPlaying = false;
        if(button != null)
            button.setText("Play");
        mp.stop();
        mp.reset();
        if (timer != null) {
            timer.cancel();
        }
    }

    private void loadTrackList() throws IOException
    {
        urlLoader = new URLLoader(this);
        urlLoader.execute(listSource);
    }

}
