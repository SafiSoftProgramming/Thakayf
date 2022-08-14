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
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class FinalActivity extends AppCompatActivity {

    DBthakaifconnction myDbHelper ;
    Cursor c = null;
    Cursor c_score = null;
    AccessToken accessToken ;
    GraphRequest request  ;
    String USER_ID = "";
    String USER_NAME ="";
    ImageView player_circular_image ;
    TextView txtv_player_name ;
    TextView txtv_question_type ;
    String TYPE_FROM_INTENT = "";
    String FINAL_TABLE_NAME = "";
    String FINAL_SCORE_COLUMN_NAME = "";
    TextView txtv_score_name ;
    ImageButton imgv_question_type ;
    ImageButton btn_share_final_post_facebook ;
    TextView txtv_score ;
    ImageView img_player_stars_rate ;
    TextView txtv_message_mabrok ;
    TextView txtv_message ;
    ImageView img_final_rate ;
    int score_star_rsc_int ;
    int score_emoj_rsc_int ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);

        TYPE_FROM_INTENT = getIntent().getStringExtra("TYPE");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        DetectInternet detectInternet =new DetectInternet();
        if(detectInternet.isConnected(getApplicationContext())) {

        player_circular_image = findViewById(R.id.player_circular_image);
        txtv_player_name =findViewById(R.id.txtv_player_name);
        txtv_question_type = findViewById(R.id.txtv_question_type);
        imgv_question_type = findViewById(R.id.imgv_question_type);
        txtv_score_name  =findViewById(R.id.txtv_score_name);
        txtv_score = findViewById(R.id.txtv_score);
        img_player_stars_rate = findViewById(R.id.img_player_stars_rate);
        txtv_message_mabrok = findViewById(R.id.txtv_message_mabrok);
        txtv_message =findViewById(R.id.txtv_message);
        img_final_rate = findViewById(R.id.img_final_rate);
        btn_share_final_post_facebook = findViewById(R.id.btn_share_final_post_facebook);

        myDbHelper = new DBthakaifconnction(FinalActivity.this);
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


        accessToken = AccessToken.getCurrentAccessToken();

        LOAD_DATA();

        Load_Stars_Text_Rate(Integer.parseInt(txtv_score.getText().toString()));





        txtv_message_mabrok.setText("رائع لقد تخطيت مائة سؤال فى مجال "+txtv_question_type.getText().toString());


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


        btn_share_final_post_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(FinalActivity.this, FacebookFinalPostActivity.class);
                intent.putExtra("String_Question_Category",txtv_question_type.getText().toString());
                intent.putExtra("String_Point_Number",txtv_score.getText() );
                intent.putExtra("Image_emoj",score_emoj_rsc_int);
                intent.putExtra("Image_Star",score_star_rsc_int);
                startActivity(intent);
                finish();

            }
        });

        }
        else {
            Intent intent = new Intent(FinalActivity.this, SplashActivity.class);
            startActivity(intent);
            finish();
        }



    }



    private void LOAD_DATA(){

        c_score = myDbHelper.queryscore("score", null, null, null, null, null, null);
        c_score.moveToPosition(0);


        if(TYPE_FROM_INTENT.equals("THAKAFE")){
            FINAL_TABLE_NAME = "skafa";
            FINAL_SCORE_COLUMN_NAME = "skafa_score";
            txtv_question_type.setText("ثقافة");
            txtv_score_name.setText("نقاط ثقافة");
            txtv_score.setText(c_score.getString(5));
            imgv_question_type.setBackgroundResource(R.drawable.btn_eff_thakafa);
        }
        if(TYPE_FROM_INTENT.equals("REYADA")){
            FINAL_TABLE_NAME = "sport";
            FINAL_SCORE_COLUMN_NAME = "sport_score";
            txtv_question_type.setText("رياضة");
            txtv_score_name.setText("نقاط رياضة");
            txtv_score.setText(c_score.getString(6));
            imgv_question_type.setBackgroundResource(R.drawable.btn_eff_reyada);
        }
        if(TYPE_FROM_INTENT.equals("GOGRAFYA")){
            FINAL_TABLE_NAME = "gorafya";
            FINAL_SCORE_COLUMN_NAME = "gorafya_score";
            txtv_question_type.setText("جغرافيا");
            txtv_score_name.setText("نقاط جغرافيا");
            txtv_score.setText(c_score.getString(1));
            imgv_question_type.setBackgroundResource(R.drawable.btn_eff_gografya);
        }
        if(TYPE_FROM_INTENT.equals("MOKTAREE")){
            FINAL_TABLE_NAME = "invint";
            FINAL_SCORE_COLUMN_NAME = "invint_score";
            txtv_question_type.setText("مخترعين");
            txtv_score_name.setText("نقاط مخترعين");
            txtv_score.setText(c_score.getString(3));
            imgv_question_type.setBackgroundResource(R.drawable.btn_eff_moktaree);
        }
        if(TYPE_FROM_INTENT.equals("ELOOM")){
            FINAL_TABLE_NAME = "since";
            FINAL_SCORE_COLUMN_NAME = "since_score";
            txtv_question_type.setText("علوم");
            txtv_score_name.setText("نقاط علوم");
            txtv_score.setText(c_score.getString(4));
            imgv_question_type.setBackgroundResource(R.drawable.btn_eff_eloom);
        }
        if(TYPE_FROM_INTENT.equals("TAREEK")){
            FINAL_TABLE_NAME = "history";
            FINAL_SCORE_COLUMN_NAME = "history_score";
            txtv_question_type.setText("تاريخ");
            txtv_score_name.setText("نقاط تاريخ");
            txtv_score.setText(c_score.getString(2));
            imgv_question_type.setBackgroundResource(R.drawable.btn_eff_tareek);
        }
    }

    private void Load_Stars_Text_Rate(int total){
        if (total >= 0){
            img_player_stars_rate.setBackgroundResource(R.drawable.ic_stars_rate_nun);
            txtv_message.setText("يبدو انك لم تستطيع حل اى سؤال أتمنى ان تكون استفدت من الحلول و عرفت بعض المعلومات");
            img_final_rate.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_final_rate_one));
            score_star_rsc_int = R.drawable.ic_stars_rate_nun ;
            score_emoj_rsc_int = R.drawable.ic_final_rate_one ;
        }
        if (total >= 25){
            img_player_stars_rate.setBackgroundResource(R.drawable.ic_stars_rate_one);
            txtv_message.setText("قليل من المعلومات افضل من لا شيئ على الأطلاق محاوله جيدة");
            img_final_rate.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_final_rate_two));
            score_star_rsc_int = R.drawable.ic_stars_rate_one ;
            score_emoj_rsc_int = R.drawable.ic_final_rate_two ;

        }
        if (total >= 50){
            img_player_stars_rate.setBackgroundResource(R.drawable.ic_stars_rate_two);
            txtv_message.setText("نعلم ان الأسئله كانت صعبة حاول تحسين معلومات");
            img_final_rate.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_final_rate_two));
            score_star_rsc_int = R.drawable.ic_stars_rate_two ;
            score_emoj_rsc_int = R.drawable.ic_final_rate_two ;

        }
        if (total >= 100){
            img_player_stars_rate.setBackgroundResource(R.drawable.ic_stars_rate_three);
            txtv_message.setText("لقد حاولت لا تيأس نتمنى لك أداء أفضل فى الأسئلة الأخرى");
            img_final_rate.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_final_rate_three));
            score_star_rsc_int = R.drawable.ic_stars_rate_three ;
            score_emoj_rsc_int = R.drawable.ic_final_rate_three ;

        }
        if (total >= 200){
            img_player_stars_rate.setBackgroundResource(R.drawable.ic_stars_rate_four);
            txtv_message.setText("لقد جاوبت على عدد لا بأس به من الأسئلة جيد");
            img_final_rate.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_final_rate_four));
            score_star_rsc_int = R.drawable.ic_stars_rate_four ;
            score_emoj_rsc_int = R.drawable.ic_final_rate_four ;

        }
        if (total >= 300){
            img_player_stars_rate.setBackgroundResource(R.drawable.ic_stars_rate_five);
            txtv_message.setText("جيد جدا عدد مقبول من النقاط و الأجابات");
            img_final_rate.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_final_rate_five));
            score_star_rsc_int = R.drawable.ic_stars_rate_five ;
            score_emoj_rsc_int = R.drawable.ic_final_rate_five ;

        }
        if (total >= 400){
            img_player_stars_rate.setBackgroundResource(R.drawable.ic_stars_rate_six);
            txtv_message.setText("يبدو أنك كنت تحاول جاهدا لتصل إلى الحل الصحيح مجهود يستحق الأحترام");
            img_final_rate.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_final_rate_six));
            score_star_rsc_int = R.drawable.ic_stars_rate_six ;
            score_emoj_rsc_int = R.drawable.ic_final_rate_six ;
        }
        if (total >= 500){
            img_player_stars_rate.setBackgroundResource(R.drawable.ic_stars_rate_seven);
            txtv_message.setText("رائع جدا لم يتمكن الكثير من الوصول إلى هذا العدد من النقاط");
            img_final_rate.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_final_rate_seven));
            score_star_rsc_int = R.drawable.ic_stars_rate_seven ;
            score_emoj_rsc_int = R.drawable.ic_final_rate_seven ;

        }
        if (total >= 600){
            img_player_stars_rate.setBackgroundResource(R.drawable.ic_stars_rate_eight);
            txtv_message.setText("شيئ مبهر حقا أن تتمكن من حل كل هذه الأسئلة أحسنت");
            img_final_rate.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_final_rate_eight));
            score_star_rsc_int = R.drawable.ic_stars_rate_eight ;
            score_emoj_rsc_int = R.drawable.ic_final_rate_eight ;

        }
        if (total >= 700){
            img_player_stars_rate.setBackgroundResource(R.drawable.ic_stars_rate_nine);
            txtv_message.setText("مبهر حقا يبدوا أنك شخص لديه الكثير من المعلومات");
            img_final_rate.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_final_rate_eight));
            score_star_rsc_int = R.drawable.ic_stars_rate_nine ;
            score_emoj_rsc_int = R.drawable.ic_final_rate_eight ;

        }
        if (total >= 800){
            img_player_stars_rate.setBackgroundResource(R.drawable.ic_stars_rate_ten);
            txtv_message.setText("لديك روح المحارب لقدت صمدت أمام هذه الأسئلة عمل جيد حقا");
            img_final_rate.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_final_rate_nine));
            score_star_rsc_int = R.drawable.ic_stars_rate_ten ;
            score_emoj_rsc_int = R.drawable.ic_final_rate_nine ;

        }
        if (total >= 900){
            img_player_stars_rate.setBackgroundResource(R.drawable.ic_stars_rate_eleven);
            txtv_message.setText("يجب علينا جميعا أحترام أصحاب العلم مثلك لا شك أنك شخص مميز");
            img_final_rate.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_final_rate_nine));
            score_star_rsc_int = R.drawable.ic_stars_rate_eleven ;
            score_emoj_rsc_int = R.drawable.ic_final_rate_nine ;

        }
        if (total >= 1000){
            img_player_stars_rate.setBackgroundResource(R.drawable.ic_stars_rate_twelve);
            txtv_message.setText("لا أصدق هذا لم أعتقد أبدا ان بمقدور أحد الوصول إلى هذه المرحلة لقد أبهرت الجميع");
            img_final_rate.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_final_rate_ten));
            score_star_rsc_int = R.drawable.ic_stars_rate_twelve ;
            score_emoj_rsc_int = R.drawable.ic_final_rate_ten ;

        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(FinalActivity.this, StartActivity.class);
        startActivity(intent);
        finish();
    }
}