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
import com.gema.photocontroller.commons.PhotoHelper;
import com.gema.photocontroller.db.PhotoControllerContract;
import com.gema.photocontroller.interfaces.PlacementAdv;
import com.gema.photocontroller.models.JournalRecord;
import com.gema.photocontroller.models.PlacementPlace;
import com.gema.photocontroller.models.Wagon;

import java.io.File;

public class SinglePhotoStickyActivity extends Activity {

    private final int GALLERY_REQUEST = 435278;
    private final int WAGON_REQUEST = 984763;
    private final int PLACEMENT_REQUEST = 620718;
    private final String TYPE = "Фотоотчет ЛА";

    private PhotoHelper photoHelper;
    private File photoFile;
    private Wagon wagon;
    private PlacementAdv stickyPackage;

    private TextView wagonTextView;
    private TextView placementTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_photo_sticky);
        setTextClick();
        setButtons();
        photoHelper = new PhotoHelper();
    }

    private void setTextClick() {
        wagonTextView = (TextView) findViewById(R.id.change_wagon);
        wagonTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WagonActivity.class);
                startActivityForResult(intent, WAGON_REQUEST);
            }
        });
        placementTextView = (TextView) findViewById(R.id.change_package);
        placementTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PlacementActivity.class);
                intent.putExtra("data", "get");
                startActivityForResult(intent, PLACEMENT_REQUEST);
            }
        });
    }

    private void setButtons() {
        Typeface typeface = Photocontroler.getFont(getApplicationContext());
        final Button make_single_photo_sticky_btn = (Button)findViewById(R.id.make_single_photo_sticky_btn);
        make_single_photo_sticky_btn.setTypeface(typeface);
        make_single_photo_sticky_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makePhoto();
            }
        });

        final Button get_single_photo_sticky_btn = (Button)findViewById(R.id.get_single_photo_sticky_btn);
        get_single_photo_sticky_btn.setTypeface(typeface);
        get_single_photo_sticky_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPhotoFromGallery();
            }
        });

        final Button single_photo_sticky_send_btn = (Button) findViewById(R.id.single_photo_sticky_send_btn);
        single_photo_sticky_send_btn.setTypeface(typeface);
        single_photo_sticky_send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), R.string.record_journal_created, Toast.LENGTH_SHORT).show();

                JournalRecord journalRecord = new JournalRecord(TYPE, stickyPackage, wagon, photoFile);
                final EditText editText = (EditText) findViewById(R.id.single_photo_comment);
                String comment = editText.getText().toString().trim();
                if (!comment.isEmpty()) {
                    journalRecord.setComment(comment);
                }
                journalRecord.add(true);
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

            } else if (requestCode == WAGON_REQUEST) {
                try {
                    Wagon wagon = PhotoControllerContract.WagonEntry.getOneEntry(data.getIntExtra("data", 0));
                    if (wagon != null) {
                        this.wagon = wagon;
                        wagonTextView.setText(this.wagon.getName());
                    }
                } catch (Exception e) {
                    Log.e("STICKY ACTIVITY WAGON", e.getMessage());
                }
            } else if (requestCode == PLACEMENT_REQUEST) {
                try {
                    PlacementPlace placementPlace = PhotoControllerContract.PlacementEntry.getOneEntry(data.getLongExtra("data", 0));
                    if (placementPlace != null) {
                        this.stickyPackage = placementPlace;
                        placementTextView.setText(this.stickyPackage.getRepresentation());
                    }
                } catch (Exception e) {
                    Log.e("STICKY ACTIVITY PLACE", e.getMessage());
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
            ImageView imageView = (ImageView) findViewById(R.id.sticky_photo_image);
            imageView.setImageBitmap(bitmap);
        }
    }
}
