package com.gema.photocontroller.commons;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhotoHelper {

    private File mOutputFile;

    private final int CAMERA_RESULT = 278361361;
    private final static String JPEG_FILE_PREFIX = "photo";
    private final static String JPEG_FILE_SUFFIX = ".jpg";
    private final int EXIT_FROM_GROUP = 0;

    public PhotoHelper() {

    }

    public void saveFullImage(Activity parent) {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        mOutputFile = getOutputMediaFile();
        if (mOutputFile != null) {
            intent.putExtra( MediaStore.EXTRA_OUTPUT, Uri.fromFile(mOutputFile));
            parent.startActivityForResult(intent, CAMERA_RESULT);
        }
    }

    private static File getOutputMediaFile(){

        File mediaFile = null;
        File mediaStorageDir = createOrGetDirectory();
        if (mediaStorageDir != null) {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    JPEG_FILE_PREFIX + "_"+ timeStamp + JPEG_FILE_SUFFIX);
        }
        return mediaFile;
    }

    private static File createOrGetDirectory() {

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "PhotoControler");

        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("Camera", "failed to create directory");
                mediaStorageDir = null;
            }
        }
        return mediaStorageDir;
    }

    public File getResult(int requestCode, int resultCode, Intent data, Activity parent) {

        if (resultCode == EXIT_FROM_GROUP) {
            mOutputFile = null;
        } else {
            if (requestCode == CAMERA_RESULT) {
                galleryAddPic(parent);
            }
        }
        return mOutputFile;
    }

    private void galleryAddPic(Activity parent) {
        if (mOutputFile != null) {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            mediaScanIntent.setData(Uri.fromFile(mOutputFile));
            parent.sendBroadcast(mediaScanIntent);
        }
    }

    public Bitmap getThumbnail(File file) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeFile(file.getPath()); //MediaStore.Images.Media.getBitmap(getContentResolver(), photoUri);
        } catch (Exception e) {
            e.getStackTrace();
        }
        return bitmap;
    }

    public String getRealPathFromURI(Uri contentUri, Context context) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
