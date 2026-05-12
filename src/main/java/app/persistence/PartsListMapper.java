package app.persistence;

import app.entities.*;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PartsListMapper {
    private ConnectionPool connectionPool;

    public PartsListMapper() {
        this.connectionPool = ConnectionPool.getInstance();
    }

    public int createPartListId() throws DatabaseException {
        String sql = "insert into parts_lists default values";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                throw new DatabaseException("An error occurred trying to insert new parts_list");
            }
        } catch (SQLException e) {
            String errorMessage = "Fejl ved oprettelse af ny stykliste, prøv og konfigurer på nyt";
            throw new DatabaseException(errorMessage, e.getMessage());
        }
    }

    public void createProductPartsList(int productId, int partListId, double quantity) throws DatabaseException {

        String sql = "insert into products_parts_lists (product_id, parts_list_id, quantity) values (?,?,?)";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, productId);
            preparedStatement.setInt(2, partListId);
            preparedStatement.setDouble(3, quantity);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("An error occurred trying to insert values in the products_parts_lists table");
            }
        } catch (SQLException e) {
            String errorMessage = "Fejl ved tilføjelse af produkt til liste, prøv at genindlæs siden eller kontakt os, hvis problemet stadig er der";
            throw new DatabaseException(errorMessage, e.getMessage());
        }
    }

    public List<PartsListEntry> getPartsListById(int partsListId) throws DatabaseException {
        List<PartsListEntry> partList = new ArrayList<>();

        String sql = "select ppl.quantity, p.product_id, p.cost_price, p.retail_price, p.length, p.unit, p.product_group, p.description from products_parts_lists ppl \n" +
                "join products p using (product_id)\n" +
                "where parts_list_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, partsListId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                double quantity = resultSet.getDouble("quantity");
                int productId = resultSet.getInt("product_id");
                double costPrice = resultSet.getDouble("cost_price");
                double retail_price = resultSet.getDouble("retail_price");
                double length = resultSet.getDouble("length");
                String unit = resultSet.getString("unit");
                String productGroup = resultSet.getString("product_group");
                String description = resultSet.getString("description");

                Product product = new Product(productId, costPrice, retail_price, length, unit, productGroup, description);
                partList.add(new PartsListEntry(product, quantity));
            }
            return partList;
        } catch (SQLException e) {
            String message = "Fejl ved at hente styk liste";
            throw new DatabaseException(message, e.getMessage());
        }
    }
}
