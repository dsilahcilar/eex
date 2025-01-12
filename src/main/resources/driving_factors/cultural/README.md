# Cultural Driving Factors

This directory contains driving factors related to team culture and practices. These factors focus on knowledge sharing, collaboration, and developer experience.

## Structure
Each driving factor is defined in a YAML file with the following structure:
```yaml
id: unique_identifier
name: 'Factor Name'
description: 'Detailed description'
type: 'cultural'
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
- **Developer Experience Issues**: Challenges in development environment and workflow
- **Knowledge Gaps**: Gaps in technical knowledge and understanding
- **Collaboration Friction**: Barriers in team collaboration
- **Knowledge Sharing Gaps**: Ineffective knowledge transfer practices

## Relationships
Each factor owns its relationships with remediation actions, specifying:
- Which remediation actions address this factor
- Whether each action is a primary or secondary solution
- The impact level of each action (HIGH/MEDIUM/LOW)

## Subcategories
- developer_satisfaction
- team_capability
- team_collaboration
- knowledge_management 