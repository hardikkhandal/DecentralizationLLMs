package com.example.central_node.controller;

import com.example.central_node.java.GroqService;
import com.example.central_node.java.PromptRequest;
import com.example.central_node.model.VirtualEdgeNode;
import com.example.central_node.repository.EdgeNodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;


@Service
public class TaskManager {
    private final EdgeNodeRepository edgeNodeRepository;
    private final GroqService groqService;
    @Autowired
    public TaskManager(EdgeNodeRepository edgeNodeRepository, GroqService groqService) {
        this.groqService = groqService;
        this.edgeNodeRepository = edgeNodeRepository;
    }

    // Method to register a new virtual edge node
    public void registerNode(VirtualEdgeNode node) {

            edgeNodeRepository.save(node); // Save the node to the database
    }

    public CompletableFuture<String> assignTaskToClosestNode(String request) {
        List<VirtualEdgeNode> availableNodes = edgeNodeRepository.findByAvailableTrue(); // Fetch available nodes

        if (availableNodes.isEmpty()) {
            return CompletableFuture.completedFuture("No available nodes to process the request.");
        }

        // Find the closest node based on distance
        VirtualEdgeNode closestNode = availableNodes.stream()
                .min((node1, node2) -> Double.compare(node1.getDistance(), node2.getDistance()))
                .orElse(null);

        if (closestNode != null) {
            // Start processing the task and handle the future response
            closestNode.setAvailable(false);
            edgeNodeRepository.save(closestNode);
            return closestNode.startProcessingTask(request, groqService).thenApply(result -> {
                closestNode.setAvailable(true); // Mark node as available again
                edgeNodeRepository.save(closestNode); // Update node status in DB
                return result; // Return the processed result
            }).exceptionally(ex -> {
                closestNode.setAvailable(true); // Ensure the node is marked available on error
                edgeNodeRepository.save(closestNode); // Update node status in DB
                return "Error processing request: " + ex.getMessage();
            });
        } else {
            CompletableFuture<String> future = new CompletableFuture<>();
            future.complete("No available nodes to process the request.");
            return future; // Return a completed future with a message
        }


    }

}