package com.gema.photocontroller;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.gema.photocontroller.adapters.JournalAdapter;
import com.gema.photocontroller.application.Photocontroler;
import com.gema.photocontroller.commons.JournalList;
import com.gema.photocontroller.models.JournalRecord;

public class JournalActivity extends ListActivity {

    private ArrayAdapter<JournalRecord> adapter;
    private JournalList journalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);
        getJournal();
        setButton();
    }

    private void setButton() {
        final Button send_all_btn = (Button) findViewById(R.id.send_all_btn);
        send_all_btn.setTypeface(Photocontroler.getFont(getApplicationContext()));
        send_all_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAll();
            }
        });
    }

    private void sendAll() {
        for (JournalRecord record : this.journalList.getList()) {
            if(!record.getSendState()) {
                SendJournalRecordTask task = new SendJournalRecordTask(record);
                task.execute();
            }
        }
    }

    private void getJournal() {
        journalList = new JournalList("journal.json", getApplicationContext());
        journalList.sort();
        if (journalList.getSize() == 0) {
            Toast.makeText(getApplicationContext(), R.string.no_records_in_journal, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        this.adapter = new JournalAdapter(this, journalList.getList());
        setListAdapter(adapter);

        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                JournalAdapter journalAdapter = (JournalAdapter) adapter;
                JournalRecord current = journalAdapter.getItem(pos);
                if (current != null) {
                    int message;
                    if (!current.getSendState()) {
                        message = R.string.send_journal_record;
                        SendJournalRecordTask task = new SendJournalRecordTask(current);
                        task.execute();
                    } else {
                        message = R.string.journal_record_was_send;
                    }
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    private class SendJournalRecordTask extends AsyncTask<Void, Void, Void> {

        private JournalRecord journalRecord;

        private SendJournalRecordTask(JournalRecord journalRecord) {
            this.journalRecord = journalRecord;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                this.journalRecord.send(getApplicationContext());
            } catch (Exception e) {
                Log.e("JOURNALRECORDSEND", "Ошибка отправления записи журнала.");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (journalRecord.getSendState()) {
                adapter.notifyDataSetChanged();
            }
            super.onPostExecute(result);
        }
    }
}
