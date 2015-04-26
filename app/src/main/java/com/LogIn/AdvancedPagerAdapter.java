/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.LogIn;

import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Calendar;

class AdvancedPagerAdapter extends PagerAdapter {

    @Override
    public int getCount() {
        return Utility.num_days_experiment_length+1;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return o == view;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Add Log";
        }

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Utility.year_start);
        cal.set(Calendar.MONTH, Utility.month_start);
        cal.set(Calendar.DAY_OF_MONTH, Utility.day_start);

        cal.add(Calendar.DATE, position - 1);

        return cal.get(Calendar.MONTH)+1 + "/" + cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Instantiate the {@link View} which should be displayed at {@code position}. Here we
     * inflate a layout from the apps resources and then change the text view to signify the position.
     */
    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        if (position == 0) {
            view = inflater.inflate(R.layout.input, container, false);
            container.addView(view);

            final SeekBar seekBar = (SeekBar) view.findViewById(R.id.seekBar);
            final TextView txt = (TextView) view.findViewById(R.id.textView2);

            Button btn = (Button) view.findViewById(R.id.button_submit_log);
            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    RadioGroup radioGroup = (RadioGroup) container.findViewById(R.id.radio_group);
                    if (radioGroup.getCheckedRadioButtonId() == R.id.accomplishment) {
                        System.out.println("accomplishment");
                    } else if (radioGroup.getCheckedRadioButtonId() == R.id.pleasure) {
                        System.out.println("pleasure");
                    }
                    Utility.parseWrite(seekBar.getProgress() + 1);
                    Utility.getDataFromParse();
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Utility.updateViewPager(container);
                        }
                    }, 1000);
                }
            });

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    txt.setText(Utility.convertSleepinessValueToDescription(seekBar.getProgress() + 1));
                }
            });
        } else {
            view = inflater.inflate(R.layout.visualization_scroll, container, false);
            container.addView(view);

            // Let view know which page it is
            CalendarView cal = (CalendarView) view.findViewById(R.id.CalendarView);
            cal.setPageIndex(position);
        }
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}