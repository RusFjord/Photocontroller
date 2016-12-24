package com.gema.photocontroller;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gema.photocontroller.adapters.PhotoreportsAdapter;
import com.gema.photocontroller.application.Photocontroler;
import com.gema.photocontroller.commons.JournalList;
import com.gema.photocontroller.models.JournalRecord;
import com.gema.photocontroller.commons.PhotoHelper;
import com.gema.photocontroller.models.Stations;

import java.io.File;
import java.util.ArrayList;

public class PhotoreportsActivity extends ListActivity {

    private static final String IS_GROUP_STATE_KEY = "IS_GROUP_STATE_KEY";
    private final int STATIONS_REQUEST = 676213899;

    private boolean isFirst = true;
    private boolean isGroup = false;
    private boolean isSingle = true;
    private boolean showUriList = false;
    private PhotoHelper photoHelper;
    private ArrayList<File> listFilePhoto;

    private Stations station;
    private TextView change_stations;

    private ListView listView;

    final String TYPE = "Серия";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO: добавить выбор эскалаторного спуска/подъема
        setContentView(R.layout.activity_photoreports);
        listFilePhoto = new ArrayList<>();
        prepareList();
        setUI();
        photoHelper = new PhotoHelper();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private void setUI() {

        Typeface typeface = Photocontroler.getFont(getApplicationContext());

        final Button group_photo_send_btn = (Button) findViewById(R.id.group_photo_send_btn);
        group_photo_send_btn.setTypeface(typeface);
        group_photo_send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (station == null) {
                    Toast.makeText(getApplicationContext(), R.string.no_station, Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getApplicationContext(), R.string.record_journal_created, Toast.LENGTH_SHORT).show();
                JournalRecord journalRecord = new JournalRecord(TYPE, listFilePhoto, station);
                final EditText editText = (EditText) findViewById(R.id.group_photo_comment);
                String comment = editText.getText().toString().trim();
                if (!comment.isEmpty()) {
                    journalRecord.setComment(comment);
                }
                JournalList journal = new JournalList("journal.json", getApplicationContext());
                journal.add(journalRecord, getApplicationContext());
                finish();
            }
        });

        final TextView photoreports_list_header = (TextView) findViewById(R.id.photoreports_list_header);
        photoreports_list_header.setTypeface(typeface);

        this.change_stations =(TextView) findViewById(R.id.change_stations);
        this.change_stations.setTypeface(typeface);
        this.change_stations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StationsActivity.class);
                startActivityForResult(intent, STATIONS_REQUEST);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFirst) {
            groupOrSingle();
            isFirst = false;
            return;
        }
        if (showUriList) {
            getPhotoreportsList();
            return;
        }
        if (isGroup) {
            groupPhoto();
        } else {
            finish();
        }

    }

    private void groupOrSingle() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PhotoreportsActivity.this);
        builder.setTitle(R.string.app_name)
                .setMessage(R.string.text_group_or_single)
                .setIcon(R.drawable.ic_logo)
                .setCancelable(false)
                .setPositiveButton(R.string.button_one_photo,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                singlePhoto();
                                dialog.cancel();
                            }
                        })
                .setNegativeButton(R.string.button_group_photo,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                isGroup = true;
                                isSingle = false;
                                groupPhoto();
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.getWindow().setDimAmount(1);
        alert.show();
    }

    private void groupPhoto() {
        photoHelper.saveFullImage(this);
    }

    private void singlePhoto() {
        Intent intent = new Intent(this, SinglePhotoActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == STATIONS_REQUEST) {
                try {
                    this.station = new Stations(data.getStringArrayExtra("data")[0], data.getStringArrayExtra("data")[1]);
                    this.change_stations.setText(this.station.getName());
                } catch (Exception e) {
                    Log.e("STATIONS_REQUEST", "Невозможно получить станцию метро");
                }
            }
        }
        if (requestCode != STATIONS_REQUEST) {
            File result = photoHelper.getResult(requestCode, resultCode, data, this);
            if (result == null) {
                isGroup = false;
                showUriList = true;
            } else {
                addFileList(result);
            }
        }
    }

    private void addFileList(File file) {
        listFilePhoto.add(file);
    }

    private void getPhotoreportsList() {
        if (!listFilePhoto.isEmpty()) {
            ArrayAdapter<File> adapter = new PhotoreportsAdapter(this, listFilePhoto);
            setListAdapter(adapter);
        }

    }

    private void prepareList() {
        listView = (ListView) findViewById(android.R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                Intent intent = new Intent(getApplicationContext(), ShowPhotoActivity.class);
                intent.putExtra("path", listFilePhoto.get(pos).getPath());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle saveInstanceState) {

        // Сохраняем его состояние
        saveInstanceState.putBoolean(IS_GROUP_STATE_KEY, isGroup);

        // всегда вызывайте суперкласс для сохранения состояний видов
        super.onSaveInstanceState(saveInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        // Restore state members from saved instance
        isGroup = savedInstanceState.getBoolean(IS_GROUP_STATE_KEY);

    }


}
