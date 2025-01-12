# Process Driving Factors

This directory contains driving factors related to development processes and workflows. These factors focus on workflow efficiency, quality control, and deployment processes.

## Structure
Each driving factor is defined in a YAML file with the following structure:
```yaml
id: unique_identifier
name: 'Factor Name'
description: 'Detailed description'
type: 'process'
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
- **Quality Control Gaps**: Inadequate quality assurance processes
- **Deployment Automation Gaps**: Missing or incomplete deployment automation
- **Workflow Inefficiencies**: Inefficient development and delivery processes
- **Deployment Risk**: Factors increasing deployment failure likelihood

## Relationships
Each factor owns its relationships with remediation actions, specifying:
- Which remediation actions address this factor
- Whether each action is a primary or secondary solution
- The impact level of each action (HIGH/MEDIUM/LOW)

## Subcategories
- quality_assurance
- deployment_operations
- development_workflow
- process_automation 