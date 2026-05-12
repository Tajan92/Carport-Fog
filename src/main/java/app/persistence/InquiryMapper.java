package app.persistence;

import app.entities.Carport;
import app.entities.Inquiry;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InquiryMapper {
    private ConnectionPool connectionPool;

    public InquiryMapper() {
        this.connectionPool = ConnectionPool.getInstance();
    }

    public void createInquiry(Inquiry inquiry) throws DatabaseException {

        String sql = "insert into inquiries (customer_id, remark, carport_id) values (?,?,?)";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, inquiry.getCustomerId());
            preparedStatement.setString(2, inquiry.getRemark());
            preparedStatement.setInt(3, inquiry.getCarportId());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("An error occurred trying to insert values in the inquiry table");
            }
        } catch (SQLException e) {
            String errorMessage = "Fejl ved oprettelse af inquiry, prøv at genindlæs siden eller kontakt os, hvis problemet stadig er der";
            throw new DatabaseException(errorMessage, e.getMessage());
        }
    }

    public Inquiry getInquiryById(int inquiryId) throws DatabaseException {

        String sql = "select * from inquiries where inquiry_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, inquiryId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int customerId = resultSet.getInt("customer_id");
                String remark = resultSet.getString("remark");
                int carportId = resultSet.getInt("carport_id");

                return new Inquiry(inquiryId, customerId, remark, carportId);
            } else {
                throw new DatabaseException("An error occurred, when trying to get inquiry by provided id: " + inquiryId);
            }
        } catch (SQLException e) {
            String message = "Fejl ved at hente inquiry";
            throw new DatabaseException(message, e.getMessage());
        }
    }

    public void deleteInquiryById(int inquiryId) throws DatabaseException {
        String sql = "delete from inquiries where inquiry_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, inquiryId);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("An error occurred trying to remove a inquiry from DB");
            }
        } catch (SQLException e) {
            String errorMessage = "Fejl ved forsøg på at fjerne inquiry";
            throw new DatabaseException(errorMessage, e.getMessage());
        }
    }
}
