package com.gema.photocontroller;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.gema.photocontroller.adapters.StationsAdapter;
import com.gema.photocontroller.commons.StationsList;
import com.gema.photocontroller.models.Stations;

public class StationsActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stations);
        getStations();
    }


    public void getStations() {
        StationsList stationsList = new StationsList("stations.json", getApplicationContext());
        final ArrayAdapter<Stations> adapter = new StationsAdapter(this, stationsList.getList());
        setListAdapter(adapter);
        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                StationsAdapter stationsAdapter = (StationsAdapter) adapter;
//                String[] data = new String[2];
//                data[0] = stationsAdapter.getItem(pos).getId();
//                data[1] = stationsAdapter.getItem(pos).getName();
                Intent intent = getIntent();
                intent.putExtra("data", stationsAdapter.getItem(pos).getId());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        final EditText problems_search = (EditText) findViewById(R.id.stations_search);
        problems_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}

