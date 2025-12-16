package be.lifeisbananas.dao;

import be.lifeisbananas.domain.BioEngineer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Data Access Object interface for BioEngineer entity.
 * Defines CRUD operations for managing bio-engineers in the database.
 */
public interface BioEngineerDAO {
    BioEngineer save(BioEngineer bioEngineer) throws SQLException;
    BioEngineer findById(int id) throws SQLException;
    List<BioEngineer> findAll() throws SQLException;
    void update(BioEngineer bioEngineer) throws SQLException;
    void delete(BioEngineer bioEngineer) throws SQLException;
    Connection getConnection();
}
