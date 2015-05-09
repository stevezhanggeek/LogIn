package com.LogIn;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Setting extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.setting);
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