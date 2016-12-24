package com.gema.photocontroller.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gema.photocontroller.R;
import com.gema.photocontroller.models.Task;

import java.text.DateFormat;
import java.util.ArrayList;

public class TasksAdapter extends ArrayAdapter<Task> {

    public TasksAdapter(Context context, ArrayList<Task> tasks) {
        super(context, android.R.layout.simple_list_item_2, tasks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Task task = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_tasks, null);
        }
        DateFormat df = android.text.format.DateFormat.getDateFormat(getContext());
        String date = df.format(task.getDate().getTime());
        ((TextView) convertView.findViewById(R.id.date_task))
                .setText(date);
        ((TextView) convertView.findViewById(R.id.placeforads_task))
                .setText(task.getPlaceForAds().getName());
        ((TextView) convertView.findViewById(R.id.type_task))
                .setText(task.getType());
        ((TextView) convertView.findViewById(R.id.layout_task))
                .setText(task.getLayout());
        return convertView;
    }


}
