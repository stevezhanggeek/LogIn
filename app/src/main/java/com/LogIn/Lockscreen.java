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

public class Lockscreen extends Activity {
    private boolean grid_triggered = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("Lock");
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.lockscreen);

        if(getIntent()!=null&&getIntent().hasExtra("kill")&&getIntent().getExtras().getInt("kill")==1){
            finish();
        }

        // Use user desktop wallpaper in lockscreen
        final WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        RelativeLayout ll = (RelativeLayout) findViewById(R.id.main);
        ll.setBackground(wallpaperManager.getFastDrawable());

        grid_triggered = false;
        final GlowPadView glowPad = (GlowPadView) findViewById(R.id.incomingCallWidget);
        final ImageView moodGrid = (ImageView) findViewById(R.id.mood_grid);
        final ImageView whiteBackground = (ImageView) findViewById(R.id.white_background);

        glowPad.setOnTriggerListener(new GlowPadView.OnTriggerListener() {
            @Override
            public void onGrabbed(View v, int handle) {
            }

            @Override
            public void onReleased(View v, int handle) {
                if (grid_triggered) {
                    moodGrid.setVisibility(View.INVISIBLE);
                    whiteBackground.setVisibility(View.INVISIBLE);
                    Toast.makeText(Lockscreen.this, String.valueOf(glowPad.mgrid_X) + ", " + String.valueOf(glowPad.mgrid_Y), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onTrigger(View v, int target) {
                Utility.parseWrite(target - 2);
                String sleepiness_description = Utility.convertSleepinessValueToDescription(target - 2);
                Toast.makeText(Lockscreen.this, sleepiness_description, Toast.LENGTH_SHORT).show();
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
                System.out.println(target);
                final TextView txt = (TextView) findViewById(R.id.textView);
                String sleepiness_description = Utility.convertSleepinessValueToDescription(target - 2);

                if (target == 3) {
                    glowPad.setVisibility(View.GONE);
                    glowPad.setVibrateEnabled(false);
                    moodGrid.setVisibility(View.VISIBLE);
                    whiteBackground.setVisibility(View.VISIBLE);
                    grid_triggered = true;
                }
                if (glowPad.mhandle == 0)
                    sleepiness_description = "Pleasure " + String.valueOf(target);
                txt.setText(sleepiness_description);
            }
        });
    }
}
