package org.example.demoorganization

import org.example.demoorganization.dtos.CalculationFullResponse
import org.example.demoorganization.dtos.CalculationShortResponse
import org.example.demoorganization.dtos.CreateCalculationRequest
import org.example.demoorganization.dtos.CreateEmployeeRequest
import org.example.demoorganization.dtos.CreateOrganizationRequest
import org.example.demoorganization.dtos.CreateRegionRequest
import org.example.demoorganization.dtos.EmployeeFullResponse
import org.example.demoorganization.dtos.EmployeeShortResponse
import org.example.demoorganization.dtos.OrganizationFullResponse
import org.example.demoorganization.dtos.OrganizationShortResponse
import org.example.demoorganization.dtos.RegionFullResponse
import org.example.demoorganization.dtos.RegionShortResponse
import org.springframework.stereotype.Component

@Component
class OrganizationMapper(
    private val regionMapper: RegionMapper,
) {
    fun toEntity(dto: CreateOrganizationRequest,region: Region,org: Organization?=null): Organization {
        dto.run {
            return Organization(
                name = name,
                region = region,
                parent = org?.parent,
            )
        }
    }
    fun toShortResponse(entity: Organization): OrganizationShortResponse {
        return OrganizationShortResponse(
            id =  entity.id!!,
            name = entity.name,
            region = regionMapper.toShortResponse(entity.region),
        )
    }
    fun toFullResponse(entity: Organization): OrganizationFullResponse {
        return OrganizationFullResponse(
            id = entity.id!!,
            name = entity.name,
            createdDate = entity.createdDate,
            modifiedDate = entity.modifiedDate,
            createdBy = entity.createdBy,
            lastModifiedBy = entity.lastModifiedBy,
            region = regionMapper.toShortResponse(entity.region),
            parent = entity.parent?.let { toShortResponse(it) }
        )
    }
}

@Component
class RegionMapper(

) {
    fun toEntity(dto : CreateRegionRequest): Region {
        return Region(
            name = dto.name
        )
    }
    fun toFullResponse(entity: Region): RegionFullResponse {
        return RegionFullResponse(
            id = entity.id!!,
            name = entity.name,
            createdDate = entity.createdDate,
            modifiedDate = entity.modifiedDate,
            createdBy = entity.createdBy,
            lastModifiedBy = entity.lastModifiedBy,
            organizationCount = entity.organizations.size
        )
    }
    fun toShortResponse(entity: Region): RegionShortResponse {
        return RegionShortResponse(
            id = entity.id!!,
            name = entity.name
        )
    }
}

@Component
class EmployeeMapper(
    private val orgMapper: OrganizationMapper
){
    fun toEntity(dto: CreateEmployeeRequest,org: Organization): Employee {
        return Employee(
            firstName = dto.firstName,
            lastName = dto.lastName,
            pinfl = dto.pinfl,
            hireDate = dto.hireDate,
            organization = org
        )
    }

    fun toShortResponse(entity: Employee): EmployeeShortResponse {
        return EmployeeShortResponse(
            id = entity.id!!,
            firstName = entity.firstName,
            lastName = entity.lastName,
            pinfl = entity.pinfl,
            hireDate = entity.hireDate,
            organization = orgMapper.toShortResponse(entity.organization)
        )
    }

    fun toFullResponse(entity: Employee): EmployeeFullResponse {
        return EmployeeFullResponse(
            id = entity.id!!,
            firstName = entity.firstName,
            lastName = entity.lastName,
            pinfl = entity.pinfl,
            hireDate = entity.hireDate,
            organization = orgMapper.toShortResponse(entity.organization),
            modifiedDate = entity.modifiedDate,
            createdDate = entity.createdDate,
            createdBy = entity.createdBy,
            lastModifiedBy = entity.lastModifiedBy
        )
    }
}
@Component
class CalculationTableMapper(
    private val organizationMapper: OrganizationMapper,
    private val employeeMapper: EmployeeMapper,
){
    fun toEntity(dto: CreateCalculationRequest,org: Organization,emp: Employee) : CalculationTable {
        return CalculationTable(
            amount = dto.amount,
            organization = org,
            employee = emp,
            calculationType = dto.calculationType,
            date = dto.date,
            workRate = dto.workRate
        )
    }

    fun toShortResponse(entity: CalculationTable): CalculationShortResponse {
        return CalculationShortResponse(
            id = entity.id!!,
            amount = entity.amount,
            workRate =  entity.workRate,
            date = entity.date,
            calculationType = entity.calculationType,
            organization = organizationMapper.toShortResponse(entity.organization),
            employee = employeeMapper.toShortResponse(entity.employee)
        )

    }
    fun toFullResponse(entity: CalculationTable): CalculationFullResponse {
        return CalculationFullResponse(
            id = entity.id!!,
            amount = entity.amount,
            workRate =  entity.workRate,
            date = entity.date,
            calculationType = entity.calculationType,
            organization = organizationMapper.toShortResponse(entity.organization),
            employee = employeeMapper.toShortResponse(entity.employee),
            createdDate = entity.createdDate,
            modifiedDate = entity.modifiedDate,
            createdBy = entity.createdBy,
            lastModifiedBy = entity.lastModifiedBy
        )

    }
}