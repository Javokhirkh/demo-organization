package org.example.demoorganization

import org.example.demoorganization.dtos.*
import org.example.demoorganization.services.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid

@RestController
@RequestMapping("/api/regions")
class RegionController(
    private val regionService: RegionService
) {

    @PostMapping
    fun createRegion(
        @Valid @RequestBody request: CreateRegionRequest
    ): ResponseEntity<Unit> {
        regionService.create(request)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PutMapping("/{id}")
    fun updateRegion(
        @PathVariable id: Long,
        @Valid @RequestBody request: CreateRegionRequest
    ): ResponseEntity<Unit> {
        regionService.update(id, request)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/{id}")
    fun deleteRegion(
        @PathVariable id: Long
    ): ResponseEntity<Unit> {
        regionService.delete(id)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/{id}")
    fun getRegion(
        @PathVariable id: Long
    ): ResponseEntity<RegionFullResponse> {
        val region = regionService.get(id)
        return ResponseEntity.ok(region)
    }

    @GetMapping
    fun getAllRegions(): ResponseEntity<List<RegionShortResponse>> {
        val regions = regionService.getAll()
        return ResponseEntity.ok(regions)
    }
}

@RestController
@RequestMapping("/api/organizations")
class OrganizationController(
    private val organizationService: OrganizationService
) {

    @PostMapping
    fun createOrganization(
        @Valid @RequestBody request: CreateOrganizationRequest
    ): ResponseEntity<Unit> {
        organizationService.create(request)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PutMapping("/{id}")
    fun updateOrganization(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateOrganizationRequest
    ): ResponseEntity<Unit> {
        organizationService.update(id, request)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/{id}")
    fun deleteOrganization(
        @PathVariable id: Long
    ): ResponseEntity<Unit> {
        organizationService.delete(id)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/{id}")
    fun getOrganization(
        @PathVariable id: Long
    ): ResponseEntity<OrganizationFullResponse> {
        val organization = organizationService.get(id)
        return ResponseEntity.ok(organization)
    }

    @GetMapping
    fun getAllOrganizations(): ResponseEntity<List<OrganizationShortResponse>> {
        val organizations = organizationService.getAll()
        return ResponseEntity.ok(organizations)
    }
}

@RestController
@RequestMapping("/api/employees")
class EmployeeController(
    private val employeeService: EmployeeService
) {

    @PostMapping
    fun createEmployee(
        @Valid @RequestBody request: CreateEmployeeRequest
    ): ResponseEntity<Unit> {
        employeeService.create(request)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PutMapping("/{id}")
    fun updateEmployee(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateEmployeeRequest
    ): ResponseEntity<Unit> {
        employeeService.update(id, request)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/{id}")
    fun deleteEmployee(
        @PathVariable id: Long
    ): ResponseEntity<Unit> {
        employeeService.delete(id)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/{id}")
    fun getEmployee(
        @PathVariable id: Long
    ): ResponseEntity<EmployeeFullResponse> {
        val employee = employeeService.get(id)
        return ResponseEntity.ok(employee)
    }

    @GetMapping
    fun getAllEmployees(): ResponseEntity<List<EmployeeShortResponse>> {
        val employees = employeeService.getAll()
        return ResponseEntity.ok(employees)
    }
}

@RestController
@RequestMapping("/api/calculations")
class CalculationTableController(
    private val calculationService: CalculationTableService
) {

    @PostMapping
    fun createCalculation(
        @Valid @RequestBody request: CreateCalculationRequest
    ): ResponseEntity<Unit> {
        calculationService.create(request)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PutMapping("/{id}")
    fun updateCalculation(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateCalculationRequest
    ): ResponseEntity<Unit> {
        calculationService.update(id, request)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/{id}")
    fun deleteCalculation(
        @PathVariable id: Long
    ): ResponseEntity<Unit> {
        calculationService.delete(id)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/{id}")
    fun getCalculation(
        @PathVariable id: Long
    ): ResponseEntity<CalculationFullResponse> {
        val calculation = calculationService.get(id)
        return ResponseEntity.ok(calculation)
    }

    @GetMapping
    fun getAllCalculations(): ResponseEntity<List<CalculationShortResponse>> {
        val calculations = calculationService.getAll()
        return ResponseEntity.ok(calculations)
    }
}