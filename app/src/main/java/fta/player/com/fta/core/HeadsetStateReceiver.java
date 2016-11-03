package fta.player.com.fta.core;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import fta.player.com.fta.MainActivity;

/**
 * Created by Taras on 27.10.2016.
 */

public class HeadsetStateReceiver extends BroadcastReceiver {

    private MainActivity MA;
    public HeadsetStateReceiver(MainActivity MA)
    {
        this.MA = MA;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)){
            int state = intent.getIntExtra("state", 4);
            if(state == 0){
                //headphones off
            }else if(state == 1){
                //headphones on
            }else {
            }
        }

    }
}
