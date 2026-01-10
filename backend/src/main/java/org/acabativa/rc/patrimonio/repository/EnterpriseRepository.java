package org.acabativa.rc.patrimonio.repository;

import org.acabativa.rc.patrimonio.entity.Enterprise;
import org.acabativa.rc.patrimonio.entity.Enterprise.EnterpriseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EnterpriseRepository extends JpaRepository<Enterprise, Long> {

    List<Enterprise> findByActiveTrue();
    
    List<Enterprise> findByActiveTrueOrderByCreateDateDesc();
    
    List<Enterprise> findAllByOrderByCreateDateDesc();
    
    List<Enterprise> findByStatus(EnterpriseStatus status);
    
    List<Enterprise> findByActiveTrueAndStatus(EnterpriseStatus status);
    
    List<Enterprise> findByPropertyId(Long propertyId);
    
    List<Enterprise> findByProjectId(Long projectId);
    
    Optional<Enterprise> findByIdAndActiveTrue(Long id);

    @Query("SELECT e FROM Enterprise e WHERE e.active = true AND e.status = :status ORDER BY e.createDate DESC")
    List<Enterprise> findActiveByStatus(@Param("status") EnterpriseStatus status);

    @Query("SELECT e FROM Enterprise e WHERE e.active = true AND e.fundingDeadline >= :currentDate ORDER BY e.fundingDeadline ASC")
    List<Enterprise> findActiveWithOpenFunding(@Param("currentDate") LocalDate currentDate);

    @Query("SELECT e FROM Enterprise e WHERE e.active = true AND e.fundingDeadline < :currentDate AND e.status != 'COMPLETED' AND e.status != 'CANCELLED'")
    List<Enterprise> findOverdueFunding(@Param("currentDate") LocalDate currentDate);

    @Query("SELECT COALESCE(SUM(e.totalInvestmentRequired), 0) FROM Enterprise e WHERE e.active = true")
    BigDecimal getTotalInvestmentRequired();

    @Query("SELECT COALESCE(SUM(e.totalInvestmentRaised), 0) FROM Enterprise e WHERE e.active = true")
    BigDecimal getTotalInvestmentRaised();

    @Query("SELECT COUNT(e) FROM Enterprise e WHERE e.active = true")
    long countActiveEnterprises();

    @Query("SELECT COUNT(e) FROM Enterprise e WHERE e.active = true AND e.status = :status")
    long countByStatus(@Param("status") EnterpriseStatus status);

    @Query("SELECT e FROM Enterprise e WHERE e.active = true AND " +
           "(LOWER(e.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(e.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<Enterprise> searchByNameOrDescription(@Param("searchTerm") String searchTerm);

    @Query("SELECT e FROM Enterprise e " +
           "JOIN e.property p " +
           "WHERE e.active = true AND " +
           "(LOWER(e.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(e.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<Enterprise> searchByNameDescriptionOrProperty(@Param("searchTerm") String searchTerm);

    @Query("SELECT e FROM Enterprise e WHERE e.active = true AND " +
           "e.totalInvestmentRaised < e.totalInvestmentRequired ORDER BY e.fundingDeadline ASC")
    List<Enterprise> findUnderfundedEnterprises();

    @Query("SELECT AVG(e.expectedCommodityValueIncrease) FROM Enterprise e WHERE e.active = true AND e.expectedCommodityValueIncrease IS NOT NULL")
    Double getAverageCommodityValueIncrease();

    @Query("SELECT e FROM Enterprise e WHERE e.active = true AND " +
           "e.expectedCompletionDate BETWEEN :startDate AND :endDate ORDER BY e.expectedCompletionDate ASC")
    List<Enterprise> findByExpectedCompletionDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}