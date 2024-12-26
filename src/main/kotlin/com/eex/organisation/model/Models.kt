package com.eex.organisation.model

import com.eex.metrics.model.Metric
import com.fasterxml.jackson.annotation.JsonIgnore

data class Department(
    val id: String,
    val name: String,
    val description: String,
    val parentDepartmentId: String? = null,
    val metricIds: List<String> = emptyList(),
    val subDepartmentIds: List<String> = emptyList(),
    val teamIds: List<String> = emptyList(),
    @JsonIgnore
    val metrics: List<Metric> = emptyList(),
    @JsonIgnore
    val subDepartments: List<Department> = emptyList(),
    @JsonIgnore
    val teams: List<Team> = emptyList()
)

data class Team(
    val id: String,
    val name: String,
    val description: String,
    val departmentId: String,
    val metricIds: List<String> = emptyList(),
    @JsonIgnore
    val metrics: List<Metric> = emptyList()
) 