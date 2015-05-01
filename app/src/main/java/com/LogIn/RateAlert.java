/*
* Copyright 2013 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.LogIn;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class RateAlert extends Activity {
    private int rating = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.rate_alert);
        final TextView txt = (TextView) findViewById(R.id.textView);
        if (Utility.LogInType.equals("Sleepiness")) {
            txt.setText("Please Rate Sleepiness");
        } else if (Utility.LogInType.equals("Depression")) {
            txt.setText("Please Rate Depression");
        } else {
            txt.setText("Please Rate Mood");
        }

        final SeekBar seek_bar = (SeekBar) findViewById(R.id.seekBar);

        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Utility.rateWriteToParse(seek_bar.getProgress() + 1);
                txt.setText("Thanks for your rating!\nThis screen will be closed now.");

                // Exit rate alert after 3 seconds
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 3000);
            }
        });

    }
}