package com.example.bikesharela.controller;

import com.example.bikesharela.model.ChartData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.example.bikesharela.model.Statistics;
import com.example.bikesharela.service.AnalysisService;

@RestController
@CrossOrigin(origins = "*")
public class Controller {

    @Autowired
    private AnalysisService analysisService;

    @RequestMapping(value="/statistics", method = RequestMethod.GET)
    public Statistics getStat() {
        return this.analysisService.getStatistics();
    }

    @RequestMapping(value="/passholder", method = RequestMethod.GET)
    public ChartData getData() {
        return this.analysisService.getRouteData();
    }

    @RequestMapping(value="/month", method = RequestMethod.GET)
    public ChartData getMonthlyData() {
        return this.analysisService.getMonthlyData();
    }

    @RequestMapping(value="/avgdur", method = RequestMethod.GET)
    public ChartData getAverageDuration() {
        return this.analysisService.getDurationData();
    }

    @RequestMapping(value="/morning", method = RequestMethod.GET)
    public ChartData getMorningRides() {
        return this.analysisService.getMorningRides();
    }

    @RequestMapping(value="/seasonal", method = RequestMethod.GET)
    public ChartData getSeasonRidership() {
        return this.analysisService.getSeasonRidership();
    }

    @RequestMapping(value="/starttime", method = RequestMethod.GET)
    public ChartData getStartTimes() {
        return this.analysisService.getStartTimes();
    }
}
