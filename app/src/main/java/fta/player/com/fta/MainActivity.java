package fta.player.com.fta;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import fta.player.com.fta.core.HeadsetStateReceiver;
import fta.player.com.fta.core.UserPreferences;


public class MainActivity extends ActionBarActivity
implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private boolean isPlaying = false;
    private URLLoader urlLoader;
    private Timer timer = new Timer();
    public String ds = "radio-z-kryjivky";
    private String listSource = "http://mjoy.ua/radio/station/rzk/playlist.json";
    public MediaPlayer mp = new MediaPlayer();
    //private TimePicker alarmTimePicker;
    public View currentView = null;

    private static MainActivity inst;

    public static MainActivity instance() {
        return inst;
    }

    private UserPreferences up;

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

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
        up = new UserPreferences(inst);

        //supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        mp.setOnPreparedListener(onBufComplete);

        View decorView = getWindow().getDecorView();
// Hide both the navigation bar and the status bar.
// SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
// a general rule, you should design your app to hide the status bar whenever you
// hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
               // | View.SYSSYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        IntentFilter receiverFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);

        HeadsetStateReceiver receiver = new HeadsetStateReceiver(this);
        registerReceiver( receiver, receiverFilter );
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //setImageCorrection();
    }

    private void setImageCorrection()
    {
        ImageView bgView = (ImageView) findViewById(R.id.bgView);
        RelativeLayout.LayoutParams layoutParams  = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(0,0,0,0);
        bgView.setLayoutParams(layoutParams);
    }

    @Override
    protected void onStop()
    {
       // up.savePreferences();
        super.onStop();
    }

    @Override
    public boolean onKeyDown(int keycode, KeyEvent e)
    {
        switch(keycode) {
            case KeyEvent.KEYCODE_VOLUME_UP: {
                if(up.volume != 1.0f)
                    up.volume += 0.1f;
                else if(up.volume >= 1.0f)
                    up.volume = 1.0f;
                AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int) (up.volume * 10.0 ), AudioManager.FLAG_SHOW_UI);
                mp.setVolume(up.volume,up.volume);
                return true;
            }
            case KeyEvent.KEYCODE_VOLUME_DOWN: {
                if(up.volume >= 0.0f)
                    up.volume -= 0.1f;
                else
                    up.volume = 0.0f;
                AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int)(up.volume * 10.0 ), AudioManager.FLAG_SHOW_UI );
                mp.setVolume(up.volume,up.volume);
                return true;
            }
        }

        return super.onKeyDown(keycode, e);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        super.onWindowFocusChanged(hasFocus);
        getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                // View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
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
            View rootView = inflater.inflate(R.layout.urban_fragment, container, false);
            final TextView trackTitleTxt = (TextView) rootView.findViewById(R.id.trackTitle);
            trackTitleTxt.setVisibility(View.GONE);
            return rootView;
        }

        public void setImageCorrections(View rootView)
        {
            ImageView bgView = (ImageView) rootView.findViewById(R.id.bgView);
            RelativeLayout.LayoutParams layoutParams  = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            layoutParams.setMargins(0,0,0,0);
            bgView.setLayoutParams(layoutParams);
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
        //button.setEnabled(false);

        try {
            if(!isPlaying) {
                    mp.setAudioStreamType(AudioManager.STREAM_ALARM);
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

    public void setHeadphones(Boolean isIn)
    {
        AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        if(isIn && mp != null && mp.isPlaying()) {
            mp.reset();
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            audioManager.setMode(AudioManager.MODE_NORMAL);
            audioManager.setSpeakerphoneOn(false);
            audioManager.setRouting(AudioManager.MODE_NORMAL, AudioManager.ROUTE_EARPIECE, AudioManager.ROUTE_ALL);
        }
        else if(!isIn && mp != null && mp.isPlaying()) {
            mp.pause();
            audioManager.setMode(AudioManager.MODE_IN_CALL);
            audioManager.setSpeakerphoneOn(true);
            audioManager.setRouting(AudioManager.MODE_NORMAL, AudioManager.ROUTE_SPEAKER, AudioManager.ROUTE_ALL);
            mp.start();
        }
    }
}
