package be.lifeisbananas.dao.impl;

import java.sql.*;
import java.util.List;

/**
 * Abstract base class for all DAO implementations.
 * Provides common database connection management and utility methods.
 *
 * @param <T> the entity type this DAO handles
 */
public abstract class AbstractDAOImpl<T> {

    protected static Connection connection;

    /**
     * Constructor that establishes database connection if needed
     * @throws SQLException if database connection fails
     */
    public AbstractDAOImpl() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/life_is_bananas",
                    "lifeisbananasuser",
                    "lifeisbananasuserpw");
        }
    }

    /**
     * Saves a new entity to the database
     * @param entity the entity to save
     * @return the saved entity with generated ID
     * @throws SQLException if database operation fails
     */
    public abstract T save(T entity) throws SQLException;

    /**
     * Finds an entity by its ID
     * @param id the ID to search for
     * @return the entity if found, null otherwise
     * @throws SQLException if database operation fails
     */
    public abstract T findById(int id) throws SQLException;

    /**
     * Retrieves all entities from the database
     * @return list of all entities
     * @throws SQLException if database operation fails
     */
    public abstract List<T> findAll() throws SQLException;

    /**
     * Updates an existing entity in the database
     * @param entity the entity to update
     * @throws SQLException if database operation fails
     */
    public abstract void update(T entity) throws SQLException;

    /**
     * Deletes an entity from the database
     * @param entity the entity to delete
     * @throws SQLException if database operation fails
     */
    public abstract void delete(T entity) throws SQLException;

    /**
     * Utility method for executing SELECT queries with custom result processing
     * @param sql the SQL query to execute
     * @param processor the processor to transform ResultSet into entities
     * @return list of entities
     * @throws SQLException if database operation fails
     */
    protected List<T> executeQuery(String sql, ResultSetProcessor<T> processor) throws SQLException {
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return processor.process(rs);
        }
    }

    /**
     * Functional interface for processing ResultSet into entity list
     * @param <T> the entity type
     */
    @FunctionalInterface
    public interface ResultSetProcessor<T> {
        List<T> process(ResultSet rs) throws SQLException;
    }

    /**
     * Gets the current database connection
     * @return the connection
     */
    public Connection getConnection() {
        return connection;
    }
}
