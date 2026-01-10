package org.acabativa.rc.patrimonio.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "enterprise_investor")
public class EnterpriseInvestor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "enterprise_id", nullable = false)
    private Long enterpriseId;

    @Column(name = "investor_id", nullable = false)
    private Long investorId;

    @Column(name = "investment_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal investmentAmount;

    @Column(name = "investment_date", nullable = false)
    private LocalDateTime investmentDate = LocalDateTime.now();

    @Column(name = "shareholding_percentage", precision = 5, scale = 2)
    private BigDecimal shareholdingPercentage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enterprise_id", insertable = false, updatable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Enterprise enterprise;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "investor_id", insertable = false, updatable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Investor investor;

    public EnterpriseInvestor() {}

    public EnterpriseInvestor(Long enterpriseId, Long investorId, BigDecimal investmentAmount) {
        this.enterpriseId = enterpriseId;
        this.investorId = investorId;
        this.investmentAmount = investmentAmount;
        this.investmentDate = LocalDateTime.now();
    }

    public EnterpriseInvestor(Long enterpriseId, Long investorId, BigDecimal investmentAmount, BigDecimal shareholdingPercentage) {
        this.enterpriseId = enterpriseId;
        this.investorId = investorId;
        this.investmentAmount = investmentAmount;
        this.shareholdingPercentage = shareholdingPercentage;
        this.investmentDate = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public Long getInvestorId() {
        return investorId;
    }

    public void setInvestorId(Long investorId) {
        this.investorId = investorId;
    }

    public BigDecimal getInvestmentAmount() {
        return investmentAmount;
    }

    public void setInvestmentAmount(BigDecimal investmentAmount) {
        this.investmentAmount = investmentAmount;
    }

    public LocalDateTime getInvestmentDate() {
        return investmentDate;
    }

    public void setInvestmentDate(LocalDateTime investmentDate) {
        this.investmentDate = investmentDate;
    }

    public BigDecimal getShareholdingPercentage() {
        return shareholdingPercentage;
    }

    public void setShareholdingPercentage(BigDecimal shareholdingPercentage) {
        this.shareholdingPercentage = shareholdingPercentage;
    }

    public Enterprise getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(Enterprise enterprise) {
        this.enterprise = enterprise;
    }

    public Investor getInvestor() {
        return investor;
    }

    public void setInvestor(Investor investor) {
        this.investor = investor;
    }
}