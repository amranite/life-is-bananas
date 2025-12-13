package be.lifeisbananas.model.rawmaterial;

import be.lifeisbananas.model.nutrition.NutritionalValue;

/**
 * Represents vegetable-based raw materials with specific vegetable characteristics.
 * Extends RawMaterial with vegetable-specific attributes like firmness and moisture content.
 */

@SuppressWarnings("unused") // This annotation is to get rid of the "is never used" warnings
public class VegetableRawMaterial extends RawMaterial {

    // Firmness of the vegetable (e.g., "soft", "firm", "hard")
    private String firmness;

    // Moisture content of the vegetable (percentage or grams per 100g)
    private Double moistureContent;

    // --- Constructors ---

    // Default constructor for JPA
    public VegetableRawMaterial() {
        super();
    }

    // Constructor with base fields
    public VegetableRawMaterial(int rawMaterialID, String name, Boolean organicCertified,
                                Double price, String unit) {
        super(rawMaterialID, name, organicCertified, price, unit);
    }

    // Full constructor including vegetable-specific fields
    public VegetableRawMaterial(int rawMaterialID, String name, Boolean organicCertified,
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
