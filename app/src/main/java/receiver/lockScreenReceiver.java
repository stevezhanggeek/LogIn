package receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.LogIn.Lockscreen;
import com.LogIn.MainActivity;

public class lockScreenReceiver extends BroadcastReceiver  {
    public static boolean wasScreenOn = true;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            wasScreenOn=false;
            context.startActivity(new Intent(context, Lockscreen.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            wasScreenOn=true;
            System.out.println("On");

        }
        else if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            context.startActivity(new Intent(context, Lockscreen.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }
}
