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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.gema.photocontroller.adapters.StationsAdapter;
import com.gema.photocontroller.application.Photocontroler;
import com.gema.photocontroller.commons.PoolOfUpdate;
import com.gema.photocontroller.commons.StationsList;
import com.gema.photocontroller.db.PhotoControllerContract;
import com.gema.photocontroller.interfaces.UpdateRefsListener;
import com.gema.photocontroller.models.Stations;

public class StationsActivity extends ListActivity implements UpdateRefsListener {

    private SimpleCursorAdapter adapter;
    private EditText stations_search;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stations);
        getStations();
    }

    @Override
    protected void onResume() {
        PoolOfUpdate poolOfUpdate = Photocontroler.getPoolOfUpdate();
        poolOfUpdate.addListener(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        PoolOfUpdate poolOfUpdate = Photocontroler.getPoolOfUpdate();
        poolOfUpdate.deleteListener(this);
        super.onPause();
    }

    public void getStations() {
        Cursor cursor = PhotoControllerContract.StationsEntry.getAllEntriesCursor();
        if (cursor == null) {
            Log.e("CURSOR STATIONS", "Ошибка получения данных станций");
            finish();
        }
        String[] from = new String[] {"name"};
        int[] to = new int[] {R.id.name_stations};
        adapter = new SimpleCursorAdapter(this, R.layout.list_stations, cursor, from, to, 0);

        adapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence charSequence) {
                return PhotoControllerContract.StationsEntry.getFilterCodeEntries(charSequence.toString());
            }
        });
        listView = (ListView) findViewById(android.R.id.list);
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
//                StationsAdapter stationsAdapter = (StationsAdapter) adapter;
//                Intent intent = getIntent();
//                intent.putExtra("data", stationsAdapter.getItem(pos).getId());
//                setResult(RESULT_OK, intent);
//                finish();
            }
        });

        stations_search = (EditText) findViewById(R.id.stations_search);
        stations_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //adapter.getFilter().filter(charSequence);
                SimpleCursorAdapter filterAdapter = (SimpleCursorAdapter)listView.getAdapter();
                filterAdapter.getFilter().filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void update() {
        String filter = this.stations_search.getText().toString();
        Cursor newCursor = null;
        if(filter.isEmpty()) {
            newCursor = PhotoControllerContract.PlaceForAdsEntry.getAllEntriesCursor();
        } else {
            newCursor = PhotoControllerContract.PlaceForAdsEntry.getFilterCodeEntries(filter);
        }
        if(newCursor != null) {
            this.adapter.changeCursor(newCursor);
            this.adapter.notifyDataSetChanged();
        }
    }
}

