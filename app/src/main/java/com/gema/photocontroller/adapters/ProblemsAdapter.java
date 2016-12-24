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
import com.gema.photocontroller.models.Problems;

import java.util.ArrayList;
import java.util.List;

public class ProblemsAdapter extends ArrayAdapter<Problems> implements Filterable {

    private Filter mFilter;
    private List<Problems> originalData;
    private List<Problems> filteredData;

    public ProblemsAdapter(Context context, ArrayList<Problems> problems) {
        super(context, android.R.layout.simple_list_item_2, problems);
        this.originalData = problems;
        this.filteredData = problems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Problems problem = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_problems, null);
        }
        ((TextView) convertView.findViewById(R.id.problems_name))
                .setText(problem.getName());

        return convertView;
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

    public Problems getItem(int position) {
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

            final List<Problems> list = originalData;

            int count = list.size();
            final ArrayList<Problems> nlist = new ArrayList<>(count);

            for (int i = 0; i < count; i++) {
                Problems element = list.get(i);
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
            filteredData = (ArrayList<Problems>) results.values;
            notifyDataSetChanged();
        }
    }
}
