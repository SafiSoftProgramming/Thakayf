package com.example.root.thaqayif;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class CelebrateActivity extends AppCompatActivity {

    TextView textView_username;

    ImageView imageView_email;
    ImageView imageView_insta;
    ImageView imageView_youtube;
    ImageView imageView_facebook;

    Cursor c = null;
    String all_data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_celebrate);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// hide notification bar


        textView_username = (TextView) findViewById(R.id.textView_username);

        imageView_email = (ImageView) findViewById(R.id.imageView_gmil);
        imageView_insta = (ImageView) findViewById(R.id.imageView_insta);
        imageView_youtube = (ImageView) findViewById(R.id.imageView_youtube);
        imageView_facebook = (ImageView) findViewById(R.id.imageView_facebook);


        final DBthakaifconnction myDbHelper = new DBthakaifconnction(CelebrateActivity.this);
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



        textView_username.setText(c.getString(0));

        String user_name = c.getString(0);
        String user_age = c.getString(1);
        String user_gander = c.getString(2);
        String user_email = c.getString(3);
        all_data = user_name+"-"+user_age+"-"+user_gander+"-"+"   من فضلك اكتب تعليقك هنا    ";


        imageView_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendEmail();

            }
        });

        imageView_insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent instaIntent = new Intent(Intent.ACTION_VIEW);
                instaIntent.setData(Uri.parse("https://www.instagram.com/safisoft.programming/"));
                startActivity(instaIntent);

            }
        });

        imageView_youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent youtubelIntent = new Intent(Intent.ACTION_VIEW);
                youtubelIntent.setData(Uri.parse("https://www.youtube.com/channel/UCtC8eqUUZmktsUoFznAL91w"));
                startActivity(youtubelIntent);

            }
        });

        imageView_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                facebookIntent.setData(Uri.parse("https://www.facebook.com/SafiSoft.programming/"));
                startActivity(facebookIntent);

            }
        });


}

    protected void sendEmail() {
        Log.i("Send email", "");
        String[] TO = {"safisoft.programmer@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "مستخدم ثقائف");
        emailIntent.putExtra(Intent.EXTRA_TEXT, all_data);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email.", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(CelebrateActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
    }

