package be.lifeisbananas.domain;

import be.lifeisbananas.domain.NutritionalValue;

/**
 * Abstract base class representing raw materials (fresh ingredients) used in freeze-dried snacks.
 * Raw materials can be organic or locally sourced and form the basis for product formulas.
 *
 * This class uses JPA Single Table Inheritance strategy with a discriminator column.
 */

@SuppressWarnings("unused") // This annotation is to get rid of the "is never used" warnings
public abstract class RawMaterial {

    // The id of raw material
    private int rawMaterialID;

    // Name of the raw material (e.g., "Banana", "Strawberry", "Spinach")
    private String name;

    // Indicates whether the raw material is organically certified
    private Boolean organicCertified;

    // Price of the raw material
    private Double price;

    // Price of the raw material
    private String unit;

    /**
     * Nutritional information per 100g of this raw material.
     * One-to-one composition relationship - nutritional value cannot exist without raw material.
     */
    private NutritionalValue nutritionalValue;

    // --- Constructors ---

    // Default constructor for JPA
    public RawMaterial() {
    }

    // Constructor with essential fields
    public RawMaterial(int rawMaterialID, String name, Boolean organicCertified,
                       Double price, String unit) {
        this.rawMaterialID = rawMaterialID;
        this.name = name;
        this.organicCertified = organicCertified;
        this.price = price;
        this.unit = unit;
    }

    // Full constructor including nutritional value
    public RawMaterial(int rawMaterialID, String name, Boolean organicCertified,
                       Double price, String unit, NutritionalValue nutritionalValue) {
        this.rawMaterialID = rawMaterialID;
        this.name = name;
        this.organicCertified = organicCertified;
        this.price = price;
        this.unit = unit;
        this.nutritionalValue = nutritionalValue;
    }

    // --- Getters and Setters ---

    public int getRawMaterialID() {
        return rawMaterialID;
    }

    public void setRawMaterialID(int rawMaterialID) {
        this.rawMaterialID = rawMaterialID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getOrganicCertified() {
        return organicCertified;
    }

    public void setOrganicCertified(Boolean organicCertified) {
        this.organicCertified = organicCertified;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public NutritionalValue getNutritionalValue() {
        return nutritionalValue;
    }

    public void setNutritionalValue(NutritionalValue nutritionalValue) {
        this.nutritionalValue = nutritionalValue;
    }
}
