package com.example.bikesharela.model;

public class Statistics {

    private Integer totalTrips;
    private Double averageDuration;
    private Integer mostPopularStartingStation;
    private Integer commuteTrips;


    public Integer getTotalTrips() {
        return totalTrips;
    }
    public void setTotalTrips(Integer totalTrips) {
        this.totalTrips = totalTrips;
    }
    public Double getAverageDuration() {
        return averageDuration;
    }
    public void setAverageDuration(Double averageDuration) {
        this.averageDuration = averageDuration;
    }
    public Integer getMostPopularStartingStation() {
        return mostPopularStartingStation;
    }
    public void setMostPopularStartingStation(Integer mostPopularStartingStation) {
        this.mostPopularStartingStation = mostPopularStartingStation;
    }
    public Integer getCommuteTrips() {
        return commuteTrips;
    }
    public void setCommuteTrips(Integer commuteTrips) {
        this.commuteTrips = commuteTrips;
    }
}
