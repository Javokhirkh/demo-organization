package org.example.demoorganization.services

import jakarta.transaction.Transactional
import org.example.demoorganization.RegionMapper
import org.example.demoorganization.RegionNameAlreadyExistsException
import org.example.demoorganization.RegionNotFoundException
import org.example.demoorganization.RegionRepository
import org.example.demoorganization.dtos.CreateRegionRequest
import org.example.demoorganization.dtos.RegionFullResponse
import org.example.demoorganization.dtos.RegionShortResponse
import org.springframework.stereotype.Service

interface RegionService {
    fun create(dto: CreateRegionRequest)
    fun update(id: Long, dto: CreateRegionRequest)
    fun delete(id: Long)
    fun get(id: Long): RegionFullResponse
    fun getAll(): List<RegionShortResponse>
}
@Service
class RegionServiceImpl(
    private val repository: RegionRepository,
    private val mapper: RegionMapper
) : RegionService {
    @Transactional
    override fun create(dto: CreateRegionRequest) {
        repository.existsByNameAndDeletedFalse(dto.name)?.let {
            throw RegionNameAlreadyExistsException()
        }
        val region = mapper.toEntity(dto)
        repository.save(region)
    }

    @Transactional
    override fun update(id: Long, dto: CreateRegionRequest) {
        val existingRegion = repository.findByIdAndDeletedFalse(id)
            ?: throw RegionNotFoundException()
        if(dto.name != existingRegion.name){
            repository.existsByNameAndDeletedFalse(dto.name)?.let {
                throw RegionNameAlreadyExistsException()
            }
        }
        existingRegion.name = dto.name
        repository.save(existingRegion)
    }
    @Transactional
    override fun delete(id: Long) {
        repository.trash(id)?:throw RegionNotFoundException()
    }

    override fun get(id: Long): RegionFullResponse {
        val region = repository.findByIdAndDeletedFalse(id)
            ?: throw RegionNotFoundException()
        return mapper.toFullResponse(region)
    }

    override fun getAll(): List<RegionShortResponse> {
        val regions = repository.findAllNotDeleted()
        return regions.map { mapper.toShortResponse(it) }
    }

}