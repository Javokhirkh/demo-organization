package org.example.demoorganization

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaAuditing
@EnableJpaRepositories(repositoryBaseClass = BaseRepositoryImpl::class)
class DemoOrganizationApplication

fun main(args: Array<String>) {
    runApplication<DemoOrganizationApplication>(*args)
}
