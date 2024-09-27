package com.example.central_node.task;

import com.example.central_node.model.EdgeNode;
import com.example.central_node.repository.EdgeNodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class NodeAvailablityChecker {
    @Autowired
    private EdgeNodeRepository edgeNodeRepository;

    // Scheduled task to check node availability
    @Scheduled(fixedRate = 5000) // Check every 5 seconds
    public void checkNodeAvailability() {
        List<EdgeNode> nodes = edgeNodeRepository.findAll();
        LocalDateTime now = LocalDateTime.now();
        for (EdgeNode node : nodes) {
            long timeoutThreshold = 15; // seconds

            if (node.getLastHeartbeat() != null) {
                long secondsSinceLastHeartbeat = ChronoUnit.SECONDS.between(node.getLastHeartbeat(), now);
                if (secondsSinceLastHeartbeat > timeoutThreshold) {
                    node.setAvailable(false); // Mark the node as unavailable
                } else {
                    node.setAvailable(true); // Node is still available
                }
            }
            // Save the updated node availability status back to the database
            edgeNodeRepository.save(node);
        }
    }
}
