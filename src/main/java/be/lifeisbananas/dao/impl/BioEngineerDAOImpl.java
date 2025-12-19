package be.lifeisbananas.dao.impl;

import be.lifeisbananas.dao.BioEngineerDAO;
import be.lifeisbananas.domain.BioEngineer;
import be.lifeisbananas.domain.Formula;
import be.lifeisbananas.domain.Product;
import be.lifeisbananas.domain.FreezeDryInstruction;
import be.lifeisbananas.domain.ProductStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of BioEngineerDAO interface.
 * Handles database operations for BioEngineer entities.
 */
public class BioEngineerDAOImpl extends AbstractDAOImpl<BioEngineer> implements BioEngineerDAO {

    public BioEngineerDAOImpl() throws SQLException {
        super();
    }

    @Override
    public BioEngineer save(BioEngineer bioEngineer) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO bio_engineers (name, email, phone_number, specialization) VALUES (?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, bioEngineer.getName());
            stmt.setString(2, bioEngineer.getEmail());
            stmt.setString(3, bioEngineer.getPhoneNumber());
            stmt.setString(4, bioEngineer.getSpecialization());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    bioEngineer.setId(generatedKeys.getInt(1));
                }
            }
        }
        return bioEngineer;
    }

    @Override
    public BioEngineer findById(int id) throws SQLException {
        return executeQuery("SELECT * FROM bio_engineers WHERE id = " + id, rs -> {
            if (rs.next()) {
                BioEngineer bioEngineer = new BioEngineer();
                bioEngineer.setId(rs.getInt("id"));
                bioEngineer.setName(rs.getString("name"));
                bioEngineer.setEmail(rs.getString("email"));
                bioEngineer.setPhoneNumber(rs.getString("phone_number"));
                bioEngineer.setSpecialization(rs.getString("specialization"));

                // Load Formulas relationship
                List<Formula> formulas = getFormulasForBioEngineer(bioEngineer);
                bioEngineer.setFormulas(formulas);

                // Load Products relationship
                List<Product> products = getProductsForBioEngineer(bioEngineer);
                bioEngineer.setProducts(products);

                // Load FreezeDryInstructions relationship
                List<FreezeDryInstruction> instructions = getFreezeDryInstructionsForBioEngineer(bioEngineer);
                bioEngineer.setFreezeDryInstructions(instructions);

                return List.of(bioEngineer);
            }
            return List.of();
        }).stream().findFirst().orElse(null);
    }

    private List<Formula> getFormulasForBioEngineer(BioEngineer bioEngineer) throws SQLException {
        String sql = "SELECT * FROM formulas WHERE bio_engineer_id = " + bioEngineer.getId();
        List<Formula> formulas = new ArrayList<>();

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Formula formula = new Formula();
                formula.setFormulaID(rs.getInt("id"));
                formula.setName(rs.getString("name"));
                formula.setVersion(rs.getString("version"));
                formula.setPreparationMethod(rs.getString("preparation_method"));
                formula.setCreationDate(rs.getDate("creation_date").toLocalDate());
                formula.setLastModified(rs.getDate("last_modified").toLocalDate());
                formula.setBioEngineer(bioEngineer);
                formulas.add(formula);
            }
        }

        return formulas;
    }

    private List<Product> getProductsForBioEngineer(BioEngineer bioEngineer) throws SQLException {
        String sql = "SELECT * FROM products WHERE bio_engineer_id = " + bioEngineer.getId();
        List<Product> products = new ArrayList<>();

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Product product = new Product();
                product.setProductID(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setShelfLife(rs.getInt("shelf_life"));
                product.setWeight(rs.getInt("weight"));
                product.setBatchNumber(rs.getString("batch_number"));

                Date prodDate = rs.getDate("production_date");
                if (prodDate != null) {
                    product.setProductionDate(prodDate.toLocalDate());
                }

                String statusStr = rs.getString("status");
                if (statusStr != null) {
                    product.setStatus(ProductStatus.valueOf(statusStr));
                }

                product.setBioEngineer(bioEngineer);
                products.add(product);
            }
        }

        return products;
    }

    private List<FreezeDryInstruction> getFreezeDryInstructionsForBioEngineer(BioEngineer bioEngineer) throws SQLException {
        String sql = "SELECT * FROM freeze_dry_instructions WHERE bio_engineer_id = " + bioEngineer.getId();
        List<FreezeDryInstruction> instructions = new ArrayList<>();

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                FreezeDryInstruction instruction = new FreezeDryInstruction();
                instruction.setInstructionID(rs.getInt("id"));
                instruction.setFormulaName(rs.getString("formula_name"));
                instruction.setVersion(rs.getString("version"));
                instruction.setCreationDate(rs.getDate("creation_date").toLocalDate());
                instruction.setSteps(rs.getString("steps"));
                instruction.setRemarks(rs.getString("remarks"));
                instruction.setBioEngineer(bioEngineer);
                instructions.add(instruction);
            }
        }

        return instructions;
    }

    @Override
    public List<BioEngineer> findAll() throws SQLException {
        List<BioEngineer> bioEngineers = new ArrayList<>();
        String query = "SELECT * FROM bio_engineers";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                BioEngineer bioEngineer = new BioEngineer();
                bioEngineer.setId(resultSet.getInt("id"));
                bioEngineer.setName(resultSet.getString("name"));
                bioEngineer.setEmail(resultSet.getString("email"));
                bioEngineer.setPhoneNumber(resultSet.getString("phone_number"));
                bioEngineer.setSpecialization(resultSet.getString("specialization"));

                // Load Formulas relationship
                List<Formula> formulas = getFormulasForBioEngineer(bioEngineer);
                bioEngineer.setFormulas(formulas);

                // Load Products relationship
                List<Product> products = getProductsForBioEngineer(bioEngineer);
                bioEngineer.setProducts(products);

                // Load FreezeDryInstructions relationship
                List<FreezeDryInstruction> instructions = getFreezeDryInstructionsForBioEngineer(bioEngineer);
                bioEngineer.setFreezeDryInstructions(instructions);

                bioEngineers.add(bioEngineer);
            }
        }
        return bioEngineers;
    }

    @Override
    public void update(BioEngineer bioEngineer) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(
                "UPDATE bio_engineers SET name = ?, email = ?, phone_number = ?, specialization = ? WHERE id = ?")) {
            stmt.setString(1, bioEngineer.getName());
            stmt.setString(2, bioEngineer.getEmail());
            stmt.setString(3, bioEngineer.getPhoneNumber());
            stmt.setString(4, bioEngineer.getSpecialization());
            stmt.setInt(5, bioEngineer.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(BioEngineer bioEngineer) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM bio_engineers WHERE id = ?")) {
            stmt.setInt(1, bioEngineer.getId());
            stmt.executeUpdate();
        }
    }
}