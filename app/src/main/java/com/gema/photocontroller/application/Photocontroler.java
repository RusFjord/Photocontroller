package com.gema.photocontroller.application;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;

import com.gema.photocontroller.commons.SettingsHelper;
import com.gema.photocontroller.db.SQLHelper;

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

}
