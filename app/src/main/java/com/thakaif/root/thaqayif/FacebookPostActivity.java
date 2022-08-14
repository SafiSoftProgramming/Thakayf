package com.thakaif.root.thaqayif;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class FacebookPostActivity extends AppCompatActivity {

    AccessToken accessToken ;
    GraphRequest request  ;
    String USER_ID = "";
    String USER_NAME ="";
    Cursor c = null;
    ImageButton btn_share_help_facebook ;

    ImageView player_circular_image ;
    TextView txtv_player_name ;
    TextView txtv_hint ;

    ImageView imgv_facebook_post ;
    String String_Question_Category ="";
    String String_Question = "";
    String String_Question_Answer_one = "";
    String String_Question_Answer_two = "";
    String String_Question_Answer_three = "";
    String String_Player_Name = "";
    Bitmap Image_Question_Category ;
    Bitmap Image_Player_Photo;
    Bitmap Player_Name_Image_and_final ;
    Bitmap Question ;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_post);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        DetectInternet detectInternet =new DetectInternet();
        if(detectInternet.isConnected(getApplicationContext())) {

        String_Question_Category = getIntent().getStringExtra("String_Question_Category");
        String_Question = getIntent().getStringExtra("String_Question");
        String_Question_Answer_one = getIntent().getStringExtra("String_Question_Answer_one");
        String_Question_Answer_two = getIntent().getStringExtra("String_Question_Answer_two");
        String_Question_Answer_three = getIntent().getStringExtra("String_Question_Answer_three");

        imgv_facebook_post = findViewById(R.id.imgv_facebook_post);
        accessToken = AccessToken.getCurrentAccessToken();
        btn_share_help_facebook =findViewById(R.id.btn_share_help_facebook);
        player_circular_image = findViewById(R.id.player_circular_image);
        txtv_player_name =findViewById(R.id.txtv_player_name);
        txtv_hint = findViewById(R.id.txtv_hint);

        btn_share_help_facebook.setEnabled(false);

        txtv_hint.setText("تذكر دائما لا تدخل إلى قسم "+ String_Question_Category +" ألا عند معرفتك الأجابة الصحيحه لهذا السؤال من أصدقائك حتى لا تخسر الوقت المتبقى لحل السؤال.");


        final DBthakaifconnction myDbHelper = new DBthakaifconnction(FacebookPostActivity.this);
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
        c.moveToPosition(0);

        if(!c.getString(0).equals("player_thakayif_default")){
            String_Player_Name = c.getString(0);
            Image_Player_Photo = BitmapFactory.decodeResource(getResources(),R.drawable.ic_player_icon);
            Make_Post();
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
                                String_Player_Name = USER_NAME ;
                                txtv_player_name.setText(USER_NAME);
                                URL url = new URL("https://graph.facebook.com/"+ object.getString("id")+"/picture?type=large&access_token=GG|246070050613989|5Q1yiEG8Ug3GJXO_FdTyaBohVeQ");
                                Image_Player_Photo = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                                player_circular_image.setImageBitmap(bmp);
                                Make_Post();
                                btn_share_help_facebook.setEnabled(true);
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


        btn_share_help_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharePhoto photo = new SharePhoto.Builder()
                        .setBitmap(Player_Name_Image_and_final)
                        .build();

                SharePhotoContent content = new SharePhotoContent.Builder()
                        .addPhoto(photo)
                      //  .setContentUrl(Uri.parse("www.google.com"))
                        .build();
                ShareDialog shareDialog = new ShareDialog(FacebookPostActivity.this);
                shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);

            }
        });


        }
        else {
            Intent intent = new Intent(FacebookPostActivity.this, SplashActivity.class);
            startActivity(intent);
            finish();
        }

    }

    public static Bitmap Image_WaterMark(int rw, int rh , Bitmap source,Bitmap gym_logo) {
        int w , h ;
        Canvas c;
        Paint paint;
        Bitmap bmp, watermark;
        Matrix matrix;
        float scale;
        RectF r;
        w = 1200 ;
        h = 630 ;
        // Create the new bitmap
        bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.FILTER_BITMAP_FLAG);
        // Copy the original bitmap into the new one
        c = new Canvas(bmp);
        c.drawBitmap(source, 0, 0, paint);
        // Load the watermark
        watermark = gym_logo;
        // Scale the watermark to be approximately 40% of the source image height
       // scale = (float) (((float) h * v) / (float) watermark.getHeight());
        // Create the matrix
        matrix = new Matrix();
       // matrix.postScale(scale, scale);
        // Determine the post-scaled size of the watermark
        r = new RectF(0, 0, watermark.getWidth(), watermark.getHeight());
        matrix.mapRect(r);
        // Move the watermark to the bottom right corner
        //  matrix.postTranslate(w - r.width(), h - r.height());
        matrix.postTranslate(rw, rh);
        // Draw the watermark
        c.drawBitmap(watermark, matrix, paint);
        // Free up the bitmap memory
        watermark.recycle();
        return bmp;
    }

    public static Bitmap text_watermark(Bitmap src, String watermark,int x,int y , int size) {
        int w = 1200;
        int h = 630;
        Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());

        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(src, 0, 0, null);

        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#FFFFFFFF"));
        paint.setAlpha(500);
        paint.setTextSize(size);
        paint.setAntiAlias(true);
        paint.setFakeBoldText(true);
        canvas.drawText(watermark, x, y, paint);

        return result;
    }

    private int Calculate_String_Y_Position(String The_String){
        int Position = 0 ;
        int String_length = The_String.length();
            Position = String_length * 5 ;
        return Position ;
    }

    private void Make_Post(){


        Bitmap Facebook_Post_bg = BitmapFactory.decodeResource(getResources(),R.drawable.ic_facebook_post_bg);
        Image_Question_Category = BitmapFactory.decodeResource(getResources(),Question_Category_Image_Finder());
      //  Image_Player_Name = BitmapFactory.decodeResource(getResources(),R.drawable.ic_player_icon);


        Facebook_Post_bg = Bitmap.createScaledBitmap(Facebook_Post_bg, 1200, 630, false); // important  to change any image size to w1000 h1000 after crop .
        Image_Question_Category = Bitmap.createScaledBitmap(Image_Question_Category, 90, 90, false); // important  to change any image size to w1000 h1000 after crop .
        Image_Player_Photo = Bitmap.createScaledBitmap(getCroppedBitmap(Image_Player_Photo), 120, 120, false); // important  to change any image size to w1000 h1000 after crop .


        Bitmap Question_Category_Text = text_watermark(Facebook_Post_bg,String_Question_Category,785 - Calculate_String_Y_Position(String_Question_Category),90,30);

        if(String_Question.length() <= 40) {
             Question = text_watermark(Question_Category_Text, String_Question, 755 - Calculate_String_Y_Position(String_Question), 160, 35);
        }
        else {
            Question = TWO_LINE_QUESTION(Question_Category_Text , String_Question ) ;
        }

        Bitmap Answer_One = text_watermark(Question,String_Question_Answer_one,785 - Calculate_String_Y_Position(String_Question_Answer_one),312,30);

        Bitmap Answer_Two = text_watermark(Answer_One,String_Question_Answer_two,785 - Calculate_String_Y_Position(String_Question_Answer_two),387,30);

        Bitmap Answer_Three = text_watermark(Answer_Two,String_Question_Answer_three,785 - Calculate_String_Y_Position(String_Question_Answer_three),462,30);

        Bitmap Player_Name_Text = text_watermark(Answer_Three,String_Player_Name,190 - Calculate_String_Y_Position(String_Player_Name),330,35);

        Bitmap Question_Category_Image = Image_WaterMark(427,25,Player_Name_Text,Image_Question_Category);

        Player_Name_Image_and_final = Image_WaterMark(284,132,Question_Category_Image, Image_Player_Photo);

        imgv_facebook_post.setImageBitmap(Player_Name_Image_and_final);

    }

    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }

    public int Question_Category_Image_Finder(){

        int Category = 0 ;

        switch (String_Question_Category) {

            case "ثقافة" :
                Category = R.drawable.ic_btn_thakafa ;
                break;

            case "رياضة" :
                Category = R.drawable.ic_btn_reyada ;
                break;

            case "جغرافيا" :
                Category = R.drawable.ic_btn_gografya ;
                break;

            case "مخترعين" :
                Category = R.drawable.ic_btn_moktaree ;
                break;

            case "علوم" :
                Category = R.drawable.ic_btn_eloom ;
                break;

            case "تاريخ" :
                Category = R.drawable.ic_btn_tareek ;
                break;
        }
        return Category ;
    }

    private Bitmap TWO_LINE_QUESTION(Bitmap UP_Bitmap , String Question){
        Bitmap Two_Line_Question = null;
        String Question_Line_one = "";
        String Question_Line_two = "";

        String[] parts = Question.split(" ");

        for (int i = 0 ; i <= parts.length - 1 ; i++){

            String one_word = parts[i];
            
            if(Question_Line_one.length() <= 40){
            Question_Line_one = Question_Line_one + " " + one_word ;
            }
            else {
                Question_Line_two = Question_Line_two + " " + one_word ;
            }
        }

        Bitmap Question_Line_one_Bitmap = text_watermark(UP_Bitmap, Question_Line_one, 755 - Calculate_String_Y_Position(Question_Line_one), 160, 35);

        Bitmap Question_Line_two_Bitmap = text_watermark(Question_Line_one_Bitmap, Question_Line_two, 755 - Calculate_String_Y_Position(Question_Line_two), 215, 35);

        Two_Line_Question = Question_Line_two_Bitmap ;
        
        return Two_Line_Question ;
    }





    @Override
    public void onBackPressed() {
        Intent intent = new Intent(FacebookPostActivity.this, StartActivity.class);
        startActivity(intent);
        finish();
    }
}