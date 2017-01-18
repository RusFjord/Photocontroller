package com.gema.photocontroller;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.gema.photocontroller.application.Photocontroler;

public class ChangeTypeReportActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_type_report);
        setButtons();
    }

    private void setButtons() {
        Typeface typeface = Photocontroler.getFont(getApplicationContext());
        final Button change_shield_btn = (Button)findViewById(R.id.change_shield_btn);
        change_shield_btn.setTypeface(typeface);
        change_shield_btn.setOnClickListener(this);
        final Button change_sticky_btn = (Button)findViewById(R.id.change_sticky_btn);
        change_sticky_btn.setTypeface(typeface);
        change_sticky_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.change_shield_btn:
                intent = new Intent(this, PhotoreportsActivity.class);
                break;
            case R.id.change_sticky_btn:
                intent = new Intent(this, SinglePhotoStickyActivity.class);
                break;
        }
        if(intent != null) {
            startActivity(intent);
            finish();
        }
    }
}
