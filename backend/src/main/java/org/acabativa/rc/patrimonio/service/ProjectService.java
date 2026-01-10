package org.acabativa.rc.patrimonio.service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.acabativa.rc.patrimonio.entity.Project;
import org.acabativa.rc.patrimonio.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class ProjectService {

    private final Logger LOGGER = Logger.getLogger(ProjectService.class.getName());

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Optional<Project> getProjectById(Long projectId) {
        return this.projectRepository.findById(projectId);
    }

    public List<Project> getAllProjects() {
        List<Project> projects = this.projectRepository.findAll();
        LOGGER.info(projects.size() + " projects loaded.");
        return projects;
    }

    public List<Project> getActiveProjects() {
        List<Project> projects = this.projectRepository.findAll();
        LOGGER.info(projects.size() + " active projects loaded.");
        return projects;
    }

    public List<Project> getProjectsByStatus(Project.Status status) {
        return this.projectRepository.findByStatus(status);
    }

    public List<Project> getProjectsByPriority(Project.Priority priority) {
        return this.projectRepository.findByPriority(priority);
    }

    public List<Project> getProjectsByCategory(String category) {
        return this.projectRepository.findByCategory(category);
    }

    public List<Project> searchProjects(String searchTerm) {
        return this.projectRepository.findByNameOrDescriptionContaining(searchTerm);
    }

    public List<String> getAllCategories() {
        return this.projectRepository.findDistinctCategories();
    }

    public Project createProject(Project project) {
        LOGGER.info("Creating project: " + project);
        project = this.projectRepository.save(project);
        LOGGER.info("Created project with id: " + project.getId());
        return project;
    }

    public void deleteProject(Long projectId) {
        if (this.projectRepository.existsById(projectId)) {
            this.projectRepository.deleteById(projectId);
            LOGGER.info("Deleted project with id: " + projectId);
        } else {
            throw new IllegalStateException("Project with id does not exist: " + projectId);
        }
    }

    @Transactional
    public Project updateProject(Project project) {
        if (this.projectRepository.existsById(project.getId())) {
            Optional<Project> projectFromDb = this.projectRepository.findById(project.getId());
            if (projectFromDb.isPresent()) {
                Project existingProject = projectFromDb.get();
                existingProject.setName(project.getName());
                existingProject.setCategory(project.getCategory());
                existingProject.setDescription(project.getDescription());
                existingProject.setStartDate(project.getStartDate());
                existingProject.setEstimatedEndDate(project.getEstimatedEndDate());
                existingProject.setEndDate(project.getEndDate());
                existingProject.setPriority(project.getPriority());
                existingProject.setTotalEstimatedCosts(project.getTotalEstimatedCosts());
                existingProject.setTotalCosts(project.getTotalCosts());
                existingProject.setStatus(project.getStatus());
                existingProject.setEstimatedReturnOverInvestment(project.getEstimatedReturnOverInvestment());
                existingProject.setTotalInvestment(project.getTotalInvestment());
                
                LOGGER.info("Updated project with id: " + project.getId());
                return existingProject;
            }
        }
        throw new IllegalStateException("Cannot update, project not found with id: " + project.getId());
    }

    @Transactional
    public Project updateProjectStatus(Long projectId, Project.Status newStatus) {
        Optional<Project> projectOpt = this.projectRepository.findById(projectId);
        if (projectOpt.isPresent()) {
            Project project = projectOpt.get();
            project.setStatus(newStatus);
            LOGGER.info("Updated project status to " + newStatus + " for project id: " + projectId);
            return project;
        }
        throw new IllegalStateException("Cannot update status, project not found with id: " + projectId);
    }
}