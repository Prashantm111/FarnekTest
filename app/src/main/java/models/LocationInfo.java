package models;

import java.util.Calendar;

public class LocationInfo {

    public double latitude;
    public double longitude;
    public String deviceID;
    public String date;

    public LocationInfo() {
    }

    public LocationInfo(double latitude, double longitude, String deviceID) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.deviceID = deviceID;
        this.date = Calendar.getInstance().getTime().toString();
    }
    public LocationInfo(double latitude, double longitude, String deviceID,String date) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.deviceID = deviceID;
        this.date = date;
    }
}
