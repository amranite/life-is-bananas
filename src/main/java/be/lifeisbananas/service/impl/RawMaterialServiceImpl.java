package be.lifeisbananas.service.impl;

import be.lifeisbananas.dao.RawMaterialDAO;
import be.lifeisbananas.dao.impl.RawMaterialDAOImpl;
import be.lifeisbananas.domain.RawMaterial;
import be.lifeisbananas.service.RawMaterialService;

import java.sql.SQLException;
import java.util.List;

/**
 * Implementation of RawMaterialService interface.
 * Handles business logic for RawMaterial operations.
 */
public class RawMaterialServiceImpl implements RawMaterialService {

    private RawMaterialDAO rawMaterialDAO;

    public RawMaterialServiceImpl() throws SQLException {
        this.rawMaterialDAO = new RawMaterialDAOImpl();
    }

    @Override
    public RawMaterial add() throws SQLException {
        RawMaterial rawMaterial = new RawMaterial() {}; // Anonymous subclass since RawMaterial is abstract
        rawMaterialDAO.save(rawMaterial);
        return rawMaterial;
    }

    @Override
    public void save(RawMaterial rawMaterial) throws SQLException {
        rawMaterialDAO.save(rawMaterial);
    }

    @Override
    public RawMaterial findById(int id) throws SQLException {
        return rawMaterialDAO.findById(id);
    }

    @Override
    public List<RawMaterial> findAll() throws SQLException {
        return rawMaterialDAO.findAll();
    }

    @Override
    public void update(RawMaterial rawMaterial) throws SQLException {
        rawMaterialDAO.update(rawMaterial);
    }

    @Override
    public void delete(RawMaterial rawMaterial) throws SQLException {
        rawMaterialDAO.delete(rawMaterial);
    }
}
