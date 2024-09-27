package com.example.central_node.service;

import com.example.central_node.model.EdgeNode;
import com.example.central_node.repository.EdgeNodeRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class CheckHeartBeat {
    private final EdgeNodeRepository edgeNodeRepository;

    public CheckHeartBeat(EdgeNodeRepository edgeNodeRepository) {
        this.edgeNodeRepository = edgeNodeRepository;
    }
    @Async // This method will run asynchronously
    public void checkNodeHeartbeats() {
        List<EdgeNode> nodes = edgeNodeRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        for (EdgeNode node : nodes) {
            // Check if the node is overdue for a heartbeat
            if (node.getLastHeartbeat() != null && node.getLastHeartbeat().isBefore(now.minusSeconds(10))) {
                // If the node hasn't sent a heartbeat in the last 10 seconds, mark it as unavailable
                node.setAvailable(false);
                edgeNodeRepository.save(node);
                // You can also notify the central coordinator about this node's status
                sendHeartbeatResponseToCoordinator(node, false);
            } else {
                sendHeartbeatResponseToCoordinator(node, true);
            }
        }
    }
    private void sendHeartbeatResponseToCoordinator(EdgeNode node, boolean isAlive) {
        String status = isAlive ? "alive" : "unavailable";
        System.out.println("Heartbeat from node " + node.getName() + ": " + status);
        // Add actual logic to communicate with the central coordinator here if needed.
    }
}
