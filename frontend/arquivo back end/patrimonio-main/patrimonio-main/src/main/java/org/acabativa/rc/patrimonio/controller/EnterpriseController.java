package org.acabativa.rc.patrimonio.controller;

import org.acabativa.rc.patrimonio.entity.Enterprise;
import org.acabativa.rc.patrimonio.entity.EnterpriseInvestor;
import org.acabativa.rc.patrimonio.entity.Enterprise.EnterpriseStatus;
import org.acabativa.rc.patrimonio.entity.Property;
import org.acabativa.rc.patrimonio.entity.Project;
import org.acabativa.rc.patrimonio.entity.Investor;
import org.acabativa.rc.patrimonio.service.EnterpriseService;
import org.acabativa.rc.patrimonio.service.ProjectService;
import org.acabativa.rc.patrimonio.service.InvestorService;
import org.acabativa.rc.patrimonio.repository.PropertyDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/enterprises")
public class EnterpriseController {

    private final EnterpriseService enterpriseService;
    private final ProjectService projectService;
    private final InvestorService investorService;
    private final PropertyDAO propertyDAO;

    @Autowired
    public EnterpriseController(EnterpriseService enterpriseService,
                              ProjectService projectService,
                              InvestorService investorService,
                              PropertyDAO propertyDAO) {
        this.enterpriseService = enterpriseService;
        this.projectService = projectService;
        this.investorService = investorService;
        this.propertyDAO = propertyDAO;
    }

    @GetMapping
    public String listEnterprises(Model model) {
        List<Enterprise> enterprises = enterpriseService.getAllEnterprises();
        
        // Load property and project data for each enterprise
        for (Enterprise enterprise : enterprises) {
            if (enterprise.getPropertyId() != null) {
                propertyDAO.findById(enterprise.getPropertyId()).ifPresent(enterprise::setProperty);
            }
            if (enterprise.getProjectId() != null) {
                projectService.getProjectById(enterprise.getProjectId()).ifPresent(enterprise::setProject);
            }
        }
        
        model.addAttribute("enterprises", enterprises);
        
        // Statistics
        model.addAttribute("totalEnterprises", enterpriseService.getTotalEnterpriseCount());
        model.addAttribute("totalInvestmentRequired", enterpriseService.getTotalInvestmentRequired() != null ? 
            enterpriseService.getTotalInvestmentRequired() : BigDecimal.ZERO);
        model.addAttribute("totalInvestmentRaised", enterpriseService.getTotalInvestmentRaised() != null ? 
            enterpriseService.getTotalInvestmentRaised() : BigDecimal.ZERO);
        model.addAttribute("averageExpectedReturn", enterpriseService.getAverageExpectedReturn());
        
        // Status counts
        model.addAttribute("planningCount", enterpriseService.getEnterpriseCountByStatus(EnterpriseStatus.PLANNING));
        model.addAttribute("activeEnterprises", enterpriseService.getEnterpriseCountByStatus(EnterpriseStatus.ACTIVE));
        model.addAttribute("completedCount", enterpriseService.getEnterpriseCountByStatus(EnterpriseStatus.COMPLETED));
        
        return "enterprises/index";
    }

    @GetMapping("/{id}")
    public String viewEnterprise(@PathVariable Long id, Model model) {
        Optional<Enterprise> enterprise = enterpriseService.getEnterpriseById(id);
        if (enterprise.isEmpty()) {
            return "redirect:/enterprises";
        }
        
        Enterprise ent = enterprise.get();
        
        // Load property and project data
        if (ent.getPropertyId() != null) {
            propertyDAO.findById(ent.getPropertyId()).ifPresent(ent::setProperty);
        }
        if (ent.getProjectId() != null) {
            projectService.getProjectById(ent.getProjectId()).ifPresent(ent::setProject);
        }
        
        model.addAttribute("enterprise", ent);
        
        // Get investors for this enterprise
        List<EnterpriseInvestor> enterpriseInvestors = enterpriseService.getEnterpriseInvestors(id);
        
        // Load investor data for each EnterpriseInvestor
        for (EnterpriseInvestor ei : enterpriseInvestors) {
            investorService.getInvestorById(ei.getInvestorId()).ifPresent(ei::setInvestor);
        }
        
        model.addAttribute("enterpriseInvestors", enterpriseInvestors);
        
        // Get available investors for adding new ones
        try {
            List<Investor> availableInvestors = investorService.getActiveInvestors();
            model.addAttribute("availableInvestors", availableInvestors);
        } catch (Exception e) {
            // If investor service fails, provide empty list
            model.addAttribute("availableInvestors", new java.util.ArrayList<>());
        }
        
        return "enterprises/view";
    }

    @GetMapping("/new")
    public String newEnterpriseForm(Model model) {
        model.addAttribute("enterprise", new Enterprise());
        model.addAttribute("properties", propertyDAO.findActiveProperties());
        model.addAttribute("projects", projectService.getActiveProjects());
        model.addAttribute("statuses", EnterpriseStatus.values());
        return "enterprises/form";
    }

    @GetMapping("/{id}/edit")
    public String editEnterpriseForm(@PathVariable Long id, Model model) {
        Optional<Enterprise> enterprise = enterpriseService.getEnterpriseById(id);
        if (enterprise.isEmpty()) {
            return "redirect:/enterprises";
        }
        
        model.addAttribute("enterprise", enterprise.get());
        model.addAttribute("properties", propertyDAO.findActiveProperties());
        model.addAttribute("projects", projectService.getActiveProjects());
        model.addAttribute("statuses", EnterpriseStatus.values());
        return "enterprises/form";
    }

    @PostMapping
    public String saveEnterprise(@ModelAttribute Enterprise enterprise, RedirectAttributes redirectAttributes) {
        try {
            Enterprise saved = enterpriseService.saveEnterprise(enterprise);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Empreendimento " + (enterprise.getId() == null ? "criado" : "atualizado") + " com sucesso!");
            return "redirect:/enterprises/" + saved.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao salvar empreendimento: " + e.getMessage());
            return "redirect:/enterprises/new";
        }
    }

    @PostMapping("/{id}")
    public String updateEnterprise(@PathVariable Long id, @ModelAttribute Enterprise enterprise, RedirectAttributes redirectAttributes) {
        try {
            enterprise.setId(id);
            Enterprise saved = enterpriseService.saveEnterprise(enterprise);
            redirectAttributes.addFlashAttribute("successMessage", "Empreendimento atualizado com sucesso!");
            return "redirect:/enterprises/" + saved.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao atualizar empreendimento: " + e.getMessage());
            return "redirect:/enterprises/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteEnterprise(@PathVariable Long id, Model model, 
                                 @RequestHeader(value = "HX-Request", required = false) String hxRequest) {
        try {
            enterpriseService.deleteEnterprise(id);
            
            // If this is an HTMX request, return just the table fragment
            if ("true".equals(hxRequest)) {
                // Reload enterprise list for the table
                List<Enterprise> enterprises = enterpriseService.getAllActiveEnterprises();
                
                // Load property and project data for each enterprise
                for (Enterprise enterprise : enterprises) {
                    if (enterprise.getPropertyId() != null) {
                        propertyDAO.findById(enterprise.getPropertyId()).ifPresent(enterprise::setProperty);
                    }
                    if (enterprise.getProjectId() != null) {
                        projectService.getProjectById(enterprise.getProjectId()).ifPresent(enterprise::setProject);
                    }
                }
                
                model.addAttribute("enterprises", enterprises);
                
                // Statistics
                model.addAttribute("totalEnterprises", enterpriseService.getActiveEnterpriseCount());
                model.addAttribute("totalInvestmentRequired", enterpriseService.getTotalInvestmentRequired() != null ? 
                    enterpriseService.getTotalInvestmentRequired() : BigDecimal.ZERO);
                model.addAttribute("totalInvestmentRaised", enterpriseService.getTotalInvestmentRaised() != null ? 
                    enterpriseService.getTotalInvestmentRaised() : BigDecimal.ZERO);
                model.addAttribute("activeEnterprises", enterpriseService.getEnterpriseCountByStatus(EnterpriseStatus.ACTIVE));
                
                return "enterprises/fragments/table :: table";
            }
            
            // For regular requests, redirect normally
            return "redirect:/enterprises";
        } catch (Exception e) {
            // For HTMX requests, still return the table with error handling
            if ("true".equals(hxRequest)) {
                model.addAttribute("errorMessage", "Erro ao remover empreendimento: " + e.getMessage());
                List<Enterprise> enterprises = enterpriseService.getAllActiveEnterprises();
                model.addAttribute("enterprises", enterprises);
                return "enterprises/fragments/table :: table";
            }
            return "redirect:/enterprises";
        }
    }

    // Investor Management
    @PostMapping("/{enterpriseId}/investors")
    public String addInvestorToEnterprise(
            @PathVariable Long enterpriseId,
            @RequestParam Long investorId,
            @RequestParam BigDecimal investmentAmount,
            RedirectAttributes redirectAttributes) {
        try {
            if (!enterpriseService.isMinimumInvestmentMet(enterpriseId, investmentAmount)) {
                redirectAttributes.addFlashAttribute("errorMessage", "Valor de investimento abaixo do mínimo exigido!");
                return "redirect:/enterprises/" + enterpriseId;
            }
            
            if (!enterpriseService.canAcceptInvestment(enterpriseId, investmentAmount)) {
                redirectAttributes.addFlashAttribute("errorMessage", "Empreendimento não pode aceitar este investimento!");
                return "redirect:/enterprises/" + enterpriseId;
            }
            
            enterpriseService.addInvestorToEnterprise(enterpriseId, investorId, investmentAmount);
            enterpriseService.checkAndUpdateFundingStatus(enterpriseId);
            
            redirectAttributes.addFlashAttribute("successMessage", "Investidor adicionado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao adicionar investidor: " + e.getMessage());
        }
        return "redirect:/enterprises/" + enterpriseId;
    }

    @PostMapping("/{enterpriseId}/investors/{investorId}/remove")
    public String removeInvestorFromEnterprise(
            @PathVariable Long enterpriseId,
            @PathVariable Long investorId,
            RedirectAttributes redirectAttributes) {
        try {
            enterpriseService.removeInvestorFromEnterprise(enterpriseId, investorId);
            redirectAttributes.addFlashAttribute("successMessage", "Investidor removido com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao remover investidor: " + e.getMessage());
        }
        return "redirect:/enterprises/" + enterpriseId;
    }

    // Filter views
    @GetMapping("/status/{status}")
    public String listEnterprisesByStatus(@PathVariable String status, Model model) {
        try {
            EnterpriseStatus enterpriseStatus = EnterpriseStatus.valueOf(status.toUpperCase());
            List<Enterprise> enterprises = enterpriseService.getEnterprisesByStatus(enterpriseStatus);
            model.addAttribute("enterprises", enterprises);
            model.addAttribute("statusFilter", enterpriseStatus);
            model.addAttribute("totalEnterprises", enterprises.size());
            return "enterprises/index";
        } catch (IllegalArgumentException e) {
            return "redirect:/enterprises";
        }
    }

    @GetMapping("/open-funding")
    public String listOpenFundingEnterprises(Model model) {
        List<Enterprise> enterprises = enterpriseService.getOpenFundingEnterprises();
        model.addAttribute("enterprises", enterprises);
        model.addAttribute("pageTitle", "Empreendimentos com Captação Aberta");
        model.addAttribute("totalEnterprises", enterprises.size());
        return "enterprises/index";
    }

    @GetMapping("/underfunded")
    public String listUnderfundedEnterprises(Model model) {
        List<Enterprise> enterprises = enterpriseService.getUnderfundedEnterprises();
        model.addAttribute("enterprises", enterprises);
        model.addAttribute("pageTitle", "Empreendimentos Sub-capitalizados");
        model.addAttribute("totalEnterprises", enterprises.size());
        return "enterprises/index";
    }
}