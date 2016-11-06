package fta.player.com.fta.utils;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import fta.player.com.fta.MainActivity;


/**
 * Created by tpetr on 04.11.2016.
 */

public class ImageFileUtils {

    public static Context getContext()
    {
        return MainActivity.appContext;
    }
    public static void saveImage(Bitmap bitmap, String imageName)
    {
        if(!isImageExist(imageName))
        {
            FileOutputStream outputStream = null;

            try {
                outputStream = getContext().openFileOutput(imageName, Context.MODE_PRIVATE);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (outputStream != null) {
                        outputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public static boolean isImageExist(String imageName)
    {
        File f = new File(getImagePath(imageName));
        return f.exists();
    }

    public static String getImagePath(String imageName)
    {
        return getContext().getFilesDir().getPath()+File.separator+imageName;
    }
}
