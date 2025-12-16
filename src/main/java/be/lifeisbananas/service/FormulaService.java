package be.lifeisbananas.service;

import be.lifeisbananas.domain.Formula;
import be.lifeisbananas.domain.Ingredient;
import java.sql.SQLException;
import java.util.List;

/**
 * Service interface for Formula business logic.
 * Defines operations for managing formulas in the system.
 */
public interface FormulaService {

    Formula add() throws SQLException;

    void save(Formula formula) throws SQLException;
    Formula findById(int id) throws SQLException;
    List<Formula> findAll() throws SQLException;
    void update(Formula formula) throws SQLException;
    void delete(Formula formula) throws SQLException;

    // Business methods
    void addIngredient(Formula formula, Ingredient ingredient) throws SQLException;
    void removeIngredient(Formula formula, Ingredient ingredient) throws SQLException;
    void markAsModified(Formula formula) throws SQLException;
}
