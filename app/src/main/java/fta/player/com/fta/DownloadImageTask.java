package fta.player.com.fta;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.InputStream;

import fta.player.com.fta.MainActivity;
import fta.player.com.fta.fragments.IRadioFragment;

/**
 * Created by Taras on 14.04.2015.
 */
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    IRadioFragment radioFragment;

    public DownloadImageTask(IRadioFragment radioFragment) {
        this.radioFragment = radioFragment;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        radioFragment.processLoadedImage(result);
    }
}
