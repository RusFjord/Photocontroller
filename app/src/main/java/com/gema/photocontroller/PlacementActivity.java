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
import android.widget.Toast;

import com.gema.photocontroller.application.Photocontroler;
import com.gema.photocontroller.commons.PlacementList;
import com.gema.photocontroller.commons.PoolOfUpdate;
import com.gema.photocontroller.db.PhotoControllerContract;
import com.gema.photocontroller.interfaces.UpdateRefsListener;

public class PlacementActivity extends ListActivity implements UpdateRefsListener {

    private final int PLACEMENT_SHOW = 3233212;
    private final String GET_PLACEMENT = "get";
    private boolean isCall = false;
    private SimpleCursorAdapter adapter;
    private EditText placement_search;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placement);
        getPlacement();
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

    private void getPlacement() {
        Cursor cursor = PhotoControllerContract.PlacementEntry.getAllEntriesCursor();
        if (cursor == null) {
            Log.e("CURSOR PLACEMENTS", "Ошибка получения данных размещений");
            finish();
        }
        String[] from = new String[] {"aid", "placeforadsName", "brandname"};
        int[] to = new int[] {R.id.id_placement, R.id.placeforads_placement, R.id.brand_name_placement};
        adapter = new SimpleCursorAdapter(this, R.layout.list_placement, cursor, from, to, 0);
        adapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence charSequence) {
                return PhotoControllerContract.PlacementEntry.getFilterAidEntries(charSequence.toString());
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

                String data = "";
                Bundle currentData = getIntent().getExtras();
                if (currentData != null) {
                    data = currentData.getString("data");
                }

                Intent intent;
                if (data.equals(GET_PLACEMENT)) {
                    intent = getIntent();
                    intent.putExtra("data", cursor.getLong(0));
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    intent = new Intent(getApplicationContext(), ShowPlacement.class);
                    intent.putExtra("data", cursor.getLong(0));
                    startActivityForResult(intent, PLACEMENT_SHOW);
                }
            }
        });

        placement_search = (EditText) findViewById(R.id.placement_search);
        placement_search.addTextChangedListener(new TextWatcher() {
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
        String filter = this.placement_search.getText().toString();
        Cursor newCursor = null;
        if(filter.isEmpty()) {
            newCursor = PhotoControllerContract.PlacementEntry.getAllEntriesCursor();
        } else {
            newCursor = PhotoControllerContract.PlacementEntry.getFilterAidEntries(filter);
        }
        if(newCursor != null) {
            this.adapter.changeCursor(newCursor);
            this.adapter.notifyDataSetChanged();
        }
    }
}
