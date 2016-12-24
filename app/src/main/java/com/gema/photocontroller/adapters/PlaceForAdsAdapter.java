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
import com.gema.photocontroller.models.PlaceForAds;

import java.util.ArrayList;
import java.util.List;

public class PlaceForAdsAdapter extends ArrayAdapter<PlaceForAds> implements Filterable {

    private Filter mFilter;
    private List<PlaceForAds> originalData;
    private List<PlaceForAds> filteredData;

    public PlaceForAdsAdapter(Context context, ArrayList<PlaceForAds> placeForAdses) {
        super(context, android.R.layout.simple_list_item_2, placeForAdses);
        this.originalData = placeForAdses;
        this.filteredData = placeForAdses;
     }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PlaceForAds placeForAds = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_placeforads, null);
        }
        ((TextView) convertView.findViewById(R.id.id_placeforads))
                .setText(placeForAds.getId());
        ((TextView) convertView.findViewById(R.id.name_placeforads))
                .setText(placeForAds.getName());
        return convertView;
    }

    public PlaceForAds getElement(int index) {
        PlaceForAds placeForAds = getItem(index);
        return placeForAds;
    }

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

    public PlaceForAds getItem(int position) {
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

            final List<PlaceForAds> list = originalData;

            int count = list.size();
            final ArrayList<PlaceForAds> nlist = new ArrayList<>(count);

            for (int i = 0; i < count; i++) {
                PlaceForAds element = list.get(i);
                if (element.getId().toLowerCase().contains(filterString)) {
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
            filteredData = (ArrayList<PlaceForAds>) results.values;
            notifyDataSetChanged();
        }
    }
}
