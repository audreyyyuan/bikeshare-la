package com.example.bikesharela.service;

import java.io.*;
import java.util.Map;
import java.util.HashMap;

import com.example.bikesharela.model.StationData;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.example.bikesharela.model.BikeShareData;

@Service
public class ParseStationService {

    public Map<Integer, StationData> loadStations(String fname) throws FileNotFoundException, IOException {

        HashMap<Integer, StationData> stations = new HashMap<>();

        //File f = new ClassPathResource(fname).getFile();
        //File file = ResourceUtils.getFile("classpath:" + fname);
        InputStream in = getClass().getClassLoader().getResourceAsStream(fname);
        CSVParser parser = new CSVParser(new InputStreamReader(in), CSVFormat.DEFAULT.withHeader());

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
