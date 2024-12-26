package com.eex.organisation.service

import com.eex.organisation.entity.DepartmentEntity
import com.eex.organisation.entity.TeamEntity
import com.eex.organisation.model.Department
import com.eex.organisation.model.Team
import com.eex.organisation.repository.DepartmentRepository
import com.eex.organisation.repository.TeamRepository
import com.eex.metrics.model.Metric
import com.eex.metrics.model.MetricRelationships
import com.eex.metrics.repository.MetricRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class OrganizationService(
    private val departmentRepository: DepartmentRepository,
    private val teamRepository: TeamRepository,
    private val metricRepository: MetricRepository
) {
    fun getDepartment(id: String): Department? {
        return departmentRepository.findById(id).map { it.toModel() }.orElse(null)
    }

    fun getAllDepartments(): List<Department> {
        return departmentRepository.findAll().map { it.toModel() }
    }

    fun getSubDepartments(id: String): List<Department> {
        return departmentRepository.findByParentDepartmentId(id).map { it.toModel() }
    }

    fun createDepartment(department: Department): Department {
        val entity = DepartmentEntity(
            id = department.id,
            name = department.name,
            description = department.description
        )

        // Set parent department if specified
        department.parentDepartmentId?.let { parentId ->
            departmentRepository.findById(parentId).ifPresent { parentEntity ->
                entity.parentDepartment = parentEntity
            }
        }

        // Link metrics
        department.metricIds.forEach { metricId ->
            metricRepository.findById(metricId).ifPresent { metricEntity ->
                entity.metrics.add(metricEntity)
            }
        }

        // Link sub-departments
        department.subDepartmentIds.forEach { subDeptId ->
            departmentRepository.findById(subDeptId).ifPresent { subDeptEntity ->
                entity.subDepartments.add(subDeptEntity)
                subDeptEntity.parentDepartment = entity
                departmentRepository.save(subDeptEntity)
            }
        }

        return departmentRepository.save(entity).toModel()
    }

    fun getTeam(id: String): Team? {
        return teamRepository.findById(id).map { it.toModel() }.orElse(null)
    }

    fun getAllTeams(): List<Team> {
        return teamRepository.findAll().map { it.toModel() }
    }

    fun getTeamsByDepartment(departmentId: String): List<Team> {
        return teamRepository.findByDepartmentId(departmentId).map { it.toModel() }
    }

    @Transactional
    fun createTeam(team: Team): Team {
        val department = departmentRepository.findById(team.departmentId).orElseThrow {
            IllegalArgumentException("Department not found with ID: ${team.departmentId}")
        }

        val entity = TeamEntity(
            id = team.id,
            name = team.name,
            description = team.description,
            department = department
        )

        // Link metrics
        team.metricIds.forEach { metricId ->
            metricRepository.findById(metricId).ifPresent { metricEntity ->
                entity.metrics.add(metricEntity)
            }
        }

        return teamRepository.save(entity).toModel()
    }

    private fun DepartmentEntity.toModel(): Department {
        return Department(
            id = id,
            name = name,
            description = description,
            parentDepartmentId = parentDepartment?.id,
            subDepartmentIds = subDepartments.map { it.id },
            metricIds = metrics.map { it.id }
        )
    }

    private fun TeamEntity.toModel(): Team {
        return Team(
            id = id,
            name = name,
            description = description,
            departmentId = department.id,
            metricIds = metrics.map { it.id },
            metrics = metrics.map { 
                Metric(
                    id = it.id,
                    name = it.name,
                    description = it.description,
                    type = it.type,
                    drivingFactors = it.drivingFactors.map { df -> df.id },
                    relationships = MetricRelationships(
                        leadingIndicators = it.relationships.leadingIndicators,
                        laggingIndicators = it.relationships.laggingIndicators
                    )
                )
            }
        )
    }
} 