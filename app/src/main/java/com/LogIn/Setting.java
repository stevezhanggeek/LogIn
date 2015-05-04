package com.LogIn;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.widget.DatePicker;

import java.util.Calendar;

public class Setting extends PreferenceActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.setting);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }
}