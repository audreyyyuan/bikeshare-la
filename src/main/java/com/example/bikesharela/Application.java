package com.example.bikesharela;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.example.bikesharela.model.BikeShareData;
import com.example.bikesharela.service.ParseBikeShareService;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.bikesharela"})

public class Application implements  CommandLineRunner{

    @Autowired
    private ParseBikeShareService parseBikeShareService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        List<BikeShareData> rows = this.parseBikeShareService.loadData("data.csv");

        Integer total = rows.size();
        System.out.println("Loaded " + total);


        Double averageDuration = rows
                .stream()
                .collect(Collectors.averagingInt(r -> r.getDuration()));

        System.out.println("Average duration=" + averageDuration);


        Map<Integer, Long> startingStationCounting = rows.stream().collect(
                Collectors.groupingBy(BikeShareData::getStartingStationId, Collectors.counting()));


        System.out.println("Most popular starting station: " );
        startingStationCounting.entrySet().stream()
                .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
                .limit(5)
                .forEach(System.out::println); // or any other terminal method

        System.out.println(startingStationCounting);

        // check commute hours

        System.out.println("Commute: " + rows.stream().filter(r -> (r.getStartHour() > 7 && r.getStartHour() < 9 ||r.getStartHour() > 17 && r.getStartHour() < 19 )).count());
    }
}
