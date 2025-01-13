package com.eex.metrics.model

import com.eex.metrics.entity.*
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

    @Schema(description = "Categories this metric belongs to", example = "['ex', 'cognitive_load']")
    val categories: List<String> = emptyList(),

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

    @Schema(description = "Type of the driving factor", example = "TECHNICAL")
    val type: DrivingFactorType,

    @Schema(description = "Subcategory of the driving factor", example = "code_quality")
    val subcategory: String,

    @Schema(description = "Areas impacted by this driving factor")
    val impactAreas: List<String> = emptyList(),

    @Schema(description = "List of metric IDs that are impacted by this driving factor")
    val metricsImpacted: List<String> = emptyList(),

    @Schema(description = "Detailed remediation action links")
    val remediationActionLinks: List<RemediationActionLink> = emptyList()
)

@Schema(description = "Remediation action information")
data class RemediationAction(
    @Schema(description = "Unique identifier of the remediation action", example = "implement_ci")
    val id: String,

    @Schema(description = "Name of the remediation action", example = "Implement Continuous Integration")
    val name: String,

    @Schema(description = "Detailed description of the remediation action", example = "Set up continuous integration pipelines to automatically build, test, and deploy code changes")
    val description: String,

    @Schema(description = "Type of the remediation action", example = "manual")
    val type: RemediationActionType,

    @Schema(description = "Complexity level of the remediation action", example = "medium")
    val implementationComplexity: ComplexityLevel,

    @Schema(description = "Time investment required for the remediation action", example = "100 hours")
    val timeInvestment: InvestmentLevel,

    @Schema(description = "Cost investment required for the remediation action", example = "$10,000")
    val costInvestment: InvestmentLevel,

    @Schema(description = "Expected outcomes of the remediation action")
    val expectedOutcomes: List<String> = emptyList(),

    @Schema(description = "Implementation steps of the remediation action")
    val implementationSteps: List<String> = emptyList(),

    @Schema(description = "Metrics that are impacted by the remediation action")
    val successMetrics: List<String> = emptyList(),

    @Schema(description = "Resources needed for the remediation action")
    val resourcesNeeded: List<String> = emptyList()
)

@Schema(description = "Metric value information")
data class MetricValue(
    @Schema(description = "Unique identifier of the metric value")
    val id: String,

    @Schema(description = "ID of the metric this value belongs to")
    val metricId: String,

    @Schema(description = "ID of the team this metric value belongs to")
    val teamId: String,

    @Schema(description = "The actual value of the metric")
    val value: Double,

    @Schema(description = "The timestamp when this value was recorded", example = "2024-01-01T00:00:00Z")
    val timestamp: java.time.Instant,

    @Schema(description = "Optional notes about this metric value")
    val notes: String? = null
)

@Schema(description = "Remediation action link information")
data class RemediationActionLink(
    @Schema(description = "ID of the remediation action")
    val remediationActionId: String,

    @Schema(description = "Whether this is a primary remediation action")
    val primary: Boolean,

    @Schema(description = "Impact level of this remediation action")
    val impact: ImpactLevel
) 