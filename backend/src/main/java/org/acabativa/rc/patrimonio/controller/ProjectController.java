package org.acabativa.rc.patrimonio.controller;

import org.acabativa.rc.patrimonio.entity.Project;
import org.acabativa.rc.patrimonio.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/project")
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        List<Project> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        Optional<Project> project = projectService.getProjectById(id);
        return project.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        Project createdProject = projectService.createProject(project);
        return ResponseEntity.ok(createdProject);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Long id, @RequestBody Project project) {
        project.setId(id);
        try {
            Project updatedProject = projectService.updateProject(project);
            return ResponseEntity.ok(updatedProject);
        } catch (IllegalStateException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        try {
            projectService.deleteProject(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Project>> getProjectsByStatus(@PathVariable String status) {
        try {
            Project.Status statusEnum = Project.Status.valueOf(status.toUpperCase());
            List<Project> projects = projectService.getProjectsByStatus(statusEnum);
            return ResponseEntity.ok(projects);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<Project>> getProjectsByPriority(@PathVariable String priority) {
        try {
            Project.Priority priorityEnum = Project.Priority.valueOf(priority.toUpperCase());
            List<Project> projects = projectService.getProjectsByPriority(priorityEnum);
            return ResponseEntity.ok(projects);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Project>> getProjectsByCategory(@PathVariable String category) {
        List<Project> projects = projectService.getProjectsByCategory(category);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Project>> searchProjects(@RequestParam String q) {
        List<Project> projects = projectService.searchProjects(q);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAllCategories() {
        List<String> categories = projectService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Project> updateProjectStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            Project.Status statusEnum = Project.Status.valueOf(status.toUpperCase());
            Project updatedProject = projectService.updateProjectStatus(id, statusEnum);
            return ResponseEntity.ok(updatedProject);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.notFound().build();
        }
    }
}