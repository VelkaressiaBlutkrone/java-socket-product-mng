package server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductRepository {
    private final Connection conn = DbConnection.getConnection();

    int insert(Product product) throws SQLException {
        String sql = "insert into product (name,price,qty) value(?,?,?)";
        try (PreparedStatement pstmt = Objects.requireNonNull(conn).prepareStatement(sql)) {
            pstmt.setString(1, product.getName());
            pstmt.setInt(2, product.getPrice());
            pstmt.setInt(3, product.getQty());

            return pstmt.executeUpdate();
        }
    }

    List<Product> findByAll() throws SQLException {
        String sql = "select * from product";
        List<Product> list = new ArrayList<>();
        try (PreparedStatement pstmt = Objects.requireNonNull(conn).prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Product product = Product.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .price(rs.getInt("price"))
                        .qty(rs.getInt("qty"))
                        .build();

                list.add(product);
            }
        }

        return list;
    }

    Product findById(int id) throws SQLException {
        String sql = "select * from product where id = ?";
        try (PreparedStatement pstmt = Objects.requireNonNull(conn).prepareStatement(sql)) {
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();
            rs.next();

            return Product.builder()
                    .id(rs.getInt("id"))
                    .name(rs.getString("name"))
                    .price(rs.getInt("price"))
                    .qty(rs.getInt("qty"))
                    .build();
        }

    }

    int delete(int id) throws SQLException {
        String sql = "delete from product where id = ?";
        try (PreparedStatement pstmt = Objects.requireNonNull(conn).prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate();
        }
    }
}
