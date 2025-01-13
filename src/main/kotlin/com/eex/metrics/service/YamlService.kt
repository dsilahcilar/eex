package com.eex.metrics.service

import com.eex.metrics.entity.*
import com.eex.metrics.model.*
import com.eex.metrics.repository.DrivingFactorRepository
import com.eex.metrics.repository.MetricRepository
import com.eex.metrics.repository.RemediationActionRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.PropertyNamingStrategies
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
    private val yamlMapper = ObjectMapper(YAMLFactory()).apply {
        registerModule(KotlinModule.Builder().build())
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true)
        configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true)
        configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
        propertyNamingStrategy = PropertyNamingStrategies.SNAKE_CASE
    }
    private val resourceResolver = PathMatchingResourcePatternResolver()

    private fun String.toEnumValue(): DrivingFactorType {
        return when (this.uppercase()) {
            "TECHNICAL" -> DrivingFactorType.TECHNICAL
            "PROCESS" -> DrivingFactorType.PROCESS
            "SYSTEM" -> DrivingFactorType.SYSTEM
            "CULTURAL" -> DrivingFactorType.CULTURAL
            "ORGANIZATIONAL" -> DrivingFactorType.ORGANIZATIONAL
            else -> throw IllegalArgumentException("Unknown driving factor type: $this")
        }
    }

    private fun String.toRemediationActionType(): RemediationActionType {
        return when (this.uppercase()) {
            "TECHNICAL" -> RemediationActionType.TECHNICAL
            "PROCESS" -> RemediationActionType.PROCESS
            "SYSTEM" -> RemediationActionType.SYSTEM
            "CULTURAL" -> RemediationActionType.CULTURAL
            "ORGANIZATIONAL" -> RemediationActionType.ORGANIZATIONAL
            else -> throw IllegalArgumentException("Unknown remediation action type: $this")
        }
    }

    @Transactional
    fun loadAllFromYaml() {
        loadDrivingFactorsFromYaml()
        loadRemediationActionsFromYaml()
        loadMetricsFromYaml()
    }

    private fun loadMetricsFromYaml() {
        val resources = resourceResolver.getResources("classpath:metrics/**/*.yml")
        resources.forEach { resource ->
            try {
                val yamlContent = resource.inputStream.bufferedReader().use { it.readText() }
                val metricMap = yamlMapper.readValue<Map<String, Any>>(yamlContent)
                
                val relationships = (metricMap["relationships"] as? Map<*, *>)?.let { rel ->
                    MetricRelationships(
                        leadingIndicators = (rel["leading_indicators"] as? List<*>)?.map { it.toString() } ?: emptyList(),
                        laggingIndicators = (rel["lagging_indicators"] as? List<*>)?.map { it.toString() } ?: emptyList()
                    )
                } ?: MetricRelationships()

                val metric = Metric(
                    id = metricMap["id"] as String,
                    name = metricMap["name"] as String,
                    description = metricMap["description"] as String,
                    type = metricMap["type"] as String,
                    categories = (metricMap["categories"] as? List<*>)?.map { it.toString() } ?: emptyList(),
                    drivingFactors = (metricMap["drivingFactors"] as? List<*>)?.map { it.toString() } ?: emptyList(),
                    relationships = relationships
                )
                saveMetric(metric)
            } catch (e: Exception) {
                println("Error loading metric from ${resource.filename}: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    private fun loadDrivingFactorsFromYaml() {
        val resources = resourceResolver.getResources("classpath:driving_factors/**/*.yml")
        resources.forEach { resource ->
            try {
                val yamlContent = resource.inputStream.bufferedReader().use { it.readText() }
                val factorMap = yamlMapper.readValue<Map<String, Any>>(yamlContent)
                
                val type = (factorMap["type"] as String).toEnumValue()
                val factor = DrivingFactor(
                    id = factorMap["id"] as String,
                    name = factorMap["name"] as String,
                    description = factorMap["description"] as String,
                    type = type,
                    subcategory = factorMap["subcategory"] as String,
                    impactAreas = (factorMap["impact_areas"] as List<*>).map { it.toString() },
                    remediationActionLinks = (factorMap["remediation_actions"] as? List<*>)?.map { action ->
                        val actionMap = action as Map<*, *>
                        RemediationActionLink(
                            remediationActionId = actionMap["id"] as String,
                            isPrimary = actionMap["primary"] as Boolean,
                            impact = ImpactLevel.valueOf((actionMap["impact"] as String).uppercase())
                        )
                    } ?: emptyList()
                )
                saveDrivingFactor(factor)
            } catch (e: Exception) {
                println("Error loading driving factor from ${resource.filename}: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    private fun loadRemediationActionsFromYaml() {
        val resources = resourceResolver.getResources("classpath:remediation_actions/**/*.yml")
        resources.forEach { resource ->
            try {
                val yamlContent = resource.inputStream.bufferedReader().use { it.readText() }
                val actionMap = yamlMapper.readValue<Map<String, Any>>(yamlContent)
                
                val action = RemediationAction(
                    id = actionMap["id"] as String,
                    name = actionMap["name"] as String,
                    description = actionMap["description"] as String,
                    type = (actionMap["type"] as String).toRemediationActionType(),
                    implementationComplexity = ComplexityLevel.valueOf((actionMap["implementation_complexity"] as String).uppercase()),
                    timeInvestment = InvestmentLevel.valueOf((actionMap["time_investment"] as String).uppercase()),
                    costInvestment = InvestmentLevel.valueOf((actionMap["cost_investment"] as String).uppercase()),
                    expectedOutcomes = (actionMap["expected_outcomes"] as? List<*>)?.map { it.toString() } ?: emptyList(),
                    implementationSteps = (actionMap["implementation_steps"] as? List<*>)?.map { it.toString() } ?: emptyList(),
                    successMetrics = (actionMap["success_metrics"] as? List<*>)?.map { it.toString() } ?: emptyList(),
                    resourcesNeeded = (actionMap["resources_needed"] as? List<*>)?.map { it.toString() } ?: emptyList()
                )
                saveRemediationAction(action)
            } catch (e: Exception) {
                println("Error loading remediation action from ${resource.filename}: ${e.message}")
                e.printStackTrace()
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
    
//    fun readDrivingFactor(id: String): DrivingFactor? {
//        return drivingFactorRepository.findById(id).map { entity ->
//            DrivingFactor(
//                id = entity.id,
//                name = entity.name,
//                description = entity.description,
//                type = entity.type,
//                subcategory = entity.subcategory,
//                impactAreas = entity.impactAreas.toList(),
//                metricsImpacted = entity.metrics.map { it.id },
//                remediationActionLinks = entity.remediationActionLinks.map { link ->
//                    RemediationActionLink(
//                        remediationActionId = link.remediationActionId,
//                        primary = link.primary,
//                        impact = link.impact
//                    )
//                }
//            )
//        }.orElse(null)
//    }
    
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
                isPrimary = link.isPrimary,
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