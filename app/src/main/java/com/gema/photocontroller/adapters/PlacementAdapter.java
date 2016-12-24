package com.gema.photocontroller.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gema.photocontroller.R;
import com.gema.photocontroller.models.PlacementPlace;

import java.util.ArrayList;

public class PlacementAdapter extends ArrayAdapter<PlacementPlace> {

    public PlacementAdapter(Context context, ArrayList<PlacementPlace> placement) {
        super(context, android.R.layout.simple_list_item_2, placement);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PlacementPlace placementPlace = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_placement, null);
        }
        ((TextView) convertView.findViewById(R.id.placeforads_placement))
                .setText(placementPlace.getPlaceForAds().getName());
        ((TextView) convertView.findViewById(R.id.id_placement))
                .setText(placementPlace.getId());
        ((TextView) convertView.findViewById(R.id.brand_name_placement))
                .setText(placementPlace.getBrandName());
        return convertView;
    }

    public PlacementPlace getElement(int index) {
        PlacementPlace place = getItem(index);
        return place;
    }

}
