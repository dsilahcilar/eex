package com.eex.metrics.controller

import com.eex.metrics.model.RelationshipGraph
import com.eex.metrics.service.GraphService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/graph")
class GraphController(private val graphService: GraphService) {
    
    @GetMapping("/relationships")
    fun getRelationshipGraph(): RelationshipGraph {
        return graphService.buildRelationshipGraph()
    }
} 