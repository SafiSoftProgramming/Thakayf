package com.example.root.thaqayif;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import java.io.IOException;
import java.util.Calendar;

public class LoginActivity extends AppCompatActivity {

    Spinner spinnerage;
    Spinner spinnerginder;

    String stage;
    String stginder;
    String getname;
    String getage;
    String getginder;
    String getmail ;

    Cursor c = null;
    RelativeLayout rlayoutlog ;


    TextView txvname,txvage,txvginder,txvmail,txvselectedage,txvselectedginder,txventerdata;
    EditText txdname,txdmail ;
    Button btnstart ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);// hide notification bar

         spinnerage       =(Spinner)findViewById(R.id.spinnerage);
         spinnerginder    =(Spinner)findViewById(R.id.spinnerginder);

        txvselectedage    =(TextView)findViewById(R.id.txvselectedage);
        txvselectedginder =(TextView)findViewById(R.id.txvselectedginder);
        txvname           =(TextView)findViewById(R.id.txvname);
        txvage            =(TextView)findViewById(R.id.txvage);
        txvginder         =(TextView)findViewById(R.id.txvginder);
        txvmail           =(TextView)findViewById(R.id.txvmail);
        txventerdata      =(TextView)findViewById(R.id.txventerdata);
        txdname           =(EditText)findViewById(R.id.txdname);
        txdmail           =(EditText)findViewById(R.id.txdmail);

        btnstart          =(Button)findViewById(R.id.btnstart);

        rlayoutlog        =(RelativeLayout)findViewById(R.id.rlayoutlog);


        final DBthakaifconnction myDbHelper = new DBthakaifconnction(LoginActivity.this);
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
        //c.moveToFirst();
        c.moveToPosition(0);


        String[] agenum =new String[] {"      أختر العمر المناسب      ","     من  10 الي 19 عام     ","     من 20 الي 39 عام     ",
                "     من 40 الي 49 عام     ","     من 50 الي 59 عام     ","     من 60 الي 69 عام     ","     من 70 الي 79 عام     ","     من 80 الي 89 عام     "};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, agenum);
        spinnerage.setAdapter(adapter);

        String [] ginder =new String[] {"     أختر النوع     ","     ذكر     ","     انثي     "};

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, ginder);
        spinnerginder.setAdapter(adapter1);



        spinnerage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                stage = spinnerage.getSelectedItem().toString();
               txvselectedage.setText(stage);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




        spinnerginder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                stginder = spinnerginder.getSelectedItem().toString();
              txvselectedginder.setText(stginder);


            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getname = txdname.getText().toString();
                getage  = txvselectedage.getText().toString();
                getginder = txvselectedginder.getText().toString();
                getmail =txvmail.getText().toString();



               // if (getname.isEmpty()||getage.equals("      أختر العمر المناسب      ")||getginder.equals("     أختر النوع     "))
                //{txventerdata.setText("من فضلك أدخل البيانات !!");}

                if (getname.isEmpty())
                {txdname.setBackgroundColor(Color.parseColor("#9DFF0000"));txventerdata.setText("من فضلك أدخل الأسم !!");}


                else if (getname.equalsIgnoreCase("player"))
                {txdname.setBackgroundColor(Color.parseColor("#9DFF0000"));txventerdata.setText("من فضلك أدخل أسم أخر !!");}



                else if (getage.equals("      أختر العمر المناسب      "))
                {txvselectedage.setBackgroundColor(Color.parseColor("#9DFF0000"));txventerdata.setText("من فضلك أدخل العمر !!");}



                else if (getginder.equals("     أختر النوع     "))
                {txvselectedginder.setBackgroundColor(Color.parseColor("#9DFF0000"));txventerdata.setText("من فضلك أدخل النوع !!");}


                else {
                    myDbHelper.updateRecordlogin("logindata","username","userage","gander","useremail","player",getname,getage,getginder,getmail);
                    Intent choose = new Intent(LoginActivity.this, ChooseActivity.class);
                    startActivity(choose);
                    setalarm();
                    addShortcut();
                    finish();
                }

            }
        });
        // clockwise();
       // zoom();
         fade();
       //blink();
       // move();
       // slide();

    }

    public void setalarm(){
//set alarm for notfiction
        Calendar calander_time = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        calander_time.set(Calendar.HOUR_OF_DAY,16);
        calander_time.set(Calendar.MINUTE,00);
        calander_time.set(Calendar.SECOND,00);
        if (now.after(calander_time)) {
            calander_time.add(Calendar.DATE, 1);
        }
        Intent intent_time = new Intent(getApplicationContext(),Notreciver.class);
        PendingIntent pendingIntent_time = PendingIntent.getBroadcast(getApplicationContext(),100,intent_time,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager_time = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager_time.setRepeating(AlarmManager.RTC_WAKEUP,calander_time.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent_time);

    }

    private void addShortcut() {
        //Adding shortcut for openingActivity
        //on Home screen
        Intent shortcutIntent = new Intent(getApplicationContext(),OpeningActivity.class);
        shortcutIntent.setAction(Intent.ACTION_MAIN);
        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "ثقائِفُ");
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                           Intent.ShortcutIconResource.fromContext(getApplicationContext(),R.drawable.ic_launcher));
        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        addIntent.putExtra("duplicate", false);
        getApplicationContext().sendBroadcast(addIntent);
    }

    private void slide() {
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide);
        txvname.startAnimation(animation1);
        txvage.startAnimation(animation1);
        txvginder.startAnimation(animation1);
        txvmail.startAnimation(animation1);
        txvselectedage.startAnimation(animation1);
        txvselectedginder.startAnimation(animation1);
        txdname.startAnimation(animation1);
        txdmail.startAnimation(animation1);
        spinnerage.startAnimation(animation1);
        spinnerginder.startAnimation(animation1);
        btnstart.startAnimation(animation1);
    }

    private void move() {
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move);

        txvname.startAnimation(animation1);
        txvage.startAnimation(animation1);
        txvginder.startAnimation(animation1);
        txvmail.startAnimation(animation1);
        txvselectedage.startAnimation(animation1);
        txvselectedginder.startAnimation(animation1);
        txdname.startAnimation(animation1);
        txdmail.startAnimation(animation1);
        spinnerage.startAnimation(animation1);
        spinnerginder.startAnimation(animation1);
        btnstart.startAnimation(animation1);
    }

    private void blink() {
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
    }

    private void fade() {

        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);

        rlayoutlog.startAnimation(animation1);
        /*txvname.startAnimation(animation1);
        txvage.startAnimation(animation1);
        txvginder.startAnimation(animation1);
        txvmail.startAnimation(animation1);
        txvselectedage.startAnimation(animation1);
        txvselectedginder.startAnimation(animation1);
        txdname.startAnimation(animation1);
        txdmail.startAnimation(animation1);
        spinnerage.startAnimation(animation1);
        spinnerginder.startAnimation(animation1);*/
        btnstart.startAnimation(animation1);

    }

    private void zoom() {
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.myanimation);
    }

    private void clockwise() {
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.clockwise);
        txvname.startAnimation(animation1);
        txvage.startAnimation(animation1);
        txvginder.startAnimation(animation1);
        txvmail.startAnimation(animation1);
        txvselectedage.startAnimation(animation1);
        txvselectedginder.startAnimation(animation1);
        txdname.startAnimation(animation1);
        txdmail.startAnimation(animation1);
        spinnerage.startAnimation(animation1);
        spinnerginder.startAnimation(animation1);
        btnstart.startAnimation(animation1);
    }



}
