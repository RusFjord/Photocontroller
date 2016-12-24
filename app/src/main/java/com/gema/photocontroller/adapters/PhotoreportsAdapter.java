package com.gema.photocontroller.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gema.photocontroller.R;

import java.io.File;
import java.util.ArrayList;

public class PhotoreportsAdapter extends ArrayAdapter<File>{
    public PhotoreportsAdapter(Context context, ArrayList<File> listFile) {
        super(context, android.R.layout.simple_list_item_2, listFile);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        File photoFile = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_photoreports, null);
        }
        ((TextView) convertView.findViewById(R.id.uri_photoreports))
                .setText(photoFile.getName());

        return convertView;
    }
}
