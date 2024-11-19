package com.example;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        try {
            OpenDataHubClient client = new OpenDataHubClient("https://mobility.api.opendatahub.com/v2");

            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("limit", "200");
            queryParams.put("select", "sorigin,sname,mvalidtime,mperiod,mvalue");
            queryParams.put("where", "sorigin.eq.route220");

            LocalDateTime from = LocalDateTime.of(2024, 11, 15, 0, 0);
            LocalDateTime to = LocalDateTime.of(2024, 11, 16, 0, 0);

            String response = client.getHistoricalData(
                "EChargingPlug", 
                "echarging-plug-status", 
                from, 
                to, 
                queryParams
            );

            System.out.println("API Response:");
            //System.out.println(response);

            StationDataService service = new StationDataService();

            List<StationData> stationDataList = service.parseStationData(response);
    
            System.out.println("\nProcessed stations:");
            stationDataList.forEach(data -> {
                System.out.println("Station: " + data.getSname() + 
                                 ", Value: " + data.getMvalue() + 
                                 ", Time: " + data.getMvalidtime());
            });

        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }


    }
}
