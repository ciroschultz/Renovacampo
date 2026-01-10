package org.acabativa.rc.patrimonio.controller;

import org.acabativa.rc.patrimonio.entity.Enterprise;
import org.acabativa.rc.patrimonio.entity.EnterpriseInvestor;
import org.acabativa.rc.patrimonio.entity.Enterprise.EnterpriseStatus;
import org.acabativa.rc.patrimonio.service.EnterpriseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/enterprises")
public class EnterpriseRestApi {

    private final EnterpriseService enterpriseService;

    @Autowired
    public EnterpriseRestApi(EnterpriseService enterpriseService) {
        this.enterpriseService = enterpriseService;
    }

    @GetMapping
    public ResponseEntity<List<Enterprise>> getAllEnterprises(@RequestParam(required = false) String search) {
        List<Enterprise> enterprises;
        
        if (search != null && !search.trim().isEmpty()) {
            enterprises = enterpriseService.searchEnterprises(search.trim());
        } else {
            enterprises = enterpriseService.getAllActiveEnterprises();
        }
        
        return ResponseEntity.ok(enterprises);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Enterprise> getEnterpriseById(@PathVariable Long id) {
        Optional<Enterprise> enterprise = enterpriseService.getEnterpriseById(id);
        return enterprise.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Enterprise> createEnterprise(@RequestBody Enterprise enterprise) {
        try {
            Enterprise saved = enterpriseService.saveEnterprise(enterprise);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Enterprise> updateEnterprise(@PathVariable Long id, @RequestBody Enterprise enterprise) {
        Optional<Enterprise> existingEnterprise = enterpriseService.getEnterpriseById(id);
        if (existingEnterprise.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            enterprise.setId(id);
            Enterprise updated = enterpriseService.saveEnterprise(enterprise);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnterprise(@PathVariable Long id) {
        Optional<Enterprise> enterprise = enterpriseService.getEnterpriseById(id);
        if (enterprise.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            enterpriseService.deleteEnterprise(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Status-based endpoints
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Enterprise>> getEnterprisesByStatus(@PathVariable String status) {
        try {
            EnterpriseStatus enterpriseStatus = EnterpriseStatus.valueOf(status.toUpperCase());
            List<Enterprise> enterprises = enterpriseService.getEnterprisesByStatus(enterpriseStatus);
            return ResponseEntity.ok(enterprises);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/open-funding")
    public ResponseEntity<List<Enterprise>> getOpenFundingEnterprises() {
        List<Enterprise> enterprises = enterpriseService.getOpenFundingEnterprises();
        return ResponseEntity.ok(enterprises);
    }

    @GetMapping("/underfunded")
    public ResponseEntity<List<Enterprise>> getUnderfundedEnterprises() {
        List<Enterprise> enterprises = enterpriseService.getUnderfundedEnterprises();
        return ResponseEntity.ok(enterprises);
    }

    @GetMapping("/overdue-funding")
    public ResponseEntity<List<Enterprise>> getOverdueFundingEnterprises() {
        List<Enterprise> enterprises = enterpriseService.getOverdueFundingEnterprises();
        return ResponseEntity.ok(enterprises);
    }

    // Property and Project associations
    @GetMapping("/property/{propertyId}")
    public ResponseEntity<List<Enterprise>> getEnterprisesByProperty(@PathVariable Long propertyId) {
        List<Enterprise> enterprises = enterpriseService.getEnterprisesByProperty(propertyId);
        return ResponseEntity.ok(enterprises);
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Enterprise>> getEnterprisesByProject(@PathVariable Long projectId) {
        List<Enterprise> enterprises = enterpriseService.getEnterprisesByProject(projectId);
        return ResponseEntity.ok(enterprises);
    }

    // Investor Management
    @GetMapping("/{enterpriseId}/investors")
    public ResponseEntity<List<EnterpriseInvestor>> getEnterpriseInvestors(@PathVariable Long enterpriseId) {
        Optional<Enterprise> enterprise = enterpriseService.getEnterpriseById(enterpriseId);
        if (enterprise.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<EnterpriseInvestor> investors = enterpriseService.getEnterpriseInvestors(enterpriseId);
        return ResponseEntity.ok(investors);
    }

    @PostMapping("/{enterpriseId}/investors")
    public ResponseEntity<Map<String, Object>> addInvestorToEnterprise(
            @PathVariable Long enterpriseId,
            @RequestBody Map<String, Object> request) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            Long investorId = Long.valueOf(request.get("investorId").toString());
            BigDecimal investmentAmount = new BigDecimal(request.get("investmentAmount").toString());
            
            if (!enterpriseService.isMinimumInvestmentMet(enterpriseId, investmentAmount)) {
                response.put("success", false);
                response.put("message", "Investment amount below minimum required");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (!enterpriseService.canAcceptInvestment(enterpriseId, investmentAmount)) {
                response.put("success", false);
                response.put("message", "Enterprise cannot accept this investment");
                return ResponseEntity.badRequest().body(response);
            }
            
            EnterpriseInvestor enterpriseInvestor = enterpriseService.addInvestorToEnterprise(
                enterpriseId, investorId, investmentAmount);
            enterpriseService.checkAndUpdateFundingStatus(enterpriseId);
            
            response.put("success", true);
            response.put("message", "Investor added successfully");
            response.put("data", enterpriseInvestor);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error adding investor: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{enterpriseId}/investors/{investorId}")
    public ResponseEntity<Map<String, Object>> removeInvestorFromEnterprise(
            @PathVariable Long enterpriseId, 
            @PathVariable Long investorId) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            enterpriseService.removeInvestorFromEnterprise(enterpriseId, investorId);
            response.put("success", true);
            response.put("message", "Investor removed successfully");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error removing investor: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Statistics
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getEnterpriseStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalEnterprises", enterpriseService.getActiveEnterpriseCount());
        stats.put("totalInvestmentRequired", enterpriseService.getTotalInvestmentRequired());
        stats.put("totalInvestmentRaised", enterpriseService.getTotalInvestmentRaised());
        stats.put("averageExpectedReturn", enterpriseService.getAverageExpectedReturn());
        
        // Status counts
        stats.put("planningCount", enterpriseService.getEnterpriseCountByStatus(EnterpriseStatus.PLANNING));
        stats.put("activeCount", enterpriseService.getEnterpriseCountByStatus(EnterpriseStatus.ACTIVE));
        stats.put("completedCount", enterpriseService.getEnterpriseCountByStatus(EnterpriseStatus.COMPLETED));
        stats.put("suspendedCount", enterpriseService.getEnterpriseCountByStatus(EnterpriseStatus.SUSPENDED));
        stats.put("cancelledCount", enterpriseService.getEnterpriseCountByStatus(EnterpriseStatus.CANCELLED));
        
        return ResponseEntity.ok(stats);
    }

    // Timeline-based queries
    @GetMapping("/completing-between")
    public ResponseEntity<List<Enterprise>> getEnterprisesCompletingBetween(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            
            List<Enterprise> enterprises = enterpriseService.getEnterprisesCompletingBetween(start, end);
            return ResponseEntity.ok(enterprises);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Investment validation
    @GetMapping("/{enterpriseId}/can-accept-investment")
    public ResponseEntity<Map<String, Object>> canAcceptInvestment(
            @PathVariable Long enterpriseId,
            @RequestParam BigDecimal amount) {
        
        Map<String, Object> response = new HashMap<>();
        
        boolean canAccept = enterpriseService.canAcceptInvestment(enterpriseId, amount);
        boolean meetsMinimum = enterpriseService.isMinimumInvestmentMet(enterpriseId, amount);
        
        response.put("canAcceptInvestment", canAccept);
        response.put("meetsMinimumInvestment", meetsMinimum);
        response.put("isValid", canAccept && meetsMinimum);
        
        return ResponseEntity.ok(response);
    }
}