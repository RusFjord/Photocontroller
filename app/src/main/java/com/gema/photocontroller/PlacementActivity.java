package com.gema.photocontroller;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.gema.photocontroller.adapters.PlacementAdapter;
import com.gema.photocontroller.commons.PlacementList;
import com.gema.photocontroller.db.PhotoControllerContract;
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

        Cursor cursor = PhotoControllerContract.PlacementEntry.getAllEntriesCursor();
        if (cursor == null) {
            Log.e("CURSOR PLACEFORADS", "Ошибка получения данных рекламных мест");
            finish();
        }

        String[] from = new String[] {"aid", "placeforads", "brandname"};
        int[] to = new int[] {R.id.id_placement, R.id.placeforads_placement, R.id.brand_name_placement};
        final SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.list_placement, cursor, from, to, 0);

//        PlacementList placementList = new PlacementList(getApplicationContext(), "placements.json");
//        final ArrayAdapter<PlacementPlace> adapter = new PlacementAdapter(this, placementList.getList());
//        setListAdapter(adapter);


        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                PlacementAdapter placementAdapter = (PlacementAdapter) adapter;
                //Toast.makeText(getApplicationContext(), placementAdapter.getElement(pos).getPlaceForAds(), Toast.LENGTH_SHORT).show();
                //String[] data = new String[4];
                PlacementPlace place = placementAdapter.getItem(pos);
                //data[0] = place.getId();
                //PlaceForAds placeForAds = new PlaceForAds()
//                data[1] = String.valueOf(place.getPlaceForAds().getId());
//                data[2] = place.getBrandName();
//                data[3] = place.getLayout();
                Intent intent = new Intent(getApplicationContext(), ShowPlacement.class);
                intent.putExtra("data", place.getId());
                startActivityForResult(intent, PLACEMENT_SHOW);
            }
        });
    }
}
