package com.eex.metrics.model.graph

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Graph node type")
enum class NodeType {
    METRIC,
    DRIVING_FACTOR,
    REMEDIATION_ACTION
}

@Schema(description = "Graph edge type")
enum class EdgeType {
    LEADS_TO,
    LAGS_BEHIND,
    IMPACTS,
    REMEDIATED_BY
}

@Schema(description = "Graph node information")
data class GraphNode(
    @Schema(description = "Unique identifier of the node")
    val id: String,
    
    @Schema(description = "Display label for the node")
    val label: String,
    
    @Schema(description = "Type of the node")
    val type: NodeType,
    
    @Schema(description = "Description of the node")
    val description: String
)

@Schema(description = "Graph edge information")
data class GraphEdge(
    @Schema(description = "Source node ID")
    val source: String,
    
    @Schema(description = "Target node ID")
    val target: String,
    
    @Schema(description = "Type of the edge")
    val type: EdgeType
)

@Schema(description = "Complete relationship graph")
data class RelationshipGraph(
    @Schema(description = "List of nodes in the graph")
    val nodes: List<GraphNode>,
    
    @Schema(description = "List of edges in the graph")
    val edges: List<GraphEdge>
) 