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
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import net.frakbot.glowpadbackport.GlowPadView;


public class MainActivity extends Activity {
    private boolean grid_triggered = false;
    private int saved_value_negative_positive = 0;
    private int saved_value_low_high = 0;

    public void openVisualization(View view) {
        Intent intent = new Intent(this, Visualization.class);
//    intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        startService(new Intent(this, LockscreenService.class));

        AlarmReceiver alarm = new AlarmReceiver();
        alarm.setAlarm(this);

        if (Utility.LogInType.equals("Sleepiness")) {
            setContentView(R.layout.input_sleepiness);

            final GlowPadView glowPad = (GlowPadView) findViewById(R.id.incomingCallWidget);

            glowPad.setOnTriggerListener(new GlowPadView.OnTriggerListener() {
                @Override
                public void onGrabbed(View v, int handle) {
                }

                @Override
                public void onReleased(View v, int handle) {
                    TextView txt = (TextView) findViewById(R.id.textView);
                    txt.setText("");
                }

                @Override
                public void onTrigger(View v, int target) {
                    Utility.sleepinessWriteToParse(target - 2);
                    glowPad.reset(true);
                    openVisualization(null);
                }

                @Override
                public void onGrabbedStateChange(View v, int handle) {
                }

                @Override
                public void onFinishFinalAnimation() {
                }

                @Override
                public void onMovedOnTarget(int target) {
                    final TextView txt = (TextView) findViewById(R.id.textView);
                    String sleepiness_description = Utility.convertSleepinessValueToDescription(target - 2);
                    txt.setText(sleepiness_description);
                }
            });
        } else if (Utility.LogInType.equals("Depression")) {
            setContentView(R.layout.input_depression);

            final GlowPadView glowPad = (GlowPadView) findViewById(R.id.incomingCallWidget);
            glowPad.setOnTriggerListener(new GlowPadView.OnTriggerListener() {
                @Override
                public void onGrabbed(View v, int handle) {
                }

                @Override
                public void onReleased(View v, int handle) {
                    TextView txt = (TextView) findViewById(R.id.textView);
                    txt.setText("");
                }

                @Override
                public void onTrigger(View v, int target) {
                    String type = "Accomplishment";
                    if (glowPad.mhandle == 0) {
                        type = "Pleasure";
                    }
                    Utility.depressionWriteToParse(type, target - 1);
                    glowPad.reset(true);
                    openVisualization(null);
                }

                @Override
                public void onGrabbedStateChange(View v, int handle) {
                }

                @Override
                public void onFinishFinalAnimation() {
                }

                @Override
                public void onMovedOnTarget(int target) {
                    final TextView txt = (TextView) findViewById(R.id.textView);
                    String depression_description;

                    if (glowPad.mhandle == 0) {
                        depression_description = Utility.convertScaleValueToAdv(target - 1) + " Pleasure";
                    } else {
                        depression_description = Utility.convertScaleValueToAdv(target - 1) + " Accomplishment";
                    }
                    txt.setText(depression_description);
                }
            });
        } else {
            setContentView(R.layout.input_mood);

            final GlowPadView glowPad = (GlowPadView) findViewById(R.id.incomingCallWidget);
            final ImageView moodGrid = (ImageView) findViewById(R.id.mood_grid);

            grid_triggered = false;

            glowPad.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent e) {
                    if (grid_triggered) {
                        double grid_size = moodGrid.getWidth() / 12;

                        String negative_positive = "Positive";
                        saved_value_negative_positive = Math.abs((int) ((double) (e.getX() - moodGrid.getWidth() / 2) / grid_size)) + 1;
                        if (e.getX() < moodGrid.getWidth() / 2) {
                            negative_positive = "Negative";
                            saved_value_negative_positive = -saved_value_negative_positive;
                        }

                        String low_high = "Low";
                        saved_value_low_high = Math.abs((int) ((double) (e.getY() - moodGrid.getHeight() / 2) / grid_size)) + 1;
                        if (e.getY() < moodGrid.getHeight() / 2) {
                            low_high = "High";
                            saved_value_low_high = -saved_value_low_high;
                        }

                        String sleepiness_description =
                                Utility.convertScaleValueToAdj(Math.abs(saved_value_negative_positive)) + " "
                                        + negative_positive + "\n" +
                                        Utility.convertScaleValueToAdj(Math.abs(saved_value_low_high)) + " "
                                        + low_high;
                        TextView txt = (TextView) findViewById(R.id.textView);
                        txt.setText(sleepiness_description);
                    }
                    return false;
                }
            });

            glowPad.setOnTriggerListener(new GlowPadView.OnTriggerListener() {
                @Override
                public void onGrabbed(View v, int handle) {
                }

                @Override
                public void onReleased(View v, int handle) {
                    if (grid_triggered) {
                        Utility.moodWriteToParse(saved_value_negative_positive, saved_value_low_high);
                        moodGrid.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onTrigger(View v, int target) {
                    glowPad.reset(true);
                    openVisualization(null);
                }

                @Override
                public void onGrabbedStateChange(View v, int handle) {
                }

                @Override
                public void onFinishFinalAnimation() {
                }

                @Override
                public void onMovedOnTarget(int target) {
                    if (target == 1) {
                        glowPad.setVisibility(View.GONE);
                        glowPad.setVibrateEnabled(false);
                        moodGrid.setVisibility(View.VISIBLE);
                        grid_triggered = true;
                    }
                }
            });
        }

    }
}