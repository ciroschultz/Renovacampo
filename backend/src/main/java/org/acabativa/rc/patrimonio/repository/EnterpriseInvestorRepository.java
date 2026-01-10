package org.acabativa.rc.patrimonio.repository;

import org.acabativa.rc.patrimonio.entity.EnterpriseInvestor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface EnterpriseInvestorRepository extends JpaRepository<EnterpriseInvestor, Long> {

    List<EnterpriseInvestor> findByEnterpriseId(Long enterpriseId);
    
    List<EnterpriseInvestor> findByInvestorId(Long investorId);
    
    Optional<EnterpriseInvestor> findByEnterpriseIdAndInvestorId(Long enterpriseId, Long investorId);
    
    boolean existsByEnterpriseIdAndInvestorId(Long enterpriseId, Long investorId);

    @Query("SELECT COALESCE(SUM(ei.investmentAmount), 0) FROM EnterpriseInvestor ei WHERE ei.enterpriseId = :enterpriseId")
    BigDecimal getTotalInvestmentByEnterprise(@Param("enterpriseId") Long enterpriseId);

    @Query("SELECT COALESCE(SUM(ei.investmentAmount), 0) FROM EnterpriseInvestor ei WHERE ei.investorId = :investorId")
    BigDecimal getTotalInvestmentByInvestor(@Param("investorId") Long investorId);

    @Query("SELECT COUNT(DISTINCT ei.investorId) FROM EnterpriseInvestor ei WHERE ei.enterpriseId = :enterpriseId")
    long countInvestorsByEnterprise(@Param("enterpriseId") Long enterpriseId);

    @Query("SELECT COUNT(DISTINCT ei.enterpriseId) FROM EnterpriseInvestor ei WHERE ei.investorId = :investorId")
    long countEnterprisesByInvestor(@Param("investorId") Long investorId);

    @Query("SELECT ei FROM EnterpriseInvestor ei ORDER BY ei.investmentDate DESC")
    List<EnterpriseInvestor> findAllOrderByInvestmentDateDesc();
}