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
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        System.out.println(Utility.hour_start);
        if (hour < Utility.hour_start || hour >= Math.min(Utility.hour_start + Utility.num_hour_experiment_length, 23)) {
            return;
        }

        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

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

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        new Intent(context, MainActivity.class).putExtra("launchFrom", "notification"),
                        PendingIntent.FLAG_ONE_SHOT
                );

        String text_content;
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
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentText(text_content)
                        .setContentIntent(resultPendingIntent)
                        .setAutoCancel(true);

        // Sets an ID for the notification, easy to remove then
        int mNotificationId = 715;
        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());

        Utility.notificationWriteToParse("Show");
    }

    public void setNotificationAlarm(Context context) {
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiverNotification.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        /*
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        if (minute > 30) {
            calendar.set(Calendar.HOUR_OF_DAY, hour + 1);
            calendar.set(Calendar.MINUTE, 00);
        } else {
            calendar.set(Calendar.MINUTE, 30);
        }
*/
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), AlarmManager.INTERVAL_HALF_HOUR, alarmIntent);
    }
}
