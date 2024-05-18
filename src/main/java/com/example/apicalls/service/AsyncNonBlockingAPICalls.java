package com.example.apicalls.service;

import com.example.apicalls.model.SampleInput;
import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AsyncNonBlockingAPICalls {

    private final List<String> endpointUrls;
    private final int numCalls;
    private final ExecutorService executorService;
    private final Gson gson = new Gson();

    public AsyncNonBlockingAPICalls(List<String> endpointUrls, int numCalls) {
        this.endpointUrls = endpointUrls;
        this.numCalls = numCalls;
        this.executorService = Executors.newFixedThreadPool(Math.min(endpointUrls.size(), 10)); // Adjust pool size as needed
    }

    public void callEndpointsAsync() throws Exception {
        for (int i = 0; i < numCalls; i++) {
            List<CompletableFuture<Void>> futures = new ArrayList<>();
            for (String url : endpointUrls) {
                futures.add(sendAsyncRequest(url, createSampleInput()));
            }
        }

        // Wait for all tasks to finish before shutting down the executor
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);
    }

    private CompletableFuture<Void> sendAsyncRequest(String url, SampleInput sampleInput) {
        HttpClient client = HttpClient.newHttpClient();
        String jsonBody = gson.toJson(sampleInput); // Convert SampleInput to JSON

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .headers("Content-Type", "application/json") // Set content type header
                .build();

        return CompletableFuture.supplyAsync(() -> {
            try {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                return response.body();
            } catch (Exception e) {
                System.err.println("Error calling endpoint " + url + ": " + e.getMessage());
                return null;
            }
        }, executorService).thenAccept(response -> {
            if (response != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                long currentTimeMillis = System.currentTimeMillis();
                String completionTime = dateFormat.format(new Date(currentTimeMillis)) + "." + String.format("%03d", currentTimeMillis % 1000); // Format milliseconds with 3 digits
                System.out.println("Thread: " + Thread.currentThread().getName() + ", Completed at: " + completionTime + " - Response from " + response.toString() + ": " + response);
            }
        });
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
        new AsyncNonBlockingAPICalls(endpoints, 10).callEndpointsAsync();
    }
}
