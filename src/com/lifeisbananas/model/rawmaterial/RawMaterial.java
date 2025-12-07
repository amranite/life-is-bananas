package com.lifeisbananas.model.rawmaterial;

import com.lifeisbananas.model.nutrition.NutritionalValue;
import jakarta.persistence.*;

/**
 * Abstract base class representing raw materials (fresh ingredients) used in freeze-dried snacks.
 * Raw materials can be organic or locally sourced and form the basis for product formulas.
 *
 * This class uses JPA Single Table Inheritance strategy with a discriminator column.
 */
@Entity
@Table(name = "raw_materials")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "material_type", discriminatorType = DiscriminatorType.STRING)
public abstract class RawMaterial {

    /**
     * Using `String` as ID (not auto-generated).
     * It is assigned manually ("FRUIT-003", "VEG-044")
     */

    @Id
    @Column(name = "raw_material_id", length = 50)
    private String rawMaterialID;

    // Name of the raw material (e.g., "Banana", "Strawberry", "Spinach")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    // Indicates whether the raw material is organically certified
    @Column(name = "organic_certified", nullable = false)
    private Boolean organicCertified;

    // Price of the raw material
    @Column(name = "price")
    private Double price;

    // Price of the raw material
    @Column(name = "unit", length = 20)
    private String unit;

    /**
     * Nutritional information per 100g of this raw material.
     * One-to-one composition relationship - nutritional value cannot exist without raw material.
     */
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "nutritional_value_id", referencedColumnName = "id")
    private NutritionalValue nutritionalValue;

    // --- Constructors ---

    // Default constructor for JPA
    public RawMaterial() {
    }

    // Constructor with essential fields
    public RawMaterial(String rawMaterialID, String name, Boolean organicCertified,
                       Double price, String unit) {
        this.rawMaterialID = rawMaterialID;
        this.name = name;
        this.organicCertified = organicCertified;
        this.price = price;
        this.unit = unit;
    }

    // Full constructor including nutritional value
    public RawMaterial(String rawMaterialID, String name, Boolean organicCertified,
                       Double price, String unit, NutritionalValue nutritionalValue) {
        this.rawMaterialID = rawMaterialID;
        this.name = name;
        this.organicCertified = organicCertified;
        this.price = price;
        this.unit = unit;
        this.nutritionalValue = nutritionalValue;
    }

    // --- Getters and Setters ---

    public String getRawMaterialID() {
        return rawMaterialID;
    }

    public void setRawMaterialID(String rawMaterialID) {
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
