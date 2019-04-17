package com.xjek.provider.utils.location_service;

public class LocationPoint {

    private int id;
    private String lat, lng, mobtime, notes, distance;

    public LocationPoint() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getMobtime() {
        return mobtime;
    }

    public void setMobtime(String mobtime) {
        this.mobtime = mobtime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}