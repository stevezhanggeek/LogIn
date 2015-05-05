package com.LogIn;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;

import java.util.Calendar;

public class AlarmReceiverNotification extends WakefulBroadcastReceiver {
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
  
    @Override
    public void onReceive(Context context, Intent intent) {
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        am.setRingerMode(AudioManager.RINGER_MODE_SILENT);

        switch (am.getRingerMode()) {
            case AudioManager.RINGER_MODE_SILENT:
                Utility.notification_mode = "Silent mode";
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                Utility.notification_mode = "Vibrate mode";
                break;
            case AudioManager.RINGER_MODE_NORMAL:
                Utility.notification_mode = "Normal mode";
                break;
        }
        System.out.println(Utility.notification_mode);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        new Intent(context, MainActivity.class),
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        String text_content = "";
        if (Utility.LogInType.equals("Sleepiness")) {
            text_content = "Click to journal Sleepiness.";
        } else if (Utility.LogInType.equals("Depression")) {
            text_content = "Click to journal Pleasure/Accomplishment.";
        } else {
            text_content = "Click to journal Mood.";
        }

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.glow_dot)
                        .setContentTitle("It's time to LogIn!")
                        .setSound(soundUri)
                        .setContentText(text_content)
                        .setContentIntent(resultPendingIntent)
                        .setAutoCancel(true);

        // Sets an ID for the notification, easy to remove then
        int mNotificationId = 715;
        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    public void setNotificationAlarm(Context context) {
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiverNotification.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, Utility.hour_start);
        calendar.set(Calendar.MINUTE, 00);

        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), AlarmManager.INTERVAL_HALF_HOUR, alarmIntent);
    }
}
