package com.example.android.quakereport;


public class Earthquake {
    private double magnitude;
    private String location;
    private long timeInMilliseconds;
    private String url;


    /**
     * Constructs a new {@link Earthquake} object.
     *
     * @param magnitude is the magnitude (size) of the earthquake
     * @param location is the city location of the earthquake
     * @param timeInMilliseconds is the time in milliseconds (from the Epoch) when the
     *  earthquake happened
     */
    public Earthquake(double magnitude, String location, long timeInMilliseconds, String url) {
        this.magnitude = magnitude;
        this.location = location;
        this.timeInMilliseconds = timeInMilliseconds;
        this.url = url;
    }

    public double getMagnitude(){
        return this.magnitude;
    }

    public String getLocation(){
        return this.location;
    }

    public long getDate(){
        return this.timeInMilliseconds;
    }

    public String getUrl(){return this.url;}

}
