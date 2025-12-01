package org.example.demoorganization


import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import org.springframework.web.servlet.AsyncHandlerInterceptor
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.i18n.SessionLocaleResolver
import org.springframework.web.servlet.support.RequestContextUtils
import java.util.Locale

@Configuration
class MessageSourceConfig {

    @Bean
    fun errorMessageSource(): ResourceBundleMessageSource {
        return ResourceBundleMessageSource().apply {
            setBasename("error")
            setDefaultEncoding("UTF-8")
            setCacheSeconds(3600)
        }
    }

    @Bean
    fun validationMessageSource(): ResourceBundleMessageSource {
        return ResourceBundleMessageSource().apply {
            setBasename("validation")
            setDefaultEncoding("UTF-8")
            setCacheSeconds(3600)
        }
    }

    @Bean
    fun validatorFactory(validationMessageSource: ResourceBundleMessageSource)
            = LocalValidatorFactoryBean().apply {
        setValidationMessageSource(validationMessageSource)
    }
}

@Configuration
class WebMvcConfig(
    private val validatorFactory: LocalValidatorFactoryBean
) : WebMvcConfigurer {

    @Bean
    fun localeResolver() = SessionLocaleResolver().apply {
        setDefaultLocale(Locale("uz"))
    }

    override fun getValidator(): org.springframework.validation.Validator? {
        return validatorFactory  // <---- THIS FIXES THE PROBLEM
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(object : AsyncHandlerInterceptor {
            override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {

                val lang = request.getHeader("hl") ?: request.getParameter("hl")
                if (!lang.isNullOrBlank()) {
                    val locale = when (lang.lowercase()) {
                        "uz", "en", "ru" -> Locale(lang)
                        else -> Locale("uz")
                    }

                    RequestContextUtils
                        .getLocaleResolver(request)
                        ?.setLocale(request, response, locale)
                }

                return true
            }
        })
    }
}
@Configuration
class SwaggerConfig {
    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("SimpleShop API")
                    .version("1.0.0")
                    .description("API documentation for SimpleShop e-commerce application")
                    .license(
                        License()
                            .name("Apache 2.0")
                            .url("https://www.apache.org/licenses/LICENSE-2.0.html")
                    )
            )

    }
}