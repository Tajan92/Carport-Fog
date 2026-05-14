package app.persistence;
import app.entities.Carport;
import app.entities.Shed;
import app.exceptions.DatabaseException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShedMapper {
    private ConnectionPool connectionPool;

    public ShedMapper() {
        this.connectionPool = ConnectionPool.getInstance();
    }

    public int createShed(Shed shed) throws DatabaseException {

        String sql = "insert into sheds (shed_width, shed_length, siding, floor) values (?,?,?,?)";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setDouble(1, shed.getWidth());
            preparedStatement.setDouble(2, shed.getLength());
            preparedStatement.setString(3, shed.getSiding());
            preparedStatement.setBoolean(4, shed.isFloor());

            int rowsAffected = preparedStatement.executeUpdate();
            int shedId = 0;
            if (rowsAffected != 1) {
                throw new DatabaseException("An error occurred trying to insert values in the sheds table");
            }
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                shedId = resultSet.getInt("shed_id");
            }
            return shedId;
        } catch (SQLException e) {
            String errorMessage = "Fejl ved oprettelse af skur, prøv at genindlæs siden eller kontakt os, hvis problemet stadig er der";
            throw new DatabaseException(errorMessage, e.getMessage());
        }
    }

    public Shed getShedById(int shedId) throws DatabaseException {

        String sql = "select * from sheds where shed_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, shedId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                double width = resultSet.getDouble("shed_width");
                double length = resultSet.getDouble("shed_length");
                String siding = resultSet.getString("siding");
                boolean hasFloor = resultSet.getBoolean("floor");

                return new Shed(shedId, width, length, siding, hasFloor);
            } else {
                throw new DatabaseException("An error occurred, when trying to get shed by provided id: " + shedId);
            }
        } catch (SQLException e) {
            String message = "Fejl ved at hente skur";
            throw new DatabaseException(message, e.getMessage());
        }
    }

    public void removeShedById(int shedId) throws DatabaseException {
        String sql = "delete from sheds where shed_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, shedId);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("An error occurred trying to remove a shed from DB");
            }
        } catch (SQLException e) {
            String errorMessage = "Fejl ved forsøg på at fjerne skur";
            throw new DatabaseException(errorMessage, e.getMessage());
        }
    }

    public List<Shed> getAllSheds() throws DatabaseException {
        List<Shed> sheds = new ArrayList<>();
        String sql = "select * from sheds";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                double width = resultSet.getDouble("shed_width");
                double length = resultSet.getDouble("shed_length");
                String siding = resultSet.getString("siding");
                boolean floor = resultSet.getBoolean("floor");

                sheds.add(new Shed(width, length, siding, floor));
            }
            return sheds;
        } catch (SQLException e) {
            String message = "Fejl ved at hente alle skur";
            throw new DatabaseException(message, e.getMessage());
        }
    }
}
