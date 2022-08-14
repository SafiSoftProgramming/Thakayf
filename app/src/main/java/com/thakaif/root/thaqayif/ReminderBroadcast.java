package com.thakaif.root.thaqayif;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import java.util.Calendar;
import static android.content.Context.ALARM_SERVICE;

public class ReminderBroadcast extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {


            Calendar calander_time = Calendar.getInstance();
            Calendar now = Calendar.getInstance();
            calander_time.set(Calendar.HOUR_OF_DAY, 16);
            calander_time.set(Calendar.MINUTE, 00);
            calander_time.set(Calendar.SECOND, 00);
            if (now.after(calander_time)) {
                calander_time.add(Calendar.DATE, 1);
            }
            Intent intent_time = new Intent(context.getApplicationContext(), ReminderBroadcast.class);
            PendingIntent pendingIntent_time = PendingIntent.getBroadcast(context.getApplicationContext(), 100, intent_time, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager_time = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            alarmManager_time.setRepeating(AlarmManager.RTC_WAKEUP, calander_time.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent_time);
        }

        else {
            NotificationUtils _notificationUtils = new NotificationUtils(context);
            NotificationCompat.Builder _builder = _notificationUtils.setNotification("أبدأ ثقائف", "وقت المعرفة أدخل ثقائف الأن واعرف معلومة جديدة ");
            _notificationUtils.getManager().notify(101, _builder.build());
        }
    }
}