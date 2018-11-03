package com.example.bikesharela.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.example.bikesharela.model.BikeShareData;

@Service
public class ParseBikeShareData {

    public List<BikeShareData> loadDate(String fileName) throws FileNotFoundException, IOException{
        List<BikeShareData> rows = new ArrayList<BikeShareData>();

        File file = new ClassPathResource(fileName).getFile();
        CSVParser parser = new CSVParser(new FileReader(file), CSVFormat.DEFAULT.withHeader());

        for (CSVRecord record : parser) {
            BikeShareData row = new BikeShareData(
                    Integer.valueOf(record.get("Duration")),
                    record.get("Start Time"),
                    record.get("End Time"),
                    "".equals(record.get("Starting Station ID"))?0:Integer.valueOf(record.get("Starting Station ID")),
                    "".equals(record.get("Ending Station ID"))?0:Integer.valueOf(record.get("Ending Station ID")),
                    "".equals(record.get("Bike ID"))?0:Integer.valueOf(record.get("Bike ID")),
                    "".equals(record.get("Plan Duration"))?0:Integer.valueOf(record.get("Plan Duration")),
                    record.get("Trip Route Category"),
                    record.get("Passholder Type")
            );
            rows.add(row);

        }
        parser.close();

        return rows;
    }
}
