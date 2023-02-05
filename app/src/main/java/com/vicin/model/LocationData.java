package com.vicin.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LocationData {
    private String latitude;
    private String longitude;
    private String locationLine1;
    private String landmark;
    private String timeStamp;

    final private String NOT_AVAILABLE = "not available";
    //Constructor that initalises values
    public LocationData() {
        setLongitude("Longitude " + NOT_AVAILABLE);
        setLatitude("Latitude " + NOT_AVAILABLE);
        setLandmark("Landmark " + NOT_AVAILABLE);
        setLocationLine1("Location details " + NOT_AVAILABLE);
        setTimeStamp(new SimpleDateFormat("dd-MMM-yyyy hh:mm a").format(new Date()));
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLocationLine1() {
        return locationLine1;
    }

    public void setLocationLine1(String locationLine1) {
        this.locationLine1 = locationLine1;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "LocationData{" +
                "latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", locationLine1='" + locationLine1 + '\'' +
                ", landmark='" + landmark + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                '}';
    }
}
