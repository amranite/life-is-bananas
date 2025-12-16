package be.lifeisbananas.service;

import be.lifeisbananas.domain.Product;
import java.sql.SQLException;
import java.util.List;

/**
 * Service interface for Product business logic.
 * Defines operations for managing products in the system.
 */
public interface ProductService {

    Product add() throws SQLException;

    void save(Product product) throws SQLException;
    Product findById(int id) throws SQLException;
    List<Product> findAll() throws SQLException;
    void update(Product product) throws SQLException;
    void delete(Product product) throws SQLException;

    // Business methods
    void markAsTested(Product product) throws SQLException;
    void approve(Product product) throws SQLException;
    void startProduction(Product product) throws SQLException;
}
