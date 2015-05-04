package com.LogIn;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.widget.DatePicker;

import java.util.Calendar;

public class Setting extends PreferenceActivity implements DatePickerDialog.OnDateSetListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.setting);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Preference btnDateFilter = findPreference("btnDateFilter");
        btnDateFilter.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                showDateDialog();
                return false;
            }
        });

    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {

    }

    private void showDateDialog(){
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(this, this, year, month, day).show();

    }
}