package com.gema.photocontroller;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.gema.photocontroller.commons.WagonUpdate;
import com.gema.photocontroller.db.PhotoControllerContract;
import com.gema.photocontroller.db.UpdateDbTable;

public class WagonActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wagon);
        getWagons();
    }

    private void getWagons() {
        Cursor cursor = PhotoControllerContract.WagonEntry.getAllEntriesCursor();
        if (cursor == null) {
            Log.e("CURSOR WAGON", "Ошибка получения данных по вагонам");
            finish();
        }
        //PlacementList placementList = new PlacementList(getApplicationContext(), "placements.json");
        UpdateDbTable wagonTypes = new WagonUpdate();
        wagonTypes.prepareTable(getApplicationContext(), "wagontypes.json");
        UpdateDbTable wagon = new WagonUpdate();
        wagon.prepareTable(getApplicationContext(), "wagon.json");
        String[] from = new String[] {"name"};
        int[] to = new int[] {R.id.wagon_name};
        final SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.list_wagon, cursor, from, to, 0);
        adapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence charSequence) {
                return PhotoControllerContract.WagonEntry.getFilteredEntriesCursor(charSequence.toString());
            }
        });

        final ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(adapter);
        listView.setFastScrollEnabled(true);
        listView.setTextFilterEnabled(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                Cursor cursor = ((SimpleCursorAdapter)adapterView.getAdapter()).getCursor();
                cursor.moveToPosition(pos);

                Intent intent = getIntent();
                intent.putExtra("data", cursor.getInt(0));
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }
}
