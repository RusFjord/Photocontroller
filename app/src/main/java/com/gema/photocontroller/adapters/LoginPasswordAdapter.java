package com.gema.photocontroller.adapters;

import android.content.Context;

import com.gema.photocontroller.commons.AppPreference;

public class LoginPasswordAdapter {

    private String password;

    public LoginPasswordAdapter(Context context) {
        AppPreference preference = new AppPreference(context);
        this.password = preference.getStringValue("password_app");
    }

    public Boolean comparePassword(String password) {
        return password.equals(this.password);
    }
}
