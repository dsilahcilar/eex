package com.eex.organisation.controller

import com.eex.organisation.model.Department
import com.eex.organisation.model.Team
import com.eex.organisation.service.OrganizationService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/organization")
@Tag(name = "Organization API", description = "API endpoints for managing departments and teams")
class OrganizationController(private val organizationService: OrganizationService) {

    @Operation(
        summary = "Get a department by ID",
        description = "Retrieves a specific department by its unique identifier"
    )
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Department found",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = Department::class))]),
        ApiResponse(responseCode = "404", description = "Department not found")
    )
    @GetMapping("/departments/{id}")
    fun getDepartment(
        @Parameter(description = "ID of the department to retrieve")
        @PathVariable id: String
    ): ResponseEntity<Department> {
        return organizationService.getDepartment(id)?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()
    }

    @Operation(
        summary = "Get all departments",
        description = "Retrieves a list of all departments"
    )
    @ApiResponse(responseCode = "200", description = "List of departments retrieved successfully",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = Array<Department>::class))])
    @GetMapping("/departments")
    fun getAllDepartments(): ResponseEntity<List<Department>> {
        return ResponseEntity.ok(organizationService.getAllDepartments())
    }

    @Operation(
        summary = "Get sub-departments",
        description = "Retrieves all sub-departments of a specific department"
    )
    @ApiResponse(responseCode = "200", description = "List of sub-departments retrieved successfully",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = Array<Department>::class))])
    @GetMapping("/departments/{id}/sub-departments")
    fun getSubDepartments(
        @Parameter(description = "ID of the parent department")
        @PathVariable id: String
    ): ResponseEntity<List<Department>> {
        return ResponseEntity.ok(organizationService.getSubDepartments(id))
    }

    @Operation(
        summary = "Create a department",
        description = "Creates a new department with the provided information"
    )
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Department created successfully",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = Department::class))])
    )
    @PostMapping("/departments")
    fun createDepartment(@RequestBody department: Department): ResponseEntity<Department> {
        return ResponseEntity.ok(organizationService.createDepartment(department))
    }

    @Operation(
        summary = "Get a team by ID",
        description = "Retrieves a specific team by its unique identifier"
    )
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Team found",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = Team::class))]),
        ApiResponse(responseCode = "404", description = "Team not found")
    )
    @GetMapping("/teams/{id}")
    fun getTeam(
        @Parameter(description = "ID of the team to retrieve")
        @PathVariable id: String
    ): ResponseEntity<Team> {
        return organizationService.getTeam(id)?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()
    }

    @Operation(
        summary = "Get all teams",
        description = "Retrieves a list of all teams"
    )
    @ApiResponse(responseCode = "200", description = "List of teams retrieved successfully",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = Array<Team>::class))])
    @GetMapping("/teams")
    fun getAllTeams(): ResponseEntity<List<Team>> {
        return ResponseEntity.ok(organizationService.getAllTeams())
    }

    @Operation(
        summary = "Get teams by department",
        description = "Retrieves all teams belonging to a specific department"
    )
    @ApiResponse(responseCode = "200", description = "List of teams retrieved successfully",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = Array<Team>::class))])
    @GetMapping("/departments/{id}/teams")
    fun getTeamsByDepartment(
        @Parameter(description = "ID of the department")
        @PathVariable id: String
    ): ResponseEntity<List<Team>> {
        return ResponseEntity.ok(organizationService.getTeamsByDepartment(id))
    }

    @Operation(
        summary = "Create a team",
        description = "Creates a new team with the provided information"
    )
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Team created successfully",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = Team::class))])
    )
    @PostMapping("/teams")
    fun createTeam(@RequestBody team: Team): ResponseEntity<Team> {
        return ResponseEntity.ok(organizationService.createTeam(team))
    }
} 