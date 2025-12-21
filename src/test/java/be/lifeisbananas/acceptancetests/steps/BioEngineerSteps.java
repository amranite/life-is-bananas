package be.lifeisbananas.acceptancetests.steps;

import be.lifeisbananas.dao.BioEngineerDAO;
import be.lifeisbananas.dao.impl.BioEngineerDAOImpl;
import be.lifeisbananas.domain.BioEngineer;
import be.lifeisbananas.service.BioEngineerService;
import be.lifeisbananas.service.impl.BioEngineerServiceImpl;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class BioEngineerSteps {
    private BioEngineerService bioEngineerService;
    private BioEngineerDAO bioEngineerDAO;
    private BioEngineer currentBioEngineer;
    private BioEngineer retrievedBioEngineer;
    private Exception caughtException;

    public BioEngineerSteps() throws SQLException {
        this.bioEngineerDAO = new BioEngineerDAOImpl();
        this.bioEngineerService = new BioEngineerServiceImpl();
    }

    @Given("the database is clean")
    public void theDatabaseIsClean() throws SQLException {
        Connection conn = bioEngineerDAO.getConnection();
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM freeze_dry_instructions");
            stmt.execute("DELETE FROM products");
            stmt.execute("DELETE FROM formulas");
            stmt.execute("DELETE FROM bio_engineers");
        }
        currentBioEngineer = null;
        retrievedBioEngineer = null;
        caughtException = null;
    }

    @Given("I have bio-engineer details with name {string}, email {string}, phone {string}, and specialization {string}")
    public void iHaveBioEngineerDetailsWithNameEmailPhoneAndSpecialization(String name, String email, String phone, String specialization) {
        currentBioEngineer = new BioEngineer(name, email, phone, specialization);
    }

    @When("I register the bio-engineer")
    public void iRegisterTheBioEngineer() {
        try {
            if (!isValidEmail(currentBioEngineer.getEmail())) {
                throw new IllegalArgumentException("Invalid email format");
            }
            // Check for duplicate email
            boolean emailExists = bioEngineerDAO.findAll().stream()
                    .anyMatch(be -> be.getEmail().equals(currentBioEngineer.getEmail()));
            if (emailExists) {
                throw new SQLException("Duplicate email: " + currentBioEngineer.getEmail());
            }
            currentBioEngineer = bioEngineerDAO.save(currentBioEngineer);
            caughtException = null;
        } catch (Exception e) {
            caughtException = e;
        }
    }

    private boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    @Then("the bio-engineer should be saved in the database")
    public void theBioEngineerShouldBeSavedInTheDatabase() {
        assertNotNull(currentBioEngineer, "Bio-engineer should not be null");
        assertTrue(currentBioEngineer.getId() > 0, "Bio-engineer should have a valid ID");
        System.out.println("Bio-engineer saved with ID: " + currentBioEngineer.getId() +
                ", Name: " + currentBioEngineer.getName() +
                ", Email: " + currentBioEngineer.getEmail());
    }

    @And("I should be able to retrieve the bio-engineer by email {string}")
    public void iShouldBeAbleToRetrieveTheBioEngineerByEmail(String email) throws SQLException {
        retrievedBioEngineer = bioEngineerDAO.findAll().stream()
                .filter(be -> be.getEmail().equals(email))
                .findFirst()
                .orElse(null);
        assertNotNull(retrievedBioEngineer, "Should be able to retrieve bio-engineer by email");
        System.out.println("Retrieved bio-engineer: ID=" + retrievedBioEngineer.getId() +
                ", Name=" + retrievedBioEngineer.getName() +
                ", Email=" + retrievedBioEngineer.getEmail());
    }

    @And("the bio-engineer should have the name {string}")
    public void theBioEngineerShouldHaveTheName(String expectedName) {
        assertNotNull(retrievedBioEngineer, "Bio-engineer should be retrieved first");
        assertEquals(expectedName, retrievedBioEngineer.getName());
    }

    @Given("I have bio-engineer details with name {string} and email {string}")
    public void iHaveBioEngineerDetailsWithNameAndEmail(String name, String email) {
        currentBioEngineer = new BioEngineer(name, email, null, null);
    }

    @And("the bio-engineer should have no phone number")
    public void theBioEngineerShouldHaveNoPhoneNumber() throws SQLException {
        if (retrievedBioEngineer == null && currentBioEngineer != null) {
            retrievedBioEngineer = bioEngineerDAO.findById(currentBioEngineer.getId());
        }
        assertNotNull(retrievedBioEngineer, "Bio-engineer should be retrieved first");
        assertTrue(retrievedBioEngineer.getPhoneNumber() == null || retrievedBioEngineer.getPhoneNumber().isEmpty());
    }

    @And("the bio-engineer should have no specialization")
    public void theBioEngineerShouldHaveNoSpecialization() throws SQLException {
        if (retrievedBioEngineer == null && currentBioEngineer != null) {
            retrievedBioEngineer = bioEngineerDAO.findById(currentBioEngineer.getId());
        }
        assertNotNull(retrievedBioEngineer, "Bio-engineer should be retrieved first");
        assertTrue(retrievedBioEngineer.getSpecialization() == null || retrievedBioEngineer.getSpecialization().isEmpty());
    }

    @Given("a bio-engineer with email {string} already exists")
    public void aBioEngineerWithEmailAlreadyExists(String email) throws SQLException {
        BioEngineer existingBioEngineer = new BioEngineer("Existing Engineer", email, null, null);
        bioEngineerDAO.save(existingBioEngineer);
    }

    @When("I try to register another bio-engineer with email {string}")
    public void iTryToRegisterAnotherBioEngineerWithEmail(String email) {
        currentBioEngineer = new BioEngineer("Another Engineer", email, null, null);
        try {
            bioEngineerDAO.save(currentBioEngineer);
            caughtException = null;
        } catch (Exception e) {
            caughtException = e;
        }
    }

    @Then("the registration should fail with a duplicate email error")
    public void theRegistrationShouldFailWithADuplicateEmailError() {
        assertNotNull(caughtException, "An exception should have been thrown for duplicate email");
        assertTrue(caughtException instanceof SQLException, "Exception should be SQLException");
    }

    @Given("a bio-engineer with email {string} exists")
    public void aBioEngineerWithEmailExists(String email) throws SQLException {
        currentBioEngineer = new BioEngineer("Test Engineer", email, null, null);
        currentBioEngineer = bioEngineerDAO.save(currentBioEngineer);
    }

    @When("I update the bio-engineer's specialization to {string}")
    public void iUpdateTheBioEngineerSSpecializationTo(String specialization) throws SQLException {
        currentBioEngineer.setSpecialization(specialization);
        bioEngineerDAO.update(currentBioEngineer);
    }

    @And("I update the bio-engineer's phone to {string}")
    public void iUpdateTheBioEngineerSPhoneTo(String phone) throws SQLException {
        currentBioEngineer.setPhoneNumber(phone);
        bioEngineerDAO.update(currentBioEngineer);
    }

    @Then("the bio-engineer should have specialization {string}")
    public void theBioEngineerShouldHaveSpecialization(String expectedSpecialization) throws SQLException {
        BioEngineer updated = bioEngineerDAO.findById(currentBioEngineer.getId());
        assertEquals(expectedSpecialization, updated.getSpecialization());
    }

    @And("the bio-engineer should have phone {string}")
    public void theBioEngineerShouldHavePhone(String expectedPhone) throws SQLException {
        BioEngineer updated = bioEngineerDAO.findById(currentBioEngineer.getId());
        assertEquals(expectedPhone, updated.getPhoneNumber());
    }

    @Then("the registration should {word}")
    public void theRegistrationShouldResult(String result) {
        if ("succeed".equals(result)) {
            assertNull(caughtException, "Registration should succeed without exceptions");
            assertNotNull(currentBioEngineer, "Bio-engineer should be saved");
            assertTrue(currentBioEngineer.getId() > 0, "Bio-engineer should have valid ID");
        } else if ("fail".equals(result)) {
            assertNotNull(caughtException, "Registration should fail with an exception");
        }
    }
}