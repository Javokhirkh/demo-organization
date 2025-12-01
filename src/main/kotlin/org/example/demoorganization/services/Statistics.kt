package org.example.demoorganization.services

import org.example.demoorganization.CalculationRepository
import org.example.demoorganization.CalculationType
import org.example.demoorganization.EmployeeRepository
import org.example.demoorganization.Organization
import org.example.demoorganization.OrganizationRepository
import org.example.demoorganization.dtos.EmployeeShortResponse
import org.example.demoorganization.dtos.OrganizationShortResponse
import org.example.demoorganization.dtos.RegionShortResponse
import org.example.demoorganization.dtos.RepsonseForWorkersWorksInDiffRegions
import org.example.demoorganization.dtos.ResponseForEmpoyeesWithSalaryAndVacations
import org.example.demoorganization.dtos.ResponseStatisticsOfOrganizationsInRegionDto
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.YearMonth

interface Statistics{
    fun generateReportTotalWorkRate(yearMonth: YearMonth,minWorkRate:Double):List<RepsonseForWorkersWorksInDiffRegions>
    fun generateReportWorkersWorksInDiffRegions(yearMonth: YearMonth):List<RepsonseForWorkersWorksInDiffRegions>
    fun generateReportResponseStatisticsOfOrganizationsInRegionDto(yearMonth: YearMonth,orgId:Long):List<ResponseStatisticsOfOrganizationsInRegionDto>
    fun generateReportResponseForEmpoyeesWithSalaryAndVacations(yearMonth: YearMonth):List<ResponseForEmpoyeesWithSalaryAndVacations>
}

@Service
class StatisticsImpl(
    private val calculationRepository: CalculationRepository,
    private val employeeRepository: EmployeeRepository,
    private val organizationRepository: OrganizationRepository
):Statistics{
    override fun generateReportTotalWorkRate(yearMonth: YearMonth, minWorkRate: Double): List<RepsonseForWorkersWorksInDiffRegions> {
        val year = yearMonth.year
        val month = yearMonth.monthValue

        val calculations = calculationRepository.findAllNotDeleted()
            .filter { calc ->
                calc.date.year == year && calc.date.monthValue == month
            }

        val employeeWorkRates = calculations.groupBy { it.employee.id }
        return employeeWorkRates
            .map { (employeeId, calcs) ->
                val employee = employeeRepository.findByIdAndDeletedFalse(employeeId!!)
                    ?: return@map null

                val totalWorkRate = calcs.sumOf { it.workRate.toDouble() }

                if (totalWorkRate >= minWorkRate) {
                    RepsonseForWorkersWorksInDiffRegions(
                        pinfl = employee.pinfl,
                        countOrganisations = calcs.map { it.organization.id }.distinct().size.toLong(),
                        totalSalary = calcs.sumOf { it.amount }
                    )
                } else {
                    null
                }
            }
            .filterNotNull()
    }

    override fun generateReportWorkersWorksInDiffRegions(yearMonth: YearMonth): List<RepsonseForWorkersWorksInDiffRegions> {
        val year = yearMonth.year
        val month = yearMonth.monthValue

        val calculations = calculationRepository.findAllNotDeleted()
            .filter { calc ->
                calc.date.year == year && calc.date.monthValue == month
            }

        val employeeCalcs = calculations.groupBy { it.employee.id }

        return employeeCalcs
            .map { (employeeId, calcs) ->
                val employee = employeeRepository.findByIdAndDeletedFalse(employeeId!!)
                    ?: return@map null

                RepsonseForWorkersWorksInDiffRegions(
                    pinfl = employee.pinfl,
                    countOrganisations = calcs.map { it.organization.id }.distinct().size.toLong(),
                    totalSalary = calcs.sumOf { it.amount }
                )
            }
            .filterNotNull()
    }

    override fun generateReportResponseStatisticsOfOrganizationsInRegionDto(yearMonth: YearMonth, orgId: Long): List<ResponseStatisticsOfOrganizationsInRegionDto> {
        val year = yearMonth.year
        val month = yearMonth.monthValue

        val mainOrg = organizationRepository.findByIdAndDeletedFalse(orgId)
            ?: return emptyList()

        val allOrganizations = mutableListOf(mainOrg)
        allOrganizations.addAll(getAllBranches(mainOrg))

        return allOrganizations
            .mapNotNull { org ->
                val calculations = calculationRepository.findAllNotDeleted()
                    .filter { calc ->
                        calc.organization.id == org.id &&
                                calc.date.year == year &&
                                calc.date.monthValue == month
                    }

                if (calculations.isEmpty()) {
                    return@mapNotNull null
                }

                val totalSalary = calculations.sumOf { it.amount }
                val avgSalary = if (calculations.isNotEmpty()) {
                    totalSalary.divide(
                        BigDecimal(calculations.size),
                        2,
                        java.math.RoundingMode.HALF_UP
                    )
                } else {
                    BigDecimal.ZERO
                }

                // Get unique employees
                val employees = calculations
                    .map { it.employee }
                    .distinctBy { it.id }
                    .map { emp ->
                        EmployeeShortResponse(
                            id = emp.id!!,
                            firstName = emp.firstName,
                            lastName = emp.lastName,
                            pinfl = emp.pinfl,
                            hireDate = emp.hireDate,
                            organization = OrganizationShortResponse(
                                id = org.id!!,
                                name = org.name,
                                region = RegionShortResponse(
                                    id = org.region.id!!,
                                    name = org.region.name
                                )
                            )
                        )
                    }

                ResponseStatisticsOfOrganizationsInRegionDto(
                    organization = OrganizationShortResponse(
                        id = org.id!!,
                        name = org.name,
                        region = RegionShortResponse(
                            id = org.region.id!!,
                            name = org.region.name
                        )
                    ),
                    avgSalary = avgSalary,
                    listEmployees = employees
                )
            }
    }

    override fun generateReportResponseForEmpoyeesWithSalaryAndVacations(yearMonth: YearMonth): List<ResponseForEmpoyeesWithSalaryAndVacations> {
        val year = yearMonth.year
        val month = yearMonth.monthValue

        val employees = employeeRepository.findAllNotDeleted()

        return employees
            .mapNotNull { employee ->
                val calculations = calculationRepository.findAllNotDeleted()
                    .filter { calc ->
                        calc.employee.id == employee.id &&
                                calc.date.year == year &&
                                calc.date.monthValue == month
                    }

                if (calculations.isEmpty()) {
                    return@mapNotNull null
                }

                val totalSalary = calculations
                    .filter { it.calculationType == CalculationType.SALARY }
                    .sumOf { it.amount }

                val totalVacation = calculations
                    .filter { it.calculationType == CalculationType.VACATION }
                    .sumOf { it.amount }

                ResponseForEmpoyeesWithSalaryAndVacations(
                    employee = EmployeeShortResponse(
                        id = employee.id!!,
                        firstName = employee.firstName,
                        lastName = employee.lastName,
                        pinfl = employee.pinfl,
                        hireDate = employee.hireDate,
                        organization = OrganizationShortResponse(
                            id = employee.organization.id!!,
                            name = employee.organization.name,
                            region = RegionShortResponse(
                                id = employee.organization.region.id!!,
                                name = employee.organization.region.name
                            )
                        )
                    ),
                    totalSalary = totalSalary,
                    totalVacations = totalVacation
                )
            }
    }

    private fun getAllBranches(org: Organization): List<Organization> {
        val branches = mutableListOf<Organization>()
        org.branches.forEach { branch ->
            branches.add(branch)
            branches.addAll(getAllBranches(branch))
        }
        return branches
    }
}