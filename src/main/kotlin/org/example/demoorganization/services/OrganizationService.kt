package org.example.demoorganization.services

import org.example.demoorganization.OrganizationMapper
import org.example.demoorganization.OrganizationNameAlreadyExistsException
import org.example.demoorganization.OrganizationNotFoundException
import org.example.demoorganization.OrganizationRepository
import org.example.demoorganization.RegionNotFoundException
import org.example.demoorganization.RegionRepository
import org.example.demoorganization.dtos.CreateOrganizationRequest
import org.example.demoorganization.dtos.OrganizationFullResponse
import org.example.demoorganization.dtos.OrganizationShortResponse
import org.example.demoorganization.dtos.UpdateOrganizationRequest
import org.springframework.stereotype.Service

interface OrganizationService {
    fun create(dto: CreateOrganizationRequest)
    fun update(id: Long, dto: UpdateOrganizationRequest)
    fun delete(id: Long)
    fun get(id: Long): OrganizationFullResponse
    fun getAll(): List<OrganizationShortResponse>
}

@Service
class OrganizationServiceImpl(
    private val repository: OrganizationRepository,
    private val mapper: OrganizationMapper,
    private val regionRepository: RegionRepository
) : OrganizationService {

    override fun create(dto: CreateOrganizationRequest) {
        val region=regionRepository.findByIdAndDeletedFalse(dto.regionId)
            ?: throw RegionNotFoundException()
        val parent = dto.parentId?.let {
            repository.findByIdAndDeletedFalse(it)
                ?: throw OrganizationNotFoundException()
        }
        repository.existsByNameAndDeletedFalse(dto.name)?.let {
            throw OrganizationNameAlreadyExistsException()
        } ?: run {
            val organization = mapper.toEntity(dto,region,parent)
            repository.save(organization)
        }
    }

    override fun update(id: Long, dto: UpdateOrganizationRequest) {
        val existingOrganization = repository.findByIdAndDeletedFalse(id)
            ?: throw OrganizationNotFoundException()

        dto.name?.let { newName ->
            if (newName != existingOrganization.name) {
                repository.existsByNameAndDeletedFalse(newName)?.let {
                    throw OrganizationNameAlreadyExistsException()
                }
                existingOrganization.name = newName
            }
        }

        dto.regionId?.let { newRegionId ->
            val region = regionRepository.findByIdAndDeletedFalse(newRegionId)
                ?: throw RegionNotFoundException()
            existingOrganization.region = region
        }

        dto.parentId?.let { newParentId ->
            val parent = repository.findByIdAndDeletedFalse(newParentId)
                ?: throw OrganizationNotFoundException()
            existingOrganization.parent = parent
        }
        repository.save(existingOrganization)
    }

    override fun delete(id: Long) {
        repository.trash(id)
            ?: throw OrganizationNotFoundException()
    }

    override fun get(id: Long): OrganizationFullResponse {
        val organization = repository.findByIdAndDeletedFalse(id)
            ?: throw OrganizationNotFoundException()
        return mapper.toFullResponse(organization)
    }

    override fun getAll(): List<OrganizationShortResponse> {
        val organizations = repository.findAllNotDeleted()
        return organizations.map { mapper.toShortResponse(it) }
    }

}