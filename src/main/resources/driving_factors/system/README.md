# System Driving Factors

This directory contains driving factors related to system-level concerns. These factors focus on reliability, performance, monitoring, and service integration aspects.

## Structure
Each driving factor is defined in a YAML file with the following structure:
```yaml
id: unique_identifier
name: 'Factor Name'
description: 'Detailed description'
type: 'system'
subcategory: 'category_name'

impact_areas:
  - 'Area 1'
  - 'Area 2'

remediation_actions:
  - id: 'action_id'
    primary: true|false
    impact: 'HIGH|MEDIUM|LOW'

metric_impacts:
  metric_name:
    severity: 'HIGH|MEDIUM|LOW'
    description: 'How this factor impacts the metric'

impact_indicators:
  high_severity_signals:
    - 'Signal 1'
    - 'Signal 2'
  warning_signals:
    - 'Warning 1'
    - 'Warning 2'
```

## Available Factors
- **Performance Bottlenecks**: System performance and resource utilization issues
- **Insufficient Monitoring**: Inadequate system observability and monitoring
- **Service Integration Issues**: Problems with service interactions and dependencies
- **Reliability Issues**: System stability and availability problems

## Relationships
Each factor owns its relationships with remediation actions, specifying:
- Which remediation actions address this factor
- Whether each action is a primary or secondary solution
- The impact level of each action (HIGH/MEDIUM/LOW)

## Subcategories
- performance
- reliability
- observability
- integration 