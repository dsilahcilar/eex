package com.eex.organisation.service

import com.eex.metrics.repository.MetricRepository
import com.eex.organisation.entity.DepartmentEntity
import com.eex.organisation.entity.TeamEntity
import com.eex.organisation.model.Department
import com.eex.organisation.model.Team
import com.eex.organisation.repository.DepartmentRepository
import com.eex.organisation.repository.TeamRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import jakarta.annotation.PostConstruct
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrganizationDataInitializationService(
    private val departmentRepository: DepartmentRepository,
    private val teamRepository: TeamRepository,
    private val metricRepository: MetricRepository
) {
    private val mapper = ObjectMapper(YAMLFactory()).registerKotlinModule()
    private val resourceResolver = PathMatchingResourcePatternResolver()

    @PostConstruct
    @Transactional
    fun initializeData() {
        // First load departments as they only depend on metrics
        loadDepartments()
        // Then load teams as they depend on departments and metrics
        loadTeams()
    }

    @Transactional
    fun loadDepartments() {
        val resources = resourceResolver.getResources("classpath:departments/*.yml")
        resources.forEach { resource ->
            val department = mapper.readValue(resource.inputStream, Department::class.java)
            val entity = DepartmentEntity(
                id = department.id,
                name = department.name,
                description = department.description
            )
            
            // Save the entity first
            departmentRepository.save(entity)
            
            // Link parent department if specified
            department.parentDepartmentId?.let { parentId ->
                departmentRepository.findById(parentId).ifPresent { parentEntity ->
                    entity.parentDepartment = parentEntity
                    parentEntity.subDepartments.add(entity)
                    departmentRepository.save(parentEntity)
                }
            }
            
            // Link metrics
            department.metrics.forEach { metric ->
                metricRepository.findById(metric.id).ifPresent { metricEntity ->
                    entity.metrics.add(metricEntity)
                }
            }
            
            // Save the entity again with updated relationships
            departmentRepository.save(entity)
        }
    }

    @Transactional
    fun loadTeams() {
        val resources = resourceResolver.getResources("classpath:teams/*.yml")
        resources.forEach { resource ->
            val team = mapper.readValue(resource.inputStream, Team::class.java)
            val entity = TeamEntity(
                id = team.id,
                name = team.name,
                description = team.description,
                department = departmentRepository.findById(team.departmentId).orElseThrow {
                    IllegalArgumentException("Department not found with ID: ${team.departmentId}")
                }
            )
            
            // Save the entity first
            teamRepository.save(entity)
            
            // Link metrics
            team.metrics.forEach { metric ->
                metricRepository.findById(metric.id).ifPresent { metricEntity ->
                    entity.metrics.add(metricEntity)
                }
            }
            
            // Save the entity again with updated relationships
            teamRepository.save(entity)
        }
    }
} 