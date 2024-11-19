package com.example;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;

public class OpenDataHubClient {
    private final String baseUrl;
    private final HttpClient client;

    public OpenDataHubClient(String baseUrl) {
        this.baseUrl = baseUrl;
        this.client = HttpClient.newHttpClient();
    }

    public String getStations(String stationType, Map<String, String> queryParams) throws Exception {
        return sendRequest("/flat/" + stationType, queryParams);
    }

    public String getLatestMeasurements(String stationType, String dataType, 
                                      Map<String, String> queryParams) throws Exception {
        return sendRequest(String.format("/flat/%s/%s/latest", stationType, dataType), 
                         queryParams);
    }

    public String getHistoricalData(String stationType, String dataType, 
                                  LocalDateTime from, LocalDateTime to, 
                                  Map<String, String> queryParams) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        String path = String.format("/flat/%s/%s/%s/%s", 
            stationType, dataType, 
            from.format(formatter), 
            to.format(formatter));
        return sendRequest(path, queryParams);
    }

    private String sendRequest(String path, Map<String, String> queryParams) throws Exception {
        String queryString = "";
        if (queryParams != null && !queryParams.isEmpty()) {
            queryString = "?" + queryParams.entrySet().stream()
                .map(entry -> encodeParam(entry.getKey()) + "=" + encodeParam(entry.getValue()))
                .collect(Collectors.joining("&"));
        }

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
            .uri(URI.create(baseUrl + path + queryString))
            .GET();

        HttpResponse<String> response = client.send(
            requestBuilder.build(), 
            HttpResponse.BodyHandlers.ofString()
        );

        if (response.statusCode() != 200) {
            throw new RuntimeException("API request failed with status: " + response.statusCode());
        }

        return response.body();
    }

    private String encodeParam(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
