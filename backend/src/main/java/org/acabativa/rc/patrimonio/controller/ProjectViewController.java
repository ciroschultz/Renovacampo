package org.acabativa.rc.patrimonio.controller;

import org.acabativa.rc.patrimonio.entity.Project;
import org.acabativa.rc.patrimonio.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/projects")
public class ProjectViewController {

    private final ProjectService projectService;

    @Autowired
    public ProjectViewController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public String listProjects(Model model) {
        List<Project> projects = projectService.getAllProjects();
        model.addAttribute("projects", projects);
        model.addAttribute("totalProjects", projects.size());
        
        // Calculate statistics
        double totalInvestment = projects.stream()
            .filter(p -> p.getTotalInvestment() != null)
            .mapToDouble(p -> p.getTotalInvestment().doubleValue())
            .sum();
        
        double totalEstimatedCosts = projects.stream()
            .filter(p -> p.getTotalEstimatedCosts() != null)
            .mapToDouble(p -> p.getTotalEstimatedCosts().doubleValue())
            .sum();
        
        long activeProjects = projects.stream()
            .filter(p -> p.getStatus() == Project.Status.IN_PROGRESS || p.getStatus() == Project.Status.APPROVED)
            .count();
        
        model.addAttribute("totalInvestment", totalInvestment);
        model.addAttribute("totalEstimatedCosts", totalEstimatedCosts);
        model.addAttribute("activeProjects", activeProjects);
        
        return "projects/index";
    }

    @GetMapping("/new")
    public String newProjectForm(Model model) {
        model.addAttribute("project", new Project());
        return "projects/form";
    }

    @GetMapping("/{id}")
    public String viewProject(@PathVariable Long id, Model model) {
        Optional<Project> project = projectService.getProjectById(id);
        if (project.isPresent()) {
            model.addAttribute("project", project.get());
            return "projects/view";
        }
        return "redirect:/projects";
    }

    @GetMapping("/{id}/edit")
    public String editProjectForm(@PathVariable Long id, Model model) {
        Optional<Project> project = projectService.getProjectById(id);
        if (project.isPresent()) {
            model.addAttribute("project", project.get());
            return "projects/form";
        }
        return "redirect:/projects";
    }

    @PostMapping
    public String createProject(@ModelAttribute Project project, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Erro de validação: " + bindingResult.getAllErrors().get(0).getDefaultMessage());
            model.addAttribute("project", project);
            return "projects/form";
        }
        
        try {
            Project saved = projectService.createProject(project);
            return "redirect:/projects/" + saved.getId();
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao criar projeto: " + e.getMessage());
            model.addAttribute("project", project);
            return "projects/form";
        }
    }

    @PostMapping("/{id}")
    public String updateProject(@PathVariable Long id, @ModelAttribute Project project, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Erro de validação: " + bindingResult.getAllErrors().get(0).getDefaultMessage());
            model.addAttribute("project", project);
            return "projects/form";
        }
        
        try {
            project.setId(id);
            projectService.updateProject(project);
            return "redirect:/projects/" + id;
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao atualizar projeto: " + e.getMessage());
            model.addAttribute("project", project);
            return "projects/form";
        }
    }

    @DeleteMapping("/{id}")
    public String deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return "redirect:/projects";
    }

    // HTMX specific endpoints
    @GetMapping("/table")
    public String getProjectsTable(Model model) {
        List<Project> projects = projectService.getAllProjects();
        model.addAttribute("projects", projects);
        return "projects/fragments/table";
    }

    @GetMapping("/{id}/card")
    public String getProjectCard(@PathVariable Long id, Model model) {
        Optional<Project> project = projectService.getProjectById(id);
        if (project.isPresent()) {
            model.addAttribute("project", project.get());
            return "projects/fragments/card";
        }
        return "projects/fragments/empty";
    }

    @PostMapping("/{id}/quick-update")
    public String quickUpdateProject(@PathVariable Long id, 
                                   @RequestParam String field, 
                                   @RequestParam String value,
                                   Model model) {
        Optional<Project> projectOpt = projectService.getProjectById(id);
        if (projectOpt.isPresent()) {
            Project project = projectOpt.get();
            
            // Update field based on name
            switch (field) {
                case "name":
                    project.setName(value);
                    break;
                case "category":
                    project.setCategory(value);
                    break;
                case "priority":
                    try {
                        project.setPriority(Project.Priority.valueOf(value.toUpperCase()));
                    } catch (IllegalArgumentException e) {
                        // Handle error
                    }
                    break;
                case "status":
                    try {
                        project.setStatus(Project.Status.valueOf(value.toUpperCase()));
                    } catch (IllegalArgumentException e) {
                        // Handle error
                    }
                    break;
            }
            
            projectService.updateProject(project);
            model.addAttribute("project", project);
            return "projects/fragments/card";
        }
        return "projects/fragments/empty";
    }
}