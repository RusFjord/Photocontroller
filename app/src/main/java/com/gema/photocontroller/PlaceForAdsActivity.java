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

import com.gema.photocontroller.application.Photocontroler;
import com.gema.photocontroller.commons.PoolOfUpdate;
import com.gema.photocontroller.db.PhotoControllerContract;
import com.gema.photocontroller.commons.PlaceForAdsList;
import com.gema.photocontroller.interfaces.UpdateRefsListener;

public class PlaceForAdsActivity extends ListActivity implements UpdateRefsListener {

    private ListView listView;
    private EditText placeforads_search;
    private SimpleCursorAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_for_ads);
        getPlaceForAds();
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

    private void getPlaceForAds() {
        Cursor cursor = PhotoControllerContract.PlaceForAdsEntry.getAllEntriesCursor();
        if (cursor == null) {
            Log.e("CURSOR PLACEFORADS", "Ошибка получения данных рекламных мест");
            finish();
        }

        String[] from = new String[] {"code", "name"};
        int[] to = new int[] {R.id.id_placeforads, R.id.name_placeforads};
        this.adapter = new SimpleCursorAdapter(this, R.layout.list_placeforads, cursor, from, to, 0);

        adapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence charSequence) {
                return PhotoControllerContract.PlaceForAdsEntry.getFilterCodeEntries(charSequence.toString());
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
            }
        });

        placeforads_search = (EditText) findViewById(R.id.placeforads_search);
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

    @Override
    public void update() {
        String filter = this.placeforads_search.getText().toString();
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
