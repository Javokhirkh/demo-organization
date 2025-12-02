package org.example.demoorganization

import jakarta.persistence.*
import jakarta.persistence.CascadeType
import jakarta.persistence.FetchType
import org.hibernate.annotations.ColumnDefault
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
    @CreatedDate @Temporal(TemporalType.TIMESTAMP) var createdDate: LocalDateTime? = null,
    @LastModifiedDate @Temporal(TemporalType.TIMESTAMP) var modifiedDate: LocalDateTime? = null,
    @CreatedBy var createdBy: String? = null,
    @LastModifiedBy var lastModifiedBy: String? = null,
    @Column(nullable = false) @ColumnDefault(value = "false") var deleted: Boolean = false,
)

@Entity
@Table(name = "region")
data class Region(
    @Column( nullable = false, length = 100, unique = true)
    var name: String,

    @OneToMany(
        mappedBy = "region",
        cascade = [CascadeType.PERSIST, CascadeType.MERGE],
        fetch = FetchType.LAZY,
        orphanRemoval = false
    )
    val organizations: MutableList<Organization> = mutableListOf()
) : BaseEntity()

@Entity
@Table(
    name = "organization"
)
data class Organization(
    @Column( nullable = false, length = 255)
    var name: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", nullable = false)
    var region: Region,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    var parent: Organization? = null,

    @OneToMany(
        mappedBy = "parent",
        cascade = [CascadeType.PERSIST, CascadeType.MERGE],
        fetch = FetchType.LAZY,
        orphanRemoval = false
    )
    val branches: MutableList<Organization> = mutableListOf(),
    @OneToMany(
        mappedBy = "organization",
        cascade = [CascadeType.PERSIST, CascadeType.MERGE],
        fetch = FetchType.LAZY,
        orphanRemoval = false
    )
    val employees: MutableList<Employee> = mutableListOf(),


    @OneToMany(
        mappedBy = "organization",
        cascade = [CascadeType.PERSIST, CascadeType.MERGE],
        fetch = FetchType.LAZY,
        orphanRemoval = false
    )
    val calculations: MutableList<CalculationTable> = mutableListOf()
) : BaseEntity()

@Entity
@Table(
    name = "employee"
)
data class Employee(
    @Column(name = "first_name", nullable = false, length = 100)
    var firstName: String,

    @Column(name = "last_name", nullable = false, length = 100)
    var lastName: String,

    @Column(nullable = false, length = 14)
    var pinfl: String,

    @Column(name = "hire_date", nullable = false)
    var hireDate: LocalDate,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    val organization: Organization,

    @OneToMany(
        mappedBy = "employee",
        cascade = [CascadeType.PERSIST, CascadeType.MERGE],
        fetch = FetchType.LAZY,
        orphanRemoval = false
    )
    val calculations: MutableList<CalculationTable> = mutableListOf()
) : BaseEntity()

@Entity
@Table(
    name = "calculation_table"
)
data class CalculationTable(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employe_id", nullable = false)
    var employee: Employee,

    @Column(nullable = false, precision = 12, scale = 2)
    var amount: BigDecimal,

    @Column(name = "work_rate", nullable = false)
    var workRate: Float,

    @Column( nullable = false)
    var date: LocalDate,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    var organization: Organization,

    @Enumerated(EnumType.STRING)
    @Column(name = "calculation_type", nullable = false, length = 20)
    var calculationType: CalculationType
) : BaseEntity()