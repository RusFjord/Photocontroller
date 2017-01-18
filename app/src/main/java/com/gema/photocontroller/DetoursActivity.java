package com.gema.photocontroller;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gema.photocontroller.application.Photocontroler;
import com.gema.photocontroller.db.PhotoControllerContract;
import com.gema.photocontroller.models.JournalRecord;
import com.gema.photocontroller.commons.PhotoHelper;
import com.gema.photocontroller.models.PlaceForAds;
import com.gema.photocontroller.models.Problems;

import java.io.File;

public class DetoursActivity extends Activity {

    private final int GALLERY_REQUEST = 6786876;
    private final int PLACEFORADS_REQUEST = 1;
    private final int PROBLEMS_REQUEST = 2;
    private final String TYPE = "Объезд";

    private PhotoHelper photoHelper;
    private File photoFile;

    TextView placeForAdsTextView;
    TextView problemsTextView;

    PlaceForAds placeForAds;
    Problems problem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detours);
        setButtons();
        setTextClick();
        photoHelper = new PhotoHelper();
    }

    private void setTextClick() {
        placeForAdsTextView = (TextView) findViewById(R.id.change_place_for_ads);
        placeForAdsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PlaceForAdsActivity.class);
                startActivityForResult(intent, PLACEFORADS_REQUEST);
            }
        });

        problemsTextView = (TextView) findViewById(R.id.change_problems_for_ads);
        problemsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProblemsActivity.class);
                startActivityForResult(intent, PROBLEMS_REQUEST);
            }
        });
    }
    private void setButtons() {
        Typeface typeface = Photocontroler.getFont(getApplicationContext());
        final Button make_photo_btn = (Button)findViewById(R.id.make_single_photo_btn);
        make_photo_btn.setTypeface(typeface);
        make_photo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makePhoto();
            }
        });

        final Button get_photo_btn = (Button)findViewById(R.id.get_single_photo_btn);
        get_photo_btn.setTypeface(typeface);
        get_photo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPhotoFromGallery();
            }
        });

        final Button photo_send_btn = (Button)findViewById(R.id.photo_send_btn);
        photo_send_btn.setTypeface(typeface);
        photo_send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (placeForAds == null) {
                    Toast.makeText(getApplicationContext(), R.string.no_placeforads, Toast.LENGTH_SHORT).show();
                    return;
                }
                JournalRecord journalRecord = new JournalRecord(TYPE, placeForAds, photoFile, problem);
                final EditText editText = (EditText) findViewById(R.id.detours_comment);
                String comment = editText.getText().toString().trim();
                if (!comment.isEmpty()) {
                    journalRecord.setComment(comment);
                }
                journalRecord.add(true);
                Toast.makeText(getApplicationContext(), R.string.record_journal_created, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void makePhoto() {
        photoHelper.saveFullImage(this);
    }

    private void getPhotoFromGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_REQUEST) {

                Uri selectedImage = data.getData();
                photoFile = new File(photoHelper.getRealPathFromURI(selectedImage, getApplicationContext()));

            } else if (requestCode == PLACEFORADS_REQUEST) {
                try {
                    PlaceForAds placeForAds = PhotoControllerContract.PlaceForAdsEntry.getOneEntry(data.getIntExtra("data", 0));
                    if (placeForAds != null) {
                        this.placeForAds = placeForAds;
                        placeForAdsTextView.setText(this.placeForAds.getName());
                    }
                } catch (Exception e) {
                    Log.e("PLACEFORADS_REQUEST", "Невозможно получить рекламное место");
                }

            } else if (requestCode == PROBLEMS_REQUEST) {
                try {
                    Problems problem = PhotoControllerContract.ProblemsEntry.getOneEntry(data.getIntExtra("data", 0));
                    if (problem != null) {
                        this.problem = problem;
                        problemsTextView.setText(this.problem.getName());
                    }
                } catch (Exception e) {
                    e.getStackTrace();
                }
            } else {
                photoFile = photoHelper.getResult(requestCode, resultCode, data, this);
            }
        }

        if (photoFile != null) {
            showPhoto();
        }

    }

    private void showPhoto() {
        Bitmap bitmap = photoHelper.getThumbnail(photoFile);
        if (bitmap != null) {
            ImageView imageView = (ImageView) findViewById(R.id.photo_image);
            imageView.setImageBitmap(bitmap);
        }
    }

}
