package com.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.List;

public class StationDataService {
    private final ObjectMapper mapper;

    public StationDataService() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    public List<StationData> parseStationData(String jsonString) {
        try {
            // Parse the root JSON object
            JsonNode rootNode = mapper.readTree(jsonString);

            // Get the data array from the response
            JsonNode dataNode = rootNode.get("data");

            if (dataNode == null || !dataNode.isArray()) {
                throw new RuntimeException("Invalid response format: 'data' array not found");
            }

            // Convert the data array to List<StationData>
            return mapper.convertValue(dataNode, new TypeReference<List<StationData>>(){});
        } catch (Exception e) {
            System.err.println("JSON parsing error: " + e.getMessage());
            throw new RuntimeException("Error parsing JSON data", e);
        }
    }
}
