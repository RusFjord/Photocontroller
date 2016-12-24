package com.gema.photocontroller;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.gema.photocontroller.models.Task;
import com.gema.photocontroller.adapters.TasksAdapter;
import com.gema.photocontroller.commons.TasksList;

public class TasksActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        getTasksList();
    }

    private void getTasksList() {
        TasksList tasksList = new TasksList("tasks.json", getApplicationContext());
        tasksList.sort();
        ArrayAdapter<Task> adapter = new TasksAdapter(this, tasksList.getList());
        setListAdapter(adapter);
    }

}
