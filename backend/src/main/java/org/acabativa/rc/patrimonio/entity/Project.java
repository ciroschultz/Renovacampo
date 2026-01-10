package org.acabativa.rc.patrimonio.entity;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "project")
public class Project {

    @Id
    @SequenceGenerator(
        name = "project_sequence",
        sequenceName = "project_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "project_sequence"
    )
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(length = 100)
    private String category;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "start_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Column(name = "estimated_end_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate estimatedEndDate;

    @Column(name = "end_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Priority priority;

    @Column(name = "total_estimated_costs", precision = 15, scale = 2)
    private BigDecimal totalEstimatedCosts;

    @Column(name = "total_costs", precision = 15, scale = 2)
    private BigDecimal totalCosts;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Status status;

    @Column(name = "estimated_return_over_investment", precision = 5, scale = 2)
    private BigDecimal estimatedReturnOverInvestment;

    @Column(name = "total_investment", precision = 15, scale = 2)
    private BigDecimal totalInvestment;

    // Campo para armazenar dados extras do formulario em formato JSON
    @Column(columnDefinition = "TEXT")
    private String additionalData;

    public enum Priority {
        LOW, MEDIUM, HIGH, CRITICAL
    }

    public enum Status {
        PLANNING, APPROVED, IN_PROGRESS, ON_HOLD, COMPLETED, CANCELLED
    }

    public Project() {
        this.status = Status.PLANNING;
    }

    public Project(String name, String category, String description, LocalDate startDate, 
                   LocalDate estimatedEndDate, Priority priority, BigDecimal totalEstimatedCosts, 
                   BigDecimal estimatedReturnOverInvestment, BigDecimal totalInvestment) {
        this();
        this.name = name;
        this.category = category;
        this.description = description;
        this.startDate = startDate;
        this.estimatedEndDate = estimatedEndDate;
        this.priority = priority;
        this.totalEstimatedCosts = totalEstimatedCosts;
        this.estimatedReturnOverInvestment = estimatedReturnOverInvestment;
        this.totalInvestment = totalInvestment;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEstimatedEndDate() {
        return estimatedEndDate;
    }

    public void setEstimatedEndDate(LocalDate estimatedEndDate) {
        this.estimatedEndDate = estimatedEndDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public BigDecimal getTotalEstimatedCosts() {
        return totalEstimatedCosts;
    }

    public void setTotalEstimatedCosts(BigDecimal totalEstimatedCosts) {
        this.totalEstimatedCosts = totalEstimatedCosts;
    }

    public BigDecimal getTotalCosts() {
        return totalCosts;
    }

    public void setTotalCosts(BigDecimal totalCosts) {
        this.totalCosts = totalCosts;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public BigDecimal getEstimatedReturnOverInvestment() {
        return estimatedReturnOverInvestment;
    }

    public void setEstimatedReturnOverInvestment(BigDecimal estimatedReturnOverInvestment) {
        this.estimatedReturnOverInvestment = estimatedReturnOverInvestment;
    }

    public BigDecimal getTotalInvestment() {
        return totalInvestment;
    }

    public void setTotalInvestment(BigDecimal totalInvestment) {
        this.totalInvestment = totalInvestment;
    }

    public String getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(String additionalData) {
        this.additionalData = additionalData;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", status=" + status +
                ", priority=" + priority +
                '}';
    }
}