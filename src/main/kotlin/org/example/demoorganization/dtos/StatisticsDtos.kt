package org.example.demoorganization.dtos

import com.fasterxml.jackson.annotation.JsonFormat
import java.math.BigDecimal
import java.time.YearMonth


data class ResponseWorkRateAndPINFlDto(
    var sumWorkRate: Double,
    var pinfl: String
)


data class RepsonseForWorkersWorksInDiffRegions(
    var pinfl: String,
    var countOrganisations:Long,
    var totalSalary: BigDecimal
)
data class ResponseStatisticsOfOrganizationsInRegionDto(
    var organization: OrganizationShortResponse,
    var avgSalary: BigDecimal,
    var listEmployees:List<EmployeeShortResponse>
)
data class ResponseForEmpoyeesWithSalaryAndVacations(
    var employee:EmployeeShortResponse,
    var totalSalary: BigDecimal,
    var totalVacations: BigDecimal
)