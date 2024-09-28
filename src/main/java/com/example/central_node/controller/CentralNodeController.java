package com.example.central_node.controller;


import com.example.central_node.model.PromptRequest;
import com.example.central_node.model.VirtualEdgeNode;
import com.example.central_node.repository.EdgeNodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


import com.example.central_node.model.VirtualEdgeNode;
import com.example.central_node.repository.EdgeNodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.ReentrantLock;

@RestController
@RequestMapping("/api/central-node")
public class CentralNodeController {

    private final TaskManager taskManager;
    public CentralNodeController() {
        this.taskManager = new TaskManager();

        // Registering some virtual edge nodes at startup
        taskManager.registerNode(new VirtualEdgeNode("Node1", 10.0));
        taskManager.registerNode(new VirtualEdgeNode("Node2", 5.0));
        taskManager.registerNode(new VirtualEdgeNode("Node3", 20.0));
    }
    @PostMapping("/process-prompt")
    public CompletableFuture<String> processPrompt(@RequestBody PromptRequest request) {
        // Use CompletableFuture to handle the asynchronous task
        return CompletableFuture.supplyAsync(() -> {
            String response = taskManager.assignTaskToClosestNode(request); // Assuming this method returns a String response
            return response; // Return the response after processing
        }).thenApply(result -> {
            return "Prompt is being processed. Result: " + result;
        });
    }
    @GetMapping("/node-status")
    public List<String> getNodeStatus() {
        List<String> statuses = new ArrayList<>();
        taskManager.printNodeStatus(statuses); // This method could be modified to return statuses as a list
        return statuses;
    }
}