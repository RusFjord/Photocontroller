package com.gema.photocontroller;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.gema.photocontroller.adapters.JournalAdapter;
import com.gema.photocontroller.commons.JournalList;
import com.gema.photocontroller.interfaces.ChangeSendStateJournalRecordListener;
import com.gema.photocontroller.interfaces.ChangeSendStateJournalRecordPublisher;
import com.gema.photocontroller.models.JournalRecord;

import java.util.ArrayList;

public class JournalActivity extends ListActivity implements ChangeSendStateJournalRecordPublisher{

    private ArrayList<ChangeSendStateJournalRecordListener> listeners;
    private ArrayAdapter<JournalRecord> adapter;
    private JournalList journalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);
        getJournal();
        this.listeners = new ArrayList<>();
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
        });
    }

    @Override
    public void addListener(ChangeSendStateJournalRecordListener listener) {
        int index = this.listeners.indexOf(listener);
        if (index < 0) {
            this.listeners.add(listener);
        }
    }

    @Override
    public void deleteListener(ChangeSendStateJournalRecordListener listener) {
        int index = this.listeners.indexOf(listener);
        if (index >= 0 ) {
            this.listeners.remove(index);
        }
    }

    @Override
    public void notifyListeners() {
        for (ChangeSendStateJournalRecordListener listener : this.listeners) {
            listener.SendStatusChanged();
        }
    }

    class SendJournalRecordTask extends AsyncTask<Void, Void, Void> {

        private JournalRecord journalRecord;
        private boolean isSend = false;
        //JournalAdapter journalAdapter;

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
                isSend = true;
            } catch (Exception e) {
                Log.e("JOURNALRECORDSEND", "Ошибка отправления записи журнала.");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (isSend) {
                journalList.writeJSONArrayIntoFile(getApplicationContext());
                adapter.notifyDataSetChanged();
            }
            super.onPostExecute(result);
        }
    }
}
