package com.LogIn;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;

import java.util.Calendar;

public class AlarmReceiver extends WakefulBroadcastReceiver {
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
  
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent alarmIntent = new Intent();
        alarmIntent.setClass(context, RateAlert.class);
        alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(alarmIntent);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        new Intent(context, MainActivity.class),
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        String text_content;
        if (Utility.LogInType.equals("Sleepiness")) {
            text_content = "Click to journal your current Sleepiness.";
        } else if (Utility.LogInType.equals("Depression")) {
            text_content = "Click to journal your current Accomplishment/Pleasure.";
        } else {
            text_content = "Click to journal your current Mood.";
        }

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.glow_dot)
                        .setContentTitle("It's time to LogIn!")
                        .setContentText(text_content)
                        .setContentIntent(resultPendingIntent);

        // Sets an ID for the notification, easy to remove then
        int mNotificationId = 715;
        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    public void setAlarm(Context context) {
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 00);

        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
    }
}
