package com.example.bikesharela;

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

public class Application {

    @Autowired
    private ParseBikeShareService parseBikeShareService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
