package com.eex.organisation.repository

import com.eex.organisation.entity.DepartmentEntity
import com.eex.organisation.entity.TeamEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface DepartmentRepository : JpaRepository<DepartmentEntity, String> {
    fun findByParentDepartmentId(parentDepartmentId: String): List<DepartmentEntity>
}

@Repository
interface TeamRepository : JpaRepository<TeamEntity, String> {
    fun findByDepartmentId(departmentId: String): List<TeamEntity>
} 