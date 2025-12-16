package be.lifeisbananas.service;

import be.lifeisbananas.domain.RawMaterial;
import java.sql.SQLException;
import java.util.List;

/**
 * Service interface for RawMaterial business logic.
 * Defines operations for managing raw materials in the system.
 */
public interface RawMaterialService {

    RawMaterial add() throws SQLException;

    void save(RawMaterial rawMaterial) throws SQLException;
    RawMaterial findById(int id) throws SQLException;
    List<RawMaterial> findAll() throws SQLException;
    void update(RawMaterial rawMaterial) throws SQLException;
    void delete(RawMaterial rawMaterial) throws SQLException;
}
