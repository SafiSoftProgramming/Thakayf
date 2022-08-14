package com.thakaif.root.thaqayif;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.view.WindowManager;
import com.facebook.AccessToken;
import com.google.android.material.snackbar.Snackbar;
import java.io.IOException;
import java.util.Calendar;

public class SplashActivity extends AppCompatActivity {

    Cursor c = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
      //  getWindow().getDecorView().getWindowInsetsController().hide(android.view.WindowInsets.Type.statusBars() | android.view.WindowInsets.Type.navigationBars());  // hide notification bar android 30
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);// hide notification bar

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        DetectInternet detectInternet =new DetectInternet();
        if(detectInternet.isConnected(getApplicationContext())) {

            setalarm();

            final DBthakaifconnction myDbHelper = new DBthakaifconnction(SplashActivity.this);
            try {
                myDbHelper.createDataBase();
            } catch (IOException ioe) {
                throw new Error("Unable to create database");
            }
            try {
                myDbHelper.openDataBase();
            } catch (SQLException sqle) {
                throw sqle;
            }
            c = myDbHelper.querylogindata("logindata", null, null, null, null, null, null);
            c.moveToFirst();

            if (c.getString(0).equals("player_thakayif_default") && AccessToken.getCurrentAccessToken() == null) {
                Go_to_Login();
            } else {
                Go_to_Start();
            }


            // reminderNotification();
        }
        else {
            Snackbar.make(findViewById(android.R.id.content), "تأكد من أتصالك بالأنترنت", Snackbar.LENGTH_LONG).show();
        }

    }


    public void Go_to_Login() {
        new CountDownTimer(500, 500) {
            public void onTick(long millisUntilFinished) { }
            public void onFinish() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }.start();
    }

    public void Go_to_Start() {
        new CountDownTimer(500, 500) {
            public void onTick(long millisUntilFinished) { }
            public void onFinish() {
                Intent intent = new Intent(SplashActivity.this, StartActivity.class);
                startActivity(intent);
                finish();
            }
        }.start();
    }

    public void reminderNotification()
    {
        NotificationUtils _notificationUtils = new NotificationUtils(this);
        long _currentTime = System.currentTimeMillis();
        long tenSeconds = 1000 * 10;
        long _triggerReminder = _currentTime + tenSeconds; //triggers a reminder after 10 seconds.
        _notificationUtils.setReminder(_triggerReminder);
    }

    public void setalarm(){//set alarm for notfiction
        Calendar calander_time = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        calander_time.set(Calendar.HOUR_OF_DAY,16);
        calander_time.set(Calendar.MINUTE,00);
        calander_time.set(Calendar.SECOND,00);
        if (now.after(calander_time)) {
            calander_time.add(Calendar.DATE, 1);
        }
        Intent intent_time = new Intent(getApplicationContext(),ReminderBroadcast.class);
        PendingIntent pendingIntent_time = PendingIntent.getBroadcast(getApplicationContext(),100,intent_time,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager_time = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager_time.setRepeating(AlarmManager.RTC_WAKEUP,calander_time.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent_time);

    }




}