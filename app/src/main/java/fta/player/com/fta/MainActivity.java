package fta.player.com.fta;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import fta.player.com.fta.utils.ImageFileUtils;


public class MainActivity extends ActionBarActivity
implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    public static String PACKAGE_NAME;

    private boolean isPlaying = false;
    private URLLoader urlLoader;
    private Timer timer = new Timer();
    public String ds = "radio-z-kryjivky";
    private String listSource = "http://mjoy.ua/radio/station/rzk/playlist.json";
    public MediaPlayer mp = new MediaPlayer();

    private static MainActivity inst;

    public static MainActivity instance() {
        return inst;
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    private MediaPlayer.OnPreparedListener onBufComplete = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
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

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PACKAGE_NAME = getApplicationContext().getPackageName();
        ImageFileUtils.PATH = getApplicationContext().getFilesDir().getAbsolutePath();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        mp.setOnPreparedListener(onBufComplete);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    public boolean isTablet() {
        return (getApplicationContext().getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(!isTablet())
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    protected void onStop()
    {
        super.onStop();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        super.onWindowFocusChanged(hasFocus);
        getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
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
            case 4: {
                mTitle = getString(R.string.title_section4);
                ds = getString(R.string.radio4);
                listSource = getString(R.string.radioS4);
                stopPlaying();
                break;
            }
            case 5: {
                mTitle = getString(R.string.title_section5);
                ds = getString(R.string.radio5);
                listSource = getString(R.string.radioS5);
                stopPlaying();
                break;
            }
            case 6: {
                mTitle = getString(R.string.title_section6);
                ds = getString(R.string.radio6);
                listSource = getString(R.string.radioS6);
                stopPlaying();
                break;
            }
            case 7: {
                mTitle = getString(R.string.title_section7);
                ds = getString(R.string.radio7);
                listSource = getString(R.string.radioS7);
                stopPlaying();
                break;
            }
            case 8: {
                mTitle = getString(R.string.title_section8);
                ds = getString(R.string.radio8);
                listSource = getString(R.string.radioS8);
                stopPlaying();
                break;
            }
            case 9: {
                mTitle = getString(R.string.title_section9);
                ds = getString(R.string.radio9);
                listSource = getString(R.string.radioS9);
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
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, AlarmActivity.class);
            this.startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

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
            View rootView = inflater.inflate(R.layout.radio_fragment, container, false);
            final TextView trackTitleTxt = (TextView) rootView.findViewById(R.id.trackTitle);
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

        final TextView trackTitleTxt;
        try {
            trackTitleTxt = (TextView) findViewById(R.id.trackTitle);
            trackTitleTxt.setVisibility(View.VISIBLE);
        }catch (Exception e)
        {

        }

        try {
            if(!isPlaying) {
                    mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
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
            }
            else
            {
                stopPlaying();
            }
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

    public void stopPlaying()
    {
        isPlaying = false;
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
