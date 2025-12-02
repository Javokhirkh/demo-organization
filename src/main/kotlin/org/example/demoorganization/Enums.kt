package org.example.demoorganization

enum class CalculationType {
  SALARY, PENSION, AWARD, VACATION
}
enum class ErrorCode(val code: Int){
    REGION_NAME_ALREADY_EXISTS(100),
    REGION_NOT_FOUND(101),
    ORGANIZATION_NOT_FOUND(102),
    ORGANIZATION_NAME_ALREADY_EXISTS(103),
    EMPLOYEE_NOT_FOUND(105),
    CALCULATION_TABLE_NOT_FOUND(106),


}