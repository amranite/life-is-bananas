package be.lifeisbananas.dao.impl;

import be.lifeisbananas.dao.RawMaterialDAO;
import be.lifeisbananas.domain.RawMaterial;
import be.lifeisbananas.domain.FruitRawMaterial;
import be.lifeisbananas.domain.VegetableRawMaterial;
import be.lifeisbananas.domain.NutritionalValue;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of RawMaterialDAO interface.
 * Handles database operations for RawMaterial entities using Single Table Inheritance.
 */
public class RawMaterialDAOImpl extends AbstractDAOImpl<RawMaterial> implements RawMaterialDAO {

    public RawMaterialDAOImpl() throws SQLException {
        super();
    }

    @Override
    public RawMaterial save(RawMaterial rawMaterial) throws SQLException {
        String materialType = rawMaterial instanceof FruitRawMaterial ? "FRUIT" : "VEGETABLE";

        String sql = "INSERT INTO RawMaterial (name, organicCertified, price, unit, materialType, " +
                     "ripenessLevel, sugarContent, acidityLevel, firmness, moistureContent, nutritionalValue_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, rawMaterial.getName());

            if (rawMaterial.getOrganicCertified() != null) {
                stmt.setBoolean(2, rawMaterial.getOrganicCertified());
            } else {
                stmt.setNull(2, Types.BOOLEAN);
            }

            if (rawMaterial.getPrice() != null) {
                stmt.setDouble(3, rawMaterial.getPrice());
            } else {
                stmt.setNull(3, Types.DOUBLE);
            }

            stmt.setString(4, rawMaterial.getUnit());
            stmt.setString(5, materialType);

            // Fruit-specific fields
            if (rawMaterial instanceof FruitRawMaterial) {
                FruitRawMaterial fruit = (FruitRawMaterial) rawMaterial;
                stmt.setString(6, fruit.getRipenessLevel());

                if (fruit.getSugarContent() != null) {
                    stmt.setDouble(7, fruit.getSugarContent());
                } else {
                    stmt.setNull(7, Types.DOUBLE);
                }

                if (fruit.getAcidityLevel() != null) {
                    stmt.setDouble(8, fruit.getAcidityLevel());
                } else {
                    stmt.setNull(8, Types.DOUBLE);
                }

                stmt.setNull(9, Types.VARCHAR);  // firmness
                stmt.setNull(10, Types.DOUBLE);  // moistureContent
            }
            // Vegetable-specific fields
            else if (rawMaterial instanceof VegetableRawMaterial) {
                VegetableRawMaterial vegetable = (VegetableRawMaterial) rawMaterial;
                stmt.setNull(6, Types.VARCHAR);  // ripenessLevel
                stmt.setNull(7, Types.DOUBLE);   // sugarContent
                stmt.setNull(8, Types.DOUBLE);   // acidityLevel

                stmt.setString(9, vegetable.getFirmness());

                if (vegetable.getMoistureContent() != null) {
                    stmt.setDouble(10, vegetable.getMoistureContent());
                } else {
                    stmt.setNull(10, Types.DOUBLE);
                }
            }

            // NutritionalValue
            if (rawMaterial.getNutritionalValue() != null) {
                stmt.setInt(11, rawMaterial.getNutritionalValue().getId());
            } else {
                stmt.setNull(11, Types.INTEGER);
            }

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    rawMaterial.setRawMaterialID(generatedKeys.getInt(1));
                }
            }
        }
        return rawMaterial;
    }

    @Override
    public RawMaterial findById(int id) throws SQLException {
        return executeQuery("SELECT * FROM RawMaterial WHERE rawMaterialID = " + id, rs -> {
            if (rs.next()) {
                RawMaterial rawMaterial = mapResultSetToRawMaterial(rs);
                return List.of(rawMaterial);
            }
            return List.of();
        }).stream().findFirst().orElse(null);
    }

    @Override
    public List<RawMaterial> findAll() throws SQLException {
        List<RawMaterial> rawMaterials = new ArrayList<>();
        String sql = "SELECT * FROM RawMaterial";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                RawMaterial rawMaterial = mapResultSetToRawMaterial(rs);
                rawMaterials.add(rawMaterial);
            }
        }
        return rawMaterials;
    }

    @Override
    public void update(RawMaterial rawMaterial) throws SQLException {
        String materialType = rawMaterial instanceof FruitRawMaterial ? "FRUIT" : "VEGETABLE";

        String sql = "UPDATE RawMaterial SET name = ?, organicCertified = ?, price = ?, unit = ?, " +
                     "materialType = ?, ripenessLevel = ?, sugarContent = ?, acidityLevel = ?, " +
                     "firmness = ?, moistureContent = ?, nutritionalValue_id = ? WHERE rawMaterialID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, rawMaterial.getName());

            if (rawMaterial.getOrganicCertified() != null) {
                stmt.setBoolean(2, rawMaterial.getOrganicCertified());
            } else {
                stmt.setNull(2, Types.BOOLEAN);
            }

            if (rawMaterial.getPrice() != null) {
                stmt.setDouble(3, rawMaterial.getPrice());
            } else {
                stmt.setNull(3, Types.DOUBLE);
            }

            stmt.setString(4, rawMaterial.getUnit());
            stmt.setString(5, materialType);

            // Fruit-specific fields
            if (rawMaterial instanceof FruitRawMaterial) {
                FruitRawMaterial fruit = (FruitRawMaterial) rawMaterial;
                stmt.setString(6, fruit.getRipenessLevel());

                if (fruit.getSugarContent() != null) {
                    stmt.setDouble(7, fruit.getSugarContent());
                } else {
                    stmt.setNull(7, Types.DOUBLE);
                }

                if (fruit.getAcidityLevel() != null) {
                    stmt.setDouble(8, fruit.getAcidityLevel());
                } else {
                    stmt.setNull(8, Types.DOUBLE);
                }

                stmt.setNull(9, Types.VARCHAR);  // firmness
                stmt.setNull(10, Types.DOUBLE);  // moistureContent
            }
            // Vegetable-specific fields
            else if (rawMaterial instanceof VegetableRawMaterial) {
                VegetableRawMaterial vegetable = (VegetableRawMaterial) rawMaterial;
                stmt.setNull(6, Types.VARCHAR);  // ripenessLevel
                stmt.setNull(7, Types.DOUBLE);   // sugarContent
                stmt.setNull(8, Types.DOUBLE);   // acidityLevel

                stmt.setString(9, vegetable.getFirmness());

                if (vegetable.getMoistureContent() != null) {
                    stmt.setDouble(10, vegetable.getMoistureContent());
                } else {
                    stmt.setNull(10, Types.DOUBLE);
                }
            }

            // NutritionalValue
            if (rawMaterial.getNutritionalValue() != null) {
                stmt.setInt(11, rawMaterial.getNutritionalValue().getId());
            } else {
                stmt.setNull(11, Types.INTEGER);
            }

            stmt.setInt(12, rawMaterial.getRawMaterialID());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(RawMaterial rawMaterial) throws SQLException {
        String sql = "DELETE FROM RawMaterial WHERE rawMaterialID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, rawMaterial.getRawMaterialID());
            stmt.executeUpdate();
        }
    }

    private RawMaterial mapResultSetToRawMaterial(ResultSet rs) throws SQLException {
        String materialType = rs.getString("materialType");
        RawMaterial rawMaterial;

        if ("FRUIT".equals(materialType)) {
            FruitRawMaterial fruit = new FruitRawMaterial();
            fruit.setRipenessLevel(rs.getString("ripenessLevel"));

            double sugarContent = rs.getDouble("sugarContent");
            if (!rs.wasNull()) {
                fruit.setSugarContent(sugarContent);
            }

            double acidityLevel = rs.getDouble("acidityLevel");
            if (!rs.wasNull()) {
                fruit.setAcidityLevel(acidityLevel);
            }

            rawMaterial = fruit;
        } else {
            VegetableRawMaterial vegetable = new VegetableRawMaterial();
            vegetable.setFirmness(rs.getString("firmness"));

            double moistureContent = rs.getDouble("moistureContent");
            if (!rs.wasNull()) {
                vegetable.setMoistureContent(moistureContent);
            }

            rawMaterial = vegetable;
        }

        // Set common fields
        rawMaterial.setRawMaterialID(rs.getInt("rawMaterialID"));
        rawMaterial.setName(rs.getString("name"));

        boolean organicCertified = rs.getBoolean("organicCertified");
        if (!rs.wasNull()) {
            rawMaterial.setOrganicCertified(organicCertified);
        }

        double price = rs.getDouble("price");
        if (!rs.wasNull()) {
            rawMaterial.setPrice(price);
        }

        rawMaterial.setUnit(rs.getString("unit"));

        // Load NutritionalValue if exists
        int nutritionalValueId = rs.getInt("nutritionalValue_id");
        if (!rs.wasNull()) {
            NutritionalValue nutritionalValue = getNutritionalValueById(nutritionalValueId);
            rawMaterial.setNutritionalValue(nutritionalValue);
        }

        return rawMaterial;
    }

    private NutritionalValue getNutritionalValueById(int id) throws SQLException {
        String sql = "SELECT * FROM NutritionalValue WHERE id = " + id;
        NutritionalValue nutritionalValue = new NutritionalValue();

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                nutritionalValue.setId(rs.getInt("id"));

                double calories = rs.getDouble("calories");
                if (!rs.wasNull()) {
                    nutritionalValue.setCalories(calories);
                }

                double proteins = rs.getDouble("proteins");
                if (!rs.wasNull()) {
                    nutritionalValue.setProteins(proteins);
                }

                double carbohydrates = rs.getDouble("carbohydrates");
                if (!rs.wasNull()) {
                    nutritionalValue.setCarbohydrates(carbohydrates);
                }

                double fats = rs.getDouble("fats");
                if (!rs.wasNull()) {
                    nutritionalValue.setFats(fats);
                }

                double fibers = rs.getDouble("fibers");
                if (!rs.wasNull()) {
                    nutritionalValue.setFibers(fibers);
                }

                double sugars = rs.getDouble("sugars");
                if (!rs.wasNull()) {
                    nutritionalValue.setSugars(sugars);
                }

                double salt = rs.getDouble("salt");
                if (!rs.wasNull()) {
                    nutritionalValue.setSalt(salt);
                }

                double vitaminA = rs.getDouble("vitaminA");
                if (!rs.wasNull()) {
                    nutritionalValue.setVitaminA(vitaminA);
                }

                double vitaminC = rs.getDouble("vitaminC");
                if (!rs.wasNull()) {
                    nutritionalValue.setVitaminC(vitaminC);
                }

                double calcium = rs.getDouble("calcium");
                if (!rs.wasNull()) {
                    nutritionalValue.setCalcium(calcium);
                }

                double iron = rs.getDouble("iron");
                if (!rs.wasNull()) {
                    nutritionalValue.setIron(iron);
                }
            }
        }
        return nutritionalValue;
    }
}
