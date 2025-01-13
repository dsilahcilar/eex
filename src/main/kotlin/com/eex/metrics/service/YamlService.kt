package com.eex.metrics.service

import com.eex.metrics.entity.*
import com.eex.metrics.model.*
import com.eex.metrics.repository.DrivingFactorRepository
import com.eex.metrics.repository.MetricRepository
import com.eex.metrics.repository.RemediationActionRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.core.io.Resource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class YamlService(
    private val metricRepository: MetricRepository,
    private val drivingFactorRepository: DrivingFactorRepository,
    private val remediationActionRepository: RemediationActionRepository
) {
    private val yamlMapper = ObjectMapper(YAMLFactory())
        .registerModule(KotlinModule.Builder().build())
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true)
        .configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true)
    private val resourceResolver = PathMatchingResourcePatternResolver()

    @Transactional
    fun loadAllFromYaml() {
        loadRemediationActionsFromYaml()
        loadDrivingFactorsFromYaml()
        loadMetricsFromYaml()
    }

    private fun loadMetricsFromYaml() {
        val resources = resourceResolver.getResources("classpath:metrics/**/*.yml")
        resources.forEach { resource ->
            try {
                val metric: Metric = yamlMapper.readValue(resource.inputStream)
                saveMetric(metric)
            } catch (e: Exception) {
                println("Error loading metric from ${resource.filename}: ${e.message}")
            }
        }
    }

    private fun loadDrivingFactorsFromYaml() {
        val resources = resourceResolver.getResources("classpath:driving_factors/**/*.yml")
        resources.forEach { resource ->
            try {
                val factor: DrivingFactor = yamlMapper.readValue(resource.inputStream)
                saveDrivingFactor(factor)
            } catch (e: Exception) {
                println("Error loading driving factor from ${resource.filename}: ${e.message}")
            }
        }
    }

    private fun loadRemediationActionsFromYaml() {
        val resources = resourceResolver.getResources("classpath:remediation_actions/**/*.yml")
        resources.forEach { resource ->
            try {
                val action: RemediationAction = yamlMapper.readValue(resource.inputStream)
                saveRemediationAction(action)
            } catch (e: Exception) {
                println("Error loading remediation action from ${resource.filename}: ${e.message}")
            }
        }
    }

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
                type = entity.type,
                subcategory = entity.subcategory,
                impactAreas = entity.impactAreas.toList(),
                metricsImpacted = entity.metrics.map { it.id },
                remediationActionLinks = entity.remediationActionLinks.map { link ->
                    RemediationActionLink(
                        remediationActionId = link.remediationActionId,
                        primary = link.primary,
                        impact = link.impact
                    )
                }
            )
        }.orElse(null)
    }
    
    fun readRemediationAction(id: String): RemediationAction? {
        return remediationActionRepository.findById(id).map { entity ->
            RemediationAction(
                id = entity.id,
                name = entity.name,
                description = entity.description,
                type = entity.type,
                implementationComplexity = entity.implementationComplexity,
                timeInvestment = entity.timeInvestment,
                costInvestment = entity.costInvestment,
                expectedOutcomes = entity.expectedOutcomes.toList(),
                implementationSteps = entity.implementationSteps.toList(),
                successMetrics = entity.successMetrics.toList(),
                resourcesNeeded = entity.resourcesNeeded.toList()
            )
        }.orElse(null)
    }
    
    @Transactional
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
            categories = metric.categories.toMutableList(),
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
    
    @Transactional
    fun saveDrivingFactor(factor: DrivingFactor) {
        val entity = DrivingFactorEntity(
            id = factor.id,
            name = factor.name,
            description = factor.description,
            type = factor.type,
            subcategory = factor.subcategory,
            impactAreas = factor.impactAreas.toMutableList()
        )
        
        // Add remediation action links
        factor.remediationActionLinks.forEach { link ->
            val actionLink = DrivingFactorRemediationActionLink(
                remediationActionId = link.remediationActionId,
                primary = link.primary,
                impact = link.impact
            )
            entity.remediationActionLinks.add(actionLink)
            
            // Also maintain the many-to-many relationship
            remediationActionRepository.findById(link.remediationActionId).ifPresent { actionEntity ->
                entity.remediationActions.add(actionEntity)
                actionEntity.drivingFactors.add(entity)
            }
        }
        
        drivingFactorRepository.save(entity)
    }
    
    @Transactional
    fun saveRemediationAction(action: RemediationAction) {
        val entity = RemediationActionEntity(
            id = action.id,
            name = action.name,
            description = action.description,
            type = action.type,
            implementationComplexity = action.implementationComplexity,
            timeInvestment = action.timeInvestment,
            costInvestment = action.costInvestment,
            expectedOutcomes = action.expectedOutcomes.toMutableList(),
            implementationSteps = action.implementationSteps.toMutableList(),
            successMetrics = action.successMetrics.toMutableList(),
            resourcesNeeded = action.resourcesNeeded.toMutableList()
        )
        remediationActionRepository.save(entity)
    }
} 