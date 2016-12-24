package com.gema.photocontroller.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.gema.photocontroller.R;
import com.gema.photocontroller.models.Stations;

import java.util.ArrayList;
import java.util.List;

public class StationsAdapter extends ArrayAdapter<Stations> implements Filterable {

    private Filter mFilter;
    private List<Stations> originalData;
    private List<Stations> filteredData;

    public StationsAdapter(Context context, ArrayList<Stations> stations) {
        super(context, android.R.layout.simple_list_item_2, stations);
        this.originalData = stations;
        this.filteredData = stations;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Stations station = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_stations, null);
        }
        ((TextView) convertView.findViewById(R.id.name_stations))
                .setText(station.getName());

        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ItemFilter();
        }
        return mFilter;
    }

    @Override
    public int getCount (){
        return filteredData.size();
    }

    public Stations getItem(int position) {
        return filteredData.get(position);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<Stations> list = originalData;

            int count = list.size();
            final ArrayList<Stations> nlist = new ArrayList<>(count);

            for (int i = 0; i < count; i++) {
                Stations element = list.get(i);
                if (element.getName().toLowerCase().contains(filterString)) {
                    nlist.add(element);
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<Stations>) results.values;
            notifyDataSetChanged();
        }
    }
}

