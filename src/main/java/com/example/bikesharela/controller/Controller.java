package com.example.bikesharela.controller;

import com.example.bikesharela.model.ChartData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.bikesharela.model.Statistics;
import com.example.bikesharela.service.AnalysisService;

@RestController
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
}
