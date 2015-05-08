package com.LogIn;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Utility extends Activity {
    private static String deviceID = "";
    private static String name_datastore = "Logs_";

    public static String LogInType = "Depression";
    public static final int num_days_experiment_length = 18;
    public static final int year_start = 2015;
    public static int month_start = 3; // start from 0
    public static int day_start = 21;
    public static int hour_start = 14;
    public static final int num_hour_experiment_length = 14;
    public static int hour_rate = 22;
    public static int condition = 1;
    public static String notification_mode = "Normal Mode";

    public static List<ParseObject> m_valueList;

    public static String getUniquePsuedoID() {
        String m_szDevIDShort = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) + (Build.CPU_ABI.length() % 10) + (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);
        String serial = null;
        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            serial = "serial"; // some value
        }
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    public static void initParameters() {
        // Parse doesn't allow "-" in object name
        deviceID = getUniquePsuedoID().replace("-", "");
        name_datastore = name_datastore + deviceID;
    }

    public static void sleepinessWriteToParse(int value) {
        ParseObject parseObj = new ParseObject(name_datastore);
        parseObj.put("time", new Date());
        parseObj.put("value", value);
        parseObj.saveInBackground();
        parseObj.pinInBackground();
    }

    public static void depressionWriteToParse(String type, int value) {
        ParseObject parseObj = new ParseObject(name_datastore);
        parseObj.put("time", new Date());
        parseObj.put("type", type);
        parseObj.put("value", value);
        parseObj.saveInBackground();
        parseObj.pinInBackground();
    }

    public static void moodWriteToParse(int negative_positive, int low_high) {
        ParseObject parseObj = new ParseObject(name_datastore);
        parseObj.put("time", new Date());
        parseObj.put("negative_positive", negative_positive);
        parseObj.put("low_high", low_high);
        parseObj.saveInBackground();
        parseObj.pinInBackground();
    }

    public static void rateWriteToParse(int rate) {
        ParseObject parseObj = new ParseObject("Rating");
        parseObj.put("time", new Date());
        parseObj.put("deviceID", deviceID);
        parseObj.put("rate", rate);
        parseObj.saveInBackground();
        parseObj.pinInBackground();
    }

    public static List<ParseObject> getDataFromParse() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(name_datastore);
        query.fromLocalDatastore();
        query.addAscendingOrder("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> valueList, ParseException e) {
                if (e == null) {
                    m_valueList = valueList;
                } else {
                    System.out.println("WTH");
                }
            }
        });
        return m_valueList;
    }

    public static int getDaysDiff() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Utility.year_start);
        cal.set(Calendar.MONTH, Utility.month_start);
        cal.set(Calendar.DAY_OF_MONTH, Utility.day_start);

        long diff = new Date().getTime() - cal.getTime().getTime();
        long elapsedDays = diff / (1000*60*60*24);
        return (int)elapsedDays;
    }

    public static String convertSleepinessValueToDescription(int value) {
        String sleepiness_description;
        switch(value) {
            case 1:
                sleepiness_description = "Feeling active, vital, alert, or wide awake";
                break;
            case 2:
                sleepiness_description = "Functioning at high levels, but not at peak; able to concentrate";
                break;
            case 3:
                sleepiness_description = "Awake, but relaxed; responsive but not fully alert";
                break;
            case 4:
                sleepiness_description = "Somewhat foggy, let down";
                break;
            case 5:
                sleepiness_description = "Foggy; losing interest in remaining awake; slowed down";
                break;
            case 6:
                sleepiness_description = "Sleepy, woozy, fighting sleep; prefer to lie down";
                break;
            case 7:
                sleepiness_description = "No longer fighting sleep, sleep onset soon; having dream-like thoughts";
                break;
            default:
                sleepiness_description = "";
        }
        return sleepiness_description;
    }

    public static int convertSleepinessValueToColor(int value) {
        int diff = 255/6;
        return Color.rgb((value-1)*diff, 255 - (value-1)*diff, 0);
    }

    public static int convertDepressionValueToColor(int value) {
        int diff = 255/4;
        return Color.rgb((value-1)*diff, 255 - (value-1)*diff, 0);
    }

    public static int convertMoodValueToColor(int value) {
        int diff = 255/9;
        return Color.rgb(255/2 - value*diff, 255/2 + value*diff, 0);
    }

    public static String convertScaleValueToAdj(int value) {
        switch(value) {
            case 1:
                return "Minimal";
            case 2:
                return "Slightly";
            case 3:
                return "Somewhat";
            case 4:
                return "Very";
            case 5:
                return "Extremely";
            default:
                return "";
        }
    }

    public static String convertScaleValueToAdv(int value) {
        switch(value) {
            case 1:
                return "Minimal";
            case 2:
                return "Fair";
            case 3:
                return "Good";
            case 4:
                return "Very Good";
            case 5:
                return "Extreme";
            default:
                return "";
        }
    }
}
