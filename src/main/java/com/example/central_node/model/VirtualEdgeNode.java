package com.example.central_node.model;

import jakarta.persistence.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

@Entity
public class VirtualEdgeNode implements Runnable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double distance; // Represents the distance from the central node
    private Boolean isAvailable; // Changed from AtomicBoolean to Boolean
    private final ReentrantLock lock = new ReentrantLock(); // Added a lock for thread safety

    public VirtualEdgeNode() {
        this.isAvailable = true; // Initially, the node is available
    }

    public VirtualEdgeNode(String name, double distance) {
        this.name = name;
        this.distance = distance;
        this.isAvailable = true; // Initially, the node is available
    }

    public String processPrompt(PromptRequest request) {
        System.out.println("Node " + name + " is processing prompt: " + request.getPrompt());
        try {
            // Simulating processing time (e.g., 2 seconds)
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
            System.err.println("Processing interrupted for node " + name);
        }

        // Simulating a response delay (5 seconds)
        try {
            Thread.sleep(5000); // Wait for 5 seconds before responding
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
            System.err.println("Response delay interrupted for node " + name);
        }

        System.out.println("Node " + name + " finished processing prompt.");
        return "Hello, process complete."; // Returning the response after processing
    }

    public void markUnavailable() {
        lock.lock(); // Acquire the lock
        try {
            isAvailable = false;
            System.out.println("Node " + name + " marked as unavailable.");
        } finally {
            lock.unlock(); // Ensure the lock is released
        }
    }

    public void markAvailable() {
        lock.lock(); // Acquire the lock
        try {
            isAvailable = true;
            System.out.println("Node " + name + " marked as available.");
        } finally {
            lock.unlock(); // Ensure the lock is released
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isAvailable() {
        lock.lock(); // Acquire the lock for reading
        try {
            return isAvailable; // Return the current availability status
        } finally {
            lock.unlock(); // Ensure the lock is released
        }
    }

    public void setAvailable(boolean value) {
        lock.lock(); // Acquire the lock
        try {
            isAvailable = value;
        } finally {
            lock.unlock(); // Ensure the lock is released
        }
    }

    public String getNodeName() {
        return name;
    }

    public void setNodeName(String name) {
        this.name = name;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public void run() {
        // Placeholder for task execution
        // This will run whenever a task is assigned to this node
    }
}