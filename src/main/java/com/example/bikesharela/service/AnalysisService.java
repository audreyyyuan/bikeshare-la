package com.example.bikesharela.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import com.example.bikesharela.model.StationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bikesharela.model.BikeShareData;
import com.example.bikesharela.model.Statistics;

@Service
public class AnalysisService {

    @Autowired
    private ParseBikeShareService parseBikeShareService;
    @Autowired
    private ParseStationService parseStationService;

    private List<BikeShareData> dataList;
    private AbstractMap<Integer, StationData> stationList;

    private static final double Rearth = 3963.191; //miles
    private static final double speed = 7.456; //from https://www.citibikenyc.com/system-data

    @PostConstruct
    public void init() throws FileNotFoundException, IOException {
        this.dataList = this.parseBikeShareService.loadData("data.csv");
        this.stationList = this.parseStationService.loadStations("stations.csv");
    }

    public Statistics getStatistics() {

        Statistics stat = new Statistics();

        stat.setTotalTrips(this.dataList.size());

        //calculates average duration of bike rides
        Double averageDuration = this.dataList
                .stream()
                .collect(Collectors.averagingInt(r -> r.getDuration()));

        stat.setAverageDuration(averageDuration);
        stat.setAverageDuration(averageDuration);

        //calculates average distance of bike rides
        Double averageDistance = this.dataList
                .stream()
                .collect(Collectors.averagingDouble(r -> {

                    double distance = 0;

                    if(r.getTripCategory().equals("Round Trip")) {

                        int time = r.getDuration();
                        double duration = (double) time / 60.0; //in minutes
                        double rate = speed / 60.0; //in minutes
                        distance = rate * duration;
                    }

                    else
                        distance = calculateDistance(r.getStartingLatitude(), r.getStartingLongitude(), r.getEndingLatitude(), r.getEndingLongitude());

                    return distance;
                }));

        stat.setAverageDistance(averageDistance);

        //determines most popular starting station
        Map<Integer, Long> startingStationCounting = this.dataList.stream().collect(
                Collectors.groupingBy(BikeShareData::getStartingStationId, Collectors.counting()));


        Integer mostPopularStartingStationId = startingStationCounting.entrySet().stream()
                .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
                .limit(1).findFirst().get().getKey();

        //stat.setMostPopularStartingStation(mostPopularStartingStationId);

        StationData popularStart = this.stationList.get(mostPopularStartingStationId);
        stat.setMostPopularStartingStation(popularStart.getStationName());

        //determines most popular ending station

        // assume trips started between 7-9 and 17-19 are commute trips
        stat.setCommuteTrips(new Long(this.dataList.stream().filter(r -> (r.getStartHour() > 7 && r.getStartHour() < 9 ||r.getStartHour() > 17 && r.getStartHour() < 19 )).count()).intValue());

        return stat;
    }

    //calculates haversine distance between two points
    private double calculateDistance(double lat1, double long1, double lat2, double long2) {

        double latDiff = Math.toRadians(lat2 - lat1);
        double longDiff = Math.toRadians(long2 - long1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.pow(Math.sin(latDiff / 2),2) + Math.pow(Math.sin(longDiff / 2),2) * Math.cos(lat1) * Math.cos(lat2);
        double distance = 2 * Rearth * Math.asin(Math.sqrt(a));
        return distance;
    }

    public List<BikeShareData> getDataList() {

        return dataList;
    }

    public void setDataList(List<BikeShareData> dataList) {

        this.dataList = dataList;
    }
}
