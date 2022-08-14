package com.thakaif.root.thaqayif;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;

public class SettingsActivity extends AppCompatActivity {

    LoginButton authButton ;
    ImageButton btn_info_rateme , btn_info_apps , btn_info_facebook , btn_info_feesback , btn_info_permissions , btn_info_youtube ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        authButton = findViewById(R.id.login_button);
        btn_info_rateme = findViewById(R.id.btn_info_rateme);
        btn_info_apps = findViewById(R.id.btn_info_apps);
        btn_info_facebook = findViewById(R.id.btn_info_facebook);
        btn_info_feesback = findViewById(R.id.btn_info_feesback);
        btn_info_permissions = findViewById(R.id.btn_info_permissions);
        btn_info_youtube = findViewById(R.id.btn_info_youtube);

       authButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               LoginManager.getInstance().logOut();
               Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
               startActivity(intent);
               finish();
           }
       });


        btn_info_rateme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }

            }
        });
        btn_info_apps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=SafiSoft")));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=SafiSoft")));
                }

            }
        });
        btn_info_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/301343817018905"));
                    startActivity(intent);
                } catch(Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/SafiSoft.programming")));
                }

            }
        });
        btn_info_feesback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:SafiSoft.programmer@gmail.com")); // only email apps should handle this
                intent.putExtra(Intent.EXTRA_SUBJECT, "QR Scanner Duck User Feedback");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }

            }
        });
        btn_info_permissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingsActivity.this, "لا يستخدم التطبيق اى صلاحيات للوصول الى بيانات المستخدم او عتاد الجهاز", Toast.LENGTH_SHORT).show();
            }
        });

        btn_info_youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCtC8eqUUZmktsUoFznAL91w")));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCtC8eqUUZmktsUoFznAL91w")));
                }

            }
        });



    }




    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SettingsActivity.this, StartActivity.class);
        startActivity(intent);
        finish();
    }
}