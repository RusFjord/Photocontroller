package com.gema.photocontroller.commands;

import android.content.Context;
import android.os.AsyncTask;

import com.gema.photocontroller.application.Photocontroler;
import com.gema.photocontroller.commons.PlaceForAdsUpdate;
import com.gema.photocontroller.commons.PlacementUpdate;
import com.gema.photocontroller.commons.PoolOfUpdate;
import com.gema.photocontroller.commons.PreferenceData;
import com.gema.photocontroller.commons.ProblemsUpdate;
import com.gema.photocontroller.commons.StationsUpdate;
import com.gema.photocontroller.commons.WagonTypeUpdate;
import com.gema.photocontroller.commons.WagonUpdate;
import com.gema.photocontroller.db.UpdateDbTable;

public class RefUpdateCommand extends AsyncTask<Void, Void, Void> {

    private PreferenceData preferenceData;
    private Context context;

    public RefUpdateCommand(PreferenceData preferenceData, Context context) {
        this.preferenceData = preferenceData;
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        UpdateDbTable placeForAds = new PlaceForAdsUpdate(preferenceData);
        placeForAds.prepareTable(context, "placeforads.json", preferenceData.getCurrentMd5("placeforads.json"));
        UpdateDbTable wagonTypes = new WagonTypeUpdate(preferenceData);
        wagonTypes.prepareTable(context, "wagontypes.json", preferenceData.getCurrentMd5("wagontypes.json"));
        UpdateDbTable wagon = new WagonUpdate(preferenceData);
        wagon.prepareTable(context, "wagons.json", preferenceData.getCurrentMd5("wagons.json"));
        UpdateDbTable placement = new PlacementUpdate(preferenceData);
        placement.prepareTable(context, "placements.json", preferenceData.getCurrentMd5("placements.json"));
        UpdateDbTable stations = new StationsUpdate(preferenceData);
        stations.prepareTable(context, "stations.json", preferenceData.getCurrentMd5("stations.json"));
        UpdateDbTable problems = new ProblemsUpdate(preferenceData);
        problems.prepareTable(context, "problems.json", preferenceData.getCurrentMd5("problems.json"));
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        PoolOfUpdate poolOfUpdate = Photocontroler.getPoolOfUpdate();
        poolOfUpdate.notifyListeners();
    }
}
