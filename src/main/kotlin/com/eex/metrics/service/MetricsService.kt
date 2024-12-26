package com.eex.metrics.service

import com.eex.metrics.entity.MetricEntity
import com.eex.metrics.entity.MetricRelationshipsEntity
import com.eex.metrics.model.DrivingFactor
import com.eex.metrics.model.Metric
import com.eex.metrics.model.MetricRelationships
import com.eex.metrics.model.RemediationAction
import com.eex.metrics.repository.DrivingFactorRepository
import com.eex.metrics.repository.MetricRepository
import com.eex.metrics.repository.RemediationActionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class MetricsService(
    private val metricRepository: MetricRepository,
    private val drivingFactorRepository: DrivingFactorRepository,
    private val remediationActionRepository: RemediationActionRepository
) {
    fun getMetric(id: String): Metric? {
        return metricRepository.findById(id).map { entity ->
            Metric(
                id = entity.id,
                name = entity.name,
                description = entity.description,
                type = entity.type,
                drivingFactors = entity.drivingFactors.map { it.id },
                relationships = MetricRelationships(
                    leadingIndicators = entity.relationships.leadingIndicators,
                    laggingIndicators = entity.relationships.laggingIndicators
                )
            )
        }.orElse(null)
    }

    fun getAllMetrics(): List<Metric> {
        return metricRepository.findAll().map { entity ->
            Metric(
                id = entity.id,
                name = entity.name,
                description = entity.description,
                type = entity.type,
                drivingFactors = entity.drivingFactors.map { it.id },
                relationships = MetricRelationships(
                    leadingIndicators = entity.relationships.leadingIndicators,
                    laggingIndicators = entity.relationships.laggingIndicators
                )
            )
        }
    }

    fun createMetric(metric: Metric): Metric {
        val relationships = MetricRelationshipsEntity(
            leadingIndicators = metric.relationships.leadingIndicators,
            laggingIndicators = metric.relationships.laggingIndicators
        )

        val entity = MetricEntity(
            id = metric.id,
            name = metric.name,
            description = metric.description,
            type = metric.type,
            relationships = relationships
        )

        // Link driving factors if they exist
        metric.drivingFactors.forEach { drivingFactorId ->
            drivingFactorRepository.findById(drivingFactorId).ifPresent { drivingFactorEntity ->
                entity.drivingFactors.add(drivingFactorEntity)
            }
        }

        val savedEntity = metricRepository.save(entity)
        return Metric(
            id = savedEntity.id,
            name = savedEntity.name,
            description = savedEntity.description,
            type = savedEntity.type,
            drivingFactors = savedEntity.drivingFactors.map { it.id },
            relationships = MetricRelationships(
                leadingIndicators = savedEntity.relationships.leadingIndicators,
                laggingIndicators = savedEntity.relationships.laggingIndicators
            )
        )
    }

    fun getDrivingFactor(id: String): DrivingFactor? {
        return drivingFactorRepository.findById(id).map { entity ->
            DrivingFactor(
                id = entity.id,
                name = entity.name,
                description = entity.description,
                metricsImpacted = entity.metrics.map { it.id },
                remediationActions = entity.remediationActions.map { it.id }
            )
        }.orElse(null)
    }

    fun getAllDrivingFactors(): List<DrivingFactor> {
        return drivingFactorRepository.findAll().map { entity ->
            DrivingFactor(
                id = entity.id,
                name = entity.name,
                description = entity.description,
                metricsImpacted = entity.metrics.map { it.id },
                remediationActions = entity.remediationActions.map { it.id }
            )
        }
    }

    fun getRemediationAction(id: String): RemediationAction? {
        return remediationActionRepository.findById(id).map { entity ->
            RemediationAction(
                id = entity.id,
                name = entity.name,
                description = entity.description
            )
        }.orElse(null)
    }

    fun getAllRemediationActions(): List<RemediationAction> {
        return remediationActionRepository.findAll().map { entity ->
            RemediationAction(
                id = entity.id,
                name = entity.name,
                description = entity.description
            )
        }
    }
} 