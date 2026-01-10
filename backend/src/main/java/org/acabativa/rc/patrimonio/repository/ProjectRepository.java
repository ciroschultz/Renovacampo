package org.acabativa.rc.patrimonio.repository;

import org.acabativa.rc.patrimonio.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    
    List<Project> findByStatus(Project.Status status);
    
    List<Project> findByPriority(Project.Priority priority);
    
    List<Project> findByCategory(String category);
    
    @Query("SELECT p FROM Project p WHERE p.name LIKE %?1% OR p.description LIKE %?1%")
    List<Project> findByNameOrDescriptionContaining(String searchTerm);
    
    @Query("SELECT p FROM Project p WHERE p.status = ?1 AND p.priority = ?2")
    List<Project> findByStatusAndPriority(Project.Status status, Project.Priority priority);
    
    @Query("SELECT DISTINCT p.category FROM Project p WHERE p.category IS NOT NULL ORDER BY p.category")
    List<String> findDistinctCategories();
}