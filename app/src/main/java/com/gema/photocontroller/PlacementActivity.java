package com.gema.photocontroller;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.gema.photocontroller.commons.PlacementList;
import com.gema.photocontroller.db.PhotoControllerContract;

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
        adapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence charSequence) {
                return PhotoControllerContract.PlacementEntry.getFilterAidEntries(charSequence.toString());
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

                Intent intent = new Intent(getApplicationContext(), ShowPlacement.class);
                intent.putExtra("data", cursor.getLong(0));
                startActivityForResult(intent, PLACEMENT_SHOW);
            }
        });

        final EditText placeforads_search = (EditText) findViewById(R.id.placement_search);
        placeforads_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                SimpleCursorAdapter filterAdapter = (SimpleCursorAdapter)listView.getAdapter();
                filterAdapter.getFilter().filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
