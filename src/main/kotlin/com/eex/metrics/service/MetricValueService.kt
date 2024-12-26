package com.eex.metrics.service

import com.eex.metrics.entity.MetricValueEntity
import com.eex.metrics.model.MetricValue
import com.eex.metrics.repository.MetricValueRepository
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class MetricValueService(
    private val metricValueRepository: MetricValueRepository
) {
    fun createMetricValue(metricValue: MetricValue): MetricValue {
        val entity = MetricValueEntity(
            id = metricValue.id.ifBlank { UUID.randomUUID().toString() },
            metricId = metricValue.metricId,
            teamId = metricValue.teamId,
            value = metricValue.value,
            timestamp = metricValue.timestamp,
            notes = metricValue.notes
        )
        return metricValueRepository.save(entity).toModel()
    }

    fun getMetricValue(id: String): MetricValue? {
        return metricValueRepository.findById(id).map { it.toModel() }.orElse(null)
    }

    fun getMetricValuesByTeam(teamId: String): List<MetricValue> {
        return metricValueRepository.findByTeamId(teamId).map { it.toModel() }
    }

    fun getMetricValuesByMetric(metricId: String): List<MetricValue> {
        return metricValueRepository.findByMetricId(metricId).map { it.toModel() }
    }

    fun getMetricValuesByTeamAndMetric(teamId: String, metricId: String): List<MetricValue> {
        return metricValueRepository.findByTeamIdAndMetricId(teamId, metricId).map { it.toModel() }
    }

    fun getMetricValuesByTeamAndTimeRange(teamId: String, startTime: Instant, endTime: Instant): List<MetricValue> {
        return metricValueRepository.findByTeamIdAndTimestampBetween(teamId, startTime, endTime).map { it.toModel() }
    }

    fun getMetricValuesByTeamAndMetricAndTimeRange(
        teamId: String,
        metricId: String,
        startTime: Instant,
        endTime: Instant
    ): List<MetricValue> {
        return metricValueRepository.findByTeamIdAndMetricIdAndTimestampBetween(teamId, metricId, startTime, endTime)
            .map { it.toModel() }
    }
} 