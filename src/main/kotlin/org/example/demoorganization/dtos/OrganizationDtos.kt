package org.example.demoorganization.dtos

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size
import org.example.demoorganization.Region
import java.time.LocalDateTime

data class CreateOrganizationRequest(
    @field:NotBlank(message = "{organization.name.notblank}")
    @field:Size(min = 2, max = 255, message = "{organization.name.size}")
    val name: String,

    @field:NotNull(message = "{organization.regionid.notnull}")
    var regionId: Long,

    @field:Positive(message = "{organization.parentid.positive}")
    val parentId: Long? = null
)

data class UpdateOrganizationRequest(
    @field:Size(min = 2, max = 255, message = "{organization.name.size}")
    val name: String?= null,
    val regionId: Long? = null,
    @field:Positive(message = "{organization.parentid.positive}")
    val parentId: Long? = null
)

data class OrganizationShortResponse(
    val id: Long,
    val name: String,
    val region: RegionShortResponse
)

data class OrganizationFullResponse(
    val id: Long,
    val name: String,
    val region: RegionShortResponse,
    val parent: OrganizationShortResponse? = null,
    val employeeCount: Int = 0,
    val branchCount: Int = 0,
    val calculationCount: Int = 0,

    @JsonFormat(pattern = "yyyy.MM.dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    val createdDate: LocalDateTime? = null,

    @JsonFormat(pattern = "yyyy.MM.dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    val modifiedDate: LocalDateTime? = null,

    val createdBy: String? = null,
    val lastModifiedBy: String? = null,
    val deleted: Boolean = false
)

