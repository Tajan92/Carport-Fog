package app.persistence;
import app.entities.Carport;
import app.entities.PartsList;
import app.entities.Roof;
import app.entities.Shed;
import app.exceptions.DatabaseException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CarportMapper {
    private ConnectionPool connectionPool;

    public CarportMapper() {
        this.connectionPool = ConnectionPool.getInstance();
    }

    public void createCarport(Carport carport, int addonId, int partsListId) throws DatabaseException {

        String sql = "insert into carports (carport_width, carport_height, carport_length, addon_id, price, parts_list_id) join zip_codes using zip_code values (?,?,?,?,?,?)";

        try(Connection connection = connectionPool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDouble(1, carport.getWidth());
            preparedStatement.setDouble(2, carport.getHeight());
            preparedStatement.setDouble(3, carport.getLength());
            preparedStatement.setInt(4, addonId);
            preparedStatement.setDouble(5, carport.getPrice());
            preparedStatement.setInt(6, partsListId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected != 1){
                throw new DatabaseException("An error occurred trying to insert values in the carports table");
            }
        } catch (SQLException e) {
            String errorMessage = "Fejl ved oprettelse af forespørgsel, prøv at genindlæs siden eller kontakt os, hvis problemet stadig er der";
            throw new DatabaseException(errorMessage, e.getMessage());
        }
    }

    public Carport getCarportById(int carportId, PartsList partsList, Shed shed, Roof roof) throws DatabaseException {

        String sql = "select carport_width, carport_height, carport_length, price from carports where carport_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, carportId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                double width = resultSet.getDouble("carport_width");
                double height = resultSet.getDouble("carport_height");
                double length = resultSet.getDouble("carport_length");
                double price = resultSet.getDouble("price");

                return new Carport(carportId,width,height,length,price, partsList, shed, roof);
            } else
            {
                throw new DatabaseException("An error occurred, when trying to get carport by provided id: " + carportId);
            }
        } catch (SQLException e) {
            String message = "Fejl ved at hente carport";
            throw new DatabaseException(message, e.getMessage());
        }
    }


    public void updateCarportById(Carport carport, int partsListId) throws DatabaseException{

        String sql = "update carports set carport_width = ?, carport_height = ?, carport_length = ?, carport_price = ?, parts_list_id = ? where carport_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDouble(1, carport.getWidth());
            preparedStatement.setDouble(2, carport.getHeight());
            preparedStatement.setDouble(3, carport.getLength());
            preparedStatement.setDouble(4, carport.getPrice());
            preparedStatement.setInt(5, partsListId);
            preparedStatement.setInt(6,carport.getCarportId());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected != 1) {
                throw new DatabaseException("Could not update carport");
            }
        }catch (SQLException e) {
            String errorMessage = "Fejl ved oprettelse af forespørgsel, prøv at genindlæs siden eller kontakt os, hvis problemet stadig er der";
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
        }catch (SQLException e) {
            String errorMessage = "Fejl ved forsøg på at fjerne carport";
            throw new DatabaseException(errorMessage, e.getMessage());
        }
    }
}
