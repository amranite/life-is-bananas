package be.lifeisbananas.dao.impl;

import be.lifeisbananas.dao.ProductDAO;
import be.lifeisbananas.dao.FormulaDAO;
import be.lifeisbananas.dao.BioEngineerDAO;
import be.lifeisbananas.domain.Product;
import be.lifeisbananas.domain.Formula;
import be.lifeisbananas.domain.BioEngineer;
import be.lifeisbananas.domain.ProductStatus;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOImpl extends AbstractDAOImpl<Product> implements ProductDAO {

    private FormulaDAO formulaDAO;
    private BioEngineerDAO bioEngineerDAO;

    public ProductDAOImpl() throws SQLException {
        super();
    }

    public void setFormulaDAO(FormulaDAO formulaDAO) {
        this.formulaDAO = formulaDAO;
    }

    public void setBioEngineerDAO(BioEngineerDAO bioEngineerDAO) {
        this.bioEngineerDAO = bioEngineerDAO;
    }

    @Override
    public Product save(Product product) throws SQLException {
        String sql = "INSERT INTO products (name, description, shelf_life, weight, batch_number, " +
                "production_date, status, formula_id, bio_engineer_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());

            if (product.getShelfLife() != null) {
                stmt.setInt(3, product.getShelfLife());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }

            if (product.getWeight() != null) {
                stmt.setInt(4, product.getWeight());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }

            stmt.setString(5, product.getBatchNumber());

            if (product.getProductionDate() != null) {
                stmt.setDate(6, Date.valueOf(product.getProductionDate()));
            } else {
                stmt.setNull(6, Types.DATE);
            }

            if (product.getStatus() != null) {
                stmt.setString(7, product.getStatus().name());
            } else {
                stmt.setNull(7, Types.VARCHAR);
            }

            if (product.getFormula() != null) {
                stmt.setInt(8, product.getFormula().getFormulaID());
            } else {
                stmt.setNull(8, Types.INTEGER);
            }

            if (product.getBioEngineer() != null) {
                stmt.setInt(9, product.getBioEngineer().getId());
            } else {
                stmt.setNull(9, Types.INTEGER);
            }

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    product.setProductID(generatedKeys.getInt(1));
                }
            }
        }
        return product;
    }

    @Override
    public Product findById(int id) throws SQLException {
        String sql = "SELECT * FROM products WHERE id = " + id;
        return executeQuery(sql, rs -> {
            if (rs.next()) {
                Product product = mapResultSetToProduct(rs);
                return List.of(product);
            }
            return List.of();
        }).stream().findFirst().orElse(null);
    }

    @Override
    public List<Product> findAll() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Product product = mapResultSetToProduct(rs);
                products.add(product);
            }
        }
        return products;
    }

    @Override
    public void update(Product product) throws SQLException {
        String sql = "UPDATE products SET name = ?, description = ?, shelf_life = ?, weight = ?, " +
                "batch_number = ?, production_date = ?, status = ?, formula_id = ?, bio_engineer_id = ? " +
                "WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());

            if (product.getShelfLife() != null) {
                stmt.setInt(3, product.getShelfLife());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }

            if (product.getWeight() != null) {
                stmt.setInt(4, product.getWeight());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }

            stmt.setString(5, product.getBatchNumber());

            if (product.getProductionDate() != null) {
                stmt.setDate(6, Date.valueOf(product.getProductionDate()));
            } else {
                stmt.setNull(6, Types.DATE);
            }

            if (product.getStatus() != null) {
                stmt.setString(7, product.getStatus().name());
            } else {
                stmt.setNull(7, Types.VARCHAR);
            }

            if (product.getFormula() != null) {
                stmt.setInt(8, product.getFormula().getFormulaID());
            } else {
                stmt.setNull(8, Types.INTEGER);
            }

            if (product.getBioEngineer() != null) {
                stmt.setInt(9, product.getBioEngineer().getId());
            } else {
                stmt.setNull(9, Types.INTEGER);
            }

            stmt.setInt(10, product.getProductID());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(Product product) throws SQLException {
        String sql = "DELETE FROM products WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, product.getProductID());
            stmt.executeUpdate();
        }
    }

    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setProductID(rs.getInt("id"));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));

        int shelfLife = rs.getInt("shelf_life");
        if (!rs.wasNull()) {
            product.setShelfLife(shelfLife);
        }

        int weight = rs.getInt("weight");
        if (!rs.wasNull()) {
            product.setWeight(weight);
        }

        product.setBatchNumber(rs.getString("batch_number"));

        Date productionDate = rs.getDate("production_date");
        if (productionDate != null) {
            product.setProductionDate(productionDate.toLocalDate());
        }

        String statusStr = rs.getString("status");
        if (statusStr != null) {
            product.setStatus(ProductStatus.valueOf(statusStr));
        }

        // Load related Formula if exists
        int formulaId = rs.getInt("formula_id");
        if (!rs.wasNull() && formulaDAO != null) {
            Formula formula = formulaDAO.findById(formulaId);
            product.setFormula(formula);
        }

        // Load related BioEngineer if exists
        int bioEngineerId = rs.getInt("bio_engineer_id");
        if (!rs.wasNull() && bioEngineerDAO != null) {
            BioEngineer bioEngineer = bioEngineerDAO.findById(bioEngineerId);
            product.setBioEngineer(bioEngineer);
        }

        return product;
    }
}