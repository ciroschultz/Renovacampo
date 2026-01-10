package org.acabativa.rc.patrimonio.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Investor {

    public Investor() {
    }

    public Investor(String name, String taxId, String email, String phone, String address, 
            String city, String state, BigDecimal totalFunds, BigDecimal investedFunds, 
            String description, Boolean active) {
        this.name = name;
        this.taxId = taxId;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.state = state;
        this.totalFunds = totalFunds;
        this.investedFunds = investedFunds;
        this.description = description;
        this.active = active;
        this.createDate = LocalDateTime.now();
        this.updateDate = LocalDateTime.now();
    }

    @Id
    @SequenceGenerator(
        name = "investor_sequence",
        sequenceName = "investor_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "investor_sequence"
    )
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String taxId;

    @Column(nullable = false)
    private String email;

    private String phone;
    private String address;
    private String city;
    private String state;

    @Column(precision = 15, scale = 2)
    private BigDecimal totalFunds;

    @Column(precision = 15, scale = 2)
    private BigDecimal investedFunds;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private LocalDateTime createDate;

    @Column(nullable = false)
    private LocalDateTime updateDate;

    @Column(nullable = false)
    private Boolean active = true;

    // Campo para armazenar dados extras do formulario em formato JSON
    @Column(columnDefinition = "TEXT")
    private String additionalData;

    @PrePersist
    protected void onCreate() {
        createDate = LocalDateTime.now();
        updateDate = LocalDateTime.now();
        if (active == null) {
            active = true;
        }
        if (investedFunds == null) {
            investedFunds = BigDecimal.ZERO;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updateDate = LocalDateTime.now();
    }

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

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public BigDecimal getTotalFunds() {
        return totalFunds;
    }

    public void setTotalFunds(BigDecimal totalFunds) {
        this.totalFunds = totalFunds;
    }

    public BigDecimal getInvestedFunds() {
        return investedFunds;
    }

    public void setInvestedFunds(BigDecimal investedFunds) {
        this.investedFunds = investedFunds;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(String additionalData) {
        this.additionalData = additionalData;
    }

    @Transient
    public BigDecimal getAvailableFunds() {
        if (totalFunds != null && investedFunds != null) {
            return totalFunds.subtract(investedFunds);
        }
        return BigDecimal.ZERO;
    }
}