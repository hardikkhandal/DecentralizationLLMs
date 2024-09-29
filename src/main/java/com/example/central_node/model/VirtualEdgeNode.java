package com.example.central_node.model;

//import com.example.central_node.controller.GroqService;
//import com.example.central_node.java.GroqService;
import com.example.central_node.java.GroqService;
import com.example.central_node.java.PromptRequest;
import jakarta.persistence.*;


import java.util.concurrent.CompletableFuture;

@Entity
public class VirtualEdgeNode{

    @Id  // Adding @Id annotation to make this field the primary key
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;         // Unique ID for the node
    private String name;       // Name of the node
    private String groqId;     // Groq API key or ID
    private boolean available; // Availability status
    private double distance;


    public VirtualEdgeNode() {
        // Initialize fields if necessary
    }

    // Constructor
    public VirtualEdgeNode(String name, double distance, String groqId) {
         // Generate a unique ID
        this.name = name;
        this.groqId = groqId;
        this.available = true;  // Nodes are available by default
        this.distance = distance;

    }
    // Getter and Setter methods
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGroqId() {
        return groqId;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public double getDistance() {
        return distance;
    }


    // Start node task in a new thread
    public CompletableFuture<String> startProcessingTask(String request, GroqService groqService) {
        try {
            // Delay for 5 seconds
            Thread.sleep(15000); // 5000 milliseconds = 5 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
            // Handle the exception if needed
        }

        CompletableFuture<String> responseFuture = new CompletableFuture<>();
        // Create a new thread for processing the task
        Thread nodeThread = new Thread(() -> {
            if (this.isAvailable()) {
                try {
                    System.out.println(name + " is processing a task...");
                    this.available = false; // Mark as unavailable during processing

                    String prompt = request;
                    CompletableFuture<String> groqResponseFuture = groqService.processPromptAsync(prompt, this.groqId);

                    // Wait for the async response from Groq service
                    groqResponseFuture.thenAccept(response -> {
                        System.out.println(name + " received response: " + response);
                        this.available = true; // Mark as available again after processing
                        responseFuture.complete(response); // Complete the future with the response
                    }).exceptionally(ex -> {
                        System.out.println(name + " encountered an error: " + ex.getMessage());
                        this.available = true;
                        responseFuture.completeExceptionally(ex); // Make node available again after error
                        return null;
                    });

                } catch (Exception e) {
                    System.out.println(name + " failed to process due to an error: " + e.getMessage());
                    this.available = true; // Ensure node becomes available even after an error
                }
            }
        });

        nodeThread.start(); // Start the thread

        return responseFuture;
    }
}
