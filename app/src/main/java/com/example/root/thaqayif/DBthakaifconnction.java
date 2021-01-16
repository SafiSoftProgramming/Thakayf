package com.example.root.thaqayif;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by root on 9/23/17.
 */

public class DBthakaifconnction extends SQLiteOpenHelper {

    String DB_PATH = null;
    private static String DB_NAME = "thakaifdatabase";
    private SQLiteDatabase myDataBase;
    private final Context myContext;

    public DBthakaifconnction(Context context) {
        super(context, DB_NAME, null, 10);
        this.myContext = context;
        this.DB_PATH = "/data/data/" + context.getPackageName() + "/" + "databases/";
        Log.e("Path 1", DB_PATH);
    }


    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (dbExist) {
        } else {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        } catch (SQLiteException e) {
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }


    private void copyDataBase() throws IOException {
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[10];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();
        super.close();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion)
            try {
                copyDataBase();
            } catch (IOException e) {
                e.printStackTrace();

            }
    }

    public Cursor querysince(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        return myDataBase.query("since",null,null,null,null,null,null);
    }

    public Cursor querylogindata(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        return myDataBase.query("logindata",null,null,null,null,null,null);
    }

    public Cursor querygorafya(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        return myDataBase.query("gorafya",null,null,null,null,null,null);
    }
    public Cursor queryhistory(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        return myDataBase.query("history",null,null,null,null,null,null);
    }
    public Cursor queryskafa(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        return myDataBase.query("skafa",null,null,null,null,null,null);
    }
    public Cursor querysport(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        return myDataBase.query("sport",null,null,null,null,null,null);
    }
    public Cursor queryinvint(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        return myDataBase.query("invint",null,null,null,null,null,null);
    }
    public Cursor queryscore(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        return myDataBase.query("score",null,null,null,null,null,null);
    }



    public void insertRecordlogindata(String USERNAME,String USERAGE,String GANDER,String USEREMAIL ) {
        SQLiteDatabase database = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username",USERNAME);
        contentValues.put("userage",USERAGE);
        contentValues.put("gander",GANDER);
        contentValues.put("useremail",USEREMAIL);
        database.insert("logindata", null, contentValues);
        database.close();
    }



    public void updateRecord(String TABEL_NAME,String COLUMN_NAME,String RECORD_ID,String COLUMN_VAL) {
        SQLiteDatabase database = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME,COLUMN_VAL);
        database.update(TABEL_NAME, contentValues, "_id" + " = ?", new String[]{RECORD_ID});
        database.close();
    }

    public void updateRecordlogin(String TABEL_NAME,String COLUMN_NAME1,String COLUMN_NAME2,String COLUMN_NAME3,String COLUMN_NAME4,String RECORD_ID,String COLUMN_VAL1,String COLUMN_VAL2,String COLUMN_VAL3,String COLUMN_VAL4) {
        SQLiteDatabase database = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME1,COLUMN_VAL1);
        contentValues.put(COLUMN_NAME2,COLUMN_VAL2);
        contentValues.put(COLUMN_NAME3,COLUMN_VAL3);
        contentValues.put(COLUMN_NAME4,COLUMN_VAL4);
        database.update(TABEL_NAME, contentValues, "username" + " = ?", new String[]{RECORD_ID});
        database.close();
    }



    public void deleteRecordAlternate(String TABLE_NAME,String COLUMN_ID,String ID_NUM) {
       SQLiteDatabase database = this.getReadableDatabase();
        database.execSQL("delete from " + TABLE_NAME + " where " + COLUMN_ID + " = '" + ID_NUM + "'");
        database.close();
    }




}