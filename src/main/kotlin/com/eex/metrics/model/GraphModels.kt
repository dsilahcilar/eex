package com.eex.metrics.model

data class GraphNode(
    val id: String,
    val label: String,
    val type: NodeType,
    val description: String
)

data class GraphEdge(
    val source: String,
    val target: String,
    val type: EdgeType
)

data class RelationshipGraph(
    val nodes: List<GraphNode>,
    val edges: List<GraphEdge>
)

enum class NodeType {
    METRIC,
    DRIVING_FACTOR,
    REMEDIATION_ACTION
}

enum class EdgeType {
    IMPACTS,           // Driving factor impacts metric
    REMEDIATED_BY,     // Driving factor is remediated by action
    LEADS_TO,         // Metric leads to another metric
    LAGS_BEHIND       // Metric lags behind another metric
} 