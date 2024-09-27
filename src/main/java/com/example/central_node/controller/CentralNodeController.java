package com.example.central_node.controller;


import com.example.central_node.model.EdgeNode;
import com.example.central_node.repository.EdgeNodeRepository;
import com.example.central_node.service.CheckHeartBeat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Random;


@RestController
@RequestMapping("/api/central-node")
public class CentralNodeController {

    @Autowired
    private EdgeNodeRepository edgeNodeRepository;

    @Autowired
    private CheckHeartBeat heartbeatService;


    @PostMapping("/register")
    public ResponseEntity<String> registerNode(@RequestBody EdgeNode node) {
        edgeNodeRepository.save(node);
        return ResponseEntity.ok("Node registered successfully");
    }

    @PostMapping("/heartbeat")
    public ResponseEntity<String> heartbeat(@RequestParam String nodeName) {
        EdgeNode node = edgeNodeRepository.findByName(nodeName);
        if (node != null) {
            node.setAvailable(true);
            node.setLastHeartbeat(LocalDateTime.now());
            edgeNodeRepository.save(node);
            return ResponseEntity.ok("Heartbeat received");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Node not found");
    }

    @Scheduled(fixedRate = 5000)
    public void checkNodeAvailability() {
        heartbeatService.checkNodeHeartbeats();
    }
    public void changeNodeDistances() {
        List<EdgeNode> nodes = edgeNodeRepository.findAll();
        Random random = new Random();

        for (EdgeNode node : nodes) {
            double newDistance = random.nextDouble() * 100; // Random distance between 0 and 100
            node.setDistance((int)newDistance);
            edgeNodeRepository.save(node);
        }
    }
    @PostMapping("/process-prompt")
    public ResponseEntity<String> processPrompt(@RequestBody String prompt) {
        // Step 5.2: Logic to select the best edge node and forward the prompt
        List<EdgeNode> availableNodes = edgeNodeRepository.findByAvailableTrue();
        if(availableNodes.isEmpty()){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("No available nodes");
        }
        EdgeNode closest = null;
        int min = Integer.MAX_VALUE;
        for(EdgeNode node : availableNodes){
            if(node.getDistance() < min){
                closest = node;
                min = node.getDistance();
            }
        }
        if (closest != null) {
            // Here you would typically send the prompt to the closest node via its IP address or an API call
            return ResponseEntity.ok("Prompt sent to the closest node: " + closest.getName());
        }

        // If no closest node found, return an error (unlikely)
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("No suitable edge node found to process the prompt.");
    }

    @GetMapping("/available-nodes")
    public ResponseEntity<List<EdgeNode>> getAvailableNodes() {
        List<EdgeNode> availableNodes = edgeNodeRepository.findByAvailableTrue();
        return ResponseEntity.ok(availableNodes);
    }
}
