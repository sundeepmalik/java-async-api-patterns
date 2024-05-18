package com.example.apicalls.service;

import com.example.apicalls.model.SampleInput;
import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SyncAPICalls {

    private final List<String> endpointUrls;
    private final int numCalls;
    private final Gson gson = new Gson();

    public SyncAPICalls(List<String> endpointUrls, int numCalls) {
        this.endpointUrls = endpointUrls;
        this.numCalls = numCalls;
    }

    public void callEndpoints() {
        for (int i = 0; i < numCalls; i++) {
            for (String url : endpointUrls) {
                try {
                    String response = sendSyncRequest(url, createSampleInput());
                    logResponse(response);
                } catch (Exception e) {
                    System.err.println("Error calling endpoint " + url + ": " + e.getMessage());
                }
            }
        }
    }

    private String sendSyncRequest(String url, SampleInput sampleInput) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        String jsonBody = gson.toJson(sampleInput);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .headers("Content-Type", "application/json") // Set content type header
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    private void logResponse(String response) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long currentTimeMillis = System.currentTimeMillis();
        String completionTime = dateFormat.format(new Date(currentTimeMillis)) + "." + String.format("%03d", currentTimeMillis % 1000);
        System.out.println("Completed at: " + completionTime + " - Response: " + response); // No thread info needed
    }

    private SampleInput createSampleInput() {
        return new SampleInput("sample_epId", 10, 1234, "sample_model_input");
    }

    public static void main(String[] args) throws Exception {
        List<String> endpoints = List.of(
                "http://localhost:8080/api-category-l1",
                "http://localhost:8080/api-category-l2",
                "http://localhost:8080/api-category-l3"
        );
        new SyncAPICalls(endpoints, 10).callEndpoints();
    }
}
