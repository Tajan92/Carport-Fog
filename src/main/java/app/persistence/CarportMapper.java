package app.persistence;
import app.entities.Carport;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CarportMapper {
    private ConnectionPool connectionPool;

    public CarportMapper(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public void createCarport(Carport carport, int addonId, int partsListId) throws DatabaseException {

        String sql = "insert into carports (carport_width, carport_height, carport_length, addon_id, price, parts_list_id) values (?,?,?,?,?,?)";

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



}
