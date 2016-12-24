package com.gema.photocontroller.commons;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsHelper {

    private static SettingsHelper mInstance;

    public static SharedPreferences settings;

    private SettingsHelper (Context context) {
        String APP_PREFERENCES = "app_settings";
        settings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    public static void initInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SettingsHelper(context);
        }
    }

    public static SettingsHelper getInstance() {
        return mInstance;
    }

}
