package com.gema.photocontroller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.gema.photocontroller.application.Photocontroler;
import com.gema.photocontroller.commands.AppUpdateCommand;
import com.gema.photocontroller.commons.AppPreference;
import com.gema.photocontroller.commons.DownloadFiles;
import com.gema.photocontroller.commons.PlaceForAdsUpdate;
import com.gema.photocontroller.commons.PoolOfUpdate;
import com.gema.photocontroller.commons.PreferenceData;
import com.gema.photocontroller.commons.WagonTypeUpdate;
import com.gema.photocontroller.commons.WagonUpdate;
import com.gema.photocontroller.db.UpdateDbTable;
import com.gema.photocontroller.interfaces.Command;

public class MainActivity extends Activity implements View.OnClickListener {

    private AppPreference preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setButtons();
        setImage();
        update();

        this.preference = new AppPreference(getApplicationContext());
    }

    private void setImage() {
        ImageView imageView = (ImageView) findViewById(R.id.image_main_menu);
        imageView.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.photocontroler));
    }

    private void update() {
        DownloadFiles downloadFiles = new DownloadFiles("path", getApplicationContext());
        MyTask myTask = new MyTask(downloadFiles);
        myTask.execute();
    }

    private void tryUpdateApp(DownloadFiles downloadFiles)
    {
        if (downloadFiles.tryUpdate()) {
            Command update = new AppUpdateCommand();
            update.execute(getApplicationContext());
            finish();
        }
    }

    private void makeUpdateRefs(PreferenceData preferenceData) {
        UpdateDbTable placeForAds = new PlaceForAdsUpdate(preferenceData);
        placeForAds.prepareTable(this, "placeforads.json");
        UpdateDbTable wagonTypes = new WagonTypeUpdate(preferenceData);
        wagonTypes.prepareTable(this, "wagontypes.json");
        UpdateDbTable wagon = new WagonUpdate(preferenceData);
        wagon.prepareTable(this, "wagons.json");
        PoolOfUpdate poolOfUpdate = Photocontroler.getPoolOfUpdate();
        poolOfUpdate.notifyListeners();
    }

    class MyTask extends AsyncTask<Void, Void, Void> {

        DownloadFiles downloadFiles;

        private MyTask(DownloadFiles downloadFiles) {
            this.downloadFiles = downloadFiles;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            downloadFiles.getFiles(getApplicationContext());
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            tryUpdateApp(this.downloadFiles);
            makeUpdateRefs(this.downloadFiles.getPreferenceData());
        }
    }

    private void setButtons() {

        Typeface typeface = Photocontroler.getFont(getApplicationContext());

        final Button tasks_btn = (Button)findViewById(R.id.tasks_btn);
        tasks_btn.setOnClickListener(this);
        tasks_btn.setTypeface(typeface);

        final Button placement_btn = (Button)findViewById(R.id.placement_btn);
        placement_btn.setOnClickListener(this);
        placement_btn.setTypeface(typeface);

        final Button photoreports_btn = (Button)findViewById(R.id.photoreports_btn);
        photoreports_btn.setOnClickListener(this);
        photoreports_btn.setTypeface(typeface);

        final Button detours_btn = (Button)findViewById(R.id.detours_btn);
        detours_btn.setOnClickListener(this);
        detours_btn.setTypeface(typeface);

        final Button journal_btn = (Button)findViewById(R.id.journal_btn);
        journal_btn.setOnClickListener(this);
        journal_btn.setTypeface(typeface);

        final Button settings_btn = (Button)findViewById(R.id.settings_btn);
        settings_btn.setOnClickListener(this);
        settings_btn.setTypeface(typeface);

        final Button about_btn = (Button)findViewById(R.id.about_btn);
        about_btn.setOnClickListener(this);
        about_btn.setTypeface(typeface);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.tasks_btn:
                //Toast.makeText(this, "Это задачи", Toast.LENGTH_SHORT).show();
                intent = new Intent(this, TasksActivity.class);
                break;
            case R.id.placement_btn:
                //Toast.makeText(this, "Это размещения", Toast.LENGTH_SHORT).show();
                intent = new Intent(this, PlacementActivity.class);
                break;
            case R.id.photoreports_btn:
                //Toast.makeText(this, "Это фотоотчеты", Toast.LENGTH_SHORT).show();
                intent = new Intent(this, ChangeTypeReportActivity.class);
                break;
            case R.id.detours_btn:
                //Toast.makeText(this, "Это объезды", Toast.LENGTH_SHORT).show();
                intent = new Intent(this, DetoursActivity.class);
                break;
            case R.id.journal_btn:
                //Toast.makeText(this, "Это журнал", Toast.LENGTH_SHORT).show();
                intent = new Intent(this, JournalActivity.class);
                break;
            case R.id.settings_btn:
                intent = new Intent(this, LoginActivity.class);
                break;
            case R.id.about_btn:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.app_name)
                        .setMessage(R.string.text_about)
                        .setIcon(R.drawable.ic_logo)
                        .setCancelable(false)
                        .setNegativeButton(R.string.button_ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
                break;
        }
        if(intent != null)
            startActivity(intent);

    }

}
