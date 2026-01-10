package org.acabativa.rc.patrimonio.repository;

import org.acabativa.rc.patrimonio.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyDAO extends JpaRepository<Property, Long>{

    @Query("SELECT p FROM Property p WHERE p.totalArea > 0 ORDER BY p.name ASC")
    List<Property> findActiveProperties();

}
