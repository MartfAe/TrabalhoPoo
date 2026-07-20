package br.edu.ifba.inf008.plugins.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifba.inf008.plugins.domain.Product;

public class JdbcProductRepository implements ProductRepository {

    private static final String FIND_ALL_ACTIVE_SQL =
        "SELECT p.sku, p.name, p.description, p.unit_price, " +
        "       COALESCE(SUM(CASE " +
        "           WHEN sm.movement_type = 'INBOUND' THEN sm.quantity " +
        "           WHEN sm.movement_type IN ('OUTBOUND', 'RESERVED') THEN -sm.quantity " +
        "           ELSE 0 END), 0) AS stock_quantity " +
        "FROM products p " +
        "LEFT JOIN stock_movements sm ON sm.product_id = p.id " +
        "WHERE p.active = TRUE " +
        "GROUP BY p.id, p.sku, p.name, p.description, p.unit_price " +
        "ORDER BY p.name";

    @Override
    public List<Product> findAllActive() throws SQLException {
        List<Product> products = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_ACTIVE_SQL);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                products.add(new Product(
                    resultSet.getString("name"),
                    resultSet.getString("sku"),
                    resultSet.getString("description"),
                    resultSet.getDouble("unit_price"),
                    resultSet.getInt("stock_quantity")
                ));
            }
        }

        return products;
    }
}