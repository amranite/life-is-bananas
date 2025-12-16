package be.lifeisbananas.dao;

import be.lifeisbananas.domain.RawMaterial;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Data Access Object interface for RawMaterial entity.
 * Defines CRUD operations for managing raw materials in the database.
 */
public interface RawMaterialDAO {
    RawMaterial save(RawMaterial rawMaterial) throws SQLException;
    RawMaterial findById(int id) throws SQLException;
    List<RawMaterial> findAll() throws SQLException;
    void update(RawMaterial rawMaterial) throws SQLException;
    void delete(RawMaterial rawMaterial) throws SQLException;
    Connection getConnection();
}
