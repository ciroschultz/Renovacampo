package org.acabativa.rc.patrimonio.controller;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.acabativa.rc.patrimonio.entity.Property;
import org.acabativa.rc.patrimonio.service.Notary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/property")
public class NotaryRestApi {

    private final Logger LOGGER = Logger.getLogger(NotaryRestApi.class.getName());

    private Notary notary;

    @Autowired
    public NotaryRestApi(Notary notary) {
        this.notary = notary;
    }

    @GetMapping
    public List<Property> getProperties(){
        return this.notary.getProperties();
    }

    // Endpoint para retornar apenas propriedades aprovadas (para o frontend público)
    @GetMapping("/approved")
    public List<Property> getApprovedProperties(){
        return this.notary.getApprovedProperties();
    }

    @GetMapping("/{id}")
    public Optional<Property> getProperties(@PathVariable("id") long id){
        return this.notary.getPropertyById(id);
    }

    @PostMapping
	public long addProperty(@RequestBody Property property){
        LOGGER.info("Adding Property...");
        LOGGER.info("Property[ " + property.toString() + "]");
		return this.notary.createproperty(property).getId();
	}

	@DeleteMapping(path = "{id}")
	public void deleteProperty(@PathVariable("id") Long id){
		this.notary.deleteProperty(id);
	}
    

}
