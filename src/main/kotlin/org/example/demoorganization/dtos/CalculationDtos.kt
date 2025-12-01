package org.example.demoorganization.dtos

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import org.example.demoorganization.CalculationType
import org.example.demoorganization.Employee
import org.example.demoorganization.Organization
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime


data class CreateCalculationRequest(
    @field:NotNull(message = "{calculation.employeeid.notnull}")
    @field:Positive(message = "{calculation.employeeid.positive}")
    var employeeId: Long,

    @field:NotNull(message = "{calculation.amount.notnull}")
    @field:DecimalMin(value = "0.0", inclusive = false, message = "{calculation.amount.decimalmin}")
    var amount: BigDecimal,

    @field:NotNull(message = "{calculation.rate.notnull}")
    @field:DecimalMin(value = "0.0", message = "{calculation.rate.decimalmin}")
    @field:DecimalMax(value = "2", message = "{calculation.rate.decimalmax}")
    var workRate: Float,

    @field:NotNull(message = "{calculation.date.notnull}")
    @JsonFormat(pattern = "yyyy.MM.dd", shape = JsonFormat.Shape.STRING)
    var date: LocalDate,

    @field:NotNull(message = "{calculation.calculationtype.notnull}")
    var calculationType: CalculationType,

    @field:NotNull(message = "{calculation.organizationid.notnull}")
    @field:Positive(message = "{calculation.organizationid.positive}")
    var organizationId: Long
)

data class UpdateCalculationRequest(
    @field:DecimalMin(value = "0.0", inclusive = false, message = "{calculation.amount.decimalmin}")
    var amount: BigDecimal?,

    @field:DecimalMin(value = "0.0", message = "{calculation.rate.decimalmin}")
    @field:DecimalMax(value = "2.0", message = "{calculation.rate.decimalmax}")
    var workRate: Float?,

    @JsonFormat(pattern = "yyyy.MM.dd", shape = JsonFormat.Shape.STRING)
    var date: LocalDate?,

    var calculationType: CalculationType?,
    var organizationId: Long?,
    var employeeId: Long?
)

data class CalculationShortResponse(
    val id: Long,
    val employee: EmployeeShortResponse,
    val amount: BigDecimal,
    val workRate: Float,

    @JsonFormat(pattern = "yyyy.MM.dd", shape = JsonFormat.Shape.STRING)
    val date: LocalDate,

    val calculationType: CalculationType,
    val organization: OrganizationShortResponse
)

data class CalculationFullResponse(
    val id: Long,
    val employee: EmployeeShortResponse,
    val organization: OrganizationShortResponse,
    val amount: BigDecimal,
    val workRate: Float,

    @JsonFormat(pattern = "yyyy.MM.dd", shape = JsonFormat.Shape.STRING)
    val date: LocalDate,

    val calculationType: CalculationType,

    @JsonFormat(pattern = "yyyy.MM.dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    val createdDate: LocalDateTime? = null,

    @JsonFormat(pattern = "yyyy.MM.dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    val modifiedDate: LocalDateTime? = null,

    val createdBy: String? = null,
    val lastModifiedBy: String? = null,
    val deleted: Boolean = false
)