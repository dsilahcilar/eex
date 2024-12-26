package com.eex.metrics.controller

import com.eex.metrics.model.DrivingFactor
import com.eex.metrics.model.Metric
import com.eex.metrics.model.RemediationAction
import com.eex.metrics.service.MetricsService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/metrics")
@Tag(name = "Metrics API", description = "API endpoints for managing engineering metrics, driving factors, and remediation actions")
class MetricsController(private val metricsService: MetricsService) {

    @Operation(
        summary = "Get a metric by ID",
        description = "Retrieves a specific engineering metric by its unique identifier"
    )
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Metric found",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = Metric::class))]),
        ApiResponse(responseCode = "404", description = "Metric not found")
    )
    @GetMapping("/metrics/{id}")
    fun getMetric(
        @Parameter(description = "ID of the metric to retrieve")
        @PathVariable id: String
    ): ResponseEntity<Metric> {
        return metricsService.getMetric(id)?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()
    }

    @Operation(
        summary = "Get all metrics",
        description = "Retrieves a list of all engineering metrics"
    )
    @ApiResponse(responseCode = "200", description = "List of metrics retrieved successfully",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = Array<Metric>::class))])
    @GetMapping("/metrics")
    fun getAllMetrics(): ResponseEntity<List<Metric>> {
        return ResponseEntity.ok(metricsService.getAllMetrics())
    }

    @Operation(
        summary = "Get a driving factor by ID",
        description = "Retrieves a specific driving factor by its unique identifier"
    )
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Driving factor found",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = DrivingFactor::class))]),
        ApiResponse(responseCode = "404", description = "Driving factor not found")
    )
    @GetMapping("/driving-factors/{id}")
    fun getDrivingFactor(
        @Parameter(description = "ID of the driving factor to retrieve")
        @PathVariable id: String
    ): ResponseEntity<DrivingFactor> {
        return metricsService.getDrivingFactor(id)?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()
    }

    @Operation(
        summary = "Get all driving factors",
        description = "Retrieves a list of all driving factors"
    )
    @ApiResponse(responseCode = "200", description = "List of driving factors retrieved successfully",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = Array<DrivingFactor>::class))])
    @GetMapping("/driving-factors")
    fun getAllDrivingFactors(): ResponseEntity<List<DrivingFactor>> {
        return ResponseEntity.ok(metricsService.getAllDrivingFactors())
    }

    @Operation(
        summary = "Get a remediation action by ID",
        description = "Retrieves a specific remediation action by its unique identifier"
    )
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Remediation action found",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = RemediationAction::class))]),
        ApiResponse(responseCode = "404", description = "Remediation action not found")
    )
    @GetMapping("/remediation-actions/{id}")
    fun getRemediationAction(
        @Parameter(description = "ID of the remediation action to retrieve")
        @PathVariable id: String
    ): ResponseEntity<RemediationAction> {
        return metricsService.getRemediationAction(id)?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()
    }

    @Operation(
        summary = "Get all remediation actions",
        description = "Retrieves a list of all remediation actions"
    )
    @ApiResponse(responseCode = "200", description = "List of remediation actions retrieved successfully",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = Array<RemediationAction>::class))])
    @GetMapping("/remediation-actions")
    fun getAllRemediationActions(): ResponseEntity<List<RemediationAction>> {
        return ResponseEntity.ok(metricsService.getAllRemediationActions())
    }
} 