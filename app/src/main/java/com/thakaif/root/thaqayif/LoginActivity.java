package com.thakaif.root.thaqayif;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.material.snackbar.Snackbar;
import java.io.IOException;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    CallbackManager callbackManager ;
    AccessToken accessToken ;
    Cursor c = null;
    LoginButton authButton ;
    ImageButton btn_sign_up ;
    String Player_Name  = "";
    TextView txtv_insert_player_name_hint ;
    EditText edtxt_player_name ;



    @Override  
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        DetectInternet detectInternet =new DetectInternet();
        if(detectInternet.isConnected(getApplicationContext())) {

        accessToken = AccessToken.getCurrentAccessToken();
        callbackManager = CallbackManager.Factory.create();
        authButton = findViewById(R.id.login_button);
        btn_sign_up =findViewById(R.id.btn_sign_up);
        txtv_insert_player_name_hint = findViewById(R.id.txtv_insert_player_name_hint);
        edtxt_player_name = findViewById(R.id.edtxt_player_name);


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


        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Player_Name = edtxt_player_name.getText().toString();
                if(Player_Name.isEmpty()){
                    show_info_lay();
                }
                else {
                    myDbHelper.updateRecordlogin("logindata","username","userage","gander","useremail","player_thakayif_default",Player_Name," "," "," ");
                    Intent choose = new Intent(LoginActivity.this, StartActivity.class);
                    startActivity(choose);
                  //  setalarm();
                    finish();
                }

            }
        });


        authButton.setPermissions(Arrays.asList("gaming_user_picture"));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        Intent intent = new Intent(LoginActivity.this, StartActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    @Override
                    public void onCancel() {
                        // App code
                    }
                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
        // Checking the Access Token.
        if(AccessToken.getCurrentAccessToken()!=null){
            // If already login in .
            Intent intent = new Intent(LoginActivity.this, StartActivity.class);
            startActivity(intent);
            finish();
        }else {
            // If not login in .
        }


        }
        else {
            Intent intent = new Intent(LoginActivity.this, SplashActivity.class);
            startActivity(intent);
            finish();
        }



    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void show_info_lay (){
        txtv_insert_player_name_hint.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                txtv_insert_player_name_hint.setVisibility(View.INVISIBLE);
            }
        }, 2000);
    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Snackbar.make(findViewById(android.R.id.content), "أضغط مرةأخرى للخروج", Snackbar.LENGTH_LONG).show();
    }



}