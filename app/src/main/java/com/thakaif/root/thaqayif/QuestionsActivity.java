package com.thakaif.root.thaqayif;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static android.content.ContentValues.TAG;

public class QuestionsActivity extends AppCompatActivity {

    private AdView adView_Banner;
    private InterstitialAd mInterstitialAd;
    DBthakaifconnction myDbHelper ;
    Cursor c = null;
    Cursor c_time = null;
    Cursor c_total_score = null ;
    String Answer1 ="" ;
    String Answer2 ="";
    String Answer3 ="";
    String Wright_Answer ;
    String scoreString ="";
    TextView txtv_question_view , txtv_answer_one , txtv_answer_two , txtv_answer_three ;
    ImageButton btn_thakafe , btn_reyada , btn_gografya , btn_moktaree , btn_eloom , btn_tareek;
    TextView txtv_score ;
    ImageButton btn_answer_one , btn_answer_two , btn_answer_three ;
    MediaPlayer Answer_Wright_Sound ;
    MediaPlayer Answer_Wrong_Sound ;
    MediaPlayer Time_Tick_Sound ;
    MediaPlayer Time_End ;
    TextView txtv_life_count ;
    String TYPE_FROM_INTENT = "";
    String FINAL_TABLE_NAME = "";
    String FINAL_SCORE_COLUMN_NAME = "";
    String FINAL_TIME_COLUMN_NAME = "";
    TextView txtv_question_type ;
    ImageButton imgv_question_type ;
    TextView txtv_question_timer ;
    ImageView gif_time ;
    CountDownTimer Questions_countDownTimer ;
    CountDownTimer Sign_countDownTimer ;
    CountDownTimer if_Answer_wright_countDownTimer;
    CountDownTimer if_answer_wrong_one_countDownTimer;
    CountDownTimer if_answer_wrong_two_countDownTimer;

    Boolean is_Answer_wright_countDownTimer = false;
    Boolean is_answer_wrong_one_countDownTimer = false;
    Boolean is_answer_wrong_two_countDownTimer = false;

    ImageButton WRIGHT_ANSWER_BUTTON ;
    LinearLayout lay_facebook_hint , lay_time_hint ;
    TextView txtv_total_score ;
    TextView txtv_score_name ;
    ImageButton btn_go_home ;
    ImageButton btn_share_help_facebook ;
    ImageView player_circular_image ;
    AccessToken accessToken ;
    GraphRequest request  ;
    String USER_ID = "";
    String USER_NAME ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        TYPE_FROM_INTENT = getIntent().getStringExtra("TYPE");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        DetectInternet detectInternet =new DetectInternet();
        if(detectInternet.isConnected(getApplicationContext())) {

        adView_Banner = findViewById(R.id.adView_Banner);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView_Banner.loadAd(adRequest);
        txtv_question_view = findViewById(R.id.txtv_question_view);
        txtv_answer_one =findViewById(R.id.txtv_answer_one);
        txtv_answer_two =findViewById(R.id.txtv_answer_two);
        txtv_answer_three = findViewById(R.id.txtv_answer_three);
        txtv_score = findViewById(R.id.txtv_score);
        btn_answer_one = findViewById(R.id.btn_answer_one);
        btn_answer_two =findViewById(R.id.btn_answer_two);
        btn_answer_three = findViewById(R.id.btn_answer_three);
        txtv_life_count = findViewById(R.id.txtv_life_count);
        txtv_question_type = findViewById(R.id.txtv_question_type);
        imgv_question_type = findViewById(R.id.imgv_question_type);
        btn_thakafe = findViewById(R.id.btn_thakafe);
        btn_reyada = findViewById(R.id.btn_reyada);
        btn_gografya = findViewById(R.id.btn_gografya);
        btn_moktaree =findViewById(R.id.btn_moktaree);
        btn_eloom = findViewById(R.id.btn_eloom);
        btn_tareek = findViewById(R.id.btn_tareek);
        txtv_question_timer = findViewById(R.id.txtv_question_timer);
        gif_time = findViewById(R.id.gif_time);
        lay_facebook_hint =findViewById(R.id.lay_facebook_hint);
        lay_time_hint =findViewById(R.id.lay_time_hint);
        txtv_total_score = findViewById(R.id.txtv_total_score);
        txtv_score_name  =findViewById(R.id.txtv_score_name);
        btn_go_home = findViewById(R.id.btn_go_home);
        btn_share_help_facebook =findViewById(R.id.btn_share_help_facebook);
        player_circular_image = findViewById(R.id.player_circular_image);

        Answer_Wright_Sound = MediaPlayer.create(QuestionsActivity.this,R.raw.answer_wright);
        Answer_Wright_Sound.setAudioStreamType(AudioManager.STREAM_MUSIC);
        Answer_Wrong_Sound = MediaPlayer.create(QuestionsActivity.this, R.raw.answer_wrong);
        Answer_Wrong_Sound.setAudioStreamType(AudioManager.STREAM_MUSIC);
        Time_Tick_Sound = MediaPlayer.create(QuestionsActivity.this, R.raw.time_tick_sound);
        Time_Tick_Sound.setAudioStreamType(AudioManager.STREAM_MUSIC);
        Time_End = MediaPlayer.create(QuestionsActivity.this, R.raw.time_end);
        Time_End.setAudioStreamType(AudioManager.STREAM_MUSIC);




        myDbHelper = new DBthakaifconnction(QuestionsActivity.this);
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

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdRequest adRequest2 = new AdRequest.Builder().build();
        // real ad code ca-app-pub-5637187199850424/2969224290   test code ca-app-pub-3940256099942544/1033173712
        InterstitialAd.load(this,"ca-app-pub-5637187199850424/2969224290", adRequest2, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;
                Log.i(TAG, "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                Log.i(TAG, loadAdError.getMessage());
                mInterstitialAd = null;
            }
        });


        accessToken = AccessToken.getCurrentAccessToken();

        c = myDbHelper.querylogindata("logindata", null, null, null, null, null, null);
        c.moveToPosition(0);

        if(!c.getString(0).equals("player_thakayif_default")){
            // txtv_player_name.setText(c.getString(0));
        }
        else {
            request = GraphRequest.newMeRequest(
                    accessToken,
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            try {
                                USER_ID = object.getString("id");
                                USER_NAME = object.getString("name");
                                // txtv_player_name.setText(USER_NAME);
                                URL url = new URL("https://graph.facebook.com/"+ object.getString("id")+"/picture?type=large&access_token=GG|246070050613989|5Q1yiEG8Ug3GJXO_FdTyaBohVeQ");
                                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                                player_circular_image.setImageBitmap(bmp);
                            } catch (JSONException | MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name");
            request.setParameters(parameters);
            request.executeAsync();
        }



        btn_thakafe.setClickable(false);
        btn_reyada.setClickable(false);
        btn_gografya.setClickable(false);
        btn_moktaree.setClickable(false);
        btn_eloom.setClickable(false);
        btn_tareek.setClickable(false);

        LOAD_QUESTIONS();

        btn_answer_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String CHOOSE_ANSWER =  txtv_answer_one.getText().toString();
                if(CHOOSE_ANSWER.equals(Wright_Answer)){
                    btn_answer_one.setBackgroundResource(R.drawable.btn_eff_answer_right);
                    IF_ANSWER_WRIGHT();
                }
                else {
                    btn_answer_one.setBackgroundResource(R.drawable.btn_eff_answer_wrong);
                    btn_answer_one.setClickable(false);
                    IF_ANSWER_WRONG();
                }
            }
        });

        btn_answer_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String CHOOSE_ANSWER =  txtv_answer_two.getText().toString();
                if(CHOOSE_ANSWER.equals(Wright_Answer)){
                    btn_answer_two.setBackgroundResource(R.drawable.btn_eff_answer_right);
                    IF_ANSWER_WRIGHT();
                }
                else {
                    btn_answer_two.setBackgroundResource(R.drawable.btn_eff_answer_wrong);
                    btn_answer_two.setClickable(false);
                    IF_ANSWER_WRONG();
                }
            }
        });

        btn_answer_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String CHOOSE_ANSWER =  txtv_answer_three.getText().toString();
                if(CHOOSE_ANSWER.equals(Wright_Answer)){
                    btn_answer_three.setBackgroundResource(R.drawable.btn_eff_answer_right);
                    IF_ANSWER_WRIGHT();
                }
                else {
                    btn_answer_three.setBackgroundResource(R.drawable.btn_eff_answer_wrong);
                    btn_answer_three.setClickable(false);
                    IF_ANSWER_WRONG();
                }
            }
        });


        btn_thakafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TYPE_FROM_INTENT = "THAKAFE";
                Questions_countDownTimer.cancel();
                LOAD_QUESTIONS();
            }
        });
        btn_reyada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TYPE_FROM_INTENT = "REYADA";
                Questions_countDownTimer.cancel();
                LOAD_QUESTIONS();
            }
        });
        btn_gografya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TYPE_FROM_INTENT = "GOGRAFYA";
                Questions_countDownTimer.cancel();
                LOAD_QUESTIONS();
            }
        });
        btn_moktaree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TYPE_FROM_INTENT = "MOKTAREE";
                Questions_countDownTimer.cancel();
                LOAD_QUESTIONS();
            }
        });
        btn_eloom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TYPE_FROM_INTENT = "ELOOM";
                Questions_countDownTimer.cancel();
                LOAD_QUESTIONS();
            }
        });
        btn_tareek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TYPE_FROM_INTENT = "TAREEK";
                Questions_countDownTimer.cancel();
                LOAD_QUESTIONS();
            }
        });
        btn_go_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Time_Tick_Sound.pause();
                Questions_countDownTimer.cancel();

                if(is_Answer_wright_countDownTimer){
                    if_Answer_wright_countDownTimer.cancel();
                }
                if(is_answer_wrong_one_countDownTimer){
                    if_answer_wrong_one_countDownTimer.cancel();
                }
                if(is_answer_wrong_two_countDownTimer){
                    if_answer_wrong_two_countDownTimer.cancel();
                }

                Intent intent = new Intent(QuestionsActivity.this, StartActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btn_share_help_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuestionsActivity.this, FacebookPostActivity.class);
                intent.putExtra("String_Question_Category",txtv_question_type.getText().toString());
                intent.putExtra("String_Question",txtv_question_view.getText().toString() );
                intent.putExtra("String_Question_Answer_one",Answer1 );
                intent.putExtra("String_Question_Answer_two",Answer2 );
                intent.putExtra("String_Question_Answer_three",Answer3 );
                startActivity(intent);
                finish();

                /*
                Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.ic_facebook_post_bg);
                SharePhoto photo = new SharePhoto.Builder()
                        .setBitmap(icon)
                        .setCaption("jhgjkju")
                        .build();
                SharePhotoContent content = new SharePhotoContent.Builder()
                        .addPhoto(photo)
                        .build();
                ShareDialog shareDialog = new ShareDialog(QuestionsActivity.this);
                shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);
*/
            }
        });



        }
        else {
            Intent intent = new Intent(QuestionsActivity.this, SplashActivity.class);
            startActivity(intent);
            finish();
        }



    }


    private void LOAD_QUESTIONS(){
     //   txtv_question_timer.setText("30");

        Show_Total_Score();
        c_time = myDbHelper.querytime(null, null, null, null, null, null, null);
        c_time.moveToPosition(0);

        if(TYPE_FROM_INTENT.equals("THAKAFE")){
            FINAL_TABLE_NAME = "skafa";
            FINAL_SCORE_COLUMN_NAME = "skafa_score";
            FINAL_TIME_COLUMN_NAME = "skafa_time";
            txtv_question_type.setText("ثقافة");
            txtv_score_name.setText("نقاط ثقافة");
            ENABLE_NAVIGATION_BUTTONS(btn_thakafe);
            RESET_ANSWER_BUTTONS();
            txtv_question_timer .setText(c_time.getString(5));
            if(Integer.parseInt(c_time.getString(5)) <= 5){
                txtv_question_timer.setText("10");
            }
            imgv_question_type.setBackgroundResource(R.drawable.btn_eff_thakafa);
        }
        if(TYPE_FROM_INTENT.equals("REYADA")){
            FINAL_TABLE_NAME = "sport";
            FINAL_SCORE_COLUMN_NAME = "sport_score";
            FINAL_TIME_COLUMN_NAME = "sport_time";
            txtv_question_type.setText("رياضة");
            txtv_score_name.setText("نقاط رياضة");
            ENABLE_NAVIGATION_BUTTONS(btn_reyada);
            RESET_ANSWER_BUTTONS();
            txtv_question_timer .setText(c_time.getString(6));
            if(Integer.parseInt(c_time.getString(6)) <= 5){
                txtv_question_timer.setText("10");
            }
            imgv_question_type.setBackgroundResource(R.drawable.btn_eff_reyada);
        }
        if(TYPE_FROM_INTENT.equals("GOGRAFYA")){
            FINAL_TABLE_NAME = "gorafya";
            FINAL_SCORE_COLUMN_NAME = "gorafya_score";
            FINAL_TIME_COLUMN_NAME = "gorafya_time";
            txtv_question_type.setText("جغرافيا");
            txtv_score_name.setText("نقاط جغرافيا");
            ENABLE_NAVIGATION_BUTTONS(btn_gografya);
            RESET_ANSWER_BUTTONS();
            txtv_question_timer .setText(c_time.getString(1));
            if(Integer.parseInt(c_time.getString(1)) <= 5){
                txtv_question_timer.setText("10");
            }
            imgv_question_type.setBackgroundResource(R.drawable.btn_eff_gografya);
        }
        if(TYPE_FROM_INTENT.equals("MOKTAREE")){
            FINAL_TABLE_NAME = "invint";
            FINAL_SCORE_COLUMN_NAME = "invint_score";
            FINAL_TIME_COLUMN_NAME = "inivint_time";
            txtv_question_type.setText("مخترعين");
            txtv_score_name.setText("نقاط مخترعين");
            ENABLE_NAVIGATION_BUTTONS(btn_moktaree);
            RESET_ANSWER_BUTTONS();
            txtv_question_timer .setText(c_time.getString(3));
            if(Integer.parseInt(c_time.getString(3)) <= 5){
                txtv_question_timer.setText("10");
            }
            imgv_question_type.setBackgroundResource(R.drawable.btn_eff_moktaree);
        }
        if(TYPE_FROM_INTENT.equals("ELOOM")){
            FINAL_TABLE_NAME = "since";
            FINAL_SCORE_COLUMN_NAME = "since_score";
            FINAL_TIME_COLUMN_NAME = "since_time";
            txtv_question_type.setText("علوم");
            txtv_score_name.setText("نقاط علوم");
            ENABLE_NAVIGATION_BUTTONS(btn_eloom);
            RESET_ANSWER_BUTTONS();
            txtv_question_timer .setText(c_time.getString(4));
            if(Integer.parseInt(c_time.getString(4)) <= 5){
                txtv_question_timer.setText("10");
            }
            imgv_question_type.setBackgroundResource(R.drawable.btn_eff_eloom);
        }
        if(TYPE_FROM_INTENT.equals("TAREEK")){
            FINAL_TABLE_NAME = "history";
            FINAL_SCORE_COLUMN_NAME = "history_score";
            FINAL_TIME_COLUMN_NAME = "history_time";
            txtv_question_type.setText("تاريخ");
            txtv_score_name.setText("نقاط تاريخ");
            ENABLE_NAVIGATION_BUTTONS(btn_tareek);
            RESET_ANSWER_BUTTONS();
            txtv_question_timer .setText(c_time.getString(2));
            if(Integer.parseInt(c_time.getString(2)) <= 5){
                txtv_question_timer.setText("10");
            }
            imgv_question_type.setBackgroundResource(R.drawable.btn_eff_tareek);
        }


        c = myDbHelper.queryquestions(FINAL_TABLE_NAME, null, null, null, null, null, null);
        c.isBeforeFirst();

        if(c.moveToNext()) {
            c.moveToPosition(0);
            String questionsshowtext = c.getString(1);
            String answerposs = c.getString(5);
            Wright_Answer = c.getString(2);
            scoreString = c.getString(6);

            if (answerposs.equals("1")) {
                Answer1 = c.getString(2);
                Answer2 = c.getString(3);
                Answer3 = c.getString(4);
            }

            if (answerposs.equals("2")) {
                Answer1 = c.getString(4);
                Answer2 = c.getString(2);
                Answer3 = c.getString(3);
            }

            if (answerposs.equals("3")) {
                Answer1 = c.getString(3);
                Answer2 = c.getString(4);
                Answer3 = c.getString(2);
            }

            txtv_question_view.setText(questionsshowtext);
            txtv_answer_one.setText(Answer1);
            txtv_answer_two.setText(Answer2);
            txtv_answer_three.setText(Answer3);
            txtv_score.setText(scoreString);

            QUESTION_TIMER();
        }
        else {
            Intent intent = new Intent(QuestionsActivity.this, FinalActivity.class);
            intent.putExtra("TYPE",TYPE_FROM_INTENT );
            startActivity(intent);
            finish();
        }
    }

    private void IF_ANSWER_WRIGHT(){
        btn_answer_one.setClickable(false);
        btn_answer_two.setClickable(false);
        btn_answer_three.setClickable(false);
        Answer_Wright_Sound.start();
        Questions_countDownTimer.cancel();
        DISABLE_ALL_NAVIGATION_BUTTONS();

        if_Answer_wright_countDownTimer = new CountDownTimer(1000, 1000) {
            public void onTick(long millisUntilFinished) {
                is_Answer_wright_countDownTimer = true ;
            }
            public void onFinish() {
                is_Answer_wright_countDownTimer = false ;

        int Score_Convert_int =Integer.parseInt(txtv_score.getText().toString());
        Score_Convert_int = Score_Convert_int + 10 ;
        String Score_As_String = Integer.toString(Score_Convert_int);
        txtv_score.setText(Score_As_String);
        int id= c.getInt(0);
        int idsum = id + 1;
        String idsumstring =Integer.toString(idsum);

        myDbHelper.updateRecord(FINAL_TABLE_NAME,"score",idsumstring,Score_As_String);
        myDbHelper.updateRecord("score",FINAL_SCORE_COLUMN_NAME,"1",Score_As_String);
        myDbHelper.updateRecord("time",FINAL_TIME_COLUMN_NAME,"1","30");

        String deletposs =c.getString(0);
        myDbHelper.deleteRecordAlternate(FINAL_TABLE_NAME,"_id",deletposs);

        RESET_ANSWER_BUTTONS();

        if (c.moveToNext())
        {
          LOAD_QUESTIONS();
          ENABLE_ALL_NAVIGATION_BUTTONS();
        }
        else {
            Intent celebrateactivity =new Intent( QuestionsActivity.this, FinalActivity.class);
            celebrateactivity.putExtra("TYPE",TYPE_FROM_INTENT );
            startActivity(celebrateactivity);
            finish();
        }
            }
        }.start();
    }

    private void IF_ANSWER_WRONG(){
        int lifes = Integer.parseInt(txtv_life_count.getText().toString());
        String lifesshow = Integer.toString(lifes - 1);
        txtv_life_count.setText(lifesshow);
        Time_Tick_Sound.pause();
        Answer_Wrong_Sound.start();
        Questions_countDownTimer.cancel();
        DISABLE_ALL_NAVIGATION_BUTTONS();

        Show_Wright_Answer();

        btn_answer_one.setClickable(false);
        btn_answer_two.setClickable(false);
        btn_answer_three.setClickable(false);

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
        fadeOut.setStartOffset(1000);
        fadeOut.setDuration(1000);

        if(txtv_life_count.getText().equals("1")){
            Sign_countDownTimer = new CountDownTimer(5000, 1000) {
                public void onTick(long millisUntilFinished) {
                    lay_facebook_hint.setVisibility(View.VISIBLE);
                }
                public void onFinish() {
                    lay_facebook_hint.startAnimation(fadeOut);
                    lay_facebook_hint.setVisibility(View.INVISIBLE);
                    new CountDownTimer(5000, 1000) {
                        public void onTick(long millisUntilFinished) {
                            lay_time_hint.setVisibility(View.VISIBLE);
                        }
                        public void onFinish() {
                            lay_time_hint.startAnimation(fadeOut);
                            lay_time_hint.setVisibility(View.INVISIBLE);
                        }
                    }.start();
                }
                }.start();

            Handle_Score_When_No_Life();
            Handle_Score_When_Answer_Wrong();
            Questions_countDownTimer.cancel();

           if_answer_wrong_one_countDownTimer = new CountDownTimer(3000, 1000) {
                public void onTick(long millisUntilFinished) {
                    is_answer_wrong_one_countDownTimer = true ;
                }
                public void onFinish() {
                    is_answer_wrong_one_countDownTimer = false ;

                    LOAD_QUESTIONS();
                    ENABLE_ALL_NAVIGATION_BUTTONS();
                }
            }.start();

        }
        else {
            Handle_Score_When_No_Life();
            Handle_Score_When_Answer_Wrong();
            Questions_countDownTimer.cancel();

            if_answer_wrong_two_countDownTimer = new CountDownTimer(3000, 1000) {
                public void onTick(long millisUntilFinished) {
                    is_answer_wrong_two_countDownTimer = true ;
                }
                public void onFinish() {

                    is_answer_wrong_two_countDownTimer = false ;

            LOAD_QUESTIONS();
            ENABLE_ALL_NAVIGATION_BUTTONS();
                }
            }.start();
        }

        myDbHelper.updateRecord("time",FINAL_TIME_COLUMN_NAME,"1","30");

        if (txtv_life_count.getText().equals("0")) {
            txtv_life_count.setText("3");
            txtv_question_timer.setText("30");

            if (mInterstitialAd != null) {
                mInterstitialAd.show(QuestionsActivity.this);
            } else {
                Log.d("TAG", "The interstitial ad wasn't ready yet.");
            }
        }
    }

    private void QUESTION_TIMER(){
        Time_Tick_Sound.start();
        Questions_countDownTimer = new CountDownTimer(Integer.parseInt(txtv_question_timer.getText().toString()) * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                int Timer = Integer.parseInt(txtv_question_timer.getText().toString());
                Timer = Timer - 1 ;
                txtv_question_timer.setText(Integer.toString(Timer));
                if(Timer == 30 || Timer == 23 || Timer == 16 || Timer == 9 || Timer == 2){
                    gif_time.setBackgroundResource(R.drawable.ic_alarm_zero);
                    myDbHelper.updateRecord("time",FINAL_TIME_COLUMN_NAME,"1",Integer.toString(Timer));
                }
                if(Timer == 29 || Timer == 22 || Timer == 15 || Timer == 8 || Timer == 1){
                    gif_time.setBackgroundResource(R.drawable.ic_alarm_one);
                    myDbHelper.updateRecord("time",FINAL_TIME_COLUMN_NAME,"1",Integer.toString(Timer));
                }
                if(Timer == 28 || Timer == 21 || Timer == 14 || Timer == 7 ){
                    gif_time.setBackgroundResource(R.drawable.ic_alarm_two);
                    myDbHelper.updateRecord("time",FINAL_TIME_COLUMN_NAME,"1",Integer.toString(Timer));
                }
                if(Timer == 27 || Timer == 20 || Timer == 13 || Timer == 6 ){
                    gif_time.setBackgroundResource(R.drawable.ic_alarm_three);
                    myDbHelper.updateRecord("time",FINAL_TIME_COLUMN_NAME,"1",Integer.toString(Timer));
                }
                if(Timer == 26 || Timer == 19 || Timer == 12 || Timer == 5){
                    gif_time.setBackgroundResource(R.drawable.ic_alarm_four);
                    myDbHelper.updateRecord("time",FINAL_TIME_COLUMN_NAME,"1",Integer.toString(Timer));
                }
                if(Timer == 25 || Timer == 18 || Timer == 11 || Timer == 4){
                    gif_time.setBackgroundResource(R.drawable.ic_alarm_five);
                    myDbHelper.updateRecord("time",FINAL_TIME_COLUMN_NAME,"1",Integer.toString(Timer));
                }
                if(Timer == 24|| Timer == 17 || Timer == 10 || Timer == 3){
                    gif_time.setBackgroundResource(R.drawable.ic_alarm_six);
                    myDbHelper.updateRecord("time",FINAL_TIME_COLUMN_NAME,"1",Integer.toString(Timer));
                }
                if(Timer == 0){
                    gif_time.setBackgroundResource(R.drawable.ic_alarm_time_end);
                    myDbHelper.updateRecord("time",FINAL_TIME_COLUMN_NAME,"1",Integer.toString(Timer));
                }
            }
            public void onFinish() {
                txtv_question_timer.setText("0");
                Time_End.start();
                IF_ANSWER_WRONG();

            }
        }.start();

    }

    private void RESET_ANSWER_BUTTONS(){
        btn_answer_one.setBackgroundResource(R.drawable.ic_btn_answer_view);
        btn_answer_two.setBackgroundResource(R.drawable.ic_btn_answer_view);
        btn_answer_three.setBackgroundResource(R.drawable.ic_btn_answer_view);
        btn_answer_one.setClickable(true);
        btn_answer_two.setClickable(true);
        btn_answer_three.setClickable(true);
    }

    private void ENABLE_NAVIGATION_BUTTONS(ImageButton Disable_Button){
        btn_thakafe.setEnabled(true);
        btn_reyada.setEnabled(true);
        btn_gografya.setEnabled(true);
        btn_moktaree.setEnabled(true);
        btn_eloom.setEnabled(true);
        btn_tareek.setEnabled(true);
        Disable_Button.setEnabled(false);
    }

    private void DISABLE_ALL_NAVIGATION_BUTTONS(){
        btn_thakafe.setEnabled(false);
        btn_reyada.setEnabled(false);
        btn_gografya.setEnabled(false);
        btn_moktaree.setEnabled(false);
        btn_eloom.setEnabled(false);
        btn_tareek.setEnabled(false);
    }

    private void ENABLE_ALL_NAVIGATION_BUTTONS(){
        btn_thakafe.setEnabled(true);
        btn_reyada.setEnabled(true);
        btn_gografya.setEnabled(true);
        btn_moktaree.setEnabled(true);
        btn_eloom.setEnabled(true);
        btn_tareek.setEnabled(true);
    }

    private void Show_Wright_Answer(){

        if(Wright_Answer.equals(txtv_answer_one.getText())){
            WRIGHT_ANSWER_BUTTON = btn_answer_one ;
            btn_answer_one.setBackgroundResource(R.drawable.btn_eff_answer_right);
        }
        if(Wright_Answer.equals(txtv_answer_two.getText())){
            WRIGHT_ANSWER_BUTTON = btn_answer_two ;
            btn_answer_two.setBackgroundResource(R.drawable.btn_eff_answer_right);
        }
        if(Wright_Answer.equals(txtv_answer_three.getText())){
            WRIGHT_ANSWER_BUTTON = btn_answer_three ;
            btn_answer_three.setBackgroundResource(R.drawable.btn_eff_answer_right);
        }

        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(500); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(2);
        WRIGHT_ANSWER_BUTTON.startAnimation(anim);

    }

    private void Handle_Score_When_Answer_Wrong(){

        int Score_Convert_int =Integer.parseInt(txtv_score.getText().toString());
        if(Score_Convert_int > 5) {
            Score_Convert_int = Score_Convert_int - 5;
        }
        String Score_As_String = Integer.toString(Score_Convert_int);
        txtv_score.setText(Score_As_String);
        int id= c.getInt(0);
        int idsum = id + 1;
        String idsumstring =Integer.toString(idsum);

        myDbHelper.updateRecord(FINAL_TABLE_NAME,"score",idsumstring,Score_As_String);
        myDbHelper.updateRecord("score",FINAL_SCORE_COLUMN_NAME,"1",Score_As_String);

        String deletposs =c.getString(0);
        myDbHelper.deleteRecordAlternate(FINAL_TABLE_NAME,"_id",deletposs);

    }

    private void Handle_Score_When_No_Life(){
        if (txtv_life_count.getText().equals("0")) {
            int Score_Convert_int = Integer.parseInt(txtv_score.getText().toString());
            if (Score_Convert_int > 50) {
                Score_Convert_int = Score_Convert_int - 10;
            }
            String Score_As_String = Integer.toString(Score_Convert_int);
            txtv_score.setText(Score_As_String);
        }
    }

    private void Show_Total_Score(){

        c_total_score = myDbHelper.queryscore("score", null, null, null, null, null, null);
        c_total_score.moveToPosition(0);

        int a = Integer.parseInt(c_total_score.getString(1));
        int b = Integer.parseInt(c_total_score.getString(2));
        int c = Integer.parseInt(c_total_score.getString(3));
        int d = Integer.parseInt(c_total_score.getString(4));
        int e = Integer.parseInt(c_total_score.getString(5));
        int f = Integer.parseInt(c_total_score.getString(6));
        int total = a+b+c+d+e+f;
        String stotal= Integer.toString(total);
        txtv_total_score.setText(stotal);
    }

    @Override
    public void onBackPressed() {
        Time_Tick_Sound.pause();
        Questions_countDownTimer.cancel();

        if(is_Answer_wright_countDownTimer){
            if_Answer_wright_countDownTimer.cancel();
        }
        if(is_answer_wrong_one_countDownTimer){
            if_answer_wrong_one_countDownTimer.cancel();
        }
        if(is_answer_wrong_two_countDownTimer){
            if_answer_wrong_two_countDownTimer.cancel();
        }

        Intent intent = new Intent(QuestionsActivity.this, StartActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onPause() {
        super.onPause();
        Time_Tick_Sound.pause();
        Questions_countDownTimer.cancel();

        if(is_Answer_wright_countDownTimer){
            if_Answer_wright_countDownTimer.cancel();
        }
        if(is_answer_wrong_one_countDownTimer){
            if_answer_wrong_one_countDownTimer.cancel();
        }
        if(is_answer_wrong_two_countDownTimer){
            if_answer_wrong_two_countDownTimer.cancel();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
      Questions_countDownTimer.cancel();
      LOAD_QUESTIONS();

    }

}