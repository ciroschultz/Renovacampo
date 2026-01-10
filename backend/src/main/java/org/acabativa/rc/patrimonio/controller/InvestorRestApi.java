package org.acabativa.rc.patrimonio.controller;

import org.acabativa.rc.patrimonio.entity.Investor;
import org.acabativa.rc.patrimonio.service.InvestorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/investors")
public class InvestorRestApi {

    private final InvestorService investorService;

    @Autowired
    public InvestorRestApi(InvestorService investorService) {
        this.investorService = investorService;
    }

    @GetMapping
    public ResponseEntity<List<Investor>> getAllInvestors(@RequestParam(required = false) Boolean active) {
        List<Investor> investors;
        if (active != null && active) {
            investors = investorService.getActiveInvestors();
        } else {
            investors = investorService.getAllInvestors();
        }
        return ResponseEntity.ok(investors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Investor> getInvestorById(@PathVariable Long id) {
        return investorService.getInvestorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/taxId/{taxId}")
    public ResponseEntity<Investor> getInvestorByTaxId(@PathVariable String taxId) {
        return investorService.getInvestorByTaxId(taxId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createInvestor(@RequestBody Investor investor) {
        try {
            Investor savedInvestor = investorService.saveInvestor(investor);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedInvestor);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateInvestor(@PathVariable Long id, @RequestBody Investor investor) {
        try {
            Investor updatedInvestor = investorService.updateInvestor(id, investor);
            return ResponseEntity.ok(updatedInvestor);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvestor(@PathVariable Long id) {
        investorService.deleteInvestor(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateInvestor(@PathVariable Long id) {
        investorService.deactivateInvestor(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activateInvestor(@PathVariable Long id) {
        investorService.activateInvestor(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Investor>> searchInvestors(@RequestParam String name) {
        List<Investor> investors = investorService.searchInvestorsByName(name);
        return ResponseEntity.ok(investors);
    }

    @GetMapping("/location")
    public ResponseEntity<List<Investor>> getInvestorsByLocation(
            @RequestParam String city,
            @RequestParam String state) {
        List<Investor> investors = investorService.getInvestorsByCityAndState(city, state);
        return ResponseEntity.ok(investors);
    }

    @GetMapping("/with-funds")
    public ResponseEntity<List<Investor>> getInvestorsWithAvailableFunds() {
        List<Investor> investors = investorService.getInvestorsWithAvailableFunds();
        return ResponseEntity.ok(investors);
    }

    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getInvestorStatistics() {
        Map<String, Object> stats = Map.of(
            "totalActive", investorService.countActiveInvestors(),
            "totalAvailableFunds", investorService.getTotalAvailableFunds(),
            "totalInvestedFunds", investorService.getTotalInvestedFunds()
        );
        return ResponseEntity.ok(stats);
    }
}