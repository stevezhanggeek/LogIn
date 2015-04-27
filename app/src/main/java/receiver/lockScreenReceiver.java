package receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.LogIn.LockscreenDepression;
import com.LogIn.LockscreenMood;
import com.LogIn.LockscreenSleepiness;
import com.LogIn.MainActivity;

public class lockScreenReceiver extends BroadcastReceiver  {
    public static boolean wasScreenOn = true;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            wasScreenOn=false;
            context.startActivity(new Intent(context, LockscreenMood.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            wasScreenOn=true;
            System.out.println("On");
        }
        else if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            context.startActivity(new Intent(context, LockscreenSleepiness.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            context.startActivity(new Intent(context, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }
}