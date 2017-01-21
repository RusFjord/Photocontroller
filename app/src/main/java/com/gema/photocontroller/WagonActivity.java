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

import com.gema.photocontroller.commons.WagonTypeUpdate;
import com.gema.photocontroller.commons.WagonUpdate;
import com.gema.photocontroller.db.PhotoControllerContract;
import com.gema.photocontroller.db.UpdateDbTable;
import com.gema.photocontroller.interfaces.UpdateRefsListener;

public class WagonActivity extends ListActivity implements UpdateRefsListener {

    private SimpleCursorAdapter adapter;
    private EditText wagon_search;
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
        String[] from = new String[] {"name"};
        int[] to = new int[] {R.id.wagon_name};
        adapter = new SimpleCursorAdapter(this, R.layout.list_wagon, cursor, from, to, 0);
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
        wagon_search = (EditText) findViewById(R.id.wagon_search);
        wagon_search.addTextChangedListener(new TextWatcher() {
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
        String filter = this.wagon_search.getText().toString();
        Cursor newCursor = null;
        if(filter.isEmpty()) {
            newCursor = PhotoControllerContract.WagonEntry.getAllEntriesCursor();
        } else {
            newCursor = PhotoControllerContract.WagonEntry.getFilteredEntriesCursor(filter);
        }
        if(newCursor != null) {
            this.adapter.changeCursor(newCursor);
            this.adapter.notifyDataSetChanged();
        }
    }
}
