package com.lifeisbananas.model.rawmaterial;

import com.lifeisbananas.model.nutrition.NutritionalValue;
import jakarta.persistence.*;

/**
 * Represents vegetable-based raw materials with specific vegetable characteristics.
 * Extends RawMaterial with vegetable-specific attributes like firmness and moisture content.
 */
@Entity
@DiscriminatorValue("VEGETABLE")
public class VegetableRawMaterial extends RawMaterial {

    // Firmness of the vegetable (e.g., "soft", "firm", "hard")
    @Column(name = "firmness", length = 50)
    private String firmness;

    // Moisture content of the vegetable (percentage or grams per 100g)
    @Column(name = "moisture_content")
    private Double moistureContent;

    // --- Constructors ---

    // Default constructor for JPA
    public VegetableRawMaterial() {
        super();
    }

    // Constructor with base fields
    public VegetableRawMaterial(String rawMaterialID, String name, Boolean organicCertified,
                                Double price, String unit) {
        super(rawMaterialID, name, organicCertified, price, unit);
    }

    // Full constructor including vegetable-specific fields
    public VegetableRawMaterial(String rawMaterialID, String name, Boolean organicCertified,
                                Double price, String unit, NutritionalValue nutritionalValue,
                                String firmness, Double moistureContent) {
        super(rawMaterialID, name, organicCertified, price, unit, nutritionalValue);
        this.firmness = firmness;
        this.moistureContent = moistureContent;
    }

    // --- Getters and Setters ---

    public String getFirmness() {
        return firmness;
    }

    public void setFirmness(String firmness) {
        this.firmness = firmness;
    }

    public Double getMoistureContent() {
        return moistureContent;
    }

    public void setMoistureContent(Double moistureContent) {
        this.moistureContent = moistureContent;
    }
}
