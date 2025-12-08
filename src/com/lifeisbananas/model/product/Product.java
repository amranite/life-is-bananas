package com.lifeisbananas.model.product;

import com.lifeisbananas.model.bioengineer.BioEngineer;
import com.lifeisbananas.model.enums.ProductStatus;
import com.lifeisbananas.model.formula.Formula;
import java.time.LocalDate;

/**
 * Represents a final product (freeze-dried snack) that is the result of
 * product development and is ready for sale or testing.
 * Each product is based on a specific formula and tracks its lifecycle status.
 */
public class Product {

    // The id of the product
    private String productID;

    // Commercial name of the product (e.g., "Banana Chips Original", "Berry Mix Deluxe")
    private String name;

    // Detailed description of the product for marketing and packaging
    private String description;

    // Shelf life of the product in days from production date
    private Integer shelfLife;

    // Net weight of the product in grams
    private Integer weight;

    // Unique batch number for traceability and quality control
    private String batchNumber;

    // Date when this product batch was produced
    private LocalDate productionDate;

    // Current lifecycle status of the product
    private ProductStatus status;

    /**
     * The formula that this product is based on.
     * Many-to-one relationship: many products can be based on the same formula.
     */
    private Formula formula;

    /**
     * The bio-engineer who developed this product.
     * Many-to-one relationship: one engineer develops many products.
     */
    private BioEngineer bioEngineer;

    // --- Constructors ---

    // Default constructor for JPA
    public Product() {
    }

    // Constructor with essential fields
    public Product(String productID, String name, String description, Integer shelfLife,
                   Integer weight, String batchNumber) {
        this.productID = productID;
        this.name = name;
        this.description = description;
        this.shelfLife = shelfLife;
        this.weight = weight;
        this.batchNumber = batchNumber;
        this.productionDate = LocalDate.now();
        this.status = ProductStatus.IN_DEVELOPMENT;
    }

    // Full constructor including formula and bio-engineer
    public Product(String productID, String name, String description, Integer shelfLife,
                   Integer weight, String batchNumber, Formula formula, BioEngineer bioEngineer) {
        this.productID = productID;
        this.name = name;
        this.description = description;
        this.shelfLife = shelfLife;
        this.weight = weight;
        this.batchNumber = batchNumber;
        this.productionDate = LocalDate.now();
        this.status = ProductStatus.IN_DEVELOPMENT;
        this.formula = formula;
        this.bioEngineer = bioEngineer;
    }

    // --- Business Methods ---

    // Transitions the product to the TESTED status
    public void markAsTested() {
        if (this.status == ProductStatus.IN_DEVELOPMENT) {
            this.status = ProductStatus.TESTED;
        }
    }

    // Approves the product for production
    public void approve() {
        if (this.status == ProductStatus.TESTED) {
            this.status = ProductStatus.APPROVED;
        }
    }

    // Approves the product for production
    public void startProduction() {
        if (this.status == ProductStatus.APPROVED) {
            this.status = ProductStatus.IN_PRODUCTION;
            if (this.productionDate == null) {
                this.productionDate = LocalDate.now();
            }
        }
    }

    /**
     * Checks if the product is ready for sale (approved or in production)
     * @return true if product is approved or in production
     */
    public boolean isReadyForSale() {
        return this.status == ProductStatus.APPROVED ||
                this.status == ProductStatus.IN_PRODUCTION;
    }

    /**
     * Calculates the expiration date based on production date and shelf life
     * @return the expiration date, or null if production date or shelf life is not set
     */
    public LocalDate getExpirationDate() {
        if (this.productionDate != null && this.shelfLife != null) {
            return this.productionDate.plusDays(this.shelfLife);
        }
        return null;
    }

    // --- Getters and Setters ---

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
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

    public Integer getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(Integer shelfLife) {
        this.shelfLife = shelfLife;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public LocalDate getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(LocalDate productionDate) {
        this.productionDate = productionDate;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public Formula getFormula() {
        return formula;
    }

    public void setFormula(Formula formula) {
        this.formula = formula;
    }

    public BioEngineer getBioEngineer() {
        return bioEngineer;
    }

    public void setBioEngineer(BioEngineer bioEngineer) {
        this.bioEngineer = bioEngineer;
    }
}
