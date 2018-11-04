package com.example.bikesharela.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

import com.example.bikesharela.model.StationData;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.example.bikesharela.model.BikeShareData;

@Service
public class ParseStationService {

    public Map<Integer, StationData> loadStations(String fname) throws FileNotFoundException, IOException {

        HashMap<Integer, StationData> stations = new HashMap<>();

        File f = new ClassPathResource(fname).getFile();
        CSVParser parser = new CSVParser(new FileReader(f), CSVFormat.DEFAULT.withHeader());

        for (CSVRecord record : parser) {

            Integer stationId = Integer.valueOf(record.get("Station_ID"));

            StationData station = new StationData(
                    stationId,
                    record.get("Station_Name"),
                    record.get("Go_live_date"),
                    record.get("Region"),
                    record.get("Status")
            );

            stations.put(stationId, station);
        }
        parser.close();

        return stations;
    }
}
