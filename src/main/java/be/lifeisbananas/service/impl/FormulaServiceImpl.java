package be.lifeisbananas.service.impl;

import be.lifeisbananas.dao.FormulaDAO;
import be.lifeisbananas.dao.impl.FormulaDAOImpl;
import be.lifeisbananas.domain.Formula;
import be.lifeisbananas.domain.Ingredient;
import be.lifeisbananas.service.FormulaService;

import java.sql.SQLException;
import java.util.List;

/**
 * Implementation of FormulaService interface.
 * Handles business logic for Formula operations.
 */
public class FormulaServiceImpl implements FormulaService {

    private FormulaDAO formulaDAO;

    public FormulaServiceImpl() throws SQLException {
        this.formulaDAO = new FormulaDAOImpl();
    }

    @Override
    public Formula add() throws SQLException {
        Formula formula = new Formula();
        formulaDAO.save(formula);
        return formula;
    }

    @Override
    public void save(Formula formula) throws SQLException {
        formulaDAO.save(formula);
    }

    @Override
    public Formula findById(int id) throws SQLException {
        return formulaDAO.findById(id);
    }

    @Override
    public List<Formula> findAll() throws SQLException {
        return formulaDAO.findAll();
    }

    @Override
    public void update(Formula formula) throws SQLException {
        formulaDAO.update(formula);
    }

    @Override
    public void delete(Formula formula) throws SQLException {
        formulaDAO.delete(formula);
    }

    @Override
    public void addIngredient(Formula formula, Ingredient ingredient) throws SQLException {
        formula.addIngredient(ingredient);
        formulaDAO.update(formula);
    }

    @Override
    public void removeIngredient(Formula formula, Ingredient ingredient) throws SQLException {
        formula.removeIngredient(ingredient);
        formulaDAO.update(formula);
    }

    @Override
    public void markAsModified(Formula formula) throws SQLException {
        formula.markAsModified();
        formulaDAO.update(formula);
    }
}
