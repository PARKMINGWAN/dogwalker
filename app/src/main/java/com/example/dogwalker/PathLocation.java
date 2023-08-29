package com.example.dogwalker;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

@IgnoreExtraProperties
public class PathLocation {

    private List<Double> latitude;
    private List<Double> longitude;

    public PathLocation() {

    }

    public PathLocation(List<Double> latitude, List<Double> longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public List<Double> getLatitude() {
        return latitude;
    }

    public void setLatitude(List<Double> latitude) {
        this.latitude = latitude;
    }

    public List<Double> getLongitude() {
        return longitude;
    }

    public void setLongitude(List<Double> longitude) {
        this.longitude = longitude;
    }
}
