package com.LogIn;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class SettingHidden extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.setting_hidden);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        prefs.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences preference, String value){
        System.out.println("SettingChanged");
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        // LogIn basic setup
        Utility.condition_dayoff = SP.getString("pref_key_dayoff_condition", "1");
        Utility.conditions = SP.getString("pref_key_conditions", "123456");
        Utility.LogInType = SP.getString("pref_key_login_type", "Sleepiness");

        // For Visualization View
        Utility.month_start = Integer.parseInt(SP.getString("pref_key_start_month", "5")) - 1;
        Utility.day_start = Integer.parseInt(SP.getString("pref_key_start_day", "1"));
        Utility.hour_start = Integer.parseInt(SP.getString("pref_key_start_hour", "9"));

        // Setup Rate Alert
        Utility.hour_rate = Integer.parseInt(SP.getString("pref_key_rate_hour", "22"));
        AlarmReceiverRating alarm_rating = new AlarmReceiverRating();
        alarm_rating.setRatingAlarm(this);

        String setting = Utility.condition_dayoff + ", " + Utility.conditions + ", " + Utility.LogInType + ", "
                + Utility.month_start + ", " + Utility.day_start + ", " + Utility.hour_start + ", "
                + Utility.hour_rate;
        Utility.settingChangedWriteToParse(setting);
    }
}