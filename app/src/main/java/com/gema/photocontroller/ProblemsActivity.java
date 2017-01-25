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

import com.gema.photocontroller.adapters.ProblemsAdapter;
import com.gema.photocontroller.application.Photocontroler;
import com.gema.photocontroller.commons.PoolOfUpdate;
import com.gema.photocontroller.db.PhotoControllerContract;
import com.gema.photocontroller.interfaces.UpdateRefsListener;
import com.gema.photocontroller.models.Problems;
import com.gema.photocontroller.commons.ProblemsList;

public class ProblemsActivity extends ListActivity implements UpdateRefsListener {

    private SimpleCursorAdapter adapter;
    private EditText problems_search;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problems);
        getProblems();
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

    private void getProblems() {

//        ProblemsList problemsList = new ProblemsList("problems.json", getApplicationContext());
//        final ArrayAdapter<Problems> adapter = new ProblemsAdapter(this, problemsList.getList());
//        setListAdapter(adapter);

        Cursor cursor = PhotoControllerContract.ProblemsEntry.getAllEntriesCursor();
        if (cursor == null) {
            Log.e("CURSOR PROBLEMS", "Ошибка получения данных по проблемам");
            finish();
        }
        String[] from = new String[] {"name"};
        int[] to = new int[] {R.id.problems_name};
        adapter = new SimpleCursorAdapter(this, R.layout.list_problems, cursor, from, to, 0);
        adapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence charSequence) {
                return PhotoControllerContract.ProblemsEntry.getFilterNameEntries(charSequence.toString());
            }
        });
        listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(adapter);
        listView.setFastScrollEnabled(true);
        listView.setTextFilterEnabled(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
//                ProblemsAdapter problemsAdapter = (ProblemsAdapter) adapter;
//                Intent intent = getIntent();
//                intent.putExtra("data", problemsAdapter.getItem(pos).getId());
//                setResult(RESULT_OK, intent);
//                finish();
                Cursor cursor = ((SimpleCursorAdapter)adapterView.getAdapter()).getCursor();
                cursor.moveToPosition(pos);

                Intent intent = getIntent();
                intent.putExtra("data", cursor.getInt(0));
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        problems_search = (EditText) findViewById(R.id.problems_search);
        problems_search.addTextChangedListener(new TextWatcher() {
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
        String filter = this.problems_search.getText().toString();
        Cursor newCursor = null;
        if(filter.isEmpty()) {
            newCursor = PhotoControllerContract.ProblemsEntry.getAllEntriesCursor();
        } else {
            newCursor = PhotoControllerContract.ProblemsEntry.getFilterNameEntries(filter);
        }
        if(newCursor != null) {
            this.adapter.changeCursor(newCursor);
            this.adapter.notifyDataSetChanged();
        }
    }
}
