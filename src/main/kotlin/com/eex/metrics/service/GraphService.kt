package com.eex.metrics.service

import com.eex.metrics.model.*
import org.springframework.stereotype.Service

@Service
class GraphService(
    private val metricsService: MetricsService,
    private val yamlService: YamlService
) {
    fun buildRelationshipGraph(): RelationshipGraph {
        val nodes = mutableListOf<GraphNode>()
        val edges = mutableListOf<GraphEdge>()

        // Add metrics and their relationships
        val metrics = metricsService.getAllMetrics()
        metrics.forEach { metric ->
            nodes.add(GraphNode(
                id = metric.id,
                label = metric.name,
                type = NodeType.METRIC,
                description = metric.description
            ))

            // Add leading/lagging relationships
            metric.relationships.leadingIndicators.forEach { leadingId ->
                edges.add(GraphEdge(
                    source = leadingId,
                    target = metric.id,
                    type = EdgeType.LEADS_TO
                ))
            }
            metric.relationships.laggingIndicators.forEach { laggingId ->
                edges.add(GraphEdge(
                    source = metric.id,
                    target = laggingId,
                    type = EdgeType.LAGS_BEHIND
                ))
            }
        }

        // Add driving factors and their relationships
        val drivingFactors = metricsService.getAllDrivingFactors()
        drivingFactors.forEach { factor ->
            nodes.add(GraphNode(
                id = factor.id,
                label = factor.name,
                type = NodeType.DRIVING_FACTOR,
                description = factor.description
            ))

            // Add relationships to metrics
            factor.metricsImpacted.forEach { metricId ->
                edges.add(GraphEdge(
                    source = factor.id,
                    target = metricId,
                    type = EdgeType.IMPACTS
                ))
            }

            // Add relationships to remediation actions
            factor.remediationActions.forEach { actionId ->
                edges.add(GraphEdge(
                    source = factor.id,
                    target = actionId,
                    type = EdgeType.REMEDIATED_BY
                ))
            }
        }

        // Add remediation actions
        val remediationActions = metricsService.getAllRemediationActions()
        remediationActions.forEach { action ->
            nodes.add(GraphNode(
                id = action.id,
                label = action.name,
                type = NodeType.REMEDIATION_ACTION,
                description = action.description
            ))
        }

        return RelationshipGraph(nodes = nodes, edges = edges)
    }
} 