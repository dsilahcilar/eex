package com.eex.metrics.entity

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "metric_values")
class MetricValueEntity(
    @Id
    var id: String,

    @Column(nullable = false)
    var metricId: String,

    @Column(nullable = false)
    var teamId: String,

    @Column(name = "metric_value", nullable = false)
    var value: Double,

    @Column(nullable = false)
    var timestamp: Instant,

    @Column
    var notes: String? = null
) {
    fun toModel() = com.eex.metrics.model.MetricValue(
        id = id,
        metricId = metricId,
        teamId = teamId,
        value = value,
        timestamp = timestamp,
        notes = notes
    )
} 