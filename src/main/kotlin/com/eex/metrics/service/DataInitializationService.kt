package com.eex.metrics.service

import com.eex.metrics.entity.DrivingFactorEntity
import com.eex.metrics.entity.MetricEntity
import com.eex.metrics.entity.MetricRelationshipsEntity
import com.eex.metrics.entity.RemediationActionEntity
import com.eex.metrics.model.DrivingFactor
import com.eex.metrics.model.Metric
import com.eex.metrics.model.RemediationAction
import com.eex.metrics.repository.DrivingFactorRepository
import com.eex.metrics.repository.MetricRepository
import com.eex.metrics.repository.RemediationActionRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import jakarta.annotation.PostConstruct
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DataInitializationService(
    private val metricRepository: MetricRepository,
    private val drivingFactorRepository: DrivingFactorRepository,
    private val remediationActionRepository: RemediationActionRepository
) {
    private val mapper = ObjectMapper(YAMLFactory()).registerKotlinModule()
    private val resourceResolver = PathMatchingResourcePatternResolver()

    @PostConstruct
    @Transactional
    fun initializeData() {
        // First load remediation actions as they have no dependencies
        loadRemediationActions()
        // Then load driving factors as they depend on remediation actions
        loadDrivingFactors()
        // Then load metrics as they depend on driving factors
        loadMetrics()
    }

    @Transactional
    fun loadRemediationActions() {
        val resources = resourceResolver.getResources("classpath:remediation_actions/*.yml")
        resources.forEach { resource ->
            val action = mapper.readValue(resource.inputStream, RemediationAction::class.java)
            val entity = RemediationActionEntity(
                id = action.id,
                name = action.name,
                description = action.description
            )
            remediationActionRepository.save(entity)
        }
    }

    @Transactional
    fun loadDrivingFactors() {
        val resources = resourceResolver.getResources("classpath:driving_factors/*.yml")
        resources.forEach { resource ->
            val factor = mapper.readValue(resource.inputStream, DrivingFactor::class.java)
            val entity = DrivingFactorEntity(
                id = factor.id,
                name = factor.name,
                description = factor.description
            )
            
            // Save the entity first
            drivingFactorRepository.save(entity)
            
            // Link remediation actions after saving
            factor.remediationActions.forEach { actionId ->
                remediationActionRepository.findById(actionId).ifPresent { actionEntity ->
                    entity.remediationActions.add(actionEntity)
                    actionEntity.drivingFactors.add(entity)
                    remediationActionRepository.save(actionEntity)
                }
            }
            
            // Save the entity again with updated relationships
            drivingFactorRepository.save(entity)
        }
    }

    @Transactional
    fun loadMetrics() {
        val resources = resourceResolver.getResources("classpath:metrics/*.yml")
        resources.forEach { resource ->
            val metric = mapper.readValue(resource.inputStream, Metric::class.java)
            val entity = MetricEntity(
                id = metric.id,
                name = metric.name,
                description = metric.description,
                type = metric.type,
                relationships = MetricRelationshipsEntity(
                    leadingIndicators = metric.relationships.leadingIndicators,
                    laggingIndicators = metric.relationships.laggingIndicators
                )
            )
            
            // Save the entity first
            metricRepository.save(entity)
            
            // Link driving factors after saving
            metric.drivingFactors.forEach { factorId ->
                drivingFactorRepository.findById(factorId).ifPresent { factorEntity ->
                    entity.drivingFactors.add(factorEntity)
                    factorEntity.metrics.add(entity)
                    drivingFactorRepository.save(factorEntity)
                }
            }
            
            // Save the entity again with updated relationships
            metricRepository.save(entity)
        }
    }
} 