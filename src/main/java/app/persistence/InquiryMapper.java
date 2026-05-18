package app.persistence;

import app.entities.Carport;
import app.entities.Inquiry;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InquiryMapper {
    private ConnectionPool connectionPool;

    public InquiryMapper() {
        this.connectionPool = ConnectionPool.getInstance();
    }

    public int createInquiry(Inquiry inquiry) throws DatabaseException {

        String sql = "insert into inquiries (customer_id, remark, carport_id) values (?,?,?)";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, inquiry.getCustomerId());
            preparedStatement.setString(2, inquiry.getRemark());
            preparedStatement.setInt(3, inquiry.getCarportId());
            int inquiryId = 0;
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("An error occurred trying to insert values in the inquiry table");
            }
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                inquiryId = resultSet.getInt("inquiry_id");
            }
            return inquiryId;
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

    public List<Inquiry> getAllInquiries() throws DatabaseException {
        List<Inquiry> inquiries = new ArrayList<>();
        String sql = "select inquiry.inquiry_id, inquiry.remark, customer.customer_id, carport.carport_id from inquiries inquiry \n" +
                "left join customers customer using(customer_id) \n" +
                "left join carports carport using(carport_id)\n";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int inquiryId = resultSet.getInt("inquiry_id");
                String remark = resultSet.getString("remark");
                int customerId = resultSet.getInt("customer_id");
                int carportId = resultSet.getInt("carport_id");

                inquiries.add(new Inquiry(inquiryId, customerId, remark, carportId));
            }
            return inquiries;
        } catch (SQLException e) {
            String message = "Fejl ved at hente alle forespørgsler";
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
