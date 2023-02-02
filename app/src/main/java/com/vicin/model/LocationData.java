package com.vicin.model;

public class LocationData {
    private String latitude;
    private String longitude;
    private String locationLine1;
    private String landmark;
    private String timeStamp;

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
