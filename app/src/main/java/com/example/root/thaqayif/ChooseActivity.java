package com.example.root.thaqayif;



import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import java.io.IOException;


public class ChooseActivity extends AppCompatActivity {

    TextView gifsince ;
    TextView gifsport ;
    TextView gifmap ;
    TextView gifhistory;
    TextView gifbook ;
    TextView gifinvint;

    TextView txsince ;
    TextView txhistory ;
    TextView txsport ;
    TextView txmap ;
    TextView txthkafa ;
    TextView txinvint ;

    TextView txtotalscoore ;

    AdView chooseadview;

    ImageView Stars ;

    Cursor c = null;
    Cursor c_score = null;

    TextView txvname ;

    DBthakaifconnction myDbHelper ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);// hide notification bar


          myDbHelper = new DBthakaifconnction(ChooseActivity.this);
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



        MobileAds.initialize(this, "ca-app-pub-3940256099942544/6300978111");//testad
       // MobileAds.initialize(this, "ca-app-pub-5637187199850424/8299865613");//real add
        chooseadview =(AdView) findViewById(R.id.chooseadView);
        AdRequest adRequest = new AdRequest.Builder().build();
        chooseadview.loadAd(adRequest);

        txvname = (TextView)findViewById(R.id.txvname);
        gifsince = (TextView)findViewById(R.id.gifsince);
        gifmap =(TextView)findViewById(R.id.gifmap);
        gifhistory =(TextView)findViewById(R.id.gifhistory);
        gifbook =(TextView)findViewById(R.id.gifbook);
        gifsport =(TextView)findViewById(R.id.gifsport);
        gifinvint =(TextView)findViewById(R.id.gifinvint);
        Stars = (ImageView)findViewById(R.id.imageView_star);


        c = myDbHelper.querylogindata("logindata", null, null, null, null, null, null);
        c.moveToPosition(0);


        txvname.setText(c.getString(0));



        gifsince.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(!txsince.getText().equals("1000")) {
                    Intent sinceactivity = new Intent(ChooseActivity.this, SinceActivity.class);
                    startActivity(sinceactivity);
                }
                else {

                    Intent celebrateactivity =new Intent( ChooseActivity.this, CelebrateActivity.class);
                    startActivity(celebrateactivity);
                }

            }
        });

        gifmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!txmap.getText().equals("1000")) {
                    Intent gorafyaactivity = new Intent(ChooseActivity.this, GorafyaActivity.class);
                    startActivity(gorafyaactivity);
                }
                else{
                    Intent celebrateactivity =new Intent( ChooseActivity.this, CelebrateActivity.class);
                    startActivity(celebrateactivity);
                }

            }
        });

        gifhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!txhistory.getText().equals("1000")) {
                    Intent historyactivity = new Intent(ChooseActivity.this, HistoryActivity.class);
                    startActivity(historyactivity);
                }
                else{
                    Intent celebrateactivity =new Intent( ChooseActivity.this, CelebrateActivity.class);
                    startActivity(celebrateactivity);
                }

            }
        });

        gifbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!txthkafa.getText().equals("1000")) {
                    Intent skafaactivity = new Intent(ChooseActivity.this, SkafaActivity.class);
                    startActivity(skafaactivity);
                }
                else{
                    Intent celebrateactivity =new Intent( ChooseActivity.this, CelebrateActivity.class);
                    startActivity(celebrateactivity);
                }

            }
        });

        gifsport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!txsport.getText().equals("1000")) {
                    Intent sportactivity = new Intent(ChooseActivity.this, SportActivity.class);
                    startActivity(sportactivity);
                }
                else{
                    Intent celebrateactivity =new Intent( ChooseActivity.this, CelebrateActivity.class);
                    startActivity(celebrateactivity);
                }

            }
        });

        gifinvint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!txinvint.getText().equals("1000")) {
                    Intent invintactivity = new Intent(ChooseActivity.this, InvintActivity.class);
                    startActivity(invintactivity);
                }
                else{
                    Intent celebrateactivity =new Intent( ChooseActivity.this, CelebrateActivity.class);
                    startActivity(celebrateactivity);
                }
            }
        });
    }

    public void onResume(){
        super.onResume();
        txsince =(TextView)findViewById(R.id.txsince);
        txhistory =(TextView)findViewById(R.id.txhistory);
        txsport =(TextView)findViewById(R.id.txsport);
        txmap =(TextView)findViewById(R.id.txmap);
        txthkafa =(TextView)findViewById(R.id.txthkafa);
        txinvint =(TextView)findViewById(R.id.txinvint);
        txtotalscoore =(TextView)findViewById(R.id.txtotalscoore);



        c_score = myDbHelper.queryscore("score", null, null, null, null, null, null);
        c_score.moveToPosition(0);


        txsince.setText(c_score.getString(4));
        txhistory.setText(c_score.getString(2));
        txsport.setText(c_score.getString(6));
        txmap.setText(c_score.getString(1));
        txthkafa.setText(c_score.getString(5));
        txinvint.setText(c_score.getString(3));

        int a = Integer.parseInt(txsince.getText().toString());
        int b = Integer.parseInt(txhistory.getText().toString());
        int c = Integer.parseInt(txsport.getText().toString());
        int d = Integer.parseInt(txmap.getText().toString());
        int e = Integer.parseInt(txthkafa.getText().toString());
        int f = Integer.parseInt(txinvint.getText().toString());

        int total = a+b+c+d+e+f;

        String stotal= Integer.toString(total);
        txtotalscoore.setText(stotal);

        if (total >= 1000){Stars.setImageResource(R.drawable.onestar);}
        if (total >= 2000){Stars.setImageResource(R.drawable.towstar);}
        if (total >= 3000){Stars.setImageResource(R.drawable.threstar);}
        if (total >= 4000){Stars.setImageResource(R.drawable.forestar);}
        if (total >= 5000){Stars.setImageResource(R.drawable.fivestar);}
        if (total >= 6000){Stars.setImageResource(R.drawable.sixstar);}

    }


}
