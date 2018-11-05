package com.example.bikesharela.service;

import com.example.bikesharela.model.BikeShareData;
import com.example.bikesharela.model.ChartData;
import com.example.bikesharela.model.ChartData.Column;
import com.example.bikesharela.model.ChartData.Row;
import com.example.bikesharela.model.ChartData.Row.Cell;
import com.example.bikesharela.model.StationData;
import com.example.bikesharela.model.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private double haversineDistance(double lat1, double long1, double lat2, double long2) {

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

    private Cell dataToCell(Object v) {

        Cell c =  new ChartData().new Row().new Cell();
        c.setV(v);
        return c;
    }

    private double round(double value, int places) {

        int scale = (int) Math.pow(10, places);
        return (double) Math.round(value * scale) / scale;
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
                .collect(Collectors.averagingDouble(r -> "Round Trip".equals(r.getTripCategory())
                        ?(((double) r.getDuration() / 60.0) * (speed / 60.0))
                        :(haversineDistance(r.getStartingLatitude(), r.getStartingLongitude(), r.getEndingLatitude(), r.getEndingLongitude()))));

        stat.setAverageDistance(round(averageDistance, 2));

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

        StationData popularEnd = this.stationList.get(mostPopularEndingStation);
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

        Double[] flex = {0.0, 0.0};
        Double[] monthly = {0.0, 0.0};
        Double[] staff = {0.0, 0.0};
        Double[] walkup = {0.0, 0.0};

        int[] flexCount = {0, 0};
        int[] monthlyCount = {0, 0};
        int[] staffCount = {0, 0};
        int[] walkupCount = {0, 0};

        this.dataList
                .forEach(r -> {

                    int roundtrip = r.getTripCategory().equals("Round Trip")?1:0;
                    if(r.getPassholderType().equals("Flex Pass")) {

                        int count = flexCount[roundtrip];
                        double sum = flex[roundtrip] * count;
                        count += 1;
                        sum += r.getDuration();
                        flex[roundtrip] = sum / count;
                        flexCount[roundtrip] = count;
                    }
                    else if(r.getPassholderType().equals("Monthly Pass")) {

                        int count = monthlyCount[roundtrip];
                        double sum = monthly[roundtrip] * count;
                        count += 1;
                        sum += r.getDuration();
                        monthly[roundtrip] = sum / count;
                        monthlyCount[roundtrip] = count;
                    }
                    else if(r.getPassholderType().equals("Staff Annual")) {

                        int count = staffCount[roundtrip];
                        double sum = staff[roundtrip] * count;
                        count += 1;
                        sum += r.getDuration();
                        staff[roundtrip] = sum / count;
                        staffCount[roundtrip] = count;
                    }
                    else {

                        int count = walkupCount[roundtrip];
                        double sum = walkup[roundtrip] * count;
                        count += 1;
                        sum += r.getDuration();
                        walkup[roundtrip] = sum / count;
                        walkupCount[roundtrip] = count;
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
        rd.add(dataToCell(flex[0]/60));
        rd.add(dataToCell(flex[1]/60));
        r.setC(rd);
        rows.add(r);

        r = data.new Row();
        rd = new ArrayList<>();
        rd.add(dataToCell("Monthly Pass"));
        rd.add(dataToCell(monthly[0]/60));
        rd.add(dataToCell(monthly[1]/60));
        r.setC(rd);
        rows.add(r);

        r = data.new Row();
        rd = new ArrayList<>();
        rd.add(dataToCell("Staff Annual"));
        rd.add(dataToCell(staff[0]/60));
        rd.add(dataToCell(staff[1]/60));
        r.setC(rd);
        rows.add(r);

        r = data.new Row();
        rd = new ArrayList<>();
        rd.add(dataToCell("Walk-Up"));
        rd.add(dataToCell(walkup[0]/60));
        rd.add(dataToCell(walkup[1]/60));
        r.setC(rd);
        rows.add(r);

        data.setCols(columns);
        data.setRows(rows);

        return data;
    }

    //bar chart
    public ChartData getMonthlyData() {

        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August",
                "September", "October", "November", "December"};
        int[] month_ow = new int[12];
        int[] month_rt = new int[12];

        this.dataList.stream()
                .forEach(r -> {

                    int month = r.getMonth();
                    if("One Way".equals(r.getTripCategory()))
                        month_ow[month] = month_ow[month] + 1;
                    else
                        month_rt[month] = month_rt[month] + 1;
                });

        ChartData data = new ChartData();

        List<Column> columns = new ArrayList<>();
        Column col = data.new Column();
        col.setType("string");
        col.setLabel("Month");
        columns.add(col);
        col = data.new Column();
        col.setType("number");
        col.setLabel("One Way");
        columns.add(col);
        col = data.new Column();
        col.setType("number");
        col.setLabel("Round Trip");
        columns.add(col);

        List<Row> rows = new ArrayList<>();
        for(int i = 0; i < 12; i++) {

            List<Cell> cells = new ArrayList<>();
            cells.add(dataToCell(months[i]));
            if(month_ow[i] == 0) {
                cells.add(null);
                cells.add(null);
            }
            else {
                cells.add(dataToCell(month_ow[i]));
                cells.add(dataToCell(month_rt[i]));
            }

            Row r = data.new Row();
            r.setC(cells);
            rows.add(r);
        }

        data.setCols(columns);
        data.setRows(rows);

        return data;
    }

    //line chart
    public ChartData getDurationData() {

        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September",
                "October", "November", "December"};
        double[] avg_duration = new double[12];
        int[] count = new int[12];

        this.dataList.stream()
                .forEach(r -> {
                    int month = r.getMonth();
                    double sum = avg_duration[month] * count[month];
                    sum += r.getDuration();
                    count[month] = count[month] + 1;

                    double updatedAvg = sum / (count[month]);
                    avg_duration[month] = updatedAvg;
                });

        ChartData data = new ChartData();

        List<Column> columns = new ArrayList<>();
        Column col = data.new Column();
        col.setType("string");
        col.setLabel("Month");
        columns.add(col);
        col = data.new Column();
        col.setType("number");
        col.setLabel("Average Duration");
        columns.add(col);

        List<Row> rows = new ArrayList<>();
        for(int i = 0; i < 12; i++) {

            List<Cell> cells = new ArrayList<>();
            cells.add(dataToCell(months[i]));
            cells.add(dataToCell((double) (avg_duration[i]/60)));

            Row r = data.new Row();
            r.setC(cells);
            rows.add(r);
        }

        data.setCols(columns);
        data.setRows(rows);
        return data;
    }

    //gets station infromation throughout the day.
    public void getStationData() {

        //calculates most popular starting stations at the beginning of the day
        List<StationData> popularStartOfDay = new ArrayList<>();
        Map<Integer, Long> startingStationCounting = this.dataList.stream().collect(
                Collectors.groupingBy(BikeShareData::getStartingStationId, Collectors.counting()));

        startingStationCounting.entrySet().stream()
                .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
                .limit(3)
                .forEach(entry -> popularStartOfDay.add(this.stationList.get(entry)));

        //calculates most popular ending stations at the end of the day
        List<StationData> popularEndOfDay = new ArrayList<>();
        Map<Integer, Long> endingStationCounting = this.dataList.stream().collect(
                Collectors.groupingBy(BikeShareData::getEndingStationId, Collectors.counting()));

        endingStationCounting.entrySet().stream()
                .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
                .limit(3)
                .forEach(entry -> popularEndOfDay.add(this.stationList.get(entry)));
    }

    public ChartData getMorningRides() {

        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September",
                "October", "November", "December"};
        int[] owCount = new int[12];
        int[] rtCount = new int[12];

        this.dataList
                .stream()
                .filter(r -> r.getStartHour() >= 5 && r.getStartHour() <= 8)
                .filter(r -> r.getEndHour() >= r.getStartHour() && r.getEndHour() <= 8)
                .forEach(r -> {
                    boolean roundTrip = "Round Trip".equals(r.getTripCategory())?true:false;
                    int month = r.getMonth();

                    if(roundTrip)
                        rtCount[month] += 1;
                    else
                        owCount[month] += 1;
                });

        ChartData data = new ChartData();

        List<Column> columns = new ArrayList<>();
        Column col = data.new Column();
        col.setType("string");
        col.setLabel("Month");
        columns.add(col);
        col = data.new Column();
        col.setType("number");
        col.setLabel("One Way");
        columns.add(col);
        col = data.new Column();
        col.setType("number");
        col.setLabel("Round Trip");
        columns.add(col);

        List<Row> rows = new ArrayList<>();
        for(int i = 0; i < 12; i++) {

            List<Cell> cells = new ArrayList<>();
            cells.add(dataToCell(months[i]));
            cells.add(dataToCell(owCount[i]));
            cells.add(dataToCell(rtCount[i]));

            Row r = data.new Row();
            r.setC(cells);
            rows.add(r);
        }

        data.setCols(columns);
        data.setRows(rows);

        return data;
    }

    public ChartData getSeasonRidership() {

        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September",
                "October", "November", "December"};
        double[] flex = new double[12];
        double[] monthly = new double[12];
        double[] staff = new double[12];
        double[] walkup = new double[12];
        int[] flexCount = new int[12];
        int[] monthlyCount = new int[12];
        int[] staffCount = new int[12];
        int[] walkupCount = new int[12];

        this.dataList.stream()
                .forEach(r -> {
                    int month = r.getMonth();
                    String pass = r.getPassholderType();
                    int duration = r.getDuration();

                    if(pass.equals("Flex Pass")) {

                        int count = flexCount[month];
                        double sum = flex[month] * count;
                        sum += duration;
                        count += 1;
                        flex[month] = sum / count;
                        flexCount[month] = count;
                    }
                    else if(pass.equals("Monthly Pass")) {

                        int count = monthlyCount[month];
                        double sum = monthly[month] * count;
                        sum += duration;
                        count += 1;
                        monthly[month] = sum / count;
                        monthlyCount[month] = count;
                    }
                    else if(pass.equals("Staff Annual")) {

                        int count = staffCount[month];
                        double sum = staff[month] * count;
                        sum += duration;
                        count += 1;
                        staff[month] = sum / count;
                        staffCount[month] = count;
                    }
                    else {

                        int count = walkupCount[month];
                        double sum = walkup[month] * count;
                        sum += duration;
                        count += 1;
                        walkup[month] = sum / count;
                        walkupCount[month] = count;
                    }
                });

        ChartData data = new ChartData();

        List<Column> columns = new ArrayList<>();
        Column col = data.new Column();
        col.setType("string");
        col.setLabel("Month");
        columns.add(col);
        col = data.new Column();
        col.setType("number");
        col.setLabel("Flex Pass");
        columns.add(col);
        col = data.new Column();
        col.setType("number");
        col.setLabel("Monthly Pass");
        columns.add(col);
        col = data.new Column();
        col.setType("number");
        col.setLabel("Staff Annual");
        columns.add(col);
        col = data.new Column();
        col.setType("number");
        col.setLabel("Walk-up");
        columns.add(col);

        List<Row> rows = new ArrayList<>();
        for(int i = 0; i < 12; i++) {

            List<Cell> cells = new ArrayList<>();
            cells.add(dataToCell(months[i]));
            if(flex[i] == 0)
                cells.add(null);
            else
                cells.add(dataToCell(flex[i]));

            if(monthly[i] == 0)
                cells.add(null);
            else
                cells.add(dataToCell(monthly[i]));

            if(staff[i] == 0)
                cells.add(null);
            else
                cells.add(dataToCell(staff[i]));

            if(walkup[i] == 0)
                cells.add(null);
            else
                cells.add(dataToCell(walkup[i]));

            Row r = data.new Row();
            r.setC(cells);
            rows.add(r);
        }

        data.setCols(columns);
        data.setRows(rows);

        return data;
    }

    public ChartData getStartTimes() {

        List<Integer> startTimes = new ArrayList<>();
        this.dataList.stream()
                .forEach(r -> {
                    startTimes.add(r.getStartHour());
                });

        ChartData data = new ChartData();

        List<Column> columns = new ArrayList<>();
        Column col = data.new Column();
        col.setType("string");
        col.setLabel("Bike");
        columns.add(col);
        col = data.new Column();
        col.setType("number");
        col.setLabel("Starting Hour");
        columns.add(col);

        List<Row> rows = new ArrayList<>();
        for(int i = 0; i < startTimes.size(); i++) {

            List<Cell> cells = new ArrayList<>();
            cells.add(dataToCell("Bike"));
            cells.add(dataToCell(startTimes.get(i)));

            Row r = data.new Row();
            r.setC(cells);
            rows.add(r);
        }

        data.setCols(columns);
        data.setRows(rows);

        return data;
    }

    public List<BikeShareData> getDataList() {

        return dataList;
    }

    public void setDataList(List<BikeShareData> dataList) {

        this.dataList = dataList;
    }
}
