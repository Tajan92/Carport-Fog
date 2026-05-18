package app.persistence;
import app.entities.Roof;
import app.exceptions.DatabaseException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoofMapper {
    private ConnectionPool connectionPool;

    public RoofMapper() {
        this.connectionPool = ConnectionPool.getInstance();
    }

    public int createRoof(Roof roof) throws DatabaseException {

        String sql = "insert into roofs (roof_slope, roof_material, roof_type) values (?,?,?)";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setDouble(1, roof.getRoofSlope());
            preparedStatement.setString(2, roof.getRoofMaterial());
            preparedStatement.setString(3, roof.getRoofType());

            int rowsAffected = preparedStatement.executeUpdate();
            int carportId = 0;
            if (rowsAffected != 1) {
                throw new DatabaseException("An error occurred trying to insert values in the roofs table");
            }
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                carportId = resultSet.getInt("roof_id");
            }
            return carportId;
        } catch (SQLException e) {
            String errorMessage = "Fejl ved oprettelse af tag, prøv at genindlæs siden eller kontakt os, hvis problemet stadig er der";
            throw new DatabaseException(errorMessage, e.getMessage());
        }
    }

    public Roof getRoofById(int roofId) throws DatabaseException {

        String sql = "select roof_slope, roof_material, roof_type from roofs where roof_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, roofId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                double roofSlope = resultSet.getDouble("roof_slope");
                String roofMaterial = resultSet.getString("roof_material");
                String roofType = resultSet.getString("roof_type");

                return new Roof(roofId, roofSlope, roofMaterial, roofType);
            } else {
                throw new DatabaseException("An error occurred, when trying to get roof by provided id: " + roofId);
            }
        } catch (SQLException e) {
            String message = "Fejl ved at hente tag";
            throw new DatabaseException(message, e.getMessage());
        }
    }

    public List<Roof> getAllRoofs() throws DatabaseException {
        List<Roof> allRoofs = new ArrayList<>();

        String sql = "select * from roofs";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int roofId = resultSet.getInt("roof_id");
                double slope = resultSet.getDouble("roof_slope");
                String material = resultSet.getString("roof_material");
                String type = resultSet.getString("roof_type");

                allRoofs.add(new Roof(roofId, slope, material, type));
            }
            return allRoofs;
        } catch (SQLException e) {
            String message = "Fejl ved at hente alle tag";
            throw new DatabaseException(message, e.getMessage());
        }
    }

    public void updateRoof(Roof roof) throws DatabaseException {

        String sql = "update roofs set roof_slope = ?, roof_material = ?, roof_type = ? where roof_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDouble(1, roof.getRoofSlope());
            preparedStatement.setString(2, roof.getRoofMaterial());
            preparedStatement.setString(3, roof.getRoofType());
            preparedStatement.setInt(4, roof.getRoofId());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected != 1) {
                throw new DatabaseException("Could not update roof");
            }
        } catch (SQLException e) {
            String errorMessage = "Fejl ved opdatering af tag, prøv at genindlæs siden eller kontakt os, hvis problemet stadig er der";
            throw new DatabaseException(errorMessage, e.getMessage());
        }
    }

    public void deleteRoofById(int roofId) throws DatabaseException {

        String sql = "delete from roofs where roof_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, roofId);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("An error occurred trying to remove a roof from DB");
            }
        } catch (SQLException e) {
            String errorMessage = "Fejl ved forsøg på at fjerne tag";
            throw new DatabaseException(errorMessage, e.getMessage());
        }
    }
}
