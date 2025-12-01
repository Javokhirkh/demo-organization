package org.example.demoorganization.services

import org.example.demoorganization.CalculationRepository
import org.example.demoorganization.CalculationTableMapper
import org.example.demoorganization.CalculationTableNotFoundException
import org.example.demoorganization.EmployeeNotFoundException
import org.example.demoorganization.EmployeeRepository
import org.example.demoorganization.OrganizationNotFoundException
import org.example.demoorganization.OrganizationRepository
import org.example.demoorganization.dtos.CalculationFullResponse
import org.example.demoorganization.dtos.CalculationShortResponse
import org.example.demoorganization.dtos.CreateCalculationRequest
import org.example.demoorganization.dtos.UpdateCalculationRequest
import org.springframework.stereotype.Service

interface CalculationTableService {
    fun create (dto: CreateCalculationRequest)
    fun update (id: Long, dto: UpdateCalculationRequest)
    fun delete (id: Long)
    fun get(id: Long): CalculationFullResponse
    fun getAll(): List<CalculationShortResponse>
}

@Service
class CalculationTableServiceImpl(
    private val repository: CalculationRepository,
    private val mapper: CalculationTableMapper,
    private val orgRepository: OrganizationRepository,
    private val employeeRepository: EmployeeRepository
): CalculationTableService {
    override fun create(dto: CreateCalculationRequest) {
        val org=orgRepository.findByIdAndDeletedFalse(dto.organizationId)
            ?:throw OrganizationNotFoundException()
        val employee = employeeRepository.findByIdAndDeletedFalse(dto.employeeId)
            ?: throw EmployeeNotFoundException()
        val calculation=mapper.toEntity(dto, org ,employee)
        repository.save(calculation)
    }

    override fun update(id: Long, dto: UpdateCalculationRequest) {
        val existingCalculation = repository.findByIdAndDeletedFalse(id)
            ?: throw CalculationTableNotFoundException()
        dto.organizationId?.let{ organizationId ->
            val org=orgRepository.findByIdAndDeletedFalse(organizationId)
                ?:throw OrganizationNotFoundException()
            existingCalculation.organization=org
        }
        dto.employeeId?.let{ employeeId ->
            val employee = employeeRepository.findByIdAndDeletedFalse(employeeId)
                ?: throw EmployeeNotFoundException()
            existingCalculation.employee=employee
        }
        existingCalculation.apply {
            date = dto.date ?: date
            amount = dto.amount ?: amount
            workRate = dto.workRate ?: workRate
            calculationType=dto.calculationType ?: calculationType
        }
        repository.save(existingCalculation)
    }

    override fun delete(id: Long) {
        repository.trash(id)?:throw CalculationTableNotFoundException()
    }

    override fun get(id: Long): CalculationFullResponse {
        val calculation=repository.findByIdAndDeletedFalse(id)
            ?:throw CalculationTableNotFoundException()
        return mapper.toFullResponse(calculation)
    }

    override fun getAll(): List<CalculationShortResponse> {
        val calculations=repository.findAllNotDeleted()
        return calculations.map { mapper.toShortResponse(it) }
    }

}