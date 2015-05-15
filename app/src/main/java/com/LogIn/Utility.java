package com.LogIn;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;

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

    private static KeyguardManager.KeyguardLock k1;

    public static String LogInType = "Sleepiness";
    public static final int num_days_experiment_length = 18;
    public static final int year_start = 2015;
    public static int month_start = 4; // start from 0
    public static int day_start = 1;
    public static int hour_start = 9;
    public static final int num_hour_experiment_length = 12;
    public static int hour_rate = 22;
    public static String conditions = "453621";
    public static String condition_dayoff = "1";
    // Condition:
    // 1: No lockscreen, No notification
    // 2: No lockscreen, Notification no sound
    // 3: No lockscreen, Notification has sound
    // 4: Has lockscreen, No notification
    // 5: Has lockscreen, Notification no sound
    // 6: Has lockscreen, Notification has sound

    public static List<ParseObject> m_valueList;

    public static void setKeyguardLock(Context context) {
        KeyguardManager km =(KeyguardManager)context.getSystemService(KEYGUARD_SERVICE);
        if (k1 == null) k1 = km.newKeyguardLock("IN");
        if (Utility.needLockscreen()) {
            k1.disableKeyguard();
            settingChangedWriteToParse("Disable keyguard, Condition" + getCondition());
        } else {
            k1.reenableKeyguard();
            settingChangedWriteToParse("Reenable keyguard, Condition" + getCondition());
        }
    }

    public static char getCondition() {
        int i = getDaysDiff();
        char condition = '0';
        if (i < 0) i = 0;
        if (i%3 == 0 || i >= num_days_experiment_length) {
            condition = condition_dayoff.charAt(0);
        } else {
            condition = conditions.charAt(i/3);
        }
        return condition;
    }

    public static boolean needLockscreen() {
        char condition = getCondition();
        if (condition == '4' || condition == '5' || condition == '6') {
            return true;
        }
        return false;
    }

    public static boolean needNotification() {
        char condition = getCondition();
        if (condition == '2' || condition == '3' || condition == '5' || condition == '6') {
            return true;
        }
        return false;
    }

    public static boolean needNotificationSound() {
        char condition = getCondition();
        if (condition == '3' || condition == '6') {
            return true;
        }
        return false;
    }

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

    public static void initSettings(Context context) {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);

        // LogIn basic setup
        Utility.condition_dayoff = SP.getString("pref_key_dayoff_condition", "1");
        Utility.conditions = SP.getString("pref_key_conditions", "123456");
        Utility.LogInType = SP.getString("pref_key_login_type", "Sleepiness");

        // For Visualization View
        Utility.month_start = Integer.parseInt(SP.getString("pref_key_start_month", "5")) - 1;
        Utility.day_start = Integer.parseInt(SP.getString("pref_key_start_day", "1"));
        Utility.hour_start = Integer.parseInt(SP.getString("pref_key_start_hour", "9"));
    }

    public static void sleepinessWriteToParse(String input_source, int value) {
        ParseObject parseObj;
        if (value >=1 && value <=7) {
            parseObj = new ParseObject(name_datastore);
        } else {
            parseObj = new ParseObject(name_datastore+"_NoLog");
        }
        parseObj.put("time", new Date());
        parseObj.put("input_source", input_source);
        parseObj.put("sleepiness_value", value);
        parseObj.saveInBackground();
        parseObj.pinInBackground();
        parseObj.saveEventually();
    }

    public static void depressionWriteToParse(String input_source, String type, int value) {
        ParseObject parseObj;
        if (value >=1 && value <=5) {
            parseObj = new ParseObject(name_datastore);
        } else {
            parseObj = new ParseObject(name_datastore+"_NoLog");
        }
        parseObj.put("time", new Date());
        parseObj.put("input_source", input_source);
        parseObj.put("depression_type", type);
        parseObj.put("depression_value", value);
        parseObj.saveInBackground();
        parseObj.pinInBackground();
        parseObj.saveEventually();
    }

    public static void moodWriteToParse(String input_source, int negative_positive, int low_high) {
        ParseObject parseObj;
        if (negative_positive != -9999 && negative_positive != 0) {
            parseObj = new ParseObject(name_datastore);
        } else {
            parseObj = new ParseObject(name_datastore+"_NoLog");
        }        parseObj.put("time", new Date());
        parseObj.put("input_source", input_source);
        parseObj.put("mood_negative_positive", negative_positive);
        parseObj.put("mood_low_high", low_high);
        parseObj.saveInBackground();
        parseObj.pinInBackground();
        parseObj.saveEventually();
    }

    public static void notificationWriteToParse(String action, String notification_mode) {
        ParseObject parseObj = new ParseObject(name_datastore+"_Notif");
        parseObj.put("time", new Date());
        parseObj.put("action", action);
        parseObj.put("notification_mode", notification_mode);
        parseObj.saveInBackground();
        parseObj.pinInBackground();
        parseObj.saveEventually();
    }

    public static void rateWriteToParse(int rate) {
        ParseObject parseObj = new ParseObject("Rating");
        parseObj.put("time", new Date());
        parseObj.put("deviceID", deviceID);
        parseObj.put("rate", rate);
        parseObj.put("condition", String.valueOf(Utility.getCondition()));
        parseObj.saveInBackground();
        parseObj.pinInBackground();
        parseObj.saveEventually();
    }

    public static void settingChangedWriteToParse(String setting) {
        ParseObject parseObj = new ParseObject("SettingChanged");
        parseObj.put("time", new Date());
        parseObj.put("deviceID", deviceID);
        parseObj.put("setting", setting);
        parseObj.saveInBackground();
        parseObj.pinInBackground();
        parseObj.saveEventually();
    }

    public static List<ParseObject> getDataFromParse() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(name_datastore);
        query.fromLocalDatastore();
        query.addAscendingOrder("time");
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
        return Color.rgb(255 - (value-1)*diff, (value-1)*diff, 0);
    }

    public static int convertMoodValueToColor(int value) {
        int diff = 255/10;
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
