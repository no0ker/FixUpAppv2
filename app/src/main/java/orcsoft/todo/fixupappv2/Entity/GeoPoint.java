package orcsoft.todo.fixupappv2.Entity;

public class GeoPoint {
    private double latitude;
    private double longitude;

    public GeoPoint() {
        latitude = 0;
        longitude = 0;
    }

    public GeoPoint(double lat, double lng) {
        this.latitude = lat;
        this.longitude = lng;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

}