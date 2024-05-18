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
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class AsyncBlockingAPICalls {

    private final List<String> endpointUrls;
    private final int numCalls;
    private final ExecutorService executorService;
    private final Gson gson = new Gson();

    public AsyncBlockingAPICalls(List<String> endpointUrls, int numCalls) {
        this.endpointUrls = endpointUrls;
        this.numCalls = numCalls;
        this.executorService = Executors.newFixedThreadPool(Math.min(endpointUrls.size(), 10)); // Adjust pool size as needed
    }

    public void callEndpointsAsyncBlocking() {
        for (int i = 0; i < numCalls; i++) {
            List<CompletableFuture<String>> futures = endpointUrls.stream()
                    .map(url -> sendAsyncRequest(url, createSampleInput()))
                    .collect(Collectors.toList());

            // Block and wait for all CompletableFutures to complete (with timeout)
            futures.forEach(future -> {
                try {
                    String response = future.get(30, TimeUnit.SECONDS); // Timeout after 30 seconds
                    if (response != null) {
                        logResponse(response);
                    }
                } catch (InterruptedException | ExecutionException | TimeoutException e) {
                    System.err.println("Error getting response: " + e.getMessage());
                }
            });
        }

        executorService.shutdown();
    }

    private CompletableFuture<String> sendAsyncRequest(String url, SampleInput sampleInput) {
        HttpClient client = HttpClient.newHttpClient();
        String jsonBody = gson.toJson(sampleInput);

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
        }, executorService);
    }

    private void logResponse(String response) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long currentTimeMillis = System.currentTimeMillis();
        String completionTime = dateFormat.format(new Date(currentTimeMillis)) + "." + String.format("%03d", currentTimeMillis % 1000);
        System.out.println("Thread: " + Thread.currentThread().getName() + ", Completed at: " + completionTime + " - Response: " + response);
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
        new AsyncBlockingAPICalls(endpoints, 10).callEndpointsAsyncBlocking();
    }
}