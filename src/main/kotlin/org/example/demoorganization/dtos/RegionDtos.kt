package org.example.demoorganization.dtos

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.LocalDateTime


data class CreateRegionRequest(
    @field:NotBlank(message = "{region.name.notblank}")
    @field:Size(min = 2, max = 100, message = "{region.name.size}")
    val name: String
)

data class UpdateRegionRequest(
    @field:Size(min = 2, max = 100, message = "{region.name.size}")
    val name: String?
)

data class RegionShortResponse(
    val id: Long,
    val name: String
)

data class RegionFullResponse(
    val id: Long,
    val name: String,
    val organizationCount: Int = 0,

    @JsonFormat(pattern = "yyyy.MM.dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    val createdDate: LocalDateTime? = null,

    @JsonFormat(pattern = "yyyy.MM.dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    val modifiedDate: LocalDateTime? = null,

    val createdBy: String? = null,
    val lastModifiedBy: String? = null,
)