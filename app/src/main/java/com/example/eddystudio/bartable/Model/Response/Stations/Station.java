
package com.example.eddystudio.bartable.Model.Response.Stations;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Station {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("abbr")
    @Expose
    private String abbr;
    @SerializedName("gtfs_latitude")
    @Expose
    private String gtfsLatitude;
    @SerializedName("gtfs_longitude")
    @Expose
    private String gtfsLongitude;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("county")
    @Expose
    private String county;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("zipcode")
    @Expose
    private String zipcode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

    public String getGtfsLatitude() {
        return gtfsLatitude;
    }

    public void setGtfsLatitude(String gtfsLatitude) {
        this.gtfsLatitude = gtfsLatitude;
    }

    public String getGtfsLongitude() {
        return gtfsLongitude;
    }

    public void setGtfsLongitude(String gtfsLongitude) {
        this.gtfsLongitude = gtfsLongitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

}
