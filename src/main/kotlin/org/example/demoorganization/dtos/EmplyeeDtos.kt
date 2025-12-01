package org.example.demoorganization.dtos

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size
import java.time.LocalDate
import java.time.LocalDateTime
data class CreateEmployeeRequest(
    @field:NotBlank(message = "{employee.firstname.notblank}")
    @field:Size(min = 2, max = 100, message = "{employee.firstname.size}")
    val firstName: String,

    @field:NotBlank(message = "{employee.lastname.notblank}")
    @field:Size(min = 2, max = 100, message = "{employee.lastname.size}")
    val lastName: String,

    @field:NotBlank(message = "{employee.pinfl.notblank}")
    @field:Size(min = 14, max = 14, message = "{employee.pinfl.size}")
    val pinfl: String,

    @field:NotNull(message = "{employee.hiredate.notnull}")
    @JsonFormat(pattern = "yyyy.MM.dd", shape = JsonFormat.Shape.STRING)
    var hireDate: LocalDate,

    @field:NotNull(message = "{employee.organizationid.notnull}")
    @field:Positive(message = "{employee.organizationid.positive}")
    var organizationId: Long
)

data class UpdateEmployeeRequest(
    @field:Size(min = 2, max = 100, message = "{employee.firstname.size}")
    val firstName: String?,

    @field:Size(min = 2, max = 100, message = "{employee.lastname.size}")
    val lastName: String?,

    val pinfl: String?,

    var hireDate: LocalDate?,

    @field:Positive(message = "{employee.organizationid.positive}")
    var organizationId: Long?
)

data class EmployeeShortResponse(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val pinfl: String,

    @JsonFormat(pattern = "yyyy.MM.dd", shape = JsonFormat.Shape.STRING)
    val hireDate: LocalDate,

    val organization: OrganizationShortResponse
)

data class EmployeeFullResponse(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val pinfl: String,

    @JsonFormat(pattern = "yyyy.MM.dd", shape = JsonFormat.Shape.STRING)
    val hireDate: LocalDate,

    val organization: OrganizationShortResponse,
    val calculationCount: Int = 0,

    @JsonFormat(pattern = "yyyy.MM.dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    val createdDate: LocalDateTime? = null,

    @JsonFormat(pattern = "yyyy.MM.dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    val modifiedDate: LocalDateTime? = null,

    val createdBy: String? = null,
    val lastModifiedBy: String? = null,
    val deleted: Boolean = false
)