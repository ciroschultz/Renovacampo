package org.acabativa.rc.patrimonio.controller;

import org.acabativa.rc.patrimonio.entity.Property;
import org.acabativa.rc.patrimonio.service.Notary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/properties")
public class PropertyViewController {

    private final Notary notary;

    @Autowired
    public PropertyViewController(Notary notary) {
        this.notary = notary;
    }

    @GetMapping
    public String listProperties(Model model) {
        List<Property> properties = notary.getProperties();
        model.addAttribute("properties", properties);
        model.addAttribute("totalProperties", properties.size());
        return "properties/index";
    }

    @GetMapping("/new")
    public String newPropertyForm(Model model) {
        model.addAttribute("property", new Property());
        return "properties/form";
    }

    @GetMapping("/{id}")
    public String viewProperty(@PathVariable Long id, Model model) {
        Optional<Property> property = notary.getPropertyById(id);
        if (property.isPresent()) {
            model.addAttribute("property", property.get());
            return "properties/view";
        }
        return "redirect:/properties";
    }

    @GetMapping("/{id}/edit")
    public String editPropertyForm(@PathVariable Long id, Model model) {
        Optional<Property> property = notary.getPropertyById(id);
        if (property.isPresent()) {
            model.addAttribute("property", property.get());
            return "properties/form";
        }
        return "redirect:/properties";
    }

    @PostMapping
    public String createProperty(@ModelAttribute Property property, Model model) {
        try {
            Property saved = notary.createproperty(property);
            return "redirect:/properties/" + saved.getId();
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao criar propriedade: " + e.getMessage());
            model.addAttribute("property", property);
            return "properties/form";
        }
    }

    @PostMapping("/{id}")
    public String updateProperty(@PathVariable Long id, @ModelAttribute Property property) {
        property.setId(id);
        notary.createproperty(property); // Save/update
        return "redirect:/properties/" + id;
    }

    @DeleteMapping("/{id}")
    public String deleteProperty(@PathVariable Long id, Model model) {
        try {
            notary.deleteProperty(id);
            List<Property> properties = notary.getProperties();
            model.addAttribute("properties", properties);
            model.addAttribute("deleteSuccess", true);
            return "properties/fragments/table";
        } catch (Exception e) {
            List<Property> properties = notary.getProperties();
            model.addAttribute("properties", properties);
            model.addAttribute("deleteError", e.getMessage());
            return "properties/fragments/table";
        }
    }

    // HTMX specific endpoints
    @GetMapping("/table")
    public String getPropertiesTable(Model model) {
        List<Property> properties = notary.getProperties();
        model.addAttribute("properties", properties);
        return "properties/fragments/table";
    }

    @GetMapping("/{id}/card")
    public String getPropertyCard(@PathVariable Long id, Model model) {
        Optional<Property> property = notary.getPropertyById(id);
        if (property.isPresent()) {
            model.addAttribute("property", property.get());
            return "properties/fragments/card";
        }
        return "properties/fragments/empty";
    }

    @PostMapping("/{id}/quick-update")
    public String quickUpdateProperty(@PathVariable Long id,
                                    @RequestParam String field,
                                    @RequestParam String value,
                                    Model model) {
        Optional<Property> propertyOpt = notary.getPropertyById(id);
        if (propertyOpt.isPresent()) {
            Property property = propertyOpt.get();

            // Update field based on name
            switch (field) {
                case "name":
                    property.setName(value);
                    break;
                case "type":
                    property.setType(value);
                    break;
                case "totalArea":
                    try {
                        property.setTotalArea(Integer.parseInt(value));
                    } catch (NumberFormatException e) {
                        // Handle error
                    }
                    break;
            }

            notary.createproperty(property);
            model.addAttribute("property", property);
            return "properties/fragments/card";
        }
        return "properties/fragments/empty";
    }

    // Endpoint para aprovar/desaprovar propriedade para exibição no frontend
    @PostMapping("/{id}/toggle-approval")
    @ResponseBody
    public String toggleApproval(@PathVariable Long id) {
        Optional<Property> propertyOpt = notary.getPropertyById(id);
        if (propertyOpt.isPresent()) {
            Property property = propertyOpt.get();
            Boolean currentApproval = property.getApproved();
            property.setApproved(currentApproval == null || !currentApproval);
            notary.createproperty(property);
            return property.getApproved() ? "approved" : "pending";
        }
        return "error";
    }
}