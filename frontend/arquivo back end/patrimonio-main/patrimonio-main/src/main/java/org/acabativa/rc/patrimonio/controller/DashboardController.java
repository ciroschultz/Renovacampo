package org.acabativa.rc.patrimonio.controller;

import org.acabativa.rc.patrimonio.entity.Investor;
import org.acabativa.rc.patrimonio.entity.Project;
import org.acabativa.rc.patrimonio.entity.Property;
import org.acabativa.rc.patrimonio.entity.Enterprise;
import org.acabativa.rc.patrimonio.service.InvestorService;
import org.acabativa.rc.patrimonio.service.ProjectService;
import org.acabativa.rc.patrimonio.service.EnterpriseService;
import org.acabativa.rc.patrimonio.service.Notary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {

    private final Notary notary;
    private final ProjectService projectService;
    private final InvestorService investorService;
    private final EnterpriseService enterpriseService;

    @Autowired
    public DashboardController(Notary notary, ProjectService projectService, InvestorService investorService, EnterpriseService enterpriseService) {
        this.notary = notary;
        this.projectService = projectService;
        this.investorService = investorService;
        this.enterpriseService = enterpriseService;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        List<Property> properties = notary.getProperties();
        List<Project> projects = projectService.getAllProjects();
        List<Investor> investors = investorService.getActiveInvestors();
        List<Enterprise> enterprises = enterpriseService.getAllActiveEnterprises();
        
        // Enterprise statistics (priority - at the top)
        model.addAttribute("totalEnterprises", enterpriseService.getActiveEnterpriseCount());
        model.addAttribute("totalInvestmentRequired", enterpriseService.getTotalInvestmentRequired());
        model.addAttribute("totalInvestmentRaised", enterpriseService.getTotalInvestmentRaised());
        model.addAttribute("averageExpectedReturn", enterpriseService.getAverageExpectedReturn());
        
        // Property statistics
        model.addAttribute("totalProperties", properties.size());
        model.addAttribute("totalArea", properties.stream()
            .mapToInt(p -> p.getTotalArea() != null ? p.getTotalArea() : 0)
            .sum());
        model.addAttribute("availableArea", properties.stream()
            .mapToInt(p -> p.getAvailableArea() != null ? p.getAvailableArea() : 0)
            .sum());
        
        // Project statistics
        model.addAttribute("totalProjects", projects.size());
        model.addAttribute("totalInvestment", projects.stream()
            .filter(p -> p.getTotalInvestment() != null)
            .mapToDouble(p -> p.getTotalInvestment().doubleValue())
            .sum());
        model.addAttribute("activeProjects", projects.stream()
            .filter(p -> p.getStatus() == Project.Status.IN_PROGRESS || p.getStatus() == Project.Status.APPROVED)
            .count());
        
        // Investor statistics
        model.addAttribute("totalInvestors", investors.size());
        model.addAttribute("totalAvailableFunds", investorService.getTotalAvailableFunds());
        model.addAttribute("totalInvestedFunds", investorService.getTotalInvestedFunds());
        
        // Recent items
        model.addAttribute("recentEnterprises", enterprises.stream().limit(3).toList());
        model.addAttribute("recentProperties", properties.stream().limit(5).toList());
        model.addAttribute("recentProjects", projects.stream().limit(5).toList());
        model.addAttribute("recentInvestors", investors.stream().limit(5).toList());
        
        return "dashboard";
    }
}