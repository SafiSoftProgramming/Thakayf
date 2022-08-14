package com.thakaif.root.thaqayif;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.snackbar.Snackbar;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class StartActivity extends AppCompatActivity {
    AccessToken accessToken ;
    GraphRequest request  ;
    ImageView player_circular_image ;
    TextView txtv_player_name ;
    ImageButton btn_open_settings , btn_thakafe , btn_reyada , btn_gografya , btn_moktaree , btn_eloom , btn_tareek;
    private AdView adView_Banner;
    String USER_ID = "";
    String USER_NAME ="";
    Cursor c = null;
    Cursor c_score = null;
    TextView txtv_score_thakafa , txtv_score_reyada , txtv_score_gografya , txtv_score_moktaree , txtv_score_eloom , txtv_score_tareek ;
    TextView txtv_player_total_points ;
    ImageView img_player_stars_rate ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);




        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        DetectInternet detectInternet =new DetectInternet();
        if(detectInternet.isConnected(getApplicationContext())) {


        final DBthakaifconnction myDbHelper = new DBthakaifconnction(StartActivity.this);
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

        adView_Banner = findViewById(R.id.adView_Banner);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView_Banner.loadAd(adRequest);
        accessToken = AccessToken.getCurrentAccessToken();
        player_circular_image = findViewById(R.id.player_circular_image);
        txtv_player_name =findViewById(R.id.txtv_player_name);
        txtv_score_thakafa  =findViewById(R.id.txtv_score_thakafa);
        txtv_score_reyada =findViewById(R.id.txtv_score_reyada);
        txtv_score_gografya =findViewById(R.id.txtv_score_gografya);
        txtv_score_moktaree =findViewById(R.id.txtv_score_moktaree);
        txtv_score_eloom =findViewById(R.id.txtv_score_eloom);
        txtv_score_tareek =findViewById(R.id.txtv_score_tareek);
        txtv_player_total_points =findViewById(R.id.txtv_player_total_points);
        img_player_stars_rate = findViewById(R.id.img_player_stars_rate);
        btn_open_settings = findViewById(R.id.btn_open_settings);
        btn_thakafe = findViewById(R.id.btn_thakafe);
        btn_reyada = findViewById(R.id.btn_reyada);
        btn_gografya = findViewById(R.id.btn_gografya);
        btn_moktaree =findViewById(R.id.btn_moktaree);
        btn_eloom = findViewById(R.id.btn_eloom);
        btn_tareek = findViewById(R.id.btn_tareek);

        c = myDbHelper.querylogindata("logindata", null, null, null, null, null, null);
        c.moveToPosition(0);

        if(!c.getString(0).equals("player_thakayif_default")){
            txtv_player_name.setText(c.getString(0));
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

                                txtv_player_name.setText(USER_NAME);

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

        c_score = myDbHelper.queryscore("score", null, null, null, null, null, null);
        c_score.moveToPosition(0);


        txtv_score_gografya.setText(c_score.getString(1));
        txtv_score_tareek.setText(c_score.getString(2));
        txtv_score_moktaree.setText(c_score.getString(3));
        txtv_score_eloom.setText(c_score.getString(4));
        txtv_score_thakafa.setText(c_score.getString(5));
        txtv_score_reyada.setText(c_score.getString(6));

        int a = Integer.parseInt(txtv_score_gografya.getText().toString());
        int b = Integer.parseInt(txtv_score_tareek.getText().toString());
        int c = Integer.parseInt(txtv_score_moktaree.getText().toString());
        int d = Integer.parseInt(txtv_score_eloom.getText().toString());
        int e = Integer.parseInt(txtv_score_thakafa.getText().toString());
        int f = Integer.parseInt(txtv_score_reyada.getText().toString());
        int total = a+b+c+d+e+f;
        String stotal= Integer.toString(total);
        txtv_player_total_points.setText(stotal);


        if (total >= 500){img_player_stars_rate.setBackgroundResource(R.drawable.ic_stars_rate_one);}
        if (total >= 1000){img_player_stars_rate.setBackgroundResource(R.drawable.ic_stars_rate_two);}
        if (total >= 1500){img_player_stars_rate.setBackgroundResource(R.drawable.ic_stars_rate_three);}
        if (total >= 2000){img_player_stars_rate.setBackgroundResource(R.drawable.ic_stars_rate_four);}
        if (total >= 2500){img_player_stars_rate.setBackgroundResource(R.drawable.ic_stars_rate_five);}
        if (total >= 3000){img_player_stars_rate.setBackgroundResource(R.drawable.ic_stars_rate_six);}
        if (total >= 3500){img_player_stars_rate.setBackgroundResource(R.drawable.ic_stars_rate_seven);}
        if (total >= 4000){img_player_stars_rate.setBackgroundResource(R.drawable.ic_stars_rate_eight);}
        if (total >= 4500){img_player_stars_rate.setBackgroundResource(R.drawable.ic_stars_rate_nine);}
        if (total >= 5000){img_player_stars_rate.setBackgroundResource(R.drawable.ic_stars_rate_ten);}
        if (total >= 5500){img_player_stars_rate.setBackgroundResource(R.drawable.ic_stars_rate_eleven);}
        if (total >= 6000){img_player_stars_rate.setBackgroundResource(R.drawable.ic_stars_rate_twelve);}




        btn_open_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, SettingsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_thakafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(StartActivity.this, QuestionsActivity.class);
                    intent.putExtra("TYPE","THAKAFE" );
                    startActivity(intent);
                    finish();
            }
        });

        btn_reyada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(StartActivity.this, QuestionsActivity.class);
                    intent.putExtra("TYPE","REYADA" );
                    startActivity(intent);
                    finish();
            }
        });

        btn_gografya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(StartActivity.this, QuestionsActivity.class);
                    intent.putExtra("TYPE","GOGRAFYA" );
                    startActivity(intent);
                    finish();
            }
        });

        btn_moktaree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(StartActivity.this, QuestionsActivity.class);
                    intent.putExtra("TYPE","MOKTAREE" );
                    startActivity(intent);
                    finish();
            }
        });

        btn_eloom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(StartActivity.this, QuestionsActivity.class);
                    intent.putExtra("TYPE","ELOOM" );
                    startActivity(intent);
                    finish();
            }
        });

        btn_tareek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(StartActivity.this, QuestionsActivity.class);
                    intent.putExtra("TYPE","TAREEK" );
                    startActivity(intent);
                    finish();
            }
        });




    }
        else {
            Intent intent = new Intent(StartActivity.this, SplashActivity.class);
            startActivity(intent);
            finish();
    }



    }


    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Snackbar.make(findViewById(android.R.id.content), "أضغط مرتين للخروج", Snackbar.LENGTH_LONG).show();
    }
}