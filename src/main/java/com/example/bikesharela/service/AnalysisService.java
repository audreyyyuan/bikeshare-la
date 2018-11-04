package com.example.bikesharela.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.List;
import java.util.Date;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import com.example.bikesharela.model.StationData;
import javafx.scene.chart.Chart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bikesharela.model.BikeShareData;
import com.example.bikesharela.model.Statistics;
import com.example.bikesharela.model.ChartData;
import com.example.bikesharela.model.ChartData.*;
import com.example.bikesharela.model.ChartData.Row.*;

@Service
public class AnalysisService {

    @Autowired
    private ParseBikeShareService parseBikeShareService;
    @Autowired
    private ParseStationService parseStationService;

    private List<BikeShareData> dataList;
    private Map<Integer, StationData> stationList;

    private static final double Rearth = 3963.191; //miles
    private static final double speed = 7.456; //from https://www.citibikenyc.com/system-data

    @PostConstruct
    public void init() throws FileNotFoundException, IOException {
        this.dataList = this.parseBikeShareService.loadData("data.csv");
        this.stationList = this.parseStationService.loadStations("stations.csv");
    }

    //calculates haversine distance between two points
    private double calculateDistance(double lat1, double long1, double lat2, double long2) {

        if(lat1 == 0 || lat2 == 0 || long1 == 0 || long2 == 0)
            return 0;

        double latDiff = Math.toRadians(lat2 - lat1);
        double longDiff = Math.toRadians(long2 - long1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.pow(Math.sin(latDiff / 2),2) + Math.pow(Math.sin(longDiff / 2),2) * Math.cos(lat1) * Math.cos(lat2);
        double distance = 2 * Rearth * Math.asin(Math.sqrt(a));
        return distance;
    }

    private int getMonth(Date d) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        return cal.get(Calendar.MONTH);
    }

    private Cell dataToCell(Object v) {

        Cell c =  new ChartData().new Row().new Cell();
        c.setV(v);
        return c;
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
                .filter(r -> (r.getStartingLatitude() != 0 && r.getStartingLongitude() != 0) || (r.getEndingLatitude() != 0 && r.getEndingLongitude() != 0))
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

        StationData popularStart = this.stationList.get(mostPopularStartingStationId);
        stat.setMostPopularStartingStation(popularStart.getStationName());

        //determines most popular ending station
        Map<Integer, Long> endingStationCounting = this.dataList.stream().collect(
                Collectors.groupingBy(BikeShareData::getEndingStationId, Collectors.counting()));


        Integer mostPopularEndingStation = endingStationCounting.entrySet().stream()
                .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
                .limit(5).findFirst().get().getKey();

        System.out.println(mostPopularEndingStation);

        StationData popularEnd = this.stationList.get(mostPopularEndingStation);
        System.out.println(popularEnd.getStationName());
        stat.setMostPopularEndingStation(popularEnd.getStationName());

        // assume trips started between 7-9 and 17-19 are commute trips
        stat.setCommuteTrips(new Long(this.dataList.stream()
                .filter(r -> ((!r.getTripCategory().equals("Walk-up")) && ((r.getStartHour() > 7 && r.getStartHour() < 10) || (r.getStartHour() > 16 && r.getStartHour() < 19))))
                .count())
                .intValue());

        return stat;
    }

    //column chart
    //FIX THE HECK OUT OF THIS
    public ChartData getRouteData() {

        Integer[] flex = {0, 0};
        Integer[] monthly = {0, 0};
        Integer[] staff = {0, 0};
        Integer[] walkup = {0, 0};

        this.dataList
                .forEach(r -> {

                    int roundtrip = r.getTripCategory().equals("Round Trip")?1:0;
                    if(r.getPassholderType().equals("Flex Pass")) {

                        int current = flex[roundtrip];
                        flex[roundtrip] = current + 1;
                    }
                    else if(r.getPassholderType().equals("Monthly Pass")) {

                        int current = monthly[roundtrip];
                        monthly[roundtrip] = current + 1;
                    }
                    else if(r.getPassholderType().equals("Staff Annual")) {

                        int current = staff[roundtrip];
                        staff[roundtrip] = current + 1;
                    }
                    else {

                        int current = walkup[roundtrip];
                        walkup[roundtrip] = current + 1;
                    }


                });

        //initialize chart data for google charts
        ChartData data = new ChartData();

        //create columns ["Pass Type", "One Way", "Round Trip"]
        List<Column> columns = new ArrayList<>();
        Column c = data.new Column();
        c.setType("string");
        c.setLabel("Passholder Type");
        columns.add(c);
        c = data.new Column();
        c.setType("number");
        c.setLabel("One Way");
        columns.add(c);
        c = data.new Column();
        c.setType("number");
        c.setLabel("Round Trip");
        columns.add(c);

        //create rows
        List<Row> rows = new ArrayList<>();

        //initialize each row
        Row r = data.new Row();
        List<Cell> rd = new ArrayList<>();
        rd.add(dataToCell("Flex Pass"));
        rd.add(dataToCell(flex[0]));
        rd.add(dataToCell(flex[1]));
        r.setC(rd);
        rows.add(r);

        r = data.new Row();
        rd = new ArrayList<>();
        rd.add(dataToCell("Monthly Pass"));
        rd.add(dataToCell(monthly[0]));
        rd.add(dataToCell(monthly[1]));
        r.setC(rd);
        rows.add(r);

        r = data.new Row();
        rd = new ArrayList<>();
        rd.add(dataToCell("Staff Annual"));
        rd.add(dataToCell(staff[0]));
        rd.add(dataToCell(staff[1]));
        r.setC(rd);
        rows.add(r);

        r = data.new Row();
        rd = new ArrayList<>();
        rd.add(dataToCell("Walk-Up"));
        rd.add(dataToCell(walkup[0]));
        rd.add(dataToCell(walkup[1]));
        r.setC(rd);
        rows.add(r);

        data.setCols(columns);
        data.setRows(rows);

        return data;
    }

    //bar chart
    public ChartData getMonthlyData() {

        int[] month_ow = new int[12];
        int[] month_rt = new int[12];

        this.dataList.stream()
                .forEach(r -> {

                    int month = getMonth(r.getStartTime());
                    if("One Way".equals(r.getTripCategory()))
                        month_ow[month] = month_ow[month] + 1;
                    else
                        month_rt[month] = month_rt[month] + 1;
                });

        ChartData data = new ChartData();

        List<Column> columns = new ArrayList<>();
        Column c = data.new Column();
        c.setType("string");
        c.setLabel("Month");
        columns.add(c);
        c = data.new Column();
        c.setType("number");
        c.setLabel("One Way");
        columns.add(c);
        c = data.new Column();
        c.setType("number");
        c.setLabel("Round Trip");
        columns.add(c);

        for(int i = 0; i < 12; i++) {


        }

        return data;
    }

    //line chart
    public void getDurationData() {

        int[] sum_duration = new int[12];
        int[] num_rides = new int[12];
        double[] avg_duration = new double[12];

        this.dataList.stream()
                .forEach(r -> {
                    int month = getMonth(r.getStartTime());
                    int duration = r.getDuration();
                    sum_duration[month] = sum_duration[month] + duration;
                    num_rides[month] = num_rides[month] + 1;
                });

        for(int i = 0; i < 12; i++) {

            avg_duration[i] = ((double) sum_duration[i]) / num_rides[i];
        }
    }


    public List<BikeShareData> getDataList() {

        return dataList;
    }

    public void setDataList(List<BikeShareData> dataList) {

        this.dataList = dataList;
    }

    public static void main(String [] args) {

        AnalysisService as = new AnalysisService();
        System.out.println(as.calculateDistance(34.028511, -118.25667, 34.0342102, -118.25459));
    }
}
