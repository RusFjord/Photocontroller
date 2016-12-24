package com.gema.photocontroller.commons;

import android.content.Context;

import com.gema.photocontroller.files.WorkFiles;
import com.gema.photocontroller.models.PlaceForAds;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PlaceForAdsList extends WorkFiles{

    private ArrayList<PlaceForAds> list;

    public PlaceForAdsList(Context context, String filename) {
        super(filename);
        list = new ArrayList<PlaceForAds>();
        prepareList(context);
    }

    private void prepareList(Context context) {
//        PlaceForAds one = new PlaceForAds("1", "Место 1");
//        list.add(one);
//        PlaceForAds two = new PlaceForAds("2", "Место 2");
//        list.add(two);
//        PlaceForAds three = new PlaceForAds("24", "Место 24");
//        list.add(three);
//        PlaceForAds four = new PlaceForAds("62", "Место 62");
//        list.add(four);
//        PlaceForAds five = new PlaceForAds("217", "Место 217");
//        list.add(five);
        String fileList = super.ReadFile(context);
        try {
            JSONObject dataJson = new JSONObject(fileList);
            JSONArray placeForAdses = dataJson.getJSONArray("placeforads");
            for (int i = 0; i < placeForAdses.length(); i++) {
                JSONObject placeForAds = placeForAdses.getJSONObject(i);
                list.add(new PlaceForAds(placeForAds));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<PlaceForAds> getList() {
        return list;
    }

}
