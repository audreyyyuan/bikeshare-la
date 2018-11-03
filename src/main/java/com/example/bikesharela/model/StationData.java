package com.example.bikesharela.model;

public class StationData {

    private Integer stationID;
    private String stationName;
    private String liveDate;
    private String region;
    private boolean status;

    private boolean convertStringStatus(String status) {

        if(status.equals("Active"))
            return true;

        else
            return false;
    }

    public StationData(Integer stationID, String stationName, String liveDate, String region, String status) {

        this.stationID = stationID;
        this.stationName = stationName;
        this.liveDate = liveDate;
        this.region = region;
        this.status = convertStringStatus(status);
    }

    public Integer getStationID() {

        return stationID;
    }
    public void setStationID(Integer stationID) {

        this.stationID = stationID;
    }
    public String getStationName() {

        return stationName;
    }
    public void setStationName(String stationName) {

        this.stationName = stationName;
    }
    public String getLiveDate() {

        return liveDate;
    }
    public void setLiveDate(String liveDate) {

        this.liveDate = liveDate;
    }
    public String getRegion() {

        return region;
    }
    public void setRegion(String region) {

        this.region = region;
    }
    public boolean getStatus() {

        return status;
    }
    public void setStatus(String status) {

        this.status = this.convertStringStatus(status);
    }

}
