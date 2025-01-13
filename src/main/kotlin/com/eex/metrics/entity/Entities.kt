package com.eex.metrics.entity

import jakarta.persistence.*

@Entity
@Table(name = "metrics_old")
class MetricEntity(
    @Id
    val id: String,
    
    val name: String,
    
    @Column(length = 1000)
    val description: String,
    
    val type: String,

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "metric_categories",
        joinColumns = [JoinColumn(name = "metric_id")]
    )
    val categories: MutableList<String> = mutableListOf(),

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
    
    @Enumerated(EnumType.STRING)
    val type: DrivingFactorType,
    
    val subcategory: String,
    
    @ElementCollection
    @CollectionTable(
        name = "driving_factor_impact_areas",
        joinColumns = [JoinColumn(name = "driving_factor_id")]
    )
    val impactAreas: MutableList<String> = mutableListOf(),
    
    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "driving_factor_id")
    val remediationActionLinks: MutableList<DrivingFactorRemediationActionLink> = mutableListOf(),
    
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
@Table(name = "driving_factor_remediation_action_links")
class DrivingFactorRemediationActionLink(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    
    val remediationActionId: String,
    
    val primary: Boolean,
    
    @Enumerated(EnumType.STRING)
    val impact: ImpactLevel
)

enum class DrivingFactorType {
    TECHNICAL,
    PROCESS,
    SYSTEM,
    CULTURAL,
    ORGANIZATIONAL
}

enum class ImpactLevel {
    LOW,
    MEDIUM,
    HIGH
}

@Entity
@Table(name = "remediation_actions")
class RemediationActionEntity(
    @Id
    val id: String,
    
    val name: String,
    
    @Column(length = 1000)
    val description: String,
    
    @Enumerated(EnumType.STRING)
    val type: RemediationActionType,
    
    @Enumerated(EnumType.STRING)
    val implementationComplexity: ComplexityLevel,
    
    @Enumerated(EnumType.STRING)
    val timeInvestment: InvestmentLevel,
    
    @Enumerated(EnumType.STRING)
    val costInvestment: InvestmentLevel,
    
    @ElementCollection
    @CollectionTable(
        name = "remediation_action_expected_outcomes",
        joinColumns = [JoinColumn(name = "remediation_action_id")]
    )
    val expectedOutcomes: MutableList<String> = mutableListOf(),
    
    @ElementCollection
    @CollectionTable(
        name = "remediation_action_implementation_steps",
        joinColumns = [JoinColumn(name = "remediation_action_id")]
    )
    val implementationSteps: MutableList<String> = mutableListOf(),
    
    @ElementCollection
    @CollectionTable(
        name = "remediation_action_success_metrics",
        joinColumns = [JoinColumn(name = "remediation_action_id")]
    )
    val successMetrics: MutableList<String> = mutableListOf(),
    
    @ElementCollection
    @CollectionTable(
        name = "remediation_action_resources_needed",
        joinColumns = [JoinColumn(name = "remediation_action_id")]
    )
    val resourcesNeeded: MutableList<String> = mutableListOf(),
    
    @ManyToMany(mappedBy = "remediationActions", fetch = FetchType.EAGER)
    val drivingFactors: MutableList<DrivingFactorEntity> = mutableListOf()
)

enum class RemediationActionType {
    TECHNICAL,
    PROCESS,
    ORGANIZATIONAL,
    CULTURAL,
    SYSTEM
}

enum class ComplexityLevel {
    LOW,
    MEDIUM,
    HIGH
}

enum class InvestmentLevel {
    LOW,
    MEDIUM,
    HIGH
} 