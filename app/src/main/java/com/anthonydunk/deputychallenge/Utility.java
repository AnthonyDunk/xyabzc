package com.anthonydunk.deputychallenge;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by antho on 1/02/2017.
 */

public class Utility {


    public static String timeToTimeString(Date dt)
    {
        // Convert a time to a string in UTC format e.g. 2012-01-01T14:00:00+00:00
        DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'+'00':'00");
        timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String s = timeFormat.format(dt);
        return s;
    }

    public static Date timeStringtoTime(String s) {
        try {
            // Convert a time to a string in UTC format e.g. 2012-01-01T14:00:00+00:00
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
