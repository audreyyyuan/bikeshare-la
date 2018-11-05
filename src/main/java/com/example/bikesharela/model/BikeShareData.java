package com.example.bikesharela.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class BikeShareData {

    private Integer duration;
    private Date startTime;
    private Date endTime;
    private Integer startingStationId;
    private Double startingLatitude;
    private Double startingLongitude;
    private Integer endingStationId;
    private Double endingLatitude;
    private Double endingLongitude;
    private Integer bikeId;
    private Integer planDuration;
    private String tripCategory;
    private String passholderType;

    private Date convertStringToDate(String sDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = null;
        try {

            date = formatter.parse(sDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;

    }

    public Integer getStartHour() {
        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.setTime(this.startTime);   // assigns calendar to given date
        return calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format

    }

    public Integer getEndHour() {
        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.setTime(this.endTime);   // assigns calendar to given date
        return calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format

    }

    public Integer getMonth() {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(this.startTime);
        return calendar.get(Calendar.MONTH);
    }

    public BikeShareData(Integer duration, String startTime, String endTime, Integer startingPositionId,
                   Double startingLatitude, Double startingLongitude, Integer endingPositionId, Double endingLatitude,
                   Double endingLongitude, Integer bikeId, Integer planDuration, String tripCategory, String passholderType) {
        super();
        this.duration = duration;
        this.startTime = this.convertStringToDate(startTime);
        this.endTime = this.convertStringToDate(endTime);
        this.startingStationId = startingPositionId;
        this.startingLatitude = startingLatitude;
        this.startingLongitude = startingLongitude;
        this.endingStationId = endingPositionId;
        this.endingLatitude = endingLatitude;
        this.endingLongitude = endingLongitude;
        this.bikeId = bikeId;
        this.planDuration = planDuration;
        this.tripCategory = tripCategory;
        this.passholderType = passholderType;
    }
    public Integer getDuration() {

        return duration;
    }
    public void setDuration(Integer duration) {

        this.duration = duration;
    }
    public Date getStartTime() {

        return startTime;
    }
    public void setStartTime(Date startTime) {

        this.startTime = startTime;
    }
    public Date getEndTime() {

        return endTime;
    }
    public void setEndTime(Date endTime) {

        this.endTime = endTime;
    }
    public Integer getStartingStationId() {

        return startingStationId;
    }
    public void setStartingStationId(Integer startingStationId) {

        this.startingStationId = startingStationId;
    }
    public Double getStartingLatitude() {

        return startingLatitude;
    }
    public void setStartingLatitude(Double startingLatitude) {

        this.startingLatitude = startingLatitude;
    }
    public Double getStartingLongitude() {

        return startingLongitude;
    }
    public void setStartingLongitude(Double startingLongitude) {

        this.startingLongitude = startingLongitude;
    }
    public Integer getEndingStationId() {

        return endingStationId;
    }
    public void setEndingStationId(Integer endingStationId) {

        this.endingStationId = endingStationId;
    }
    public Double getEndingLatitude() {

        return endingLatitude;
    }
    public void getEndingLatitude(Double endingLatitude) {

        this.endingLatitude = endingLatitude;
    }
    public Double getEndingLongitude() {

        return endingLongitude;
    }
    public void getEndingLongitude(Double endingLongitude) {

        this.endingLongitude = endingLongitude;
    }
    public Integer getBikeId() {

        return bikeId;
    }
    public void setBikeId(Integer bikeId) {

        this.bikeId = bikeId;
    }
    public Integer getPlanDuration() {

        return planDuration;
    }
    public void setPlanDuration(Integer planDuration) {

        this.planDuration = planDuration;
    }
    public String getTripCategory() {

        return tripCategory;
    }
    public void setTripCategory(String tripCategory) {

        this.tripCategory = tripCategory;
    }
    public String getPassholderType() {

        return passholderType;
    }
    public void setPassholderType(String passholderType) {

        this.passholderType = passholderType;
    }
}
