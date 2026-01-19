package server;

import java.sql.SQLException;
import java.util.List;

public class ProductService implements ProductServiceInterface {
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
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Product> ProductList() {
        try {
            return repository.findByAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Product ProductDetail(int id) {
        try {
            return repository.findById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void ProductDelete(int id) {
        try {
            repository.delete(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
