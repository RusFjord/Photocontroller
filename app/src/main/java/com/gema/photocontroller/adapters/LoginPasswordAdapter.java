package com.gema.photocontroller.adapters;

public class LoginPasswordAdapter {

    final private String login = "logunov";
    final private String password = "123";

    public Boolean compareLogin(String login) {
        return login.equals(this.login);
    }

    public Boolean comparePassword(String password) {
        return password.equals(this.password);
    }
}
