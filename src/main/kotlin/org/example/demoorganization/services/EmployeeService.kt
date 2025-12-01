package org.example.demoorganization.services

import org.example.demoorganization.Employee
import org.example.demoorganization.EmployeeMapper
import org.example.demoorganization.EmployeeNotFoundException
import org.example.demoorganization.EmployeeRepository
import org.example.demoorganization.OrganizationNotFoundException
import org.example.demoorganization.OrganizationRepository
import org.example.demoorganization.PnfnAlreadyExistsException
import org.example.demoorganization.dtos.CreateEmployeeRequest
import org.example.demoorganization.dtos.EmployeeFullResponse
import org.example.demoorganization.dtos.EmployeeShortResponse
import org.example.demoorganization.dtos.UpdateEmployeeRequest
import org.springframework.stereotype.Service

interface EmployeeService {
    fun create(dto: CreateEmployeeRequest)
    fun update(id:Long,dto: UpdateEmployeeRequest)
    fun delete(id: Long)
    fun get(id: Long): EmployeeFullResponse
    fun getAll(): List<EmployeeShortResponse>
}

@Service
class EmployeeServiceImpl(
    private val repository: EmployeeRepository,
    private val mapper: EmployeeMapper,
    private val orgRepository: OrganizationRepository
): EmployeeService{
    override fun create(dto: CreateEmployeeRequest) {
        val org=orgRepository.findByIdAndDeletedFalse(dto.organizationId)
            ?:throw OrganizationNotFoundException()
        repository.existsByPinflAndDeletedFalse(dto.pinfl)?.let {
            throw PnfnAlreadyExistsException()
        }
        val employee: Employee = mapper.toEntity(dto,org)
        repository.save(employee)
    }

    override fun update(id: Long, dto: UpdateEmployeeRequest) {
        val existingEmployee = repository.findByIdAndDeletedFalse(id)
            ?: throw EmployeeNotFoundException()

        dto.pinfl?.let { newPinfl ->
            if (newPinfl != existingEmployee.pinfl) {
                repository.existsByPinflAndDeletedFalse(newPinfl)?.let {
                    throw PnfnAlreadyExistsException()
                }
            }
        }

        existingEmployee.apply {
            firstName=dto.firstName?:firstName
            lastName = dto.lastName ?: lastName
            pinfl = dto.pinfl ?: pinfl
            hireDate=dto.hireDate?:hireDate
        }

        repository.save(existingEmployee)
    }

    override fun delete(id: Long) {
        repository.trash(id) ?: throw OrganizationNotFoundException()
    }

    override fun get(id: Long): EmployeeFullResponse {
        val employee=repository.findByIdAndDeletedFalse(id)
            ?:throw OrganizationNotFoundException()
        return mapper.toFullResponse(employee)
    }

    override fun getAll(): List<EmployeeShortResponse> {
        val employees=repository.findAllNotDeleted()
        return employees.map { mapper.toShortResponse(it) }
    }

}