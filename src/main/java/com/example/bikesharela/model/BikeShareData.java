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
    private Integer startingPositionId;
    private Integer endingPositionId;
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

    public Integer getStartHour ( ) {
        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.setTime(this.startTime);   // assigns calendar to given date
        return calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format

    }

    public Integer getEndHour ( ) {
        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.setTime(this.endTime);   // assigns calendar to given date
        return calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format

    }

    public BikeShareData(Integer duration, String startTime, String endTime, Integer startingPositionId, Integer endingPositionId,
                   Integer bikeId, Integer planDuration, String tripCategory, String passholderType) {
        super();
        this.duration = duration;
        this.startTime = this.convertStringToDate(startTime);
        this.endTime = this.convertStringToDate(endTime);
        this.startingPositionId = startingPositionId;
        this.endingPositionId = endingPositionId;
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
    public Integer getStartingPositionId() {

        return startingPositionId;
    }
    public void setStartingPositionId(Integer startingPositionId) {

        this.startingPositionId = startingPositionId;
    }
    public Integer getEndingPositionId() {

        return endingPositionId;
    }
    public void setEndingPositionId(Integer endingPositionId) {

        this.endingPositionId = endingPositionId;
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
