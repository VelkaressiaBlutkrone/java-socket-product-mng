package server;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductService implements ProductServiceInterface {
    private static final Logger logger = LoggerFactory.getLogger(ProductRepository.class);
    private final ProductRepository repository = new ProductRepository();

    @Override
    public void ProductAdd(String name, int price, int qty) {
        Product product = Product.builder()
                .name(name)
                .price(price)
                .qty(qty)
                .build();
        try {
            repository.insert(product);
        } catch (SQLException e) {
            logger.atTrace().setCause(e).log(e.getMessage());
        }
    }

    @Override
    public List<Product> ProductList() {
        try {
            return repository.findByAll();
        } catch (SQLException e) {
            logger.atTrace().setCause(e).log(e.getMessage());
            return null;
        }
    }

    @Override
    public Product ProductDetail(int id) {
        try {
            return repository.findById(id);
        } catch (SQLException e) {
            logger.atTrace().setCause(e).log(e.getMessage());
            return null;
        }
    }

    @Override
    public void ProductDelete(int id) {
        try {
            repository.delete(id);
        } catch (SQLException e) {
            logger.atTrace().setCause(e).log(e.getMessage());
        }
    }
}
