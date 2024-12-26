package com.eex.organisation.entity

import com.eex.metrics.entity.MetricEntity
import jakarta.persistence.*

@Entity
@Table(name = "departments")
class DepartmentEntity(
    @Id
    val id: String,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var description: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_department_id")
    var parentDepartment: DepartmentEntity? = null,

    @OneToMany(mappedBy = "parentDepartment", cascade = [CascadeType.ALL], orphanRemoval = true)
    var subDepartments: MutableList<DepartmentEntity> = mutableListOf(),

    @OneToMany(mappedBy = "department", cascade = [CascadeType.ALL], orphanRemoval = true)
    var teams: MutableList<TeamEntity> = mutableListOf(),

    @ManyToMany
    @JoinTable(
        name = "department_metrics",
        joinColumns = [JoinColumn(name = "department_id")],
        inverseJoinColumns = [JoinColumn(name = "metric_id")]
    )
    var metrics: MutableList<MetricEntity> = mutableListOf()
)

@Entity
@Table(name = "teams")
class TeamEntity(
    @Id
    val id: String,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var description: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    var department: DepartmentEntity,

    @ManyToMany
    @JoinTable(
        name = "team_metrics",
        joinColumns = [JoinColumn(name = "team_id")],
        inverseJoinColumns = [JoinColumn(name = "metric_id")]
    )
    var metrics: MutableList<MetricEntity> = mutableListOf()
) 