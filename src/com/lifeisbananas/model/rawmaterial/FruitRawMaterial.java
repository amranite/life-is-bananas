package com.lifeisbananas.model.rawmaterial;

import com.lifeisbananas.model.nutrition.NutritionalValue;
import jakarta.persistence.*;

/**
 * Represents fruit-based raw materials with specific fruit characteristics.
 * Extends RawMaterial with fruit-specific attributes like ripenessLevel, sugarContent content and acidityLevel.
 */
@Entity
@DiscriminatorValue("FRUIT")
public class FruitRawMaterial extends RawMaterial {

    // Ripeness level of the fruit (e.g., "unripe", "ripe", "overripe")
    @Column(name = "ripeness_level", length = 50)
    private String ripenessLevel;

    // Sugar content of the fruit (grams per 100g)
    @Column(name = "sugar_content")
    private Double sugarContent;

    // Acidity level of the fruit (pH value or scale)
    @Column(name = "acidity_level")
    private Double acidityLevel;

    // --- Constructors ---

    // Default constructor for JPA
    public FruitRawMaterial() {
        super();
    }

    // Constructor with base fields
    public FruitRawMaterial(String rawMaterialID, String name, Boolean organicCertified,
                                Double price, String unit) {
        super(rawMaterialID, name, organicCertified, price, unit);
    }

    // Full constructor including fruit-specific fields
    public FruitRawMaterial(String rawMaterialID, String name, Boolean organicCertified,
                                Double price, String unit, NutritionalValue nutritionalValue,
                                String ripenessLevel, Double sugarContent, Double acidityLevel) {
        super(rawMaterialID, name, organicCertified, price, unit, nutritionalValue);
        this.ripenessLevel = ripenessLevel;
        this.sugarContent = sugarContent;
        this.acidityLevel = acidityLevel;
    }

    // --- Getters and Setters ---

    public String getRipenessLevel() {
        return  ripenessLevel;
    }

    public void setRipenessLevel(String ripenessLevel) {
        this.ripenessLevel = ripenessLevel;
    }

    public Double getSugarContent() {
        return sugarContent;
    }

    public void setSugarContent(Double sugarContent) {
        this.sugarContent = sugarContent;
    }

    public Double getAcidityLevel() {
        return acidityLevel;
    }

    public void setAcidityLevel(Double acidityLevel) {
        this.acidityLevel = acidityLevel;
    }
}
