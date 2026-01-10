package org.acabativa.rc.patrimonio.service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.acabativa.rc.patrimonio.entity.Property;
import org.acabativa.rc.patrimonio.repository.PropertyDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class Notary {

    private final Logger LOGGER = Logger.getLogger(Notary.class.getName());

    private final PropertyDAO propertyDao;

    @Autowired
    public Notary(PropertyDAO propertyDao) {
        this.propertyDao = propertyDao;
    }

    public Optional<Property> getPropertyById(Long propertyId){
      return this.propertyDao.findById(propertyId);
    }

    public List<Property> getProperties(){
        List<Property> ret = this.propertyDao.findAll();
        LOGGER.info(ret.size() + " props loaded.");
        return ret;
    }

    // Retorna apenas propriedades aprovadas para exibição no frontend público
    public List<Property> getApprovedProperties(){
        List<Property> ret = this.propertyDao.findByApprovedTrue();
        LOGGER.info(ret.size() + " approved props loaded.");
        return ret;
    }

    public Property createproperty(Property property){
        LOGGER.info("Adding property: " + property);
        property = this.propertyDao.save(property);
        LOGGER.info("Added property id: " + property.getId());
        return property;
    }

    
    public void deleteProperty(Long propertyId){
        if(this.propertyDao.existsById(propertyId)){
            this.propertyDao.deleteById(propertyId);
        }
        else{
            throw new IllegalStateException("Property with id does not exist: " + propertyId);
        }
    }

    @Transactional
    public void updateMessage(Property property){
        if(this.propertyDao.existsById(property.getId())){
            Optional<Property> propertyFromDb = this.propertyDao.findById(property.getId());
            if(propertyFromDb.isPresent()){
                propertyFromDb.get().setAddress(property.getAddress());
                propertyFromDb.get().setCity(property.getCity());
                propertyFromDb.get().setDescription(property.getDescription());
                propertyFromDb.get().setName(property.getName());
                propertyFromDb.get().setState(property.getState());
                propertyFromDb.get().setType(property.getType());
                propertyFromDb.get().setAvailableArea(property.getAvailableArea());
                propertyFromDb.get().setLatitude(property.getLatitude());
                propertyFromDb.get().setLongitude(property.getLongitude());
                propertyFromDb.get().setTotalArea(property.getTotalArea());
            }
        }
        else{
            throw new IllegalStateException("Cant update, id not found: " + property.getId());
        }
    }

}
