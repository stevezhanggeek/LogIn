package com.LogIn;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Utility extends Activity {
    private static String deviceID = "";
    public static int num_days_experiment_length = 12;
    public static int year_start = 2015;
    public static int month_start = 3; // start from 0
    public static int day_start = 21;
    public static int hour_start = 10;
    public static int num_hour_experiment_length = 10;
    public static String name_datastore = "Logs_";

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
        deviceID = getUniquePsuedoID().replace("-", "");
        name_datastore = name_datastore + deviceID;
        /*
        ParseQuery<ParseObject> query;
        query = ParseQuery.getQuery("Parameters");
        query.getInBackground("G9vWUL34Sa", new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    //HOST = object.getString("IP_Address");
                } else {
                    // something went wrong
                }
            }
        });
        query = ParseQuery.getQuery("ComplexGesture");
        query.whereEqualTo("deviceID", deviceID);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (object == null) {
                    // No result
                } else {
//                    complexGestureString = object.getString("GestureString");
                }
            }
        });
        */
    }

    public static void parseWrite(final int value) {
        ParseObject parseObj = new ParseObject(name_datastore);
        parseObj.put("value", value);
        parseObj.put("time", new Date());
        parseObj.saveInBackground();
        parseObj.pinInBackground();
    }

    public static List<ParseObject> getDataFromParse() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(name_datastore);
        query.fromLocalDatastore();
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

    public static void updateViewPager(ViewGroup v) {
        final ViewPager vp = (ViewPager) v.findViewById(R.id.viewpager);
        if (vp!=null) {
            vp.setAdapter(new SamplePagerAdapter());
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    vp.setCurrentItem(2);
                }
            }, 100);
        }
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
        int sleepiness_color;
        switch(value) {
            case 1:
                sleepiness_color = Color.rgb(0,255,0);
                break;
            case 2:
                sleepiness_color = Color.rgb(35,220,0);
                break;
            case 3:
                sleepiness_color = Color.rgb(70,185,0);
                break;
            case 4:
                sleepiness_color = Color.rgb(105,150,0);
                break;
            case 5:
                sleepiness_color = Color.rgb(140,115,0);
                break;
            case 6:
                sleepiness_color = Color.rgb(175,80,0);
                break;
            case 7:
                sleepiness_color = Color.rgb(210,45,0);
                break;
            default:
                sleepiness_color = Color.BLACK;
        }
        return sleepiness_color;
    }
}
