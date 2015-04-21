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

public class SampleActivity extends Activity {
    private boolean grid_triggered = false;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.main);

        if(getIntent()!=null&&getIntent().hasExtra("kill")&&getIntent().getExtras().getInt("kill")==1){
            finish();
        }
        // initialize receiver
        startService(new Intent(this, MyService.class));

        // Use user desktop wallpaper in lockscreen
        final WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        final Drawable wallpaperDrawable = wallpaperManager.getFastDrawable();
        final GlowPadView glowPad = (GlowPadView) findViewById(R.id.incomingCallWidget);
        final ImageView moodGrid = (ImageView) findViewById(R.id.mood_grid);
        RelativeLayout ll = (RelativeLayout) findViewById(R.id.main);
        ll.setBackground(wallpaperDrawable);
        grid_triggered = false;

        glowPad.setOnTriggerListener(new GlowPadView.OnTriggerListener() {
            @Override
            public void onGrabbed(View v, int handle) {
            }

            @Override
            public void onReleased(View v, int handle) {
                if (grid_triggered) {
                    moodGrid.setVisibility(View.INVISIBLE);
                    Toast.makeText(SampleActivity.this, String.valueOf(glowPad.mgrid_X) + ", " + String.valueOf(glowPad.mgrid_Y), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onTrigger(View v, int target) {
                String sleepiness_description = "";
                switch(target) {
                    case 3:
                        sleepiness_description = "Feeling active, vital, alert, or wide awake";
                        break;
                    case 4:
                        sleepiness_description = "Functioning at high levels, but not at peak; able to concentrate";
                        break;
                    case 5:
                        sleepiness_description = "Awake, but relaxed; responsive but not fully alert";
                        break;
                    case 6:
                        sleepiness_description = "Somewhat foggy, let down";
                        break;
                    case 7:
                        sleepiness_description = "Foggy; losing interest in remaining awake; slowed down";
                        break;
                    case 8:
                        sleepiness_description = "Sleepy, woozy, fighting sleep; prefer to lie down";
                        break;
                    case 9:
                        sleepiness_description = "No longer fighting sleep, sleep onset soon; having dream-like thoughts";

                        break;
                }
                if (!sleepiness_description.equals("")) {
                    //Toast.makeText(SampleActivity.this, sleepiness_description, Toast.LENGTH_SHORT).show();
                }
                glowPad.reset(true);
                v.setVisibility(View.GONE);
                finish();
            }

            @Override
            public void onGrabbedStateChange(View v, int handle) {
            }

            @Override
            public void onFinishFinalAnimation() {
                // Do nothing
            }

            @Override
            public void onMovedOnTarget(int target) {
                System.out.println(target);
                final TextView txt = (TextView) findViewById(R.id.textView);
                String sleepiness_description = "";
                switch(target) {
                    case 3:
                        sleepiness_description = "Feeling active, vital, alert, or wide awake";
                        glowPad.setVisibility(View.GONE);
                        glowPad.setVibrateEnabled(false);
                        moodGrid.setVisibility(View.VISIBLE);
                        grid_triggered = true;
                        break;
                    case 4:
                        sleepiness_description = "Functioning at high levels, but not at peak; able to concentrate";
                        break;
                    case 5:
                        sleepiness_description = "Awake, but relaxed; responsive but not fully alert";
                        break;
                    case 6:
                        sleepiness_description = "Somewhat foggy, let down";
                        break;
                    case 7:
                        sleepiness_description = "Foggy; losing interest in remaining awake; slowed down";
                        break;
                    case 8:
                        sleepiness_description = "Sleepy, woozy, fighting sleep; prefer to lie down";
                        break;
                    case 9:
                        sleepiness_description = "No longer fighting sleep, sleep onset soon; having dream-like thoughts";
                        break;
                }
                if (glowPad.mhandle == 0) sleepiness_description = "Pleasure " + String.valueOf(target);
                txt.setText(sleepiness_description);
            }
        });
    }
}
