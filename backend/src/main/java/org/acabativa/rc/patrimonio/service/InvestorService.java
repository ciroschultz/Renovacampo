package org.acabativa.rc.patrimonio.service;

import org.acabativa.rc.patrimonio.entity.Investor;
import org.acabativa.rc.patrimonio.repository.InvestorRepository;
import org.acabativa.rc.patrimonio.repository.EnterpriseInvestorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class InvestorService {

    private final InvestorRepository investorRepository;
    private final EnterpriseInvestorRepository enterpriseInvestorRepository;

    @Autowired
    public InvestorService(InvestorRepository investorRepository, EnterpriseInvestorRepository enterpriseInvestorRepository) {
        this.investorRepository = investorRepository;
        this.enterpriseInvestorRepository = enterpriseInvestorRepository;
    }

    public List<Investor> getAllInvestors() {
        return investorRepository.findAll();
    }

    public List<Investor> getActiveInvestors() {
        return investorRepository.findByActiveTrue();
    }

    public Optional<Investor> getInvestorById(Long id) {
        return investorRepository.findById(id);
    }

    public Optional<Investor> getInvestorByTaxId(String taxId) {
        return investorRepository.findByTaxId(taxId);
    }

    public Investor saveInvestor(Investor investor) {
        if (investor.getId() == null && investorRepository.existsByTaxId(investor.getTaxId())) {
            throw new IllegalArgumentException("Investor with taxId " + investor.getTaxId() + " already exists");
        }
        
        if (investor.getTotalFunds() == null) {
            investor.setTotalFunds(BigDecimal.ZERO);
        }
        
        if (investor.getInvestedFunds() == null) {
            investor.setInvestedFunds(BigDecimal.ZERO);
        }
        
        if (investor.getInvestedFunds().compareTo(investor.getTotalFunds()) > 0) {
            throw new IllegalArgumentException("Invested funds cannot exceed total funds");
        }
        
        return investorRepository.save(investor);
    }

    public Investor updateInvestor(Long id, Investor updatedInvestor) {
        return investorRepository.findById(id)
                .map(investor -> {
                    // Check if taxId is being changed and if new taxId already exists
                    if (!investor.getTaxId().equals(updatedInvestor.getTaxId()) && 
                        investorRepository.existsByTaxId(updatedInvestor.getTaxId())) {
                        throw new IllegalArgumentException("Investor with taxId " + updatedInvestor.getTaxId() + " already exists");
                    }
                    
                    investor.setName(updatedInvestor.getName());
                    investor.setTaxId(updatedInvestor.getTaxId());
                    investor.setEmail(updatedInvestor.getEmail());
                    investor.setPhone(updatedInvestor.getPhone());
                    investor.setAddress(updatedInvestor.getAddress());
                    investor.setCity(updatedInvestor.getCity());
                    investor.setState(updatedInvestor.getState());
                    investor.setTotalFunds(updatedInvestor.getTotalFunds() != null ? updatedInvestor.getTotalFunds() : BigDecimal.ZERO);
                    investor.setInvestedFunds(updatedInvestor.getInvestedFunds() != null ? updatedInvestor.getInvestedFunds() : BigDecimal.ZERO);
                    investor.setDescription(updatedInvestor.getDescription());
                    investor.setActive(updatedInvestor.getActive() != null ? updatedInvestor.getActive() : true);
                    
                    if (investor.getInvestedFunds().compareTo(investor.getTotalFunds()) > 0) {
                        throw new IllegalArgumentException("Invested funds cannot exceed total funds");
                    }
                    
                    return investorRepository.save(investor);
                })
                .orElseThrow(() -> new IllegalArgumentException("Investor not found with id: " + id));
    }

    public void deleteInvestor(Long id) {
        // Check if investor has enterprise associations
        long enterpriseCount = enterpriseInvestorRepository.countEnterprisesByInvestor(id);
        if (enterpriseCount > 0) {
            throw new IllegalStateException("Não é possível excluir este investidor pois ele está associado a " + enterpriseCount + " empreendimento(s). Remova as associações primeiro.");
        }
        
        investorRepository.deleteById(id);
    }
    
    public boolean hasEnterpriseAssociations(Long investorId) {
        return enterpriseInvestorRepository.countEnterprisesByInvestor(investorId) > 0;
    }

    public void deactivateInvestor(Long id) {
        investorRepository.findById(id)
                .ifPresent(investor -> {
                    investor.setActive(false);
                    investorRepository.save(investor);
                });
    }

    public void activateInvestor(Long id) {
        investorRepository.findById(id)
                .ifPresent(investor -> {
                    investor.setActive(true);
                    investorRepository.save(investor);
                });
    }

    public List<Investor> searchInvestorsByName(String name) {
        return investorRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Investor> getInvestorsByCityAndState(String city, String state) {
        return investorRepository.findByCityAndStateAndActiveTrue(city, state);
    }

    public List<Investor> getInvestorsWithAvailableFunds() {
        return investorRepository.findInvestorsWithAvailableFunds();
    }

    public BigDecimal getTotalAvailableFunds() {
        return getActiveInvestors().stream()
                .map(Investor::getAvailableFunds)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalInvestedFunds() {
        return getActiveInvestors().stream()
                .map(Investor::getInvestedFunds)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public long countActiveInvestors() {
        return investorRepository.findByActiveTrue().size();
    }
}