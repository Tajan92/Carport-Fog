package app.persistence;
import app.entities.Customer;
import app.entities.SalesRep;
import app.entities.User;
import app.exceptions.DatabaseException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper {

    private ConnectionPool connectionPool;

    public UserMapper() {
        this.connectionPool = ConnectionPool.getInstance();
    }

    public Customer customerLogin(String typedEmail, String typedPassword) throws DatabaseException {
        String sql = "select * from customers join zip_codes using (zip_code) where email=? and password=?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setString(1, typedEmail);
            ps.setString(2, typedPassword);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("customer_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                String password = rs.getString("password");
                String phoneNumber = rs.getString("phone_number");
                String address = rs.getString("address");
                String zipCode = rs.getString("zip_code");
                String city = rs.getString("town");

                return new Customer(id, firstName, lastName, email, password, phoneNumber, address, zipCode, city);



            } else
            {
                throw new DatabaseException("And error occurred trying to log in the user");
            }
        }
        catch (SQLException e)
        {
            String errorMessage = "Kunne ikke logge ind med de oplysninger, prøv igen og dobbelt tjek at dine informationer er korrekte";
            throw new DatabaseException(errorMessage, e.getMessage());
        }
    }


    public SalesRep salesRepLogin(String typedEmail, String typedPassword) throws DatabaseException {
        String sql = "select * from sales_rep where email=? and password=?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setString(1, typedEmail);
            ps.setString(2, typedPassword);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("sales_rep_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                String password = rs.getString("password");
                String phoneNumber = rs.getString("phone_number");

                return new SalesRep(id, firstName, lastName, email, password, phoneNumber);


            } else
            {
                throw new DatabaseException("And error occurred trying to log in the sales rep");
            }
        }
        catch (SQLException e)
        {
            String errorMessage = "Kunne ikke logge ind med de oplysninger, prøv igen og dobbelt tjek at dine informationer er korrekte";
            throw new DatabaseException(errorMessage, e.getMessage());
        }
    }


}
