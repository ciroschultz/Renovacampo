package org.acabativa.rc.patrimonio.controller;

import org.acabativa.rc.patrimonio.entity.Investor;
import org.acabativa.rc.patrimonio.service.InvestorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/investors")
public class InvestorController {

    private final InvestorService investorService;

    @Autowired
    public InvestorController(InvestorService investorService) {
        this.investorService = investorService;
    }

    @GetMapping
    public String listInvestors(Model model) {
        List<Investor> investors = investorService.getAllInvestors();
        model.addAttribute("investors", investors);
        
        // Statistics
        model.addAttribute("totalInvestors", investors.size());
        model.addAttribute("totalActiveInvestors", (int) investors.stream().filter(Investor::getActive).count());
        model.addAttribute("totalInactiveInvestors", (int) investors.stream().filter(i -> !i.getActive()).count());
        model.addAttribute("totalAvailableFunds", investorService.getTotalAvailableFunds());
        model.addAttribute("totalInvestedFunds", investorService.getTotalInvestedFunds());
        
        return "investors/index";
    }

    @GetMapping("/new")
    public String newInvestorForm(Model model) {
        model.addAttribute("investor", new Investor());
        return "investors/form";
    }

    @GetMapping("/{id}")
    public String viewInvestor(@PathVariable Long id, Model model) {
        return investorService.getInvestorById(id)
                .map(investor -> {
                    model.addAttribute("investor", investor);
                    return "investors/view";
                })
                .orElse("redirect:/investors");
    }

    @GetMapping("/{id}/edit")
    public String editInvestorForm(@PathVariable Long id, Model model) {
        return investorService.getInvestorById(id)
                .map(investor -> {
                    model.addAttribute("investor", investor);
                    return "investors/form";
                })
                .orElse("redirect:/investors");
    }

    @PostMapping
    public String createInvestor(@ModelAttribute Investor investor, RedirectAttributes redirectAttributes) {
        try {
            Investor savedInvestor = investorService.saveInvestor(investor);
            redirectAttributes.addFlashAttribute("success", "Investidor criado com sucesso!");
            return "redirect:/investors/" + savedInvestor.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao criar investidor: " + e.getMessage());
            return "redirect:/investors/new";
        }
    }

    @PostMapping("/{id}")
    public String updateInvestor(@PathVariable Long id, @ModelAttribute Investor investor, RedirectAttributes redirectAttributes) {
        try {
            Investor savedInvestor = investorService.updateInvestor(id, investor);
            redirectAttributes.addFlashAttribute("success", "Investidor atualizado com sucesso!");
            return "redirect:/investors/" + savedInvestor.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao atualizar investidor: " + e.getMessage());
            return "redirect:/investors/" + id + "/edit";
        }
    }

    @DeleteMapping("/{id}")
    public String deleteInvestor(@PathVariable Long id, Model model) {
        try {
            investorService.deleteInvestor(id);
            List<Investor> investors = investorService.getAllInvestors();
            model.addAttribute("investors", investors);
            model.addAttribute("totalInvestors", investors.size());
            model.addAttribute("totalActiveInvestors", (int) investors.stream().filter(Investor::getActive).count());
            model.addAttribute("totalInactiveInvestors", (int) investors.stream().filter(i -> !i.getActive()).count());
            model.addAttribute("totalAvailableFunds", investorService.getTotalAvailableFunds());
            model.addAttribute("totalInvestedFunds", investorService.getTotalInvestedFunds());
            model.addAttribute("deleteSuccess", true);
            return "investors/fragments/grid";
        } catch (IllegalStateException e) {
            // Handle enterprise association error with user-friendly message
            List<Investor> investors = investorService.getAllInvestors();
            model.addAttribute("investors", investors);
            model.addAttribute("totalInvestors", investors.size());
            model.addAttribute("totalActiveInvestors", (int) investors.stream().filter(Investor::getActive).count());
            model.addAttribute("totalInactiveInvestors", (int) investors.stream().filter(i -> !i.getActive()).count());
            model.addAttribute("totalAvailableFunds", investorService.getTotalAvailableFunds());
            model.addAttribute("totalInvestedFunds", investorService.getTotalInvestedFunds());
            model.addAttribute("deleteError", e.getMessage());
            return "investors/fragments/grid";
        } catch (Exception e) {
            // Handle other database errors
            List<Investor> investors = investorService.getAllInvestors();
            model.addAttribute("investors", investors);
            model.addAttribute("totalInvestors", investors.size());
            model.addAttribute("totalActiveInvestors", (int) investors.stream().filter(Investor::getActive).count());
            model.addAttribute("totalInactiveInvestors", (int) investors.stream().filter(i -> !i.getActive()).count());
            model.addAttribute("totalAvailableFunds", investorService.getTotalAvailableFunds());
            model.addAttribute("totalInvestedFunds", investorService.getTotalInvestedFunds());
            model.addAttribute("deleteError", "Erro ao excluir investidor. Verifique se não há dependências no sistema.");
            return "investors/fragments/grid";
        }
    }

    // HTMX specific endpoints
    @GetMapping("/grid")
    public String getInvestorsGrid(Model model) {
        List<Investor> investors = investorService.getAllInvestors();
        model.addAttribute("investors", investors);
        model.addAttribute("totalInvestors", investors.size());
        model.addAttribute("totalActiveInvestors", (int) investors.stream().filter(Investor::getActive).count());
        model.addAttribute("totalInactiveInvestors", (int) investors.stream().filter(i -> !i.getActive()).count());
        model.addAttribute("totalAvailableFunds", investorService.getTotalAvailableFunds());
        model.addAttribute("totalInvestedFunds", investorService.getTotalInvestedFunds());
        return "investors/fragments/grid";
    }
}