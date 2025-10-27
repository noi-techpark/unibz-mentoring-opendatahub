// SPDX-FileCopyrightText: 2024 NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: AGPL-3.0-or-later

package com.example;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

public class OpenDataHubContentClient {
    private final String baseUrl;
    private final HttpClient client;

    public OpenDataHubContentClient(String baseUrl) {
        this.baseUrl = baseUrl;
        this.client = HttpClient.newHttpClient();
    }

    public String get(String entity, Map<String, String> queryParams) throws Exception {
        return sendRequest(entity, queryParams);
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
