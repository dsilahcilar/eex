package com.eex.initializer

import com.eex.organisation.model.Department
import com.eex.organisation.model.Team
import com.eex.organisation.service.OrganizationService
import com.eex.metrics.model.MetricValue
import com.eex.metrics.service.MetricValueService
import com.eex.metrics.service.MetricsService
import com.eex.metrics.service.YamlService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.*
import java.util.*
import jakarta.annotation.PostConstruct

@Service
class DataInitializationService(
    private val organizationService: OrganizationService,
    private val metricsService: MetricsService,
    private val metricValueService: MetricValueService,
    private val yamlService: YamlService
) {
    @PostConstruct
    @Transactional
    fun initialize() {
        yamlService.loadAllFromYaml()
        initializeDepartmentsAndTeams()
        initializeMetricValues()
    }

    private fun initializeDepartmentsAndTeams() {
        // Create departments
        val engineeringDept = Department(
            id = "engineering",
            name = "Engineering",
            description = "Software Engineering Department",
            parentDepartmentId = null,
            subDepartmentIds = emptyList()
        )
        val productDept = Department(
            id = "product",
            name = "Product",
            description = "Product Development Department",
            parentDepartmentId = null,
            subDepartmentIds = emptyList()
        )

        organizationService.createDepartment(engineeringDept)
        organizationService.createDepartment(productDept)

        // Get all available metrics
        val allMetrics = metricsService.getAllMetrics()
        val metricIds = allMetrics.map { it.id }

        // Create teams for Engineering department
        val engineeringTeams = listOf(
            Team(id = "backend", name = "Backend Team", description = "Backend Development", departmentId = "engineering", metricIds = metricIds),
            Team(id = "frontend", name = "Frontend Team", description = "Frontend Development", departmentId = "engineering", metricIds = metricIds),
            Team(id = "devops", name = "DevOps Team", description = "Infrastructure and Operations", departmentId = "engineering", metricIds = metricIds),
            Team(id = "qa", name = "QA Team", description = "Quality Assurance", departmentId = "engineering", metricIds = metricIds),
            Team(id = "platform", name = "Platform Team", description = "Platform Development", departmentId = "engineering", metricIds = metricIds)
        )

        // Create teams for Product department
        val productTeams = listOf(
            Team(id = "design", name = "Design Team", description = "Product Design", departmentId = "product", metricIds = metricIds),
            Team(id = "analytics", name = "Analytics Team", description = "Product Analytics", departmentId = "product", metricIds = metricIds),
            Team(id = "research", name = "Research Team", description = "User Research", departmentId = "product", metricIds = metricIds),
            Team(id = "content", name = "Content Team", description = "Content Strategy", departmentId = "product", metricIds = metricIds),
            Team(id = "growth", name = "Growth Team", description = "Product Growth", departmentId = "product", metricIds = metricIds)
        )

        // Create all teams
        (engineeringTeams + productTeams).forEach { team ->
            organizationService.createTeam(team)
        }
    }

    private fun initializeMetricValues() {
        val metrics = metricsService.getAllMetrics()
        val teams = organizationService.getAllTeams()
        val now = LocalDateTime.now()

        // Generate 6 months of monthly metric values for each team
        teams.forEach { team ->
            metrics.forEach { metric ->
                // Generate 6 monthly values
                (0..5).forEach { monthsAgo ->
                    val timestamp = now.minusMonths(monthsAgo.toLong())
                        .withDayOfMonth(1)
                        .withHour(12)
                        .withMinute(0)
                        .withSecond(0)
                        .withNano(0)
                        .atZone(ZoneId.systemDefault())
                        .toInstant()

                    val value = when (metric.id) {
                        "deployment_frequency" -> Random().nextDouble() * 50 + 10 // 10-60 deployments per month
                        "lead_time_for_changes" -> Random().nextDouble() * 48 + 12 // 12-60 hours
                        "change_failure_rate" -> Random().nextDouble() * 15 // 0-15%
                        "mean_time_to_recovery" -> Random().nextDouble() * 4 + 1 // 1-5 hours
                        "system_availability" -> Random().nextDouble() * 1 + 99 // 99-100%
                        else -> generateRandomMetricValue()
                    }

                    val metricValue = MetricValue(
                        id = UUID.randomUUID().toString(),
                        metricId = metric.id,
                        teamId = team.id,
                        value = value,
                        timestamp = timestamp,
                        notes = "Monthly metric value for ${metric.name}"
                    )

                    metricValueService.createMetricValue(metricValue)
                }
            }
        }
    }

    private fun generateRandomMetricValue(): Double {
        // Generate a random value between 0 and 100 with 2 decimal places
        return (Random().nextDouble() * 100).toBigDecimal().setScale(2, java.math.RoundingMode.HALF_UP).toDouble()
    }
} 