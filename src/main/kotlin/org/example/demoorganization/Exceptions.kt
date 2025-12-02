package org.example.demoorganization

import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.data.mapping.PropertyReferenceException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.LocalDateTime
import java.util.Locale

@ControllerAdvice
class ExceptionHandler(
    private val errorMessageSource: ResourceBundleMessageSource,
    private val validationMessageSource: ResourceBundleMessageSource,
) {

    private fun currentLocale(): Locale =
        LocaleContextHolder.getLocale()

    private fun currentLanguageCode(): String =
        currentLocale().language


    @ExceptionHandler(PropertyReferenceException::class)
    fun handlePropertyReferenceException(ex: PropertyReferenceException): ResponseEntity<ErrorResponse> {
        val errorMessage = "Invalid sort property '${ex.propertyName}'. Available properties: ${ex.propertyName}"

        return ResponseEntity.badRequest().body(
            ErrorResponse(
                code = 400,
                message = errorMessage,
                timestamp = LocalDateTime.now()
            )
        )

    }
    data class ErrorResponse(
        val code: Int,
        val message: String,
        val timestamp: LocalDateTime
    )
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<ValidationErrorResponse> {
        val locale = currentLocale()
        val errors = mutableMapOf<String, MutableList<String>>()

        ex.bindingResult.fieldErrors.forEach { error ->
            val messageKey = error.defaultMessage?.removeSurrounding("{", "}") ?: error.field
            val msg = validationMessageSource.getMessage(messageKey, emptyArray(), error.defaultMessage!!, locale)
            if (msg != null) {
                errors.computeIfAbsent(error.field ?: "unknown") { mutableListOf() }.add(msg)
            }
        }

        ex.bindingResult.globalErrors.forEach { error ->
            val messageKey = error.defaultMessage?.removeSurrounding("{", "}") ?: error.objectName
            val msg = validationMessageSource.getMessage(messageKey, emptyArray(), error.defaultMessage!!, locale)
            if (msg != null) {
                errors.computeIfAbsent(error.objectName) { mutableListOf() }.add(msg)
            }
        }

        val validationMessage = errorMessageSource.getMessage(
            "VALIDATION_ERROR",
            emptyArray(),
            locale
        )

        return ResponseEntity
            .badRequest()
            .body(
                ValidationErrorResponse(
                    code = 400,
                    message = validationMessage,
                    language = currentLanguageCode(),
                    errors = errors
                )
            )
    }


    @ExceptionHandler(OrganizationAppException::class)
    fun handleShopAppExceptions(exception: OrganizationAppException): ResponseEntity<BaseMessage> {
        val locale = currentLocale()

        return ResponseEntity
            .badRequest()
            .body(
                BaseMessage(
                    code = exception.errorType().code,
                    message = errorMessageSource.getMessage(
                        exception.errorType().toString(),
                        exception.getErrorMessageArguments(),
                        locale
                    )
                )
            )
    }


    @ExceptionHandler(Throwable::class)
    fun handleOtherExceptions(exception: Throwable): ResponseEntity<BaseMessage> {
        exception.printStackTrace()

        return when (exception) {
            is OrganizationAppException -> handleShopAppExceptions(exception)
            else ->
                ResponseEntity
                    .badRequest()
                    .body(BaseMessage(100, "Iltimos support bilan bog'laning"))
        }
    }
}


sealed class OrganizationAppException(message: String? = null) : RuntimeException(message) {
    abstract fun errorType(): ErrorCode
    open fun getErrorMessageArguments(): Array<out Any> = emptyArray()

}

data class BaseMessage(
    val code: Int,
    val message: String,
)
data class ValidationErrorResponse(
    val code: Int,
    val message: String,
    val language: String,
    val errors: Map<String, List<String>>
)

class RegionNameAlreadyExistsException() : OrganizationAppException() {
    override fun errorType() = ErrorCode.REGION_NAME_ALREADY_EXISTS
}
class RegionNotFoundException() : OrganizationAppException() {
    override fun errorType() = ErrorCode.REGION_NOT_FOUND
}
class OrganizationNameAlreadyExistsException() : OrganizationAppException() {
    override fun errorType() = ErrorCode.ORGANIZATION_NAME_ALREADY_EXISTS
}
class OrganizationNotFoundException() : OrganizationAppException() {
    override fun errorType() = ErrorCode.ORGANIZATION_NOT_FOUND
}
class EmployeeNotFoundException() : OrganizationAppException() {
    override fun errorType() = ErrorCode.EMPLOYEE_NOT_FOUND
}
class CalculationTableNotFoundException() : OrganizationAppException() {
    override fun errorType() = ErrorCode.CALCULATION_TABLE_NOT_FOUND
}


