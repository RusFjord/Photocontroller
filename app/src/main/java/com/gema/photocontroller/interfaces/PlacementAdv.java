package com.gema.photocontroller.interfaces;

import android.content.ContentValues;

import org.json.JSONObject;

public interface PlacementAdv {
    JSONObject getJSON();
    ContentValues getContentValues();
    String getRepresentation();
    long getId();
}
