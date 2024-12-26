package com.eex.metrics.controller

import com.eex.metrics.model.MetricValue
import com.eex.metrics.service.MetricValueService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.Instant

@RestController
@RequestMapping("/api/metrics/values")
@Tag(name = "Metric Values API", description = "API endpoints for managing metric values")
class MetricValueController(private val metricValueService: MetricValueService) {

    @Operation(
        summary = "Create a metric value",
        description = "Records a new metric value for a team"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Metric value created successfully",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = MetricValue::class))]
    )
    @PostMapping
    fun createMetricValue(@RequestBody metricValue: MetricValue): ResponseEntity<MetricValue> {
        return ResponseEntity.ok(metricValueService.createMetricValue(metricValue))
    }

    @Operation(
        summary = "Get a metric value by ID",
        description = "Retrieves a specific metric value by its unique identifier"
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Metric value found",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = MetricValue::class))]
        ),
        ApiResponse(responseCode = "404", description = "Metric value not found")
    )
    @GetMapping("/{id}")
    fun getMetricValue(
        @Parameter(description = "ID of the metric value to retrieve")
        @PathVariable id: String
    ): ResponseEntity<MetricValue> {
        return metricValueService.getMetricValue(id)?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()
    }

    @Operation(
        summary = "Get metric values by team",
        description = "Retrieves all metric values for a specific team"
    )
    @GetMapping("/team/{teamId}")
    fun getMetricValuesByTeam(
        @Parameter(description = "ID of the team")
        @PathVariable teamId: String
    ): ResponseEntity<List<MetricValue>> {
        return ResponseEntity.ok(metricValueService.getMetricValuesByTeam(teamId))
    }

    @Operation(
        summary = "Get metric values by team and metric",
        description = "Retrieves all values for a specific metric and team"
    )
    @GetMapping("/team/{teamId}/metric/{metricId}")
    fun getMetricValuesByTeamAndMetric(
        @Parameter(description = "ID of the team")
        @PathVariable teamId: String,
        @Parameter(description = "ID of the metric")
        @PathVariable metricId: String
    ): ResponseEntity<List<MetricValue>> {
        return ResponseEntity.ok(metricValueService.getMetricValuesByTeamAndMetric(teamId, metricId))
    }

    @Operation(
        summary = "Get metric values by team and time range",
        description = "Retrieves all metric values for a team within a specific time range"
    )
    @GetMapping("/team/{teamId}/range")
    fun getMetricValuesByTeamAndTimeRange(
        @Parameter(description = "ID of the team")
        @PathVariable teamId: String,
        @Parameter(description = "Start time of the range")
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) startTime: Instant,
        @Parameter(description = "End time of the range")
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) endTime: Instant
    ): ResponseEntity<List<MetricValue>> {
        return ResponseEntity.ok(metricValueService.getMetricValuesByTeamAndTimeRange(teamId, startTime, endTime))
    }

    @Operation(
        summary = "Get metric values by team, metric, and time range",
        description = "Retrieves all values for a specific metric and team within a time range"
    )
    @GetMapping("/team/{teamId}/metric/{metricId}/range")
    fun getMetricValuesByTeamAndMetricAndTimeRange(
        @Parameter(description = "ID of the team")
        @PathVariable teamId: String,
        @Parameter(description = "ID of the metric")
        @PathVariable metricId: String,
        @Parameter(description = "Start time of the range")
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) startTime: Instant,
        @Parameter(description = "End time of the range")
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) endTime: Instant
    ): ResponseEntity<List<MetricValue>> {
        return ResponseEntity.ok(
            metricValueService.getMetricValuesByTeamAndMetricAndTimeRange(teamId, metricId, startTime, endTime)
        )
    }
} 