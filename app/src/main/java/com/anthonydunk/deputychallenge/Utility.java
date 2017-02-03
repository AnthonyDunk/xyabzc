package com.anthonydunk.deputychallenge;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;

import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by antho on 1/02/2017.
 */

public class Utility {

    //
    // Load an image from the web using a URL, and display it in the specified ImageView
    //
    public static void loadAndDisplayWebImage(String imageURL, ImageView iv, Activity act)
    {
        final String finalImageURL = imageURL;
        final ImageView finalImageView = iv;
        final Activity finalAct = act;
        Thread thread = new Thread(new Runnable() {

            public void run() {
                try {
                    URL url = new URL(finalImageURL);
                    final Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                    finalAct.runOnUiThread(new Runnable() {
                        public void run() {
                            finalImageView.setImageURI(null);
                            finalImageView.setImageBitmap(bmp);
                        }
                    });

                } catch (Exception e)
                {
                    // failure
                }
            }
        });

        thread.start();
    }

    //
    // Convert a Date object to a string in the required UTC format e.g. 2012-01-01T14:00:00+00:00
    //
    public static String timeToTimeString(Date dt)
    {
        DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'+'00':'00");
        timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String s = timeFormat.format(dt);
        return s;
    }

    //
    // Convert a UTC time string to a Date object
    //
    public static Date timeStringtoTime(String s) {
        try {

            DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'+'00':'00");
            timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date time = timeFormat.parse(s);
            return time;
        }
        catch (ParseException e)
        {
            return null;
        }
    }
}
