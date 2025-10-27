// SPDX-FileCopyrightText: 2024 NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: AGPL-3.0-or-later

package com.example;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("RETRIEVING CONTENT ---------------------------------");

            OpenDataHubContentClient client = new OpenDataHubContentClient("https://tourism.opendatahub.com/v1/");

            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("removenullvalues", "true");
            queryParams.put("pagesize", "10");
            queryParams.put("typefilter", "2"); // only B&B

            String response = client.get("Accommodation", queryParams);

            System.out.println("API Response:");

    
            System.out.println("API Response received. Now parsing...");

            // 1. Initialize the ObjectMapper from Jackson
            ObjectMapper mapper = new ObjectMapper();
            
            // 2. Parse the JSON string into a general Map (Map<String, Object>)
            // The response is likely a large object, and this will be the root map.
            Map<String, Object> apiResponseMap = mapper.readValue(response, Map.class);

            // 3. Access and Print Properties from the Map
            
            // The OpenDataHub API typically returns a structure with "TotalResults" and the actual "Items" list.
            
            // a. Print Total Results (Example)
            if (apiResponseMap.containsKey("TotalResults")) {
                System.out.println("Total Accommodations Found: " + apiResponseMap.get("TotalResults"));
            }

            // b. Access the list of items (Accommodations)
            if (apiResponseMap.containsKey("Items") && apiResponseMap.get("Items") instanceof java.util.List) {
                
                java.util.List<Map<String, Object>> items = (java.util.List<Map<String, Object>>) apiResponseMap.get("Items");
                System.out.println("Found " + items.size() + " Items in the current page.");
                
                System.out.println("\n--- Processed Accommodations (B&B) ---");

                // Loop through the list of accommodations
                for (int i = 0; i < items.size(); i++) {
                    Map<String, Object> accommodation = items.get(i);
                    
                    // Safely access properties using Map keys
                    String id = (String) accommodation.get("Id");
                    String name = "";
                    // 2. We want the first item in the list (index 0).
                    if (!items.isEmpty()) {
                        Map<String, Object> firstItem = items.get(0);

                        // 3. Get the "AccoDetail" object from the first item.
                        Object accoDetailObject = firstItem.get("AccoDetail");
                        if (accoDetailObject instanceof Map) {
                            Map<String, Object> accoDetailMap = (Map<String, Object>) accoDetailObject;

                            // 4. Get the language-specific map ("it" for Italian).
                            Object itObject = accoDetailMap.get("en");
                            if (itObject instanceof Map) {
                                Map<String, Object> itMap = (Map<String, Object>) itObject;

                                // 5. Get the "Name" property (which is a String)
                                Object nameObject = itMap.get("Name");
                                if (nameObject instanceof String) {
                                    name = (String) nameObject;
                                }
                            }
                        }
                    }

                    // Print the details
                    System.out.println((i + 1) + ". " + name + " (ID: " + id + ")");
                }
            } else {
                System.out.println("API response does not contain an 'Items' list or it's malformed.");
                System.out.println("Full API Response (first 500 chars): " + response.substring(0, Math.min(response.length(), 500)));
            }

            System.out.println("\n");

        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }

        try {
            System.out.println("RETRIEVING TIMESERIES ---------------------------------");

            OpenDataHubTimeseriesClient client = new OpenDataHubTimeseriesClient("https://mobility.api.opendatahub.com/v2");

            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("limit", "200");
            queryParams.put("select", "sorigin,sname,mvalidtime,mperiod,mvalue");
            queryParams.put("where", "sorigin.eq.route220");

            LocalDateTime from = LocalDateTime.of(2025, 10, 23, 0, 0);
            LocalDateTime to = LocalDateTime.of(2025, 10, 28, 0, 0);

            String response = client.getHistoricalData(
                "EChargingStation", 
                "number-available", 
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
