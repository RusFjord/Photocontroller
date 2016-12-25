package com.gema.photocontroller.application;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;

import com.gema.photocontroller.commons.SettingsHelper;
import com.gema.photocontroller.db.SQLHelper;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Photocontroler extends Application {

    private static SQLHelper dbHelper;
    private static SQLiteDatabase db;

    @Override
    public void onCreate() {
        super.onCreate();
        SettingsHelper.initInstance(this);
        setDb();

    }

    private void setDb() {
        dbHelper = new SQLHelper(this);
        db = dbHelper.getWritableDatabase();
    }

    public static Typeface getFont(Context context) {
        String fontPath = "fonts/Roboto-Light.ttf";
        return Typeface.createFromAsset(context.getAssets(), fontPath);
    }

    public static SQLiteDatabase getDb() {
        return db;
    }

    public static String getMD5EncryptedString(String encTarget){
        MessageDigest mdEnc = null;
        try {
            mdEnc = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Exception while encrypting to md5");
            e.printStackTrace();
        } // Encryption algorithm
        String md5 = null;
        if (mdEnc != null) {
            mdEnc.update(encTarget.getBytes(), 0, encTarget.length());
            md5 = new BigInteger(1, mdEnc.digest()).toString(16);
            while ( md5.length() < 32 ) {
                md5 = "0"+md5;
            }
        }
        return md5;
    }

}
