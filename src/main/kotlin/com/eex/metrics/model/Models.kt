package com.eex.metrics.model

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Engineering metric information")
data class Metric(
    @Schema(description = "Unique identifier of the metric", example = "deployment_frequency")
    val id: String,

    @Schema(description = "Name of the metric", example = "Deployment Frequency")
    val name: String,

    @Schema(description = "Detailed description of the metric", example = "How often changes are deployed to production")
    val description: String,

    @Schema(description = "Type of the metric (e.g., quantitative, qualitative)", example = "quantitative")
    val type: String,

    @Schema(description = "List of driving factor IDs that affect this metric")
    val drivingFactors: List<String>,

    @Schema(description = "Relationships with other metrics")
    val relationships: MetricRelationships
)

@Schema(description = "Metric relationships information")
data class MetricRelationships(
    @Schema(description = "List of metrics that are leading indicators for this metric")
    val leadingIndicators: List<String>,

    @Schema(description = "List of metrics that are lagging indicators for this metric")
    val laggingIndicators: List<String>
)

@Schema(description = "Driving factor information")
data class DrivingFactor(
    @Schema(description = "Unique identifier of the driving factor", example = "lack_of_automation")
    val id: String,

    @Schema(description = "Name of the driving factor", example = "Lack of Automation")
    val name: String,

    @Schema(description = "Detailed description of the driving factor", example = "Manual steps and limited CI/CD pipelines slow deployments")
    val description: String,

    @Schema(description = "List of metric IDs that are impacted by this driving factor")
    val metricsImpacted: List<String>,

    @Schema(description = "List of remediation action IDs that can address this driving factor")
    val remediationActions: List<String>
)

@Schema(description = "Remediation action information")
data class RemediationAction(
    @Schema(description = "Unique identifier of the remediation action", example = "implement_ci")
    val id: String,

    @Schema(description = "Name of the remediation action", example = "Implement Continuous Integration")
    val name: String,

    @Schema(description = "Detailed description of the remediation action", example = "Set up continuous integration pipelines to automatically build, test, and deploy code changes")
    val description: String
) 