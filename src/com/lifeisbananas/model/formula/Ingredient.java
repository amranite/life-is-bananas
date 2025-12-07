package com.lifeisbananas.model.formula;

import com.lifeisbananas.model.enums.UnitType;
import com.lifeisbananas.model.rawmaterial.RawMaterial;
import jakarta.persistence.*;

/**
 * Represents the link between a Formula and a RawMaterial.
 * Specifies how much of a particular raw material is needed in a formula.
 * This is an association class that connects Formula and RawMaterial.
 */
@Entity
@Table(name = "ingredients")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The amount/quantity of the raw material needed
    @Column(name = "quantity", nullable = false)
    private Double quantity;

    // Unit of measurement for the quantity (gram, kilogram, milliliter, percentage, piece)
    @Enumerated(EnumType.STRING)
    @Column(name = "unit", nullable = false, length = 20)
    private UnitType unit;

    // The order/sequence in which this ingredient should be added (step 1, 2, 3, etc.)
    @Column(name = "sequence")
    private Integer sequence;

    /**
     * The formula that contains this ingredient.
     * Many-to-one relationship: many ingredients belong to one formula.
     */
    @ManyToOne
    @JoinColumn(name = "formula_id", nullable = false)
    private Formula formula;

    /**
     * The raw material referenced by this ingredient.
     * Many-to-one relationship: many ingredients can reference the same raw material.
     */
    @ManyToOne
    @JoinColumn(name = "raw_material_id", nullable = false)
    private RawMaterial rawMaterial;

    // --- Constructors ---

    // Default constructor for JPA
    public Ingredient() {
    }

    // Constructor with essential fields
    public Ingredient(Double quantity, UnitType unit, Integer sequence) {
        this.quantity = quantity;
        this.unit = unit;
        this.sequence = sequence;
    }

    // Full constructor including formula and raw material
    public Ingredient(Double quantity, UnitType unit, Integer sequence,
                      Formula formula, RawMaterial rawMaterial) {
        this.quantity = quantity;
        this.unit = unit;
        this.sequence = sequence;
        this.formula = formula;
        this.rawMaterial = rawMaterial;
    }

    // --- Getters and Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public UnitType getUnit() {
        return unit;
    }

    public void setUnit(UnitType unit) {
        this.unit = unit;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Formula getFormula() {
        return formula;
    }

    public void setFormula(Formula formula) {
        this.formula = formula;
    }

    public RawMaterial getRawMaterial() {
        return rawMaterial;
    }

    public void setRawMaterial(RawMaterial rawMaterial) {
        this.rawMaterial = rawMaterial;
    }
}
