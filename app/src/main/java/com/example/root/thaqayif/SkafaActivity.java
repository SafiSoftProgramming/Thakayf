package com.example.root.thaqayif;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.io.IOException;

import pl.droidsonroids.gif.GifTextView;


public class SkafaActivity extends AppCompatActivity {

    Button questionsshow ;
    Button banswer1 ;
    Button banswer2 ;
    Button banswer3 ;
    Button bqustionshow ;
    TextView Txvscorenum ;
    TextView txvlifes;
    Cursor c = null;
    String ans1 ="" ;
    String ans2 ="";
    String ans3 ="";
    String answer ;
    String scoreString ="";
    int scoreconvertint= 0 ;
    AdView adViewsince;
    InterstitialAd mInterstitialAd;
    MediaPlayer wrongsound ;
    MediaPlayer bagdrob ;
    DBthakaifconnction myDbHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skafa);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);// hide notification bar


        questionsshow =(Button)findViewById(R.id.questionsshow);
        banswer1 =(Button)findViewById(R.id.banswer1);
        banswer2 =(Button)findViewById(R.id.banswer2);
        banswer3 =(Button)findViewById(R.id.banswer3);
        bqustionshow =(Button)findViewById(R.id.bqustionshow);
        Txvscorenum =(TextView)findViewById(R.id.Txvscorenum);
        txvlifes =(TextView)findViewById(R.id.txvlifes);

        bqustionshow.setClickable(false);
        questionsshow.setClickable(false);



        MobileAds.initialize(this, "ca-app-pub-3940256099942544/6300978111");//testad
        // MobileAds.initialize(this, "ca-app-pub-5637187199850424/5441358861");//real add
        adViewsince =(AdView) findViewById(R.id.adViewsince);
        AdRequest adRequest = new AdRequest.Builder().build();
        adViewsince.loadAd(adRequest);


        mInterstitialAd = new InterstitialAd(this);
        // mInterstitialAd.setAdUnitId("ca-app-pub-5637187199850424/7673125757");//real
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");//test
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


        wrongsound = MediaPlayer.create(SkafaActivity.this, R.raw.wrongsound);
        wrongsound.setAudioStreamType(AudioManager.STREAM_MUSIC);
        bagdrob = MediaPlayer.create(SkafaActivity.this,R.raw.bagdrob);
        bagdrob.setAudioStreamType(AudioManager.STREAM_MUSIC);


        myDbHelper = new DBthakaifconnction(SkafaActivity.this);
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
        //  Toast.makeText(SinceActivity.this, "Successfully Imported", Toast.LENGTH_SHORT).show();


        c = myDbHelper.queryskafa("skafa", null, null, null, null, null, null);
        c.moveToPosition(0);
        //c.getPosition();
        //c.moveToFirst();


        String questionsshowtext = c.getString(1);
        String answerposs = c.getString(5);

        if (answerposs.equals("1")){

            ans1 = c.getString(2);
            ans2 = c.getString(3);
            ans3 = c.getString(4);}

        if (answerposs.equals("2")){

            ans1 = c.getString(4);
            ans2 = c.getString(2);
            ans3 = c.getString(3);}

        if (answerposs.equals("3")){

            ans1 = c.getString(3);
            ans2 = c.getString(4);
            ans3 = c.getString(2);}

        scoreString =c.getString(6);


        bqustionshow.setText(questionsshowtext);
        banswer1.setText(ans1);
        banswer2.setText(ans2);
        banswer3.setText(ans3);
        Txvscorenum.setText(scoreString);


        banswer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String btxt = banswer1.getText().toString();
                answer = c.getString(2);
                if (btxt.equals(answer))
                {
                    banswer1.setBackgroundResource(R.drawable.qutionbakgreen);
                    ifbouttomanswerwrght();
                }
                else
                {
                    banswer1.setBackgroundResource(R.drawable.qutionbakred);
                    banswer1.setClickable(false);
                    elseanswerwrongbouttom();
                }
            }
        });

        banswer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String btxt = banswer2.getText().toString();
                final  String answer = c.getString(2);
                if (btxt.equals(answer))
                {
                    banswer2.setBackgroundResource(R.drawable.qutionbakgreen);
                    ifbouttomanswerwrght();
                }
                else
                {
                    banswer2.setBackgroundResource(R.drawable.qutionbakred);
                    banswer2.setClickable(false);
                    elseanswerwrongbouttom();
                }
            }
        });

        banswer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String btxt = banswer3.getText().toString();
                final    String answer = c.getString(2);
                if (btxt.equals(answer))
                {
                    banswer3.setBackgroundResource(R.drawable.qutionbakgreen);
                    ifbouttomanswerwrght();
                }
                else
                {
                    banswer3.setBackgroundResource(R.drawable.qutionbakred);
                    banswer3.setClickable(false);
                    elseanswerwrongbouttom();
                }
            }
        });

    }




    public void ifbouttomanswerwrght(){

        banswer1.setClickable(false);
        banswer2.setClickable(false);
        banswer3.setClickable(false);
        bagdrob.start();





        new CountDownTimer(1000, 1000) { //count to remove heartbrahe gif
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {



                scoreconvertint =Integer.parseInt(Txvscorenum.getText().toString());
                int sumscore = scoreconvertint + 10 ;
                String numberAsString = Integer.toString(sumscore);
                Txvscorenum.setText(numberAsString);
                int id= c.getInt(0);
                int idsum = id + 1;
                String idsumstring =Integer.toString(idsum);

                myDbHelper.updateRecord("skafa","score",idsumstring,numberAsString);
                myDbHelper.updateRecord("score","skafa_score","1",numberAsString);


                String deletposs =c.getString(0);
                myDbHelper.deleteRecordAlternate("skafa","_id",deletposs);

                banswer1.setBackgroundResource(R.drawable.qutionbak);
                banswer2.setBackgroundResource(R.drawable.qutionbak);
                banswer3.setBackgroundResource(R.drawable.qutionbak);

                banswer1.setClickable(true);
                banswer2.setClickable(true);
                banswer3.setClickable(true);

                if (c.moveToNext())
                {
                    String questionsshowtext = c.getString(1);
                    String answerposs = c.getString(5);

                    if (answerposs.equals("1")){

                        ans1 = c.getString(2);
                        ans2 = c.getString(3);
                        ans3 = c.getString(4);}

                    if (answerposs.equals("2")){

                        ans1 = c.getString(4);
                        ans2 = c.getString(2);
                        ans3 = c.getString(3);}

                    if (answerposs.equals("3")){

                        ans1 = c.getString(3);
                        ans2 = c.getString(4);
                        ans3 = c.getString(2);}


                    bqustionshow.setText(questionsshowtext);
                    banswer1.setText(ans1);
                    banswer2.setText(ans2);
                    banswer3.setText(ans3);
                }
                else {
                    Intent celebrateactivity =new Intent( SkafaActivity.this, CelebrateActivity.class);
                    startActivity(celebrateactivity);
                    finish();
                }


            }
        }.start();


    }
    public void elseanswerwrongbouttom(){




        int lifes = Integer.parseInt(txvlifes.getText().toString());
        String lifesshow = Integer.toString(lifes - 1);
        txvlifes.setText(lifesshow);

        wrongsound.start();

        if (txvlifes.getText().equals("0")){
            Intent intent = getIntent();
            finish();
            startActivity(intent);

            MobileAds.initialize(SkafaActivity.this, "ca-app-pub-5637187199850424/5441358861");//real add
            adViewsince =(AdView) findViewById(R.id.adViewsince);
            AdRequest adRequest = new AdRequest.Builder().build();
            adViewsince.loadAd(adRequest);

            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            }

        }

        new CountDownTimer(3000, 1000) { //count to remove heartbrahe gif
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {



            }
        }.start();


    }
}
