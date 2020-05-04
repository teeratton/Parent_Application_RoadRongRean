package com.teerat.parent_map;

import com.google.android.gms.maps.model.LatLng;

public class Bus {
    private String name;
    private  LatLng location;

    public Bus(String name, LatLng location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public LatLng getLocation() {
        return location;
    }
}
