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
        PlacementList placementList = new PlacementList(getApplicationContext(), "placements.json");
        String[] from = new String[] {"aid", "placeforadsName", "brandname"};
        int[] to = new int[] {R.id.id_placement, R.id.placeforads_placement, R.id.brand_name_placement};
        final SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.list_placement, cursor, from, to, 0);


//        final ArrayAdapter<PlacementPlace> adapter = new PlacementAdapter(this, placementList.getList());
//        setListAdapter(adapter);


        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(adapter);
        listView.setFastScrollEnabled(true);
        listView.setTextFilterEnabled(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                Cursor cursor = ((SimpleCursorAdapter)adapterView.getAdapter()).getCursor();
                cursor.moveToPosition(pos);

                Intent intent = new Intent(getApplicationContext(), ShowPlacement.class);
                intent.putExtra("data", cursor.getLong(0));
                startActivityForResult(intent, PLACEMENT_SHOW);
            }
        });
    }
}
