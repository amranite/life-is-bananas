package com.lifeisbananas.model.formula;

import com.lifeisbananas.model.bioengineer.BioEngineer;
import com.lifeisbananas.model.product.Product;
import com.lifeisbananas.model.instruction.FreezeDryInstruction;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a formula (recipe) that describes the exact composition, proportions,
 * and preparation method for a freeze-dried snack product.
 * A formula is the blueprint for creating products.
 */
public class Formula {

    // The id of the formula
    private String formulaID;

    // Name of the formula (e.g., "Tropical Mix", "Berry Blast")
    private String name;

    // Version number of this formula (e.g., "1.0", "2.3", "3.0-beta")
    private String version;

    // Detailed description of the preparation method and process
    private String preparationMethod;

    // Date when this formula was initially created
    private LocalDate creationDate;

    // Date when this formula was last modified
    private LocalDate lastModified;

    /**
     * The bio-engineer who created this formula.
     * Many-to-one relationship: many formulas can be created by one engineer.
     */
    private BioEngineer bioEngineer;

    /**
     * List of ingredients contained in this formula.
     * One-to-many aggregation: a formula contains multiple ingredients.
     * Aggregation (not composition) - ingredients can exist during formula development.
     */
    private List<Ingredient> ingredients = new ArrayList<>();

    /**
     * Products that are based on this formula.
     * One-to-many relationship: one formula can be the basis for multiple products.
     */
    private List<Product> products = new ArrayList<>();

    /**
     * Freeze-dry instructions generated from this formula.
     * One-to-many relationship: one formula can have multiple instruction versions.
     */
    private List<FreezeDryInstruction> freezeDryInstructions = new ArrayList<>();

    // --- Constructors ---

    // Default constructor for JPA
    public Formula() {
    }

    // Constructor with essential fields
    public Formula(String formulaID, String name, String version, String preparationMethod) {
        this.formulaID = formulaID;
        this.name = name;
        this.version = version;
        this.preparationMethod = preparationMethod;
        this.creationDate = LocalDate.now();
        this.lastModified = LocalDate.now();
    }

    // Full constructor including bio-engineer
    public Formula(String formulaID, String name, String version, String preparationMethod,
                   BioEngineer bioEngineer) {
        this.formulaID = formulaID;
        this.name = name;
        this.version = version;
        this.preparationMethod = preparationMethod;
        this.bioEngineer = bioEngineer;
        this.creationDate = LocalDate.now();
        this.lastModified = LocalDate.now();
    }

    // ---Business Methods ---

    /**
     * Adds an ingredient to this formula
     * @param ingredient the ingredient to add
     */
    public void addIngredient(Ingredient ingredient) {
        ingredient.setFormula(this);
        this.ingredients.add(ingredient);
    }

    /**
     * Removes an ingredient from this formula
     * @param ingredient the ingredient to remove
     */
    public void removeIngredient(Ingredient ingredient) {
        ingredient.setFormula(null);
        this.ingredients.remove(ingredient);
    }

    // Updates the last modified date to now
    public void markAsModified() {
        this.lastModified = LocalDate.now();
    }

    // --- Getters and Setters ---

    public String getFormulaID() {
        return formulaID;
    }

    public void setFormulaID(String formulaID) {
        this.formulaID = formulaID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPreparationMethod() {
        return preparationMethod;
    }

    public void setPreparationMethod(String preparationMethod) {
        this.preparationMethod = preparationMethod;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
    }

    public BioEngineer getBioEngineer() {
        return bioEngineer;
    }

    public void setBioEngineer(BioEngineer bioEngineer) {
        this.bioEngineer = bioEngineer;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<FreezeDryInstruction> getFreezeDryInstructions() {
        return freezeDryInstructions;
    }

    public void setFreezeDryInstructions(List<FreezeDryInstruction> freezeDryInstructions) {
        this.freezeDryInstructions = freezeDryInstructions;
    }
}