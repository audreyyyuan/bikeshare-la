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

    @PostConstruct
    public void init() throws FileNotFoundException, IOException {
        this.dataList = this.parseBikeShareService.loadData("data.csv");
    }

    public Statistics getStatistics() {

        Statistics stat = new Statistics();

        stat.setTotalTrips(this.dataList.size());

        //calculates average duration of bike rdies
        Double averageDuration = this.dataList
                .stream()
                .collect(Collectors.averagingInt(r -> r.getDuration()));

        stat.setAverageDuration(averageDuration);
        stat.setAverageDuration(averageDuration);

        // assume trips started between 7-9 and 17-19 are commute trips
        stat.setCommuteTrips(new Long(this.dataList.stream().filter(r -> (r.getStartHour() > 7 && r.getStartHour() < 9 ||r.getStartHour() > 17 && r.getStartHour() < 19 )).count()).intValue());

        return stat;
    }
    public List<BikeShareData> getDataList() {
        return dataList;
    }

    public void setDataList(List<BikeShareData> dataList) {
        this.dataList = dataList;
    }
}
