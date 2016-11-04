package fta.player.com.fta.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import fta.player.com.fta.DownloadImageTask;
import fta.player.com.fta.R;
import fta.player.com.fta.utils.ImageFileUtils;

/**
 * Created by Taras on 08.07.2015.
 */
public class ChristmasFragment extends Fragment implements IRadioFragment {

    private ImageView bgView;
    private String imageNameString = "Christmas.png";

    public ChristmasFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.radio_fragment, container, false);
        bgView = (ImageView) rootView.findViewById(R.id.bgView);

        if(!ImageFileUtils.isImageExist(imageNameString))
        {
            new DownloadImageTask(this).execute("http://mjoy.ua/radio/rzk/images/bg.jpg");
        }
        else
        {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            setImage( BitmapFactory.decodeFile( ImageFileUtils.getImagePath(imageNameString), options) );

        }
        return rootView;
    }

    public void processLoadedImage(Bitmap bitmap)
    {
        setImage(bitmap);
        ImageFileUtils.saveImage(bitmap, imageNameString);
    }

    private void setImage(Bitmap bitmap)
    {
        bgView.setImageBitmap(bitmap);
        RelativeLayout.LayoutParams layoutParams  = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(0,0,0,0);
        bgView.setLayoutParams(layoutParams);
    }
}
