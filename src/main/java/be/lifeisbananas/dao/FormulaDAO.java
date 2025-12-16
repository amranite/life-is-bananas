package be.lifeisbananas.dao;

import be.lifeisbananas.domain.Formula;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Data Access Object interface for Formula entity.
 * Defines CRUD operations for managing formulas in the database.
 */
public interface FormulaDAO {
    Formula save(Formula formula) throws SQLException;
    Formula findById(int id) throws SQLException;
    List<Formula> findAll() throws SQLException;
    void update(Formula formula) throws SQLException;
    void delete(Formula formula) throws SQLException;
    Connection getConnection();
}
