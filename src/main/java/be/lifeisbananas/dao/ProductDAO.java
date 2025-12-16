package be.lifeisbananas.dao;

import be.lifeisbananas.domain.Product;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Data Access Object interface for Product entity.
 * Defines CRUD operations for managing products in the database.
 */
public interface ProductDAO {
    /**
     * Saves a new product to the database
     * @param product the product to save
     * @return the saved product with generated ID
     * @throws SQLException if a database error occurs
     */
    Product save(Product product) throws SQLException;

    /**
     * Finds a product by its ID
     * @param id the product ID
     * @return the found product, or null if not found
     * @throws SQLException if a database error occurs
     */
    Product findById(int id) throws SQLException;

    /**
     * Retrieves all products from the database
     * @return list of all products
     * @throws SQLException if a database error occurs
     */
    List<Product> findAll() throws SQLException;

    /**
     * Updates an existing product
     * @param product the product to update
     * @throws SQLException if a database error occurs
     */
    void update(Product product) throws SQLException;

    /**
     * Deletes a product from the database
     * @param product the product to delete
     * @throws SQLException if a database error occurs
     */
    void delete(Product product) throws SQLException;

    /**
     * Returns the database connection
     * @return the database connection
     */
    Connection getConnection();
}
