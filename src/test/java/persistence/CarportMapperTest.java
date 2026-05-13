package persistence;

import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CarportMapperTest {

    private static final ConnectionPool connectionPool = ConnectionPool.getInstance("postgres","postgres","jdbc:postgresql://localhost:5432/%s?currentSchema=test","carport_db");

@BeforeAll
static void setupClass() throws DatabaseException {
    try(Connection connection = connectionPool.getConnection())
    {
        try(Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS test.zip_codes");
            statement.execute("DROP TABLE IF EXISTS test.products");
            statement.execute("DROP TABLE IF EXISTS test.products_parts_lists");
            statement.execute("DROP TABLE IF EXISTS test.parts_lists");
            statement.execute("DROP TABLE IF EXISTS test.roofs");
            statement.execute("DROP TABLE IF EXISTS test.sheds");
            statement.execute("DROP TABLE IF EXISTS test.addons");
            statement.execute("DROP TABLE IF EXISTS test.orders");
            statement.execute("DROP TABLE IF EXISTS test.inquries");
            statement.execute("DROP TABLE IF EXISTS test.quotes");
            statement.execute("DROP TABLE IF EXISTS test.customers");
            statement.execute("DROP TABLE IF EXISTS test.sales_reps");
            statement.execute("DROP TABLE IF EXISTS test.carports");

            statement.execute("DROP SEQUENCE IF EXISTS test.products_parts_lists_prod_parts_list_id_seq CASCADE;");
            statement.execute("DROP SEQUENCE IF EXISTS test.parts_lists_parts_list_id_seq CASCADE;");
            statement.execute("DROP SEQUENCE IF EXISTS test.sheds_shed_id_seq CASCADE;");
            statement.execute("DROP SEQUENCE IF EXISTS test.roofs_roof_id_seq CASCADE;");
            statement.execute("DROP SEQUENCE IF EXISTS test.addons_addon_id_seq CASCADE;");
            statement.execute("DROP SEQUENCE IF EXISTS test.inquiries_inquiry_id_seq CASCADE;");
            statement.execute("DROP SEQUENCE IF EXISTS test.orders_order_id_seq CASCADE;");
            statement.execute("DROP SEQUENCE IF EXISTS test.quotes_quote_id_seq CASCADE;");
            statement.execute("DROP SEQUENCE IF EXISTS test.customers_customer_id_seq CASCADE;");
            statement.execute("DROP SEQUENCE IF EXISTS test.sales_reps_sales_rep_id_seq CASCADE;");
            statement.execute("DROP SEQUENCE IF EXISTS test.carports_carport_id_seq CASCADE;");

            statement.execute("CREATE TABLE test.zip_codes AS (SELECT * from public.zip_codes) WITH NO DATA");
            statement.execute("CREATE TABLE test.products AS (SELECT * from public.products) WITH NO DATA");
            statement.execute("CREATE TABLE test.products_parts_lists AS (SELECT * from public.products_parts_lists) WITH NO DATA");
            statement.execute("CREATE TABLE test.parts_lists AS (SELECT * from public.parts_list) WITH NO DATA");
            statement.execute("CREATE TABLE test.roofs AS (SELECT * from public.roofs) WITH NO DATA");
            statement.execute("CREATE TABLE test.sheds AS (SELECT * from public.sheds) WITH NO DATA");
            statement.execute("CREATE TABLE test.addons AS (SELECT * from public.addons) WITH NO DATA");
            statement.execute("CREATE TABLE test.orders AS (SELECT * from public.orders) WITH NO DATA");
            statement.execute("CREATE TABLE test.inquiries AS (SELECT * from public.inquiries) WITH NO DATA");
            statement.execute("CREATE TABLE test.quotes AS (SELECT * from public.quotes) WITH NO DATA");
            statement.execute("CREATE TABLE test.customers AS (SELECT * from public.customers) WITH NO DATA");
            statement.execute("CREATE TABLE test.sales_reps AS (SELECT * from public.sales_reps) WITH NO DATA");
            statement.execute("CREATE TABLE test.carports AS (SELECT * from public.carports) WITH NO DATA");

            statement.execute("CREATE SEQUENCE test.products_parts_lists_prod_parts_list_id_seq");
            statement.execute("ALTER TABLE test.products_parts_lists ALTER COLUMN prod_parts_list_id SET DEFAULT nextval('test.products_parts_lists_prod_parts_list_id_seq')");

            statement.execute("CREATE SEQUENCE test.parts_lists_parts_list_id_seq");
            statement.execute("ALTER TABLE test.parts_lists ALTER COLUMN parts_list_id SET DEFAULT nextval('test.parts_lists_parts_list_id_seq')");

            statement.execute("CREATE SEQUENCE test.roofs_roof_id_seq");
            statement.execute("ALTER TABLE test.roofs ALTER COLUMN roof_id SET DEFAULT nextval('test.roofs_roof_id_seq')");

            statement.execute("CREATE SEQUENCE test.sheds_shed_id_seq");
            statement.execute("ALTER TABLE test.sheds ALTER COLUMN shed_id SET DEFAULT nextval('test.sheds_shed_id_seq')");

            statement.execute("CREATE SEQUENCE test.addons_addon_id_seq");
            statement.execute("ALTER TABLE test.addons ALTER COLUMN addon_id SET DEFAULT nextval('test.addons_addon_id_seq')");

            statement.execute("CREATE SEQUENCE test.orders_order_id_seq");
            statement.execute("ALTER TABLE test.orders ALTER COLUMN order_id SET DEFAULT nextval('test.orders_order_id_seq')");

            statement.execute("CREATE SEQUENCE test.inquiries_inquiry_id_seq");
            statement.execute("ALTER TABLE test.inquiries ALTER COLUMN inquiry_id SET DEFAULT nextval('test.inquiries_inquiry_id_seq')");

            statement.execute("CREATE SEQUENCE test.quotes_quote_id_seq");
            statement.execute("ALTER TABLE test.quotes ALTER COLUMN quote_id SET DEFAULT nextval('test.quotes_quote_id_seq')");

            statement.execute("CREATE SEQUENCE test.customers_customer_id_seq");
            statement.execute("ALTER TABLE test.customers ALTER COLUMN customer_id SET DEFAULT nextval('test.customers_customer_id_seq')");

            statement.execute("CREATE SEQUENCE test.sales_reps_sales_rep_id_seq");
            statement.execute("ALTER TABLE test.sales_reps ALTER COLUMN sales_rep_id SET DEFAULT nextval('test.sales_reps_sales_rep_id_seq')");

            statement.execute("CREATE SEQUENCE test.carports_carport_id_seq");
            statement.execute("ALTER TABLE test.carports ALTER COLUMN carport_id SET DEFAULT nextval('test.carports_carport_id_seq')");
        }
} catch (SQLException e) {
        e.printStackTrace();
        throw new DatabaseException("Database connection failed");

    }
}


    @BeforeEach
void setup(){

        try (Connection connection = connectionPool.getConnection())
        {
            try (Statement statement = connection.createStatement())
            {
                // Remove all rows from all tables
                stmt.execute("DELETE FROM test.orders");
    }

}
    @Test
}
