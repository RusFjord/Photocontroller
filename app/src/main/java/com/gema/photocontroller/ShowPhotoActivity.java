package com.gema.photocontroller;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class ShowPhotoActivity extends Activity {

    private File photoFile = null;
    private TextView imageNameView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_photo);
        preparePhoto();
        if (photoFile != null) {
            setImageView();
            setImageNameView();
        }


    }

    private void preparePhoto() {
        String path = getIntent().getStringExtra("path");
        if (path != null) {
            try {
                photoFile = new File(path);
            }
            catch (Exception e) {
                Log.e("Photo file", "Don't found file");
            }
        }
    }

    private void setImageView() {
        imageView = (ImageView) findViewById(R.id.show_image);
        Bitmap photo = null;
        try {
            photo = BitmapFactory.decodeFile(photoFile.getPath());
        } catch (Exception e) {
            e.getStackTrace();
        }
        imageView.setImageBitmap(photo);
    }

    private void setImageNameView() {
        imageNameView = (TextView) findViewById(R.id.name_image);
        String name = photoFile.getName();
        imageNameView.setText(name);
    }

}
