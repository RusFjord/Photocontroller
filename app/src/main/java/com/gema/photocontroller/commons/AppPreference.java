package com.gema.photocontroller.commons;

import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;

import java.util.HashMap;
import java.util.Map;

public class AppPreference {

    private final String DEVICE_IMEI = "device_imei";
    private final String TASKS_FILE = "tasks_file";
    private final String PLACEFORADS_FILE = "placeforads_file";
    private final String STATIONS_FILE = "stations_file";
    private final String PLACEMENTS_FILE = "placements_file";
    private final String PROBLEMS_FILE = "problems_file";
    private SharedPreferences currentPreference;

    private final String APP_FIRST_START = "app_first_start";
    private final String REMOTE_HOST = "remote_host";
    private final String REMOTE_LOGIN = "remote_login";
    private final String REMOTE_PASSWORD = "remote_password";

    public AppPreference(Context context) {
        final String APP_PREFERENCES = "PhotoControllerPreference";
        this.currentPreference = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        boolean appFirstStart = this.currentPreference.getBoolean(APP_FIRST_START, true);
        if(appFirstStart) {
            this.onFirstStart(context);
        }
    }

    public String getStringValue(String key) {
        String value = this.currentPreference.getString(key, "");
        return value;
    }

    public void setStringValue(String key, String value) {
        if (this.currentPreference.contains(key)) {
            SharedPreferences.Editor editor = this.currentPreference.edit();
            editor.putString(key, value);
            editor.apply();
        }
    }

    private void onFirstStart(Context context) {

        SharedPreferences.Editor editor = this.currentPreference.edit();
        editor.putBoolean(APP_FIRST_START, false);
        editor.putString(REMOTE_HOST, "trk-media.ru");
        editor.putString(REMOTE_LOGIN, "xml_sftp");
        editor.putString(REMOTE_PASSWORD, "cpu2800");
        editor.putString(TASKS_FILE, "tasks.zip");
        editor.putString(PLACEFORADS_FILE, "placeforads.zip");
        editor.putString(STATIONS_FILE, "stations.zip");
        editor.putString(PLACEMENTS_FILE, "placements.zip");
        editor.putString(PROBLEMS_FILE, "problems.zip");
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(context.TELEPHONY_SERVICE);
        String deviceIMEI = telephonyManager.getDeviceId();
        editor.putString(DEVICE_IMEI, deviceIMEI);
        editor.apply();
    }

    public RemoteStructure getRemoteSettings() {
        return new RemoteStructure(currentPreference.getString(REMOTE_HOST, ""), currentPreference.getString(REMOTE_LOGIN, ""), currentPreference.getString(REMOTE_PASSWORD, ""));
    }

    public Map<String, String> getRemoteFilenames() {
        Map<String, String> result = new HashMap<>();
        result.put(TASKS_FILE, this.currentPreference.getString(TASKS_FILE, ""));
        result.put(PROBLEMS_FILE, this.currentPreference.getString(PROBLEMS_FILE, ""));
        result.put(PLACEFORADS_FILE, this.currentPreference.getString(PLACEFORADS_FILE, ""));
        result.put(PLACEMENTS_FILE, this.currentPreference.getString(PLACEMENTS_FILE, ""));
        result.put(STATIONS_FILE, this.currentPreference.getString(STATIONS_FILE, ""));
        return result;
    }

    public class RemoteStructure {

        private String remoteHost;
        private String remoteLogin;
        private String remotePassword;

        RemoteStructure(String remoteHost, String remoteLogin, String remotePassword) {
            this.remoteHost = remoteHost;
            this.remoteLogin = remoteLogin;
            this.remotePassword = remotePassword;
        }


        public String getRemoteHost() {
            return remoteHost;
        }

        public String getRemoteLogin() {
            return remoteLogin;
        }

        public String getRemotePassword() {
            return remotePassword;
        }
    }

}
