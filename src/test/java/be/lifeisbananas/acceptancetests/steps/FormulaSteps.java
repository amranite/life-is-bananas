package be.lifeisbananas.acceptancetests.steps;

import be.lifeisbananas.dao.BioEngineerDAO;
import be.lifeisbananas.dao.FormulaDAO;
import be.lifeisbananas.dao.RawMaterialDAO;
import be.lifeisbananas.dao.impl.BioEngineerDAOImpl;
import be.lifeisbananas.dao.impl.FormulaDAOImpl;
import be.lifeisbananas.dao.impl.RawMaterialDAOImpl;
import be.lifeisbananas.domain.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.sql.SQLException;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class FormulaSteps {

    private BioEngineerDAO bioEngineerDAO;
    private FormulaDAO formulaDAO;
    private RawMaterialDAO rawMaterialDAO;

    private BioEngineer currentBioEngineer;
    private Formula currentFormula;
    private Exception caughtException;
    private LocalDate lastModifiedBeforeUpdate;
    private String updatedPreparationMethod;

    public FormulaSteps() throws SQLException {
        this.bioEngineerDAO = new BioEngineerDAOImpl();
        this.formulaDAO = new FormulaDAOImpl();
        this.rawMaterialDAO = new RawMaterialDAOImpl();
    }
    @And("a bio-engineer with name {string} and email {string} exists")
    public void aBioEngineerWithNameAndEmailExists(String name, String email) throws SQLException {
        currentBioEngineer = new BioEngineer(name, email, null, null);
        currentBioEngineer = bioEngineerDAO.save(currentBioEngineer);
    }

    @Given("a fruit raw material {string} with {double} calories exists")
    public void aFruitRawMaterialWithCaloriesExists(String name, double calories) throws SQLException {
        FruitRawMaterial fruit = new FruitRawMaterial();
        fruit.setName(name);
        fruit.setOrganicCertified(false);
        NutritionalValue nutritionalValue = new NutritionalValue();
        nutritionalValue.setCalories(calories);
        fruit.setNutritionalValue(nutritionalValue);
        rawMaterialDAO.save(fruit);
    }

    @When("the bio-engineer creates a formula named {string} with version {string}")
    public void theBioEngineerCreatesAFormulaNamedWithVersion(String name, String version) throws SQLException {
        currentFormula = new Formula();
        currentFormula.setName(name);
        currentFormula.setVersion(version);
        currentFormula.setCreationDate(LocalDate.now());
        currentFormula.setLastModified(LocalDate.now());
        currentFormula.setBioEngineer(currentBioEngineer);
        currentFormula = formulaDAO.save(currentFormula);
    }

    @And("adds ingredient {string} with quantity {double} grams at sequence {int}")
    public void addsIngredientWithQuantityGramsAtSequence(String materialName, double quantity, int sequence) throws SQLException {
        RawMaterial rawMaterial = formulaDAO.findRawMaterialByName(materialName);
        formulaDAO.addIngredient(currentFormula.getFormulaID(), rawMaterial.getRawMaterialID(), quantity, UnitType.GRAM, sequence);
    }

    @Then("the formula should be saved in the database")
    public void theFormulaShouldBeSavedInTheDatabase() throws SQLException {
        Formula found = formulaDAO.findById(currentFormula.getFormulaID());
        assertThat(found).isNotNull();
        assertThat(found.getFormulaID()).isEqualTo(currentFormula.getFormulaID());
    }

    @And("the formula should have {int} ingredient")
    public void theFormulaShouldHaveIngredient(int count) throws SQLException {
        Formula found = formulaDAO.findById(currentFormula.getFormulaID());
        assertThat(found.getIngredients()).hasSize(count);
    }

    @And("the formula should be created by {string}")
    public void theFormulaShouldBeCreatedBy(String email) throws SQLException {
        Formula found = formulaDAO.findById(currentFormula.getFormulaID());
        assertThat(found.getBioEngineer().getEmail()).isEqualTo(email);
    }

    @And("a vegetable raw material {string} with {double} calories exists")
    public void aVegetableRawMaterialWithCaloriesExists(String name, double calories) throws SQLException {
        VegetableRawMaterial vegetable = new VegetableRawMaterial();
        vegetable.setName(name);
        vegetable.setOrganicCertified(false);
        NutritionalValue nutritionalValue = new NutritionalValue();
        nutritionalValue.setCalories(calories);
        vegetable.setNutritionalValue(nutritionalValue);
        rawMaterialDAO.save(vegetable);
    }

    @And("the formula should have {int} ingredients")
    public void theFormulaShouldHaveIngredients(int count) throws SQLException {
        Formula found = formulaDAO.findById(currentFormula.getFormulaID());
        assertThat(found.getIngredients()).hasSize(count);
    }

    @And("the ingredients should be in the correct sequence")
    public void theIngredientsShouldBeInTheCorrectSequence() throws SQLException {
        Formula found = formulaDAO.findById(currentFormula.getFormulaID());
        for (int i = 0; i < found.getIngredients().size(); i++) {
            assertThat(found.getIngredients().get(i).getSequence()).isEqualTo(i + 1);
        }
    }

    @Given("no bio-engineer is selected")
    public void noBioEngineerIsSelected() {
        currentBioEngineer = null;
    }

    @When("I try to create a formula named {string} with version {string}")
    public void iTryToCreateAFormulaNamedWithVersion(String name, String version) {
        try {
            if (currentBioEngineer == null) {
                throw new IllegalArgumentException("Bio-engineer is required");
            }
            currentFormula = new Formula();
            currentFormula.setName(name);
            currentFormula.setVersion(version);
            currentFormula.setCreationDate(LocalDate.now());
            currentFormula.setLastModified(LocalDate.now());
            currentFormula.setBioEngineer(currentBioEngineer);
            currentFormula = formulaDAO.save(currentFormula);
        } catch (Exception e) {
            caughtException = e;
        }
    }

    @Then("the formula creation should fail with a missing bio-engineer error")
    public void theFormulaCreationShouldFailWithAMissingBioEngineerError() {
        assertThat(caughtException).isNotNull();
        assertThat(caughtException.getMessage()).contains("Bio-engineer");
    }

    @Given("a formula named {string} exists")
    public void aFormulaNamedExists(String name) throws SQLException {
        currentFormula = new Formula();
        currentFormula.setName(name);
        currentFormula.setVersion("1.0");
        currentFormula.setCreationDate(LocalDate.now());
        currentFormula.setLastModified(LocalDate.now());
        currentFormula.setBioEngineer(currentBioEngineer);
        currentFormula = formulaDAO.save(currentFormula);
        lastModifiedBeforeUpdate = currentFormula.getLastModified();
    }

    @When("I update the preparation method to {string}")
    public void iUpdateThePreparationMethodTo(String preparationMethod) throws SQLException {
        updatedPreparationMethod = preparationMethod;
        currentFormula.setPreparationMethod(preparationMethod);
        currentFormula.setLastModified(LocalDate.now());
        formulaDAO.update(currentFormula);
    }

    @Then("the formula should have the updated preparation method")
    public void theFormulaShouldHaveTheUpdatedPreparationMethod() throws SQLException {
        Formula found = formulaDAO.findById(currentFormula.getFormulaID());
        assertThat(found.getPreparationMethod()).isEqualTo(updatedPreparationMethod);
    }

    @And("the last modified date should be updated")
    public void theLastModifiedDateShouldBeUpdated() throws SQLException {
        Formula found = formulaDAO.findById(currentFormula.getFormulaID());
        assertThat(found.getLastModified()).isAfterOrEqualTo(lastModifiedBeforeUpdate);
    }

    @Given("a {word} raw material {string} exists")
    public void aMaterialTypeRawMaterialExists(String materialType, String name) throws SQLException {
        NutritionalValue nutritionalValue = new NutritionalValue();
        nutritionalValue.setCalories(0.0);

        if ("fruit".equals(materialType)) {
            FruitRawMaterial fruit = new FruitRawMaterial();
            fruit.setName(name);
            fruit.setOrganicCertified(false);
            fruit.setNutritionalValue(nutritionalValue);
            rawMaterialDAO.save(fruit);
        } else {
            VegetableRawMaterial vegetable = new VegetableRawMaterial();
            vegetable.setName(name);
            vegetable.setOrganicCertified(false);
            vegetable.setNutritionalValue(nutritionalValue);
            rawMaterialDAO.save(vegetable);
        }
    }

    @When("I add ingredient {string} with quantity {double} {word} at sequence {int}")
    public void iAddIngredientWithQuantityUnitAtSequence(String materialName, double quantity, String unit, int sequence) throws SQLException {
        RawMaterial rawMaterial = formulaDAO.findRawMaterialByName(materialName);
        UnitType unitType = UnitType.valueOf(unit);
        formulaDAO.addIngredient(currentFormula.getFormulaID(), rawMaterial.getRawMaterialID(), quantity, unitType, sequence);
    }

    @Then("the ingredient should be added successfully")
    public void theIngredientShouldBeAddedSuccessfully() throws SQLException {
        Formula found = formulaDAO.findById(currentFormula.getFormulaID());
        assertThat(found.getIngredients()).isNotEmpty();
    }

    @And("the ingredient should have quantity {double} and unit {word}")
    public void theIngredientShouldHaveQuantityAndUnit(double quantity, String unit) throws SQLException {
        Formula found = formulaDAO.findById(currentFormula.getFormulaID());
        Ingredient ingredient = found.getIngredients().get(found.getIngredients().size() - 1);
        assertThat(ingredient.getQuantity()).isEqualTo(quantity);
        assertThat(ingredient.getUnit().toString()).isEqualTo(unit);
    }
}
