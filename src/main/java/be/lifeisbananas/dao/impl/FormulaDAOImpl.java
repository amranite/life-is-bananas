package be.lifeisbananas.dao.impl;

import be.lifeisbananas.dao.FormulaDAO;
import be.lifeisbananas.domain.Formula;
import be.lifeisbananas.domain.BioEngineer;
import be.lifeisbananas.domain.Ingredient;

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
                "INSERT INTO Formula (name, version, preparationMethod, creationDate, lastModified, bioEngineer_id) VALUES (?, ?, ?, ?, ?, ?)",
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
        return executeQuery("SELECT * FROM Formula WHERE formulaID = " + id, rs -> {
            if (rs.next()) {
                Formula formula = new Formula();
                formula.setFormulaID(rs.getInt("formulaID"));
                formula.setName(rs.getString("name"));
                formula.setVersion(rs.getString("version"));
                formula.setPreparationMethod(rs.getString("preparationMethod"));
                formula.setCreationDate(rs.getDate("creationDate").toLocalDate());
                formula.setLastModified(rs.getDate("lastModified").toLocalDate());

                // Load BioEngineer relationship
                int bioEngineerId = rs.getInt("bioEngineer_id");
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
        String sql = "SELECT * FROM BioEngineer WHERE id = " + id;
        BioEngineer bioEngineer = new BioEngineer();

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                bioEngineer.setId(rs.getInt("id"));
                bioEngineer.setName(rs.getString("name"));
                bioEngineer.setEmail(rs.getString("email"));
                bioEngineer.setPhoneNumber(rs.getString("phoneNumber"));
                bioEngineer.setSpecialization(rs.getString("specialization"));
            }
        }
        return bioEngineer;
    }

    private List<Ingredient> getIngredientsForFormula(Formula formula) throws SQLException {
        String sql = "SELECT * FROM Ingredient WHERE formula_id = " + formula.getFormulaID() + " ORDER BY volgorde";
        List<Ingredient> ingredients = new ArrayList<>();

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Ingredient ingredient = new Ingredient();
                ingredient.setId(rs.getInt("id"));
                ingredient.setQuantity(rs.getDouble("quantity"));
                ingredient.setUnit(be.lifeisbananas.domain.UnitType.valueOf(rs.getString("unit")));
                ingredient.setVolgorde(rs.getInt("volgorde"));
                ingredient.setFormula(formula);

                // Load RawMaterial relationship (Grondstof)
                int rawMaterialId = rs.getInt("rawMaterial_id");
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
        String sql = "SELECT * FROM RawMaterial WHERE rawMaterialID = " + id;

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                String materialType = rs.getString("materialType");
                be.lifeisbananas.domain.RawMaterial rawMaterial;

                if ("FRUIT".equals(materialType)) {
                    be.lifeisbananas.domain.FruitRawMaterial fruit = new be.lifeisbananas.domain.FruitRawMaterial();
                    fruit.setRipenessLevel(rs.getString("ripenessLevel"));
                    double sugarContent = rs.getDouble("sugarContent");
                    if (!rs.wasNull()) fruit.setSugarContent(sugarContent);
                    double acidityLevel = rs.getDouble("acidityLevel");
                    if (!rs.wasNull()) fruit.setAcidityLevel(acidityLevel);
                    rawMaterial = fruit;
                } else {
                    be.lifeisbananas.domain.VegetableRawMaterial vegetable = new be.lifeisbananas.domain.VegetableRawMaterial();
                    vegetable.setFirmness(rs.getString("firmness"));
                    double moistureContent = rs.getDouble("moistureContent");
                    if (!rs.wasNull()) vegetable.setMoistureContent(moistureContent);
                    rawMaterial = vegetable;
                }

                rawMaterial.setRawMaterialID(rs.getInt("rawMaterialID"));
                rawMaterial.setName(rs.getString("name"));
                boolean organicCertified = rs.getBoolean("organicCertified");
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
        String query = "SELECT * FROM Formula";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Formula formula = new Formula();
                formula.setFormulaID(resultSet.getInt("formulaID"));
                formula.setName(resultSet.getString("name"));
                formula.setVersion(resultSet.getString("version"));
                formula.setPreparationMethod(resultSet.getString("preparationMethod"));
                formula.setCreationDate(resultSet.getDate("creationDate").toLocalDate());
                formula.setLastModified(resultSet.getDate("lastModified").toLocalDate());

                // Load BioEngineer relationship
                int bioEngineerId = resultSet.getInt("bioEngineer_id");
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
                "UPDATE Formula SET name = ?, version = ?, preparationMethod = ?, lastModified = ?, bioEngineer_id = ? WHERE formulaID = ?")) {
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
        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM Formula WHERE formulaID = ?")) {
            stmt.setInt(1, formula.getFormulaID());
            stmt.executeUpdate();
        }
    }
}
