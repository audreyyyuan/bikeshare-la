package com.example.bikesharela.model;

public class Statistics {

    private Integer totalTrips;
    private Double averageDuration;
    private Double averageDistance;
    private String mostPopularStartingStation;
    private String mostPopularEndingStation;
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
    public Double getAverageDistance() {
        return averageDistance;
    }
    public void setAverageDistance(Double averageDistance) {
        this.averageDistance = averageDistance;
    }
    public String getMostPopularStartingStation() {
        return mostPopularStartingStation;
    }
    public void setMostPopularStartingStation(String mostPopularStartingStation) {
        this.mostPopularStartingStation = mostPopularStartingStation;
    }
    public String getMostPopularEndingStation() {
        return mostPopularEndingStation;
    }
    public void setMostPopularEndingStation(String mostPopularEndingStation) {
        this.mostPopularEndingStation = mostPopularEndingStation;
    }
    public Integer getCommuteTrips() {
        return commuteTrips;
    }
    public void setCommuteTrips(Integer commuteTrips) {
        this.commuteTrips = commuteTrips;
    }
}
