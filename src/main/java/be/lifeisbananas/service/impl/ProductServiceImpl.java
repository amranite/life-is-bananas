package be.lifeisbananas.service.impl;

import be.lifeisbananas.dao.ProductDAO;
import be.lifeisbananas.dao.impl.ProductDAOImpl;
import be.lifeisbananas.domain.Product;
import be.lifeisbananas.service.ProductService;

import java.sql.SQLException;
import java.util.List;

/**
 * Implementation of ProductService interface.
 * Handles business logic for Product operations.
 */
public class ProductServiceImpl implements ProductService {

    private ProductDAO productDAO;

    public ProductServiceImpl() throws SQLException {
        this.productDAO = new ProductDAOImpl();
    }

    @Override
    public Product add() throws SQLException {
        Product product = new Product();
        productDAO.save(product);
        return product;
    }

    @Override
    public void save(Product product) throws SQLException {
        productDAO.save(product);
    }

    @Override
    public Product findById(int id) throws SQLException {
        return productDAO.findById(id);
    }

    @Override
    public List<Product> findAll() throws SQLException {
        return productDAO.findAll();
    }

    @Override
    public void update(Product product) throws SQLException {
        productDAO.update(product);
    }

    @Override
    public void delete(Product product) throws SQLException {
        productDAO.delete(product);
    }

    @Override
    public void markAsTested(Product product) throws SQLException {
        product.markAsTested();
        productDAO.update(product);
    }

    @Override
    public void approve(Product product) throws SQLException {
        product.approve();
        productDAO.update(product);
    }

    @Override
    public void startProduction(Product product) throws SQLException {
        product.startProduction();
        productDAO.update(product);
    }
}
