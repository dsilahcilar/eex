package com.eex.metrics.repository

import com.eex.metrics.entity.DrivingFactorEntity
import com.eex.metrics.entity.MetricEntity
import com.eex.metrics.entity.RemediationActionEntity
import org.springframework.data.jpa.repository.JpaRepository

interface MetricRepository : JpaRepository<MetricEntity, String>

interface DrivingFactorRepository : JpaRepository<DrivingFactorEntity, String>

interface RemediationActionRepository : JpaRepository<RemediationActionEntity, String> 