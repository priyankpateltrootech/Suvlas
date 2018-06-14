package com.suvlas;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by hp on 7/12/2017.
 */

public class Place {

    public String name;
    public LatLng latlng;

    public Place(String name, LatLng latlng) {
        this.name = name;
        this.latlng = latlng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getLatlng() {
        return latlng;
    }

    public void setLatlng(LatLng latlng) {
        this.latlng = latlng;
    }
}
