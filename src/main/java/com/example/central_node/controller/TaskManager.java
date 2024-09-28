package com.example.central_node.controller;


import com.example.central_node.model.VirtualEdgeNode;
import com.example.central_node.model.PromptRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class TaskManager {

    private final List<VirtualEdgeNode> edgeNodes;
    public TaskManager() {
        this.edgeNodes = new ArrayList<>(); // Simulating an in-memory repository
    }
    public void registerNode(VirtualEdgeNode node) {
        edgeNodes.add(node);
        System.out.println("Node " + node.getId() + " registered with distance: " + node.getDistance());
    }
    private synchronized VirtualEdgeNode findClosestAvailableNode() {
        // Sort nodes by distance and return the closest available one
        Optional<VirtualEdgeNode> closestNode = edgeNodes.stream()
                .filter(VirtualEdgeNode::isAvailable)
                .min(Comparator.comparingDouble(VirtualEdgeNode::getDistance));

        return closestNode.orElse(null); // Return null if no available nodes
    }
    public String assignTaskToClosestNode(PromptRequest request) {
        VirtualEdgeNode closestAvailableNode = findClosestAvailableNode();
        if (closestAvailableNode != null) {

            closestAvailableNode.setAvailable(false);
            System.out.println("Task assigned to node " + closestAvailableNode.getId());

            // Start a new thread to process the request
            new Thread(() -> {
                closestAvailableNode.processPrompt(request);

                // After processing, mark node as available again
                closestAvailableNode.setAvailable(true);
            }).start();
            return "Task is being processed by node " + closestAvailableNode.getId();
        } else {
            System.out.println("No available nodes to handle the request.");
            return "No available nodes to handle the request.";
        }
    }

    public void printNodeStatus(List<String> statuses) {
        edgeNodes.forEach(node -> {
            statuses.add("Node " + node.getId() + " - Available: " + node.isAvailable());
        });
    }
}