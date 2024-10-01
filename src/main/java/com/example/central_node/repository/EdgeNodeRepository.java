package com.example.central_node.repository;

//import com.example.central_node.model.EdgeNode;
import com.example.central_node.model.VirtualEdgeNode;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Repository
public interface EdgeNodeRepository extends JpaRepository<VirtualEdgeNode, Long> {
    List<VirtualEdgeNode> findByAvailableTrue();
}
