package com.example.central_node.repository;

import com.example.central_node.model.EdgeNode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EdgeNodeRepository extends JpaRepository<EdgeNode, Long> {
    List<EdgeNode> findByAvailableTrue(); // Fetch only available nodes
    EdgeNode findByName(String name); // Fetch a node by its name
}
