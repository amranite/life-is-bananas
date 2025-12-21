package be.lifeisbananas.dao.impl;

import be.lifeisbananas.dao.FormulaDAO;
import be.lifeisbananas.domain.Formula;
import be.lifeisbananas.domain.BioEngineer;
import be.lifeisbananas.domain.Ingredient;
import be.lifeisbananas.domain.RawMaterial;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of FormulaDAO interface.
 * Handles database operations for Formula entities.
 */
public class FormulaDAOImpl extends AbstractDAOImpl<Formula> implements FormulaDAO {

    public FormulaDAOImpl() throws SQLException {
        super();
    }

    @Override
    public Formula save(Formula formula) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO formulas (name, version, preparation_method, creation_date, last_modified, bio_engineer_id) VALUES (?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, formula.getName());
            stmt.setString(2, formula.getVersion());
            stmt.setString(3, formula.getPreparationMethod());
            stmt.setDate(4, Date.valueOf(formula.getCreationDate()));
            stmt.setDate(5, Date.valueOf(formula.getLastModified()));
            stmt.setInt(6, formula.getBioEngineer().getId());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    formula.setFormulaID(generatedKeys.getInt(1));
                }
            }
        }
        return formula;
    }

    @Override
    public Formula findById(int id) throws SQLException {
        return executeQuery("SELECT * FROM formulas WHERE id = " + id, rs -> {
            if (rs.next()) {
                Formula formula = new Formula();
                formula.setFormulaID(rs.getInt("id"));
                formula.setName(rs.getString("name"));
                formula.setVersion(rs.getString("version"));
                formula.setPreparationMethod(rs.getString("preparation_method"));
                formula.setCreationDate(rs.getDate("creation_date").toLocalDate());
                formula.setLastModified(rs.getDate("last_modified").toLocalDate());

                // Load BioEngineer relationship
                int bioEngineerId = rs.getInt("bio_engineer_id");
                if (bioEngineerId > 0) {
                    BioEngineer bioEngineer = getBioEngineerById(bioEngineerId);
                    formula.setBioEngineer(bioEngineer);
                }

                // Load Ingredients relationship
                List<Ingredient> ingredients = getIngredientsForFormula(formula);
                formula.setIngredients(ingredients);

                return List.of(formula);
            }
            return List.of();
        }).stream().findFirst().orElse(null);
    }

    private BioEngineer getBioEngineerById(int id) throws SQLException {
        String sql = "SELECT * FROM bio_engineers WHERE id = " + id;
        BioEngineer bioEngineer = new BioEngineer();

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                bioEngineer.setId(rs.getInt("id"));
                bioEngineer.setName(rs.getString("name"));
                bioEngineer.setEmail(rs.getString("email"));
                bioEngineer.setPhoneNumber(rs.getString("phone_number"));
                bioEngineer.setSpecialization(rs.getString("specialization"));
            }
        }
        return bioEngineer;
    }

    private List<Ingredient> getIngredientsForFormula(Formula formula) throws SQLException {
        String sql = "SELECT * FROM ingredients WHERE formula_id = " + formula.getFormulaID() + " ORDER BY sequence";
        List<Ingredient> ingredients = new ArrayList<>();

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Ingredient ingredient = new Ingredient();
                ingredient.setId(rs.getInt("id"));
                ingredient.setQuantity(rs.getDouble("quantity"));
                ingredient.setUnit(be.lifeisbananas.domain.UnitType.valueOf(rs.getString("unit")));
                ingredient.setSequence(rs.getInt("sequence"));
                ingredient.setFormula(formula);

                // Load RawMaterial relationship (Grondstof)
                int rawMaterialId = rs.getInt("raw_material_id");
                if (!rs.wasNull()) {
                    be.lifeisbananas.domain.RawMaterial rawMaterial = getRawMaterialById(rawMaterialId);
                    ingredient.setRawMaterial(rawMaterial);
                }

                ingredients.add(ingredient);
            }
        }

        return ingredients;
    }

    private be.lifeisbananas.domain.RawMaterial getRawMaterialById(int id) throws SQLException {
        String sql = "SELECT * FROM raw_materials WHERE id = " + id;

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                String materialType = rs.getString("material_type");
                be.lifeisbananas.domain.RawMaterial rawMaterial;

                if ("FRUIT".equals(materialType)) {
                    be.lifeisbananas.domain.FruitRawMaterial fruit = new be.lifeisbananas.domain.FruitRawMaterial();
                    fruit.setRipenessLevel(rs.getString("ripeness_level"));
                    double sugarContent = rs.getDouble("sugar_content");
                    if (!rs.wasNull()) fruit.setSugarContent(sugarContent);
                    double acidityLevel = rs.getDouble("acidity_level");
                    if (!rs.wasNull()) fruit.setAcidityLevel(acidityLevel);
                    rawMaterial = fruit;
                } else {
                    be.lifeisbananas.domain.VegetableRawMaterial vegetable = new be.lifeisbananas.domain.VegetableRawMaterial();
                    vegetable.setFirmness(rs.getString("firmness"));
                    double moistureContent = rs.getDouble("moisture_content");
                    if (!rs.wasNull()) vegetable.setMoistureContent(moistureContent);
                    rawMaterial = vegetable;
                }

                rawMaterial.setRawMaterialID(rs.getInt("id"));
                rawMaterial.setName(rs.getString("name"));
                boolean organicCertified = rs.getBoolean("organic_certified");
                if (!rs.wasNull()) rawMaterial.setOrganicCertified(organicCertified);
                double price = rs.getDouble("price");
                if (!rs.wasNull()) rawMaterial.setPrice(price);
                rawMaterial.setUnit(rs.getString("unit"));

                return rawMaterial;
            }
        }
        return null;
    }

    @Override
    public List<Formula> findAll() throws SQLException {
        List<Formula> formulas = new ArrayList<>();
        String query = "SELECT * FROM formulas";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Formula formula = new Formula();
                formula.setFormulaID(resultSet.getInt("id"));
                formula.setName(resultSet.getString("name"));
                formula.setVersion(resultSet.getString("version"));
                formula.setPreparationMethod(resultSet.getString("preparation_method"));
                formula.setCreationDate(resultSet.getDate("creation_date").toLocalDate());
                formula.setLastModified(resultSet.getDate("last_modified").toLocalDate());

                // Load BioEngineer relationship
                int bioEngineerId = resultSet.getInt("bio_engineer_id");
                if (bioEngineerId > 0) {
                    BioEngineer bioEngineer = getBioEngineerById(bioEngineerId);
                    formula.setBioEngineer(bioEngineer);
                }

                // Load Ingredients relationship
                List<Ingredient> ingredients = getIngredientsForFormula(formula);
                formula.setIngredients(ingredients);

                formulas.add(formula);
            }
        }
        return formulas;
    }

    @Override
    public void update(Formula formula) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(
                "UPDATE formulas SET name = ?, version = ?, preparation_method = ?, last_modified = ?, bio_engineer_id = ? WHERE id = ?")) {
            stmt.setString(1, formula.getName());
            stmt.setString(2, formula.getVersion());
            stmt.setString(3, formula.getPreparationMethod());
            stmt.setDate(4, Date.valueOf(formula.getLastModified()));
            stmt.setInt(5, formula.getBioEngineer().getId());
            stmt.setInt(6, formula.getFormulaID());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(Formula formula) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM formulas WHERE id = ?")) {
            stmt.setInt(1, formula.getFormulaID());
            stmt.executeUpdate();
        }
    }

    @Override
    public RawMaterial findRawMaterialByName(String name) throws SQLException {
        String sql = "SELECT * FROM raw_materials WHERE name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return getRawMaterialById(rs.getInt("id"));
                }
            }
        }
        return null;
    }

    @Override
    public void addIngredient(int formulaId, int rawMaterialId, double quantity, be.lifeisbananas.domain.UnitType unit, int sequence) throws SQLException {
        String sql = "INSERT INTO ingredients (formula_id, raw_material_id, quantity, unit, sequence) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, formulaId);
            stmt.setInt(2, rawMaterialId);
            stmt.setDouble(3, quantity);
            stmt.setString(4, unit.toString());
            stmt.setInt(5, sequence);
            stmt.executeUpdate();
        }
    }
}