package com.LogIn;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import net.frakbot.glowpadbackport.GlowPadView;

public class LockscreenMood extends Lockscreen {
    private boolean grid_triggered = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.lockscreen_mood);
        // Use user desktop wallpaper in lockscreen
        RelativeLayout ll = (RelativeLayout) findViewById(R.id.lockscreen_mood_main);
        ll.setBackground(WallpaperManager.getInstance(this).getFastDrawable());

        final GlowPadView glowPad = (GlowPadView) findViewById(R.id.incomingCallWidget);
        final ImageView moodGrid = (ImageView) findViewById(R.id.mood_grid);
        final ImageView whiteBackground = (ImageView) findViewById(R.id.dark_background);

        grid_triggered = false;

        glowPad.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e)
            {
                if (grid_triggered) {
                    final TextView txt = (TextView) findViewById(R.id.textView);

                    double grid_size = moodGrid.getWidth()/12;
                    String negative_positive = "Negative";
                    int level_negative_positive = (int)((double)(e.getX() - moodGrid.getWidth() / 2) / grid_size);
                    if (e.getX() > moodGrid.getWidth() / 2) {
                        negative_positive = "Positive";
                    }

                    String low_high = "High";
                    int level_low_high = (int)((double)(e.getY() - moodGrid.getHeight() / 2) / grid_size);
                    if (e.getY() > moodGrid.getHeight() / 2) {
                        low_high = "Low";
                    }

                    String sleepiness_description =
                            Utility.convertScaleValueToAdj(Math.abs(level_negative_positive) + 1) + " "
                                    + negative_positive + "\n" +
                                    Utility.convertScaleValueToAdj(Math.abs(level_low_high) + 1) + " "
                                    + low_high;
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
                    moodGrid.setVisibility(View.INVISIBLE);
                    whiteBackground.setVisibility(View.INVISIBLE);
                    finish();
                }
            }

            @Override
            public void onTrigger(View v, int target) {
                Utility.parseWrite(target - 2);
                glowPad.reset(true);
                v.setVisibility(View.GONE);
                finish();
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
                    whiteBackground.setVisibility(View.VISIBLE);
                    grid_triggered = true;
                }
            }
        });
    }
}
