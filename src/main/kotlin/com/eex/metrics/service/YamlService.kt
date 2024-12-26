package com.eex.metrics.service

import com.eex.metrics.entity.DrivingFactorEntity
import com.eex.metrics.entity.MetricEntity
import com.eex.metrics.entity.MetricRelationshipsEntity
import com.eex.metrics.entity.RemediationActionEntity
import com.eex.metrics.model.DrivingFactor
import com.eex.metrics.model.Metric
import com.eex.metrics.model.MetricRelationships
import com.eex.metrics.model.RemediationAction
import com.eex.metrics.repository.DrivingFactorRepository
import com.eex.metrics.repository.MetricRepository
import com.eex.metrics.repository.RemediationActionRepository
import org.springframework.stereotype.Service

@Service
class YamlService(
    private val metricRepository: MetricRepository,
    private val drivingFactorRepository: DrivingFactorRepository,
    private val remediationActionRepository: RemediationActionRepository
) {
    fun readMetric(id: String): Metric? {
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
    
    fun readDrivingFactor(id: String): DrivingFactor? {
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
    
    fun readRemediationAction(id: String): RemediationAction? {
        return remediationActionRepository.findById(id).map { entity ->
            RemediationAction(
                id = entity.id,
                name = entity.name,
                description = entity.description
            )
        }.orElse(null)
    }
    
    fun saveMetric(metric: Metric) {
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
        
        metric.drivingFactors.forEach { factorId ->
            drivingFactorRepository.findById(factorId).ifPresent { factorEntity ->
                entity.drivingFactors.add(factorEntity)
                factorEntity.metrics.add(entity)
            }
        }
        
        metricRepository.save(entity)
    }
    
    fun saveDrivingFactor(factor: DrivingFactor) {
        val entity = DrivingFactorEntity(
            id = factor.id,
            name = factor.name,
            description = factor.description
        )
        
        factor.remediationActions.forEach { actionId ->
            remediationActionRepository.findById(actionId).ifPresent { actionEntity ->
                entity.remediationActions.add(actionEntity)
                actionEntity.drivingFactors.add(entity)
            }
        }
        
        drivingFactorRepository.save(entity)
    }
    
    fun saveRemediationAction(action: RemediationAction) {
        val entity = RemediationActionEntity(
            id = action.id,
            name = action.name,
            description = action.description
        )
        remediationActionRepository.save(entity)
    }
} 