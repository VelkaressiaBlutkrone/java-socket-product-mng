package server;

import java.util.List;

public interface ProductServiceInterface {
    void ProductAdd(String name, int price, int qty);

    List<Product> ProductList();

    Product ProductDetail(int id);

    void ProductDelete(int id);
}
