package com.gema.photocontroller;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.gema.photocontroller.adapters.PlacementAdapter;
import com.gema.photocontroller.commons.PlacementList;
import com.gema.photocontroller.models.PlaceForAds;
import com.gema.photocontroller.models.PlacementPlace;

public class PlacementActivity extends ListActivity {

    private final int PLACEMENT_SHOW = 3233212;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placement);
        getPlacement();
    }

    private void getPlacement() {
        PlacementList placementList = new PlacementList(getApplicationContext(), "placements.json");
        final ArrayAdapter<PlacementPlace> adapter = new PlacementAdapter(this, placementList.getList());
        setListAdapter(adapter);


        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                PlacementAdapter placementAdapter = (PlacementAdapter) adapter;
                //Toast.makeText(getApplicationContext(), placementAdapter.getElement(pos).getPlaceForAds(), Toast.LENGTH_SHORT).show();
                String[] data = new String[4];
                PlacementPlace place = placementAdapter.getItem(pos);
                data[0] = place.getId();
                //PlaceForAds placeForAds = new PlaceForAds()
                data[1] = String.valueOf(place.getPlaceForAds().getId());
                data[2] = place.getBrandName();
                data[3] = place.getLayout();
                Intent intent = new Intent(getApplicationContext(), ShowPlacement.class);
                intent.putExtra("data", data);
                startActivityForResult(intent, PLACEMENT_SHOW);
            }
        });
    }
}
