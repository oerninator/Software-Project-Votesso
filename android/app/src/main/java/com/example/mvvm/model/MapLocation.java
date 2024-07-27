package com.example.mvvm.model;

/**
 * Klasse die Geodaten bereitstellt
 */
public class MapLocation {

    private long id;

    // Längengrad
    private double longitude;

    // Breitengrad
    private double latitude;

    // *********************************************************************************************
    // Konstruktoren, Getter, Setter

    public MapLocation(long id, double longitude, double latitude) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public long getId() {
        return id;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}
