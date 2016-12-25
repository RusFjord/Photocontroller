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

import com.gema.photocontroller.adapters.ProblemsAdapter;
import com.gema.photocontroller.models.Problems;
import com.gema.photocontroller.commons.ProblemsList;

public class ProblemsActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problems);
        getProblems();
    }

    private void getProblems() {

        ProblemsList problemsList = new ProblemsList("problems.json", getApplicationContext());
        final ArrayAdapter<Problems> adapter = new ProblemsAdapter(this, problemsList.getList());
        setListAdapter(adapter);

        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                ProblemsAdapter problemsAdapter = (ProblemsAdapter) adapter;
                //Toast.makeText(getApplicationContext(), problemsAdapter.getItem(pos).getName(), Toast.LENGTH_SHORT).show();
                //String[] data = new String[2];
                //data[0] = problemsAdapter.getItem(pos).getId();
                //data[1] = problemsAdapter.getItem(pos).getName();
                Intent intent = getIntent();
                intent.putExtra("data", problemsAdapter.getItem(pos).getId());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        final EditText problems_search = (EditText) findViewById(R.id.problems_search);
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
