package fta.player.com.fta.utils;

import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Created by tpetr on 04.11.2016.
 */

public class ImageFileUtils {

    public static  String PATH = "";
    public static void saveImage(Bitmap bitmap, String imageName)
    {
        if(!isImageExist(imageName))
        {
            FileOutputStream outputStream = null;

            try {
                outputStream = new FileOutputStream(new File(getImagePath(imageName)));
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
        return false;
//        File f = new File(getImagePath(imageName));
//        return f.exists();
    }

    public static String getImagePath(String imageName)
    {
        String path = PATH+File.separator+imageName;
        File dir = new File(PATH);
        if(!dir.exists()) {
            dir.mkdir();
        }

        return path;
    }
}
