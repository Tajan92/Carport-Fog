package app.persistence;

import app.entities.Carport;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CarportMapper {
    private ConnectionPool connectionPool;

    public CarportMapper() {
        this.connectionPool = ConnectionPool.getInstance();
    }

    public int createCarport(Carport carport, int addonId, int partsListId) throws DatabaseException {

        String sql = "insert into carports (carport_width, carport_height, carport_length, addon_id, price, parts_list_id) values (?,?,?,?,?,?)";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setDouble(1, carport.getWidth());
            preparedStatement.setDouble(2, carport.getHeight());
            preparedStatement.setDouble(3, carport.getLength());
            preparedStatement.setInt(4, addonId);
            preparedStatement.setDouble(5, carport.getPrice());
            preparedStatement.setInt(6, partsListId);


            int rowsAffected = preparedStatement.executeUpdate();
            int carportId = 0;
            if (rowsAffected != 1) {
                throw new DatabaseException("An error occurred trying to insert values in the carports table");
            }
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                carportId = resultSet.getInt("carport_id");
            }
            return carportId;
        } catch (SQLException e) {
            String errorMessage = "Fejl ved oprettelse af carport, prøv at genindlæs siden eller kontakt os, hvis problemet stadig er der";
            throw new DatabaseException(errorMessage, e.getMessage());
        }
    }

    public Carport getCarportById(int carportId, int partsListId, int shedId, int roofId) throws DatabaseException {

        String sql = "select carport_width, carport_height, carport_length, price from carports where carport_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, carportId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                double width = resultSet.getDouble("carport_width");
                double height = resultSet.getDouble("carport_height");
                double length = resultSet.getDouble("carport_length");
                double price = resultSet.getDouble("price");

                return new Carport(carportId, width, height, length, price, partsListId, shedId, roofId);
            } else {
                throw new DatabaseException("An error occurred, when trying to get carport by provided id: " + carportId);
            }
        } catch (SQLException e) {
            String message = "Fejl ved at hente carport";
            throw new DatabaseException(message, e.getMessage());
        }
    }

    public List<Carport> getAllCarports() throws DatabaseException {
        List<Carport> carports = new ArrayList<>();
        String sql = "select c.carport_id, c.carport_width, c.carport_height, c.carport_length, c.price, c.parts_list_id, s.shed_id, r.roof_id from carports c \n" +
                "left join addons using(addon_id) \n" +
                "left join sheds s using(shed_id)\n" +
                "left join roofs r using(roof_id)\n";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int carportId = resultSet.getInt("carport_id");
                double width = resultSet.getDouble("carport_width");
                double height = resultSet.getDouble("carport_height");
                double length = resultSet.getDouble("carport_length");
                double price = resultSet.getDouble("price");
                int partsListId = resultSet.getInt("parts_list_id");
                int shedId = resultSet.getInt("shed_id");
                int roofId = resultSet.getInt("roof_id");

                carports.add(new Carport(carportId, width, height, length, price, partsListId, shedId, roofId));
            }
            return carports;
        } catch (SQLException e) {
            String message = "Fejl ved at hente carport";
            throw new DatabaseException(message, e.getMessage());
        }
    }

    public void updateCarportById(Carport carport, int partsListId) throws DatabaseException {

        String sql = "update carports set carport_width = ?, carport_height = ?, carport_length = ?, price = ?, parts_list_id = ? where carport_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDouble(1, carport.getWidth());
            preparedStatement.setDouble(2, carport.getHeight());
            preparedStatement.setDouble(3, carport.getLength());
            preparedStatement.setDouble(4, carport.getPrice());
            preparedStatement.setInt(5, partsListId);
            preparedStatement.setInt(6, carport.getCarportId());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected != 1) {
                throw new DatabaseException("Could not update carport");
            }
        } catch (SQLException e) {
            String errorMessage = "Fejl ved opdatering af carport, prøv at genindlæs siden eller kontakt os, hvis problemet stadig er der";
            throw new DatabaseException(errorMessage, e.getMessage());
        }
    }

    public void removeCarportById(int carportId) throws DatabaseException {
        String sql = "delete from carports where carport_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, carportId);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("An error occurred trying to remove a carport from DB");
            }
        } catch (SQLException e) {
            String errorMessage = "Fejl ved forsøg på at fjerne carport";
            throw new DatabaseException(errorMessage, e.getMessage());
        }
    }
}
