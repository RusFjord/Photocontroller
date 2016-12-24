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

import com.gema.photocontroller.adapters.PlaceForAdsAdapter;
import com.gema.photocontroller.models.PlaceForAds;
import com.gema.photocontroller.commons.PlaceForAdsList;

public class PlaceForAdsActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_for_ads);
        getPlaceForAds();
    }

    private void getPlaceForAds() {
        PlaceForAdsList placeForAdsList = new PlaceForAdsList(getApplicationContext(), "placeforads.json");
        final ArrayAdapter<PlaceForAds> adapter = new PlaceForAdsAdapter(this, placeForAdsList.getList());
        setListAdapter(adapter);

        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                PlaceForAdsAdapter placeForAdsAdapter = (PlaceForAdsAdapter) adapter;
                PlaceForAds current = placeForAdsAdapter.getElement(pos);
                String[] data = new String[2];
                data[0] = current.getId();
                data[1] = current.getName();

                Intent intent = getIntent();
                intent.putExtra("data", data);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        final EditText placeforads_search = (EditText) findViewById(R.id.placeforads_search);
        placeforads_search.addTextChangedListener(new TextWatcher() {
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
