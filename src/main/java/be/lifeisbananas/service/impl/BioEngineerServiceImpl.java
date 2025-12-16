package be.lifeisbananas.service.impl;

import be.lifeisbananas.dao.BioEngineerDAO;
import be.lifeisbananas.dao.impl.BioEngineerDAOImpl;
import be.lifeisbananas.domain.BioEngineer;
import be.lifeisbananas.domain.Formula;
import be.lifeisbananas.domain.Product;
import be.lifeisbananas.domain.FreezeDryInstruction;
import be.lifeisbananas.service.BioEngineerService;

import java.sql.SQLException;
import java.util.List;

/**
 * Implementation of BioEngineerService interface.
 * Handles business logic for BioEngineer operations.
 */
public class BioEngineerServiceImpl implements BioEngineerService {

    private BioEngineerDAO bioEngineerDAO;

    public BioEngineerServiceImpl() throws SQLException {
        this.bioEngineerDAO = new BioEngineerDAOImpl();
    }

    @Override
    public BioEngineer add() throws SQLException {
        BioEngineer bioEngineer = new BioEngineer();
        bioEngineerDAO.save(bioEngineer);
        return bioEngineer;
    }

    @Override
    public void save(BioEngineer bioEngineer) throws SQLException {
        bioEngineerDAO.save(bioEngineer);
    }

    @Override
    public BioEngineer findById(int id) throws SQLException {
        return bioEngineerDAO.findById(id);
    }

    @Override
    public List<BioEngineer> findAll() throws SQLException {
        return bioEngineerDAO.findAll();
    }

    @Override
    public void update(BioEngineer bioEngineer) throws SQLException {
        bioEngineerDAO.update(bioEngineer);
    }

    @Override
    public void delete(BioEngineer bioEngineer) throws SQLException {
        bioEngineerDAO.delete(bioEngineer);
    }

    @Override
    public Formula createFormula(BioEngineer bioEngineer, Formula formula) throws SQLException {
        Formula createdFormula = bioEngineer.createFormula(formula);
        bioEngineerDAO.update(bioEngineer);
        return createdFormula;
    }

    @Override
    public Product developProduct(BioEngineer bioEngineer, Product product) throws SQLException {
        Product developedProduct = bioEngineer.developProduct(product);
        bioEngineerDAO.update(bioEngineer);
        return developedProduct;
    }

    @Override
    public FreezeDryInstruction generateFreezeDryInstruction(BioEngineer bioEngineer, FreezeDryInstruction instruction) throws SQLException {
        FreezeDryInstruction generatedInstruction = bioEngineer.generateFreezeDryInstruction(instruction);
        bioEngineerDAO.update(bioEngineer);
        return generatedInstruction;
    }
}
