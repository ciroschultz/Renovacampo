package org.acabativa.rc.patrimonio.service;

import org.acabativa.rc.patrimonio.entity.Enterprise;
import org.acabativa.rc.patrimonio.entity.EnterpriseInvestor;
import org.acabativa.rc.patrimonio.entity.Enterprise.EnterpriseStatus;
import org.acabativa.rc.patrimonio.repository.EnterpriseRepository;
import org.acabativa.rc.patrimonio.repository.EnterpriseInvestorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EnterpriseService {

    private final EnterpriseRepository enterpriseRepository;
    private final EnterpriseInvestorRepository enterpriseInvestorRepository;

    @Autowired
    public EnterpriseService(EnterpriseRepository enterpriseRepository, 
                           EnterpriseInvestorRepository enterpriseInvestorRepository) {
        this.enterpriseRepository = enterpriseRepository;
        this.enterpriseInvestorRepository = enterpriseInvestorRepository;
    }

    public List<Enterprise> getAllActiveEnterprises() {
        return enterpriseRepository.findByActiveTrueOrderByCreateDateDesc();
    }

    public List<Enterprise> getAllEnterprises() {
        return enterpriseRepository.findAllByOrderByCreateDateDesc();
    }

    public Optional<Enterprise> getEnterpriseById(Long id) {
        return enterpriseRepository.findByIdAndActiveTrue(id);
    }

    public Enterprise saveEnterprise(Enterprise enterprise) {
        if (enterprise.getId() == null) {
            enterprise.setCreateDate(java.time.LocalDateTime.now());
        } else {
            enterprise.setUpdateDate(java.time.LocalDateTime.now());
        }
        return enterpriseRepository.save(enterprise);
    }

    public void deleteEnterprise(Long id) {
        // Primeiro, remover todos os investidores associados ao empreendimento
        List<EnterpriseInvestor> investors = enterpriseInvestorRepository.findByEnterpriseId(id);
        if (!investors.isEmpty()) {
            enterpriseInvestorRepository.deleteAll(investors);
        }

        // Depois, excluir o empreendimento fisicamente
        enterpriseRepository.deleteById(id);
    }

    // Metodo para soft delete (marcar como inativo) caso queira manter historico
    public void deactivateEnterprise(Long id) {
        enterpriseRepository.findById(id).ifPresent(enterprise -> {
            enterprise.setActive(false);
            enterprise.setUpdateDate(java.time.LocalDateTime.now());
            enterpriseRepository.save(enterprise);
        });
    }

    public List<Enterprise> getEnterprisesByStatus(EnterpriseStatus status) {
        return enterpriseRepository.findActiveByStatus(status);
    }

    public List<Enterprise> getEnterprisesByProperty(Long propertyId) {
        return enterpriseRepository.findByPropertyId(propertyId);
    }

    public List<Enterprise> getEnterprisesByProject(Long projectId) {
        return enterpriseRepository.findByProjectId(projectId);
    }

    public List<Enterprise> getOpenFundingEnterprises() {
        return enterpriseRepository.findActiveWithOpenFunding(LocalDate.now());
    }

    public List<Enterprise> getOverdueFundingEnterprises() {
        return enterpriseRepository.findOverdueFunding(LocalDate.now());
    }

    public List<Enterprise> searchEnterprises(String searchTerm) {
        return enterpriseRepository.searchByNameDescriptionOrProperty(searchTerm);
    }

    // Investment Management
    public EnterpriseInvestor addInvestorToEnterprise(Long enterpriseId, Long investorId, BigDecimal investmentAmount) {
        if (enterpriseInvestorRepository.existsByEnterpriseIdAndInvestorId(enterpriseId, investorId)) {
            throw new IllegalArgumentException("Investor already exists in this enterprise");
        }
        
        Optional<Enterprise> enterprise = getEnterpriseById(enterpriseId);
        if (enterprise.isEmpty()) {
            throw new IllegalArgumentException("Enterprise not found");
        }

        Enterprise ent = enterprise.get();
        BigDecimal newTotal = ent.getTotalInvestmentRaised().add(investmentAmount);
        if (newTotal.compareTo(ent.getTotalInvestmentRequired()) > 0) {
            throw new IllegalArgumentException("Investment amount exceeds required funding");
        }

        // Calculate shareholding percentage
        BigDecimal shareholdingPercentage = investmentAmount
            .divide(ent.getTotalInvestmentRequired(), 4, BigDecimal.ROUND_HALF_UP)
            .multiply(new BigDecimal("100"));

        EnterpriseInvestor enterpriseInvestor = new EnterpriseInvestor(
            enterpriseId, investorId, investmentAmount, shareholdingPercentage
        );
        
        EnterpriseInvestor saved = enterpriseInvestorRepository.save(enterpriseInvestor);
        
        // Update enterprise total raised
        ent.setTotalInvestmentRaised(newTotal);
        saveEnterprise(ent);
        
        return saved;
    }

    public void removeInvestorFromEnterprise(Long enterpriseId, Long investorId) {
        Optional<EnterpriseInvestor> enterpriseInvestor = 
            enterpriseInvestorRepository.findByEnterpriseIdAndInvestorId(enterpriseId, investorId);
        
        if (enterpriseInvestor.isPresent()) {
            EnterpriseInvestor ei = enterpriseInvestor.get();
            
            // Update enterprise total raised
            Optional<Enterprise> enterprise = getEnterpriseById(enterpriseId);
            if (enterprise.isPresent()) {
                Enterprise ent = enterprise.get();
                BigDecimal newTotal = ent.getTotalInvestmentRaised().subtract(ei.getInvestmentAmount());
                ent.setTotalInvestmentRaised(newTotal.max(BigDecimal.ZERO));
                saveEnterprise(ent);
            }
            
            enterpriseInvestorRepository.delete(ei);
        }
    }

    public List<EnterpriseInvestor> getEnterpriseInvestors(Long enterpriseId) {
        return enterpriseInvestorRepository.findByEnterpriseId(enterpriseId);
    }

    public List<EnterpriseInvestor> getInvestorEnterprises(Long investorId) {
        return enterpriseInvestorRepository.findByInvestorId(investorId);
    }

    // Statistics
    public BigDecimal getTotalInvestmentRequired() {
        return enterpriseRepository.getTotalInvestmentRequired();
    }

    public BigDecimal getTotalInvestmentRaised() {
        return enterpriseRepository.getTotalInvestmentRaised();
    }

    public long getActiveEnterpriseCount() {
        return enterpriseRepository.countActiveEnterprises();
    }

    public long getTotalEnterpriseCount() {
        return enterpriseRepository.count();
    }

    public long getEnterpriseCountByStatus(EnterpriseStatus status) {
        return enterpriseRepository.countByStatus(status);
    }

    public Double getAverageCommodityValueIncrease() {
        return enterpriseRepository.getAverageCommodityValueIncrease();
    }

    // Legacy method for backward compatibility
    public Double getAverageExpectedReturn() {
        return getAverageCommodityValueIncrease();
    }

    public List<Enterprise> getUnderfundedEnterprises() {
        return enterpriseRepository.findUnderfundedEnterprises();
    }

    public List<Enterprise> getEnterprisesCompletingBetween(LocalDate startDate, LocalDate endDate) {
        return enterpriseRepository.findByExpectedCompletionDateBetween(startDate, endDate);
    }

    // Business Logic Methods
    public boolean canAcceptInvestment(Long enterpriseId, BigDecimal amount) {
        Optional<Enterprise> enterprise = getEnterpriseById(enterpriseId);
        if (enterprise.isEmpty()) {
            return false;
        }
        
        Enterprise ent = enterprise.get();
        return ent.getStatus() == EnterpriseStatus.ACTIVE && 
               ent.getFundingDeadline().isAfter(LocalDate.now()) &&
               ent.getAvailableFundingAmount().compareTo(amount) >= 0;
    }

    public boolean isMinimumInvestmentMet(Long enterpriseId, BigDecimal amount) {
        Optional<Enterprise> enterprise = getEnterpriseById(enterpriseId);
        if (enterprise.isEmpty()) {
            return false;
        }
        
        Enterprise ent = enterprise.get();
        return ent.getMinimumInvestment() == null || 
               amount.compareTo(ent.getMinimumInvestment()) >= 0;
    }

    public void checkAndUpdateFundingStatus(Long enterpriseId) {
        Optional<Enterprise> enterprise = getEnterpriseById(enterpriseId);
        if (enterprise.isPresent()) {
            Enterprise ent = enterprise.get();
            if (ent.isFundingComplete() && ent.getStatus() == EnterpriseStatus.ACTIVE) {
                ent.setStatus(EnterpriseStatus.COMPLETED);
                saveEnterprise(ent);
            }
        }
    }
}