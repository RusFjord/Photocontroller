package com.gema.photocontroller.application;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

import com.gema.photocontroller.commons.SettingsHelper;

public class Photocontroler extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SettingsHelper.initInstance(getApplicationContext());
    }

    public static Typeface getFont(Context context) {
        String fontPath = "fonts/Roboto-Light.ttf";
        return Typeface.createFromAsset(context.getAssets(), fontPath);
    }

}
