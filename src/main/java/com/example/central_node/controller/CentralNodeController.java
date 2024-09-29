package com.example.central_node.controller;


//import com.example.central_node.java.GroqService;
import com.example.central_node.java.GroqService;
import com.example.central_node.java.PromptRequest;
import com.example.central_node.model.VirtualEdgeNode;
import com.example.central_node.repository.EdgeNodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/central-node")

public class CentralNodeController {

    @Autowired
    EdgeNodeRepository edgeNodeRepository;

    @Autowired
    private final TaskManager taskManager;

    @Autowired
    private GroqService groqService;

    // Constructor to initialize TaskManager and GroqService
    @Autowired
    public CentralNodeController(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.groqService = groqService;

        // Registering some virtual edge nodes at startup
        taskManager.registerNode(new VirtualEdgeNode("Node1", 10.0, "groqId1"));
        taskManager.registerNode(new VirtualEdgeNode("Node2", 5.0, "groqId2"));
        taskManager.registerNode(new VirtualEdgeNode("Node3", 20.0, "groqId3"));
    }

    @PostMapping("/process-prompt")
    public CompletableFuture<ResponseEntity<String>> processPromptAsync(@RequestBody String request) {
        return taskManager.assignTaskToClosestNode(request)
                .thenApply(result -> ResponseEntity.ok(result)); // Transform the result into a ResponseEntity
    }

    @GetMapping("/nodes")
    public List<VirtualEdgeNode> getAllNodes() {
        return edgeNodeRepository.findByAvailableTrue();
    }

}