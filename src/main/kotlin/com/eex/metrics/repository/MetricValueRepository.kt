package com.eex.metrics.repository

import com.eex.metrics.entity.MetricValueEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
interface MetricValueRepository : JpaRepository<MetricValueEntity, String> {
    fun findByTeamId(teamId: String): List<MetricValueEntity>
    fun findByMetricId(metricId: String): List<MetricValueEntity>
    fun findByTeamIdAndMetricId(teamId: String, metricId: String): List<MetricValueEntity>
    fun findByTeamIdAndTimestampBetween(teamId: String, startTime: Instant, endTime: Instant): List<MetricValueEntity>
    fun findByTeamIdAndMetricIdAndTimestampBetween(teamId: String, metricId: String, startTime: Instant, endTime: Instant): List<MetricValueEntity>
    fun findByMetricIdAndTeamId(metricId: String, teamId: String): List<MetricValueEntity>
    fun findByTeamIdAndMetricIdIn(teamId: String, metricIds: List<String>): List<MetricValueEntity>
} 