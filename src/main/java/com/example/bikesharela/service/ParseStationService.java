package com.example.bikesharela.service;

import com.example.bikesharela.model.StationData;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Service
public class ParseStationService {

    /**
     * A service that parses and obtains information about each bike share station in LA
     * @param fname filename consisting of the LA station info
     * @return a mapping of all the station IDs to the information about that station
     * @throws IOException if the file is unable to be found
     */
    public Map<Integer, StationData> loadStations(String fname) throws IOException {

        HashMap<Integer, StationData> stations = new HashMap<>();

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
