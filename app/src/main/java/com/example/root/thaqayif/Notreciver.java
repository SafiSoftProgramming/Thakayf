package com.example.root.thaqayif;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by root on 2/1/18.
 */

public class Notreciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {


            Calendar calander_time = Calendar.getInstance();
            Calendar now = Calendar.getInstance();
            calander_time.set(Calendar.HOUR_OF_DAY, 16);
            calander_time.set(Calendar.MINUTE, 00);
            calander_time.set(Calendar.SECOND, 00);
            if (now.after(calander_time)) {
                calander_time.add(Calendar.DATE, 1);
            }
            Intent intent_time = new Intent(context.getApplicationContext(), Notreciver.class);
            PendingIntent pendingIntent_time = PendingIntent.getBroadcast(context.getApplicationContext(), 100, intent_time, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager_time = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            alarmManager_time.setRepeating(AlarmManager.RTC_WAKEUP, calander_time.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent_time);
        }

        else {


            NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            Intent intent_repet = new Intent(context, OpeningActivity.class);
            intent_repet.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent_repet = PendingIntent.getActivity(context, 100, intent_repet, PendingIntent.FLAG_UPDATE_CURRENT);

            Uri uri = Uri.parse("android.resource://"
                    + context.getPackageName() + "/" + R.raw.bagdrob);

            NotificationCompat.Builder builder_repet = new NotificationCompat.Builder(context)

                    .setContentIntent(pendingIntent_repet)
                    .setSmallIcon(R.drawable.ic_stat)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                    .setColor(R.drawable.qutionbakgreen)
                    .setContentTitle("ثقائِف")
                    .setContentText("الأن وقت معلومة جديدة أضغط وادخل ألي المعرفة")
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setSound(uri)
                    .setPriority(NotificationManager.IMPORTANCE_HIGH)
                    .setAutoCancel(true);

            notificationManager.notify(100, builder_repet.build());


        }




    }
}
