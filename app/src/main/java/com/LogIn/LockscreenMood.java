package com.LogIn;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
        final ImageView whiteBackground = (ImageView) findViewById(R.id.white_background);

        grid_triggered = false;

        glowPad.setOnTriggerListener(new GlowPadView.OnTriggerListener() {
            @Override
            public void onGrabbed(View v, int handle) {
            }

            @Override
            public void onReleased(View v, int handle) {
                if (grid_triggered) {
                    moodGrid.setVisibility(View.INVISIBLE);
                    whiteBackground.setVisibility(View.INVISIBLE);
                    Toast.makeText(LockscreenMood.this, String.valueOf(glowPad.mgrid_X) + ", " + String.valueOf(glowPad.mgrid_Y), Toast.LENGTH_SHORT).show();
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
