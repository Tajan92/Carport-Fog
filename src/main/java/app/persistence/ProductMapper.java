package app.persistence;

import app.entities.Product;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductMapper {
    private ConnectionPool connectionPool;

    public ProductMapper() {
        this.connectionPool = ConnectionPool.getInstance();
    }

    public Product getProductById(int productId) throws DatabaseException {

        String sql = "select * from products where product_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, productId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                double costPrice = resultSet.getDouble("cost_price");
                double retailPrice = resultSet.getDouble("retail_price");
                double length = resultSet.getDouble("length");
                String unit = resultSet.getString("unit");
                String productGroup = resultSet.getString("product_group");
                String description = resultSet.getString("description");

                return new Product(productId, costPrice, retailPrice, length, unit, productGroup, description);
            } else {
                throw new DatabaseException("An error occurred, when trying to get product by provided id: " + productId);
            }
        } catch (SQLException e) {
            String message = "Fejl ved at hente Produkt";
            throw new DatabaseException(message, e.getMessage());
        }
    }

    public List<Product> getAllProducts() throws DatabaseException {

        String sql = "select * from products";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            List<Product> allProducts = new ArrayList<>();
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int productId = resultSet.getInt("product_id");
                double costPrice = resultSet.getDouble("cost_price");
                double retailPrice = resultSet.getDouble("retail_price");
                double length = resultSet.getDouble("length");
                String unit = resultSet.getString("unit");
                String productGroup = resultSet.getString("product_group");
                String description = resultSet.getString("description");
                Product product = new Product(productId, costPrice, retailPrice, length, unit, productGroup, description);
                allProducts.add(product);
            }
            return allProducts;
        } catch (SQLException e) {
            String message = "Fejl ved at hente alle produkter";
            throw new DatabaseException(message, e.getMessage());
        }
    }
}

