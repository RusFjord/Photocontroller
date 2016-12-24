package com.gema.photocontroller;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gema.photocontroller.adapters.LoginPasswordAdapter;
import com.gema.photocontroller.application.Photocontroler;

public class LoginActivity extends Activity {

    LoginPasswordAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setAdapter();
        setButtons();
    }

    private void setAdapter() {
        adapter = new LoginPasswordAdapter();
    }

    private void setButtons() {

        final Button sign_in_btn = (Button)findViewById(R.id.sign_in_btn);
        Typeface typeface = Photocontroler.getFont(getApplicationContext());
        sign_in_btn.setTypeface(typeface);
        sign_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int result_text = 0;
                final EditText login_edittext = (EditText)findViewById(R.id.login_edittext);
                if (login_edittext.getText().toString().equals(""))
                {
                    result_text = R.string.error_login_required;
                }
                else
                {
                    final EditText password_edittext = (EditText)findViewById(R.id.password_edittext);
                    if (password_edittext.getText().toString().equals(""))
                    {
                        result_text = R.string.error_password_required;
                    }
                    else
                    {
                        if (adapter.compareLogin(login_edittext.getText().toString())&&adapter.comparePassword(password_edittext.getText().toString())) {

                            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                            startActivity(intent);

                        } else {

                            result_text = R.string.error_incorrect_credentials;

                        }
                    }
                }

                if (result_text != 0) {
                    Toast.makeText(getApplicationContext(), result_text, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
