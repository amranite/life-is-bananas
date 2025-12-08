package com.lifeisbananas.model.nutrition;

/**
 * Represents detailed nutritional information per 100 grams of raw material.
 * This data is essential for labeling requirements and nutritional calculations.
 *
 * All values are measured per 100g of the raw material
 */
public class NutritionalValue {

    // The id of the nutritional value
    private Long id;

    // Energy content in kcal per 100g
    private Double calories;

    // Protein content in grams per 100g
    private Double proteins;

    // Carbohydrate content in grams per 100g
    private Double carbohydrates;

    // Fat content in grams per 100g
    private Double fats;

    // Dietary fiber content in grams per 100g
    private Double fibers;

    // Sugar content in grams per 100g
    private Double sugars;

    // Salt content in grams per 100g
    private Double salt;

    // Vitamin A content in micrograms (µg) per 100g
    private Double vitaminA;

    // Vitamin C content in milligrams (mg) per 100g
    private Double vitaminC;

    // Calcium content in milligrams (mg) per 100g
    private Double calcium;

    // Iron content in milligrams (mg) per 100g
    private Double iron;

    // --- Constructors ---

    // Default constructor for JPA (Java Persistence API)
    public NutritionalValue() {
    }

    // Full constructor for creating a complete nutritional value object
    public NutritionalValue(Double calories, Double proteins, Double carbohydrates,
                            Double fats, Double fibers, Double sugars, Double salt,
                            Double vitaminA, Double vitaminC, Double calcium, Double iron) {
        this.calories = calories;
        this.proteins = proteins;
        this.carbohydrates = carbohydrates;
        this.fats = fats;
        this.fibers = fibers;
        this.sugars = sugars;
        this.salt = salt;
        this.vitaminA = vitaminA;
        this.vitaminC = vitaminC;
        this.calcium = calcium;
        this.iron = iron;
    }

    // --- Getters and Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getCalories() {
        return calories;
    }

    public void setCalories(Double calories) {
        this.calories = calories;
    }

    public Double getProteins() {
        return proteins;
    }

    public void setProteins(Double proteins) {
        this.proteins = proteins;
    }

    public Double getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(Double carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public Double getFats() {
        return fats;
    }

    public void setFats(Double fats) {
        this.fats = fats;
    }

    public Double getFibers() {
        return fibers;
    }

    public void setFibers(Double fibers) {
        this.fibers = fibers;
    }

    public Double getSugars() {
        return sugars;
    }

    public void setSugars(Double sugars) {
        this.sugars = sugars;
    }

    public Double getSalt() {
        return salt;
    }

    public void setSalt(Double salt) {
        this.salt = salt;
    }

    public Double getVitaminA() {
        return vitaminA;
    }

    public void setVitaminA(Double vitaminA) {
        this.vitaminA = vitaminA;
    }

    public Double getVitaminC() {
        return vitaminC;
    }

    public void setVitaminC(Double vitaminC) {
        this.vitaminC = vitaminC;
    }

    public Double getCalcium() {
        return calcium;
    }

    public void setCalcium(Double calcium) {
        this.calcium = calcium;
    }

    public Double getIron() {
        return iron;
    }

    public void setIron(Double iron) {
        this.iron = iron;
    }
}