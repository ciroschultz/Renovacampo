package org.acabativa.rc.patrimonio.repository;

import org.acabativa.rc.patrimonio.entity.Investor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvestorRepository extends JpaRepository<Investor, Long> {
    
    Optional<Investor> findByTaxId(String taxId);
    
    List<Investor> findByActiveTrue();
    
    List<Investor> findByActiveFalse();
    
    List<Investor> findByNameContainingIgnoreCase(String name);
    
    List<Investor> findByCityAndStateAndActiveTrue(String city, String state);
    
    @Query("SELECT i FROM Investor i WHERE i.totalFunds > i.investedFunds AND i.active = true")
    List<Investor> findInvestorsWithAvailableFunds();
    
    @Query("SELECT i FROM Investor i ORDER BY i.totalFunds DESC")
    List<Investor> findAllOrderByTotalFundsDesc();
    
    boolean existsByTaxId(String taxId);
}