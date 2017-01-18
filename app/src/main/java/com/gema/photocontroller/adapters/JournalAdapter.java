package com.gema.photocontroller.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gema.photocontroller.R;
import com.gema.photocontroller.interfaces.PlacementAdv;
import com.gema.photocontroller.models.JournalRecord;
import com.gema.photocontroller.models.Stations;

import java.text.DateFormat;
import java.util.ArrayList;

public class JournalAdapter extends ArrayAdapter<JournalRecord>{

    public JournalAdapter(Context context, ArrayList<JournalRecord> journal) {
        super(context, android.R.layout.simple_list_item_2, journal);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        JournalRecord journalRecord = getItem(position);
        int textColor;
        if (!journalRecord.getSendState()) {
           textColor = Color.RED;
        } else {
            textColor = Color.BLACK;
        }
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_journal, null);
        }
        DateFormat df = android.text.format.DateFormat.getDateFormat(getContext());
        DateFormat tf = android.text.format.DateFormat.getTimeFormat(getContext());
        String date = df.format(journalRecord.getDate().getTime());
        String time = tf.format(journalRecord.getDate().getTime());
        TextView date_journal = (TextView) convertView.findViewById(R.id.date_journal);
        date_journal.setTextColor(textColor);
        date_journal.setText(date);

        TextView time_journal = (TextView) convertView.findViewById(R.id.time_journal);
        time_journal.setText(time);
        time_journal.setTextColor(textColor);

        PlacementAdv placeForAds = journalRecord.getPlacementAdv();
        Stations station = journalRecord.getStation();
        String currentText = "";
        if (placeForAds != null) {
            currentText = placeForAds.getRepresentation();
        } else {
            if (station != null) {
                currentText = station.getName();
            }
        }

        TextView placeforads_journal = (TextView) convertView.findViewById(R.id.placeforads_journal);
        placeforads_journal.setText(currentText);
        placeforads_journal.setTextColor(textColor);

        TextView type_journal = (TextView) convertView.findViewById(R.id.type_journal);
        type_journal.setText(journalRecord.getType());
        type_journal.setTextColor(textColor);

        TextView letter_journal = (TextView) convertView.findViewById(R.id.letter_journal);
        letter_journal.setText(journalRecord.getComment());
        letter_journal.setTextColor(textColor);

        return convertView;
    }

}
