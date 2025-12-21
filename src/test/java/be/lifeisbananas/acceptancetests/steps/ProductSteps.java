package be.lifeisbananas.acceptancetests.steps;

import be.lifeisbananas.dao.BioEngineerDAO;
import be.lifeisbananas.dao.FormulaDAO;
import be.lifeisbananas.dao.ProductDAO;
import be.lifeisbananas.dao.impl.BioEngineerDAOImpl;
import be.lifeisbananas.dao.impl.FormulaDAOImpl;
import be.lifeisbananas.dao.impl.ProductDAOImpl;
import be.lifeisbananas.domain.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductSteps {

    private BioEngineerDAO bioEngineerDAO;
    private FormulaDAO formulaDAO;
    private ProductDAO productDAO;

    private BioEngineer currentBioEngineer;
    private Formula currentFormula;
    private Product currentProduct;
    private Exception caughtException;

    public ProductSteps() throws SQLException {
        this.bioEngineerDAO = new BioEngineerDAOImpl();
        this.formulaDAO = new FormulaDAOImpl();
        this.productDAO = new ProductDAOImpl();
    }

    @And("a formula named {string} with version {string} exists")
    public void aFormulaNamedWithVersionExists(String name, String version) throws SQLException {
        if (currentBioEngineer == null) {
            List<BioEngineer> bioEngineers = bioEngineerDAO.findAll();
            if (!bioEngineers.isEmpty()) {
                currentBioEngineer = bioEngineers.get(bioEngineers.size() - 1);
            }
        }

        currentFormula = new Formula();
        currentFormula.setName(name);
        currentFormula.setVersion(version);
        currentFormula.setCreationDate(LocalDate.now());
        currentFormula.setLastModified(LocalDate.now());
        currentFormula.setBioEngineer(currentBioEngineer);
        currentFormula = formulaDAO.save(currentFormula);
    }

    @When("the bio-engineer creates a product named {string}")
    public void theBioEngineerCreatesAProductNamed(String name) {
        currentProduct = new Product();
        currentProduct.setName(name);
        currentProduct.setBioEngineer(currentBioEngineer);
        currentProduct.setStatus(ProductStatus.IN_DEVELOPMENT);
    }

    @And("the product has description {string}")
    public void theProductHasDescription(String description) {
        currentProduct.setDescription(description);
    }

    @And("the product has shelf life of {int} days")
    public void theProductHasShelfLifeOfDays(int days) {
        currentProduct.setShelfLife(days);
    }

    @And("the product has weight of {int} grams")
    public void theProductHasWeightOfGrams(int grams) {
        currentProduct.setWeight(grams);
    }

    @And("the product has batch number {string}")
    public void theProductHasBatchNumber(String batchNumber) throws SQLException {
        currentProduct.setBatchNumber(batchNumber);
        currentProduct.setFormula(currentFormula);
        currentProduct = productDAO.save(currentProduct);
    }

    @Then("the product should be saved in the database")
    public void theProductShouldBeSavedInTheDatabase() throws SQLException {
        Product found = productDAO.findById(currentProduct.getProductID());
        assertThat(found).isNotNull();
        assertThat(found.getProductID()).isEqualTo(currentProduct.getProductID());
    }

    @And("the product status should be {string}")
    public void theProductStatusShouldBe(String status) throws SQLException {
        Product found = productDAO.findById(currentProduct.getProductID());
        assertThat(found.getStatus().toString()).isEqualTo(status);
    }

    @And("the product should be based on formula {string}")
    public void theProductShouldBeBasedOnFormula(String formulaName) throws SQLException {
        Product found = productDAO.findById(currentProduct.getProductID());
        assertThat(found.getFormula().getName()).isEqualTo(formulaName);
    }

    @Given("a product named {string} with status {string} exists")
    public void aProductNamedWithStatusExists(String name, String status) throws SQLException {
        currentProduct = new Product();
        currentProduct.setName(name);
        currentProduct.setBioEngineer(currentBioEngineer);
        currentProduct.setFormula(currentFormula);
        currentProduct.setStatus(ProductStatus.valueOf(status));
        currentProduct = productDAO.save(currentProduct);
    }

    @When("I mark the product as tested")
    public void iMarkTheProductAsTested() throws SQLException {
        currentProduct.markAsTested();
        productDAO.update(currentProduct);
    }

    @And("the status transition should be recorded")
    public void theStatusTransitionShouldBeRecorded() throws SQLException {
        Product found = productDAO.findById(currentProduct.getProductID());
        assertThat(found.getStatus()).isEqualTo(currentProduct.getStatus());
    }

    @When("I approve the product")
    public void iApproveTheProduct() throws SQLException {
        currentProduct.approve();
        productDAO.update(currentProduct);
    }

    @And("the product should be ready for production")
    public void theProductShouldBeReadyForProduction() {
        assertThat(currentProduct.isReadyForSale()).isTrue();
    }

    @When("I start production for the product")
    public void iStartProductionForTheProduct() throws SQLException {
        currentProduct.startProduction();
        productDAO.update(currentProduct);
    }

    @And("the production date should be set to today")
    public void theProductionDateShouldBeSetToToday() {
        assertThat(currentProduct.getProductionDate()).isEqualTo(LocalDate.now());
    }

    @When("I try to approve the product directly")
    public void iTryToApproveTheProductDirectly() {
        try {
            ProductStatus originalStatus = currentProduct.getStatus();
            currentProduct.approve();
            if (currentProduct.getStatus() == originalStatus) {
                caughtException = new IllegalStateException("Status transition not allowed");
            }
        } catch (Exception e) {
            caughtException = e;
        }
    }

    @Then("the status transition should fail")
    public void theStatusTransitionShouldFail() {
        assertThat(caughtException).isNotNull();
    }

    @And("the product status should remain {string}")
    public void theProductStatusShouldRemain(String status) throws SQLException {
        Product found = productDAO.findById(currentProduct.getProductID());
        assertThat(found.getStatus().toString()).isEqualTo(status);
    }

    @When("I try to start production for the product")
    public void iTryToStartProductionForTheProduct() {
        try {
            ProductStatus originalStatus = currentProduct.getStatus();
            currentProduct.startProduction();
            if (currentProduct.getStatus() == originalStatus) {
                caughtException = new IllegalStateException("Status transition not allowed");
            }
        } catch (Exception e) {
            caughtException = e;
        }
    }

    @When("I try to transition to status {string}")
    public void iTryToTransitionToStatus(String targetStatus) {
        try {
            ProductStatus target = ProductStatus.valueOf(targetStatus);
            ProductStatus current = currentProduct.getStatus();

            if (target == ProductStatus.TESTED && current == ProductStatus.IN_DEVELOPMENT) {
                currentProduct.markAsTested();
            } else if (target == ProductStatus.APPROVED && current == ProductStatus.TESTED) {
                currentProduct.approve();
            } else if (target == ProductStatus.IN_PRODUCTION && current == ProductStatus.APPROVED) {
                currentProduct.startProduction();
            } else {
                caughtException = new IllegalStateException("Invalid transition");
            }
            productDAO.update(currentProduct);
        } catch (Exception e) {
            caughtException = e;
        }
    }

    @Then("the transition should {word}")
    public void theTransitionShouldResult(String result) {
        if ("succeed".equals(result)) {
            assertThat(caughtException).isNull();
        } else if ("fail".equals(result)) {
            assertThat(caughtException).isNotNull();
        }
    }
}
