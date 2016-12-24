package com.gema.photocontroller.commons;

import android.content.Context;

import com.gema.photocontroller.files.WorkFiles;
import com.gema.photocontroller.models.PlacementPlace;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PlacementList extends WorkFiles {

    private ArrayList<PlacementPlace> list;

    public PlacementList(Context context, String filename) {
        super(filename);
        list = new ArrayList<PlacementPlace>();
        prepareList(context);
    }

    private void prepareList(Context context) {
//        PlacementPlace one = new PlacementPlace("Место 1", "AA432184", "Леруа Мерлен", "Макет 54");
//        list.add(one);
//        PlacementPlace two = new PlacementPlace("Место 2", "AC545722", "Ашан", "Макет 29");
//        list.add(two);
//        PlacementPlace three = new PlacementPlace("Место 24", "HT892241", "Гема Моторс", "Макет 9");
//        list.add(three);
//        PlacementPlace four = new PlacementPlace("Место 62", "YR437722", "Читай-город", "Макет 37");
//        list.add(four);
//        PlacementPlace five = new PlacementPlace("Место 217", "AH442481", "Моя семья", "Макет 15");
//        list.add(five);
        String fileList = super.ReadFile(context);
        try {
            JSONObject dataJson = new JSONObject(fileList);
            JSONArray placements = dataJson.getJSONArray("placements");
            for (int i = 0; i < placements.length(); i++) {
                JSONObject placement = placements.getJSONObject(i);
                list.add(new PlacementPlace(placement));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<PlacementPlace> getList() {
        return list;
    }

    public PlacementPlace getElement(int index) {
        PlacementPlace place = list.get(index);
        return place;
    }
}
