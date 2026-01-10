package org.acabativa.rc.patrimonio.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "enterprise")
public class Enterprise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnterpriseStatus status = EnterpriseStatus.PLANNING;

    @Column(name = "property_id", nullable = false)
    private Long propertyId;

    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @Column(name = "total_investment_required", precision = 15, scale = 2)
    private BigDecimal totalInvestmentRequired;

    @Column(name = "total_investment_raised", precision = 15, scale = 2)
    private BigDecimal totalInvestmentRaised = BigDecimal.ZERO;

    @Column(name = "expected_return_percentage", precision = 5, scale = 2)
    private BigDecimal expectedCommodityValueIncrease;

    @Column(name = "minimum_investment", precision = 15, scale = 2)
    private BigDecimal minimumInvestment;

    @Column(name = "launch_date")
    private LocalDate launchDate;

    @Column(name = "funding_deadline")
    private LocalDate fundingDeadline;

    @Column(name = "expected_completion_date")
    private LocalDate expectedCompletionDate;

    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate = LocalDateTime.now();

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @Column(nullable = false)
    private Boolean active = true;

    @OneToMany(mappedBy = "enterprise", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Set<EnterpriseInvestor> enterpriseInvestors = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", insertable = false, updatable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Property property;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", insertable = false, updatable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Project project;

    public enum EnterpriseStatus {
        PLANNING,
        ACTIVE,
        COMPLETED,
        SUSPENDED,
        CANCELLED
    }

    public Enterprise() {}

    public Enterprise(String name, String description, Long propertyId, Long projectId) {
        this.name = name;
        this.description = description;
        this.propertyId = propertyId;
        this.projectId = projectId;
        this.createDate = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updateDate = LocalDateTime.now();
    }

    public BigDecimal getAvailableFundingAmount() {
        if (totalInvestmentRequired == null || totalInvestmentRaised == null) {
            return BigDecimal.ZERO;
        }
        return totalInvestmentRequired.subtract(totalInvestmentRaised);
    }

    public BigDecimal getFundingProgress() {
        if (totalInvestmentRequired == null || totalInvestmentRequired.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        if (totalInvestmentRaised == null) {
            return BigDecimal.ZERO;
        }
        return totalInvestmentRaised.divide(totalInvestmentRequired, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100"));
    }

    public boolean isFundingComplete() {
        return getAvailableFundingAmount().compareTo(BigDecimal.ZERO) <= 0;
    }

    public int getInvestorCount() {
        return enterpriseInvestors != null ? enterpriseInvestors.size() : 0;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EnterpriseStatus getStatus() {
        return status;
    }

    public void setStatus(EnterpriseStatus status) {
        this.status = status;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public BigDecimal getTotalInvestmentRequired() {
        return totalInvestmentRequired;
    }

    public void setTotalInvestmentRequired(BigDecimal totalInvestmentRequired) {
        this.totalInvestmentRequired = totalInvestmentRequired;
    }

    public BigDecimal getTotalInvestmentRaised() {
        return totalInvestmentRaised;
    }

    public void setTotalInvestmentRaised(BigDecimal totalInvestmentRaised) {
        this.totalInvestmentRaised = totalInvestmentRaised;
    }

    public BigDecimal getExpectedCommodityValueIncrease() {
        return expectedCommodityValueIncrease;
    }

    public void setExpectedCommodityValueIncrease(BigDecimal expectedCommodityValueIncrease) {
        this.expectedCommodityValueIncrease = expectedCommodityValueIncrease;
    }

    // Legacy getter for backward compatibility with templates
    public BigDecimal getExpectedReturnPercentage() {
        return expectedCommodityValueIncrease;
    }

    public void setExpectedReturnPercentage(BigDecimal expectedReturnPercentage) {
        this.expectedCommodityValueIncrease = expectedReturnPercentage;
    }

    public BigDecimal getMinimumInvestment() {
        return minimumInvestment;
    }

    public void setMinimumInvestment(BigDecimal minimumInvestment) {
        this.minimumInvestment = minimumInvestment;
    }

    public LocalDate getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(LocalDate launchDate) {
        this.launchDate = launchDate;
    }

    public LocalDate getFundingDeadline() {
        return fundingDeadline;
    }

    public void setFundingDeadline(LocalDate fundingDeadline) {
        this.fundingDeadline = fundingDeadline;
    }

    public LocalDate getExpectedCompletionDate() {
        return expectedCompletionDate;
    }

    public void setExpectedCompletionDate(LocalDate expectedCompletionDate) {
        this.expectedCompletionDate = expectedCompletionDate;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<EnterpriseInvestor> getEnterpriseInvestors() {
        return enterpriseInvestors;
    }

    public void setEnterpriseInvestors(Set<EnterpriseInvestor> enterpriseInvestors) {
        this.enterpriseInvestors = enterpriseInvestors;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}