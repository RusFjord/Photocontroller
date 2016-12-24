package com.gema.photocontroller;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import com.gema.photocontroller.application.Photocontroler;
import com.gema.photocontroller.models.PlaceForAds;
import com.gema.photocontroller.models.PlacementPlace;

import java.util.ArrayList;

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
        String[] data = intent.getStringArrayExtra("data");

        if (data != null) {
            try {
                this.placementPlace = new PlacementPlace(data);
            } catch (Exception e) {
                e.getStackTrace();
            }

            Typeface typeface = Photocontroler.getFont(getApplicationContext());
            final TextView id_single_placement = (TextView) findViewById(R.id.id_single_placement);
            id_single_placement.setTypeface(typeface);
            id_single_placement.setText(this.placementPlace.getId());
            final TextView placeforads_single_placement = (TextView) findViewById(R.id.placeforads_single_placement);
            placeforads_single_placement.setTypeface(typeface);
            //this.placeForAds = new PlaceForAds(data[1], data[2]);
            placeforads_single_placement.setText(this.placementPlace.getPlaceForAds().getName());
            final TextView brand_name_single_placement = (TextView) findViewById(R.id.brand_name_single_placement);
            brand_name_single_placement.setTypeface(typeface);
            brand_name_single_placement.setText(this.placementPlace.getBrandName());
            //TODO: Установить картинку из файла макета
            final TextView layout_single_placement = (TextView) findViewById(R.id.layout_single_placement);
            layout_single_placement.setTypeface(typeface);
            layout_single_placement.setText(this.placementPlace.getLayout());
        }

    }
}
