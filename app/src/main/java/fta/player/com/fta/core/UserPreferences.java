package fta.player.com.fta.core;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by Taras on 18.04.2016.
 */
public class UserPreferences {

    public static final String FTA_PREF = "FTAPreferences";
    public float volume = 0;
    //SharedPreferences objects
    private SharedPreferences sp;
    private String VOLUME_STR = "volume";

    public UserPreferences(Activity a)
    {
        /*try {
            sp = a.getSharedPreferences(FTA_PREF, 0);
            volume = sp.getFloat(VOLUME_STR, 0.5f);
        }
        catch (NullPointerException e)
        {
            volume = 0.8f;
        }*/
    }

    public void setVolume(float val)
    {
        volume = val;
    }

    public void savePreferences()
    {

        /*SharedPreferences.Editor editor = sp.edit();
        editor.putFloat(VOLUME_STR, volume);

        // Commit the edits!
        editor.commit();
        */
    }
}
