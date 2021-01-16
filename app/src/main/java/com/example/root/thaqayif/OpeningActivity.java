package com.example.root.thaqayif;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;


import java.io.IOException;

public class OpeningActivity extends AppCompatActivity {


    Cursor c = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_opening);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);// hide notification bar

        final DBthakaifconnction myDbHelper = new DBthakaifconnction(OpeningActivity.this);
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
       // c.moveToPosition(0);


        new CountDownTimer(6000, 1000) { //count to move to login activity

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {





                    if(c.getString(0).equals("player")) {

                    Intent login = new Intent(OpeningActivity.this, LoginActivity.class);
                    startActivity(login);
                    finish();// end Activity stop going back to first Activity
               }
                else{



                   Intent login = new Intent(OpeningActivity.this, ChooseActivity.class);
                   startActivity(login);
                   finish();// end Ac
                }




            }

        }.start();





        /**
         * Back button listener.
         * Will close the application if the back button pressed twice.
         *//*
                @Override
                public void onBackPressed()
                {
                    if(backButtonCount >= 1)
                    {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
                        backButtonCount++;
                    }
                }*/


    }
}
