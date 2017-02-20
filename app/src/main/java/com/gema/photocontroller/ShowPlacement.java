package com.gema.photocontroller;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.gema.photocontroller.application.Photocontroler;
import com.gema.photocontroller.db.PhotoControllerContract;
import com.gema.photocontroller.models.PlaceForAds;
import com.gema.photocontroller.models.PlacementPlace;

import java.io.InputStream;
import java.util.Objects;

public class ShowPlacement extends Activity {

    private PlacementPlace placementPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_placement);
        setData();

    }

    private void setData() {
        Intent intent = getIntent();
        long data = intent.getLongExtra("data", 0);

        if (data != 0) {
            try {
                //this.placementPlace = new PlacementPlace(data);
                this.placementPlace = PhotoControllerContract.PlacementEntry.getOneEntry(data);
            } catch (Exception e) {
                e.getStackTrace();
            }

            if (this.placementPlace != null) {
                Typeface typeface = Photocontroler.getFont(getApplicationContext());
                final TextView id_single_placement = (TextView) findViewById(R.id.id_single_placement);
                id_single_placement.setTypeface(typeface);
                id_single_placement.setText(this.placementPlace.getAid());
                final TextView dates_single_placement = (TextView) findViewById(R.id.dates_single_placement);
                dates_single_placement.setTypeface(typeface);
                dates_single_placement.setText(this.placementPlace.getStartStopPlacement());
                final TextView placeforads_single_placement = (TextView) findViewById(R.id.placeforads_single_placement);
                placeforads_single_placement.setTypeface(typeface);
                //this.placeForAds = new PlacementAdv(data[1], data[2]);
                PlaceForAds placeForAds = this.placementPlace.getPlaceForAds();
                String placeForAdsText = "";
                if (placeForAds != null) {
                    placeForAdsText = placeForAds.getRepresentation();
                }
                placeforads_single_placement.setText(placeForAdsText);
                final TextView brand_name_single_placement = (TextView) findViewById(R.id.brand_name_single_placement);
                brand_name_single_placement.setTypeface(typeface);
                brand_name_single_placement.setText(this.placementPlace.getBrandName());
                //TODO: Установить картинку из файла макета
                final ImageView layout_single_placement = (ImageView) findViewById(R.id.layout_single_placement);
                //layout_single_placement.setTypeface(typeface);
                //layout_single_placement.setText(this.placementPlace.getLayout());
                String url = this.placementPlace.getLayout();
                //String url = "http://pikchyriki.net/avatar/multyashki/100/33.jpg";
                if (url != null && !Objects.equals(url, "")) {
                    DownloadImageTask downloadImageTask = new DownloadImageTask(layout_single_placement);
                    downloadImageTask.execute(url);
                }
            }
        }

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("DOWNLOAD IMAGE TASK", e.getMessage());
                //e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
