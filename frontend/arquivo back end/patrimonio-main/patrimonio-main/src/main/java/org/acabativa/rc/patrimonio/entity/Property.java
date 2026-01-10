package org.acabativa.rc.patrimonio.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

@Entity
public class Property {

    public Property(){

    }

    public Property(String name, String description, Integer totalArea, Integer availableArea, String type,
            String address, String state, String city, Double latitude, Double longitude) {
        this.name = name;
        this.description = description;
        this.totalArea = totalArea;
        this.availableArea = availableArea;
        this.type = type;
        this.address = address;
        this.state = state;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Property(Long id, String name, String description, Integer totalArea, Integer availableArea, String type,
            String address, String state, String city, Double latitude, Double longitude) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.totalArea = totalArea;
        this.availableArea = availableArea;
        this.type = type;
        this.address = address;
        this.state = state;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Id
    @SequenceGenerator(
        name = "property_sequence",
        sequenceName = "property_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "property_sequence"
    )
    private Long id;

    private String name;
    private String description;
    private Integer totalArea;
    private Integer availableArea;
    private String type;

    private String address;
    private String state;
    private String city;

    private Double latitude;
    private Double longitude;

    // Campo para armazenar dados extras do formulario em formato JSON
    @Column(columnDefinition = "TEXT")
    private String additionalData;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTotalArea() {
        return totalArea;
    }

    public void setTotalArea(Integer totalArea) {
        this.totalArea = totalArea;
    }

    public Integer getAvailableArea() {
        return availableArea;
    }

    public void setAvailableArea(Integer availableArea) {
        this.availableArea = availableArea;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(String additionalData) {
        this.additionalData = additionalData;
    }

}
