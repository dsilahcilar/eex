package com.eex.metrics.entity

import jakarta.persistence.*

@Entity
@Table(name = "metrics")
class MetricEntity(
    @Id
    val id: String,
    
    val name: String,
    
    @Column(length = 1000)
    val description: String,
    
    val type: String,

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "metric_driving_factors",
        joinColumns = [JoinColumn(name = "metric_id")],
        inverseJoinColumns = [JoinColumn(name = "driving_factor_id")]
    )
    val drivingFactors: MutableList<DrivingFactorEntity> = mutableListOf(),
    
    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    val relationships: MetricRelationshipsEntity
)

@Entity
@Table(name = "metric_relationships")
class MetricRelationshipsEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    
    @ElementCollection(fetch = FetchType.EAGER)
    val leadingIndicators: List<String>,
    
    @ElementCollection(fetch = FetchType.EAGER)
    val laggingIndicators: List<String>
)

@Entity
@Table(name = "driving_factors")
class DrivingFactorEntity(
    @Id
    val id: String,
    
    val name: String,
    
    @Column(length = 1000)
    val description: String,
    
    @ManyToMany(mappedBy = "drivingFactors", fetch = FetchType.EAGER)
    val metrics: MutableList<MetricEntity> = mutableListOf(),
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "driving_factor_remediation_actions",
        joinColumns = [JoinColumn(name = "driving_factor_id")],
        inverseJoinColumns = [JoinColumn(name = "remediation_action_id")]
    )
    val remediationActions: MutableList<RemediationActionEntity> = mutableListOf()
)

@Entity
@Table(name = "remediation_actions")
class RemediationActionEntity(
    @Id
    val id: String,
    
    val name: String,
    
    @Column(length = 1000)
    val description: String,
    
    @ManyToMany(mappedBy = "remediationActions", fetch = FetchType.EAGER)
    val drivingFactors: MutableList<DrivingFactorEntity> = mutableListOf()
) 