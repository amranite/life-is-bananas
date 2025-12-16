package be.lifeisbananas.service;

import be.lifeisbananas.domain.BioEngineer;
import be.lifeisbananas.domain.Formula;
import be.lifeisbananas.domain.Product;
import be.lifeisbananas.domain.FreezeDryInstruction;
import java.sql.SQLException;
import java.util.List;

/**
 * Service interface for BioEngineer business logic.
 * Defines operations for managing bio-engineers in the system.
 */
public interface BioEngineerService {

    BioEngineer add() throws SQLException;

    void save(BioEngineer bioEngineer) throws SQLException;
    BioEngineer findById(int id) throws SQLException;
    List<BioEngineer> findAll() throws SQLException;
    void update(BioEngineer bioEngineer) throws SQLException;
    void delete(BioEngineer bioEngineer) throws SQLException;

    // Business methods
    Formula createFormula(BioEngineer bioEngineer, Formula formula) throws SQLException;
    Product developProduct(BioEngineer bioEngineer, Product product) throws SQLException;
    FreezeDryInstruction generateFreezeDryInstruction(BioEngineer bioEngineer, FreezeDryInstruction instruction) throws SQLException;
}
