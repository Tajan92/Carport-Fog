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
void setup() throws DatabaseException {

        try (Connection connection = connectionPool.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.execute("DELETE FROM test.products");
                statement.execute("DELETE FROM test.products_parts_lists");
                statement.execute("DELETE FROM test.parts_lists");
                statement.execute("DELETE FROM test.sheds");
                statement.execute("DELETE FROM test.roofs");
                statement.execute("DELETE FROM test.addons");
                statement.execute("DELETE FROM test.inquiries");
                statement.execute("DELETE FROM test.orders");
                statement.execute("DELETE FROM test.quotes");
                statement.execute("DELETE FROM test.customers");
                statement.execute("DELETE FROM test.sales_reps");
                statement.execute("DELETE FROM test.carports");

                statement.execute(
                        "-- customers\n" +
                                "INSERT INTO test.customers (first_name, last_name, email, password, phone_number, address, zip_code) VALUES\n" +
                                "    ('Anders',  'Jensen',   'anders@email.dk',  'hashed_pw1', '12345678', 'Elmevej 4',      '2100'),\n" +
                                "    ('Mette',   'Nielsen',  'mette@email.dk',   'hashed_pw2', '23456789', 'Birkevej 12',    '2200'),\n" +
                                "    ('Lars',    'Hansen',   'lars@email.dk',    'hashed_pw3', '34567890', 'Kastanievej 7',  '8000'),\n" +
                                "    ('Sofie',   'Pedersen', 'sofie@email.dk',   'hashed_pw4', '45678901', 'Egevej 3',       '5000'),\n" +
                                "    ('Mikkel',  'Andersen', 'mikkel@email.dk',  'hashed_pw5', '56789012', 'Lindevej 22',    '9000'),\n" +
                                "    ('Camilla', 'Christensen', 'camilla@email.dk', 'hashed_pw6', '67890123', 'Fyrvej 8',   '3000');\n" +
                                "\n" +
                                "-- sales_reps\n" +
                                "INSERT INTO test.sales_reps (first_name, last_name, email, password, phone_number) VALUES\n" +
                                "    ('Thomas',  'Møller',   'thomas@carport.dk',  'hashed_rep1', '11111111'),\n" +
                                "    ('Hanne',   'Sørensen', 'hanne@carport.dk',   'hashed_rep2', '22222222'),\n" +
                                "    ('Rasmus',  'Larsen',   'rasmus@carport.dk',  'hashed_rep3', '33333333'),\n" +
                                "    ('Gitte',   'Madsen',   'gitte@carport.dk',   'hashed_rep4', '44444444');\n" +
                                "\n" +
                                "-- sheds\n" +
                                "INSERT INTO test.sheds (shed_width, shed_length, siding, floor) VALUES\n" +
                                "    (2.40, 3.00, 'Trykimp. bræddebeklædning', true),\n" +
                                "    (2.40, 5.00, 'Trykimp. bræddebeklædning', false),\n" +
                                "    (3.00, 3.00, 'Plastik panel',              true),\n" +
                                "    (3.00, 5.00, 'Trykimp. bræddebeklædning', true),\n" +
                                "    (2.40, 2.40, 'Plastik panel',              false);\n" +
                                "\n" +
                                "-- roofs\n" +
                                "INSERT INTO test.roofs (roof_slope, roof_material, roof_type) VALUES\n" +
                                "    (15, 'Betontagsten',   'Rejsning'),\n" +
                                "    (20, 'Betontagsten',   'Rejsning'),\n" +
                                "    (25, 'Bitumentagpap',  'Rejsning'),\n" +
                                "    (30, 'Stålprofil',     'Rejsning'),\n" +
                                "    (20, 'Plastmo Ecolite','Fladt'),\n" +
                                "    (15, 'Stålprofil',     'Fladt');\n" +
                                "\n" +
                                "-- addons (shed_id and roof_id are nullable)\n" +
                                "INSERT INTO test.addons (shed_id, roof_id) VALUES\n" +
                                "    (1, 1),   -- shed + roof\n" +
                                "    (2, NULL), -- shed only\n" +
                                "    (NULL, 2), -- roof only\n" +
                                "    (3, 3),   -- shed + roof\n" +
                                "    (4, NULL), -- shed only\n" +
                                "    (NULL, 4), -- roof only\n" +
                                "    (5, 5),   -- shed + roof\n" +
                                "    (NULL, NULL); -- no addons\n" +
                                "\n" +
                                "-- parts_lists (empty containers — products are linked via products_parts_lists)\n" +
                                "INSERT INTO test.parts_lists DEFAULT VALUES; -- parts_list_id = 1\n" +
                                "INSERT INTO test.parts_lists DEFAULT VALUES; -- parts_list_id = 2\n" +
                                "INSERT INTO test.parts_lists DEFAULT VALUES; -- parts_list_id = 3\n" +
                                "INSERT INTO test.parts_lists DEFAULT VALUES; -- parts_list_id = 4\n" +
                                "INSERT INTO test.parts_lists DEFAULT VALUES; -- parts_list_id = 5\n" +
                                "INSERT INTO test.parts_lists DEFAULT VALUES; -- parts_list_id = 6\n" +
                                "\n" +
                                "-- carports\n" +
                                "INSERT INTO test.carports (c_width, c_height, c_length, addon_id, price, parts_list_id) VALUES\n" +
                                "    (3.00, 2.08, 5.00, 1,    24999.00, 1),\n" +
                                "    (3.00, 2.08, 7.80, 2,    28999.00, 2),\n" +
                                "    (4.50, 2.30, 7.80, 3,    32999.00, 3),\n" +
                                "    (6.00, 2.30, 7.80, NULL, 19999.00, 4),\n" +
                                "    (3.00, 2.08, 5.00, 4,    26999.00, 5),\n" +
                                "    (4.50, 2.30, 5.00, NULL, 21999.00, 6);\n" +
                                "\n" +
                                "-- products\n" +
                                "INSERT INTO test.products (cost_price, retail_price, length, unit, product_group, description) VALUES\n" +
                                "    (48.00,  75.00,  6.00, 'stk', 'Træ',      'Stolpe 97x97 mm trykimp.'),\n" +
                                "    (120.00, 185.00, 6.00, 'stk', 'Træ',      'Spær 45x195 mm spærtræ'),\n" +
                                "    (95.00,  149.00, 6.00, 'stk', 'Træ',      'Rem 45x195 mm spærtræ'),\n" +
                                "    (35.00,  55.00,  3.00, 'stk', 'Træ',      'Stern bræt 25x200 mm'),\n" +
                                "    (12.00,  19.00,  NULL, 'rulle','Beslag',  'Hulbånd 1x20 mm'),\n" +
                                "    (89.00,  139.00, 6.00, 'stk', 'Tag',      'Plastmo Ecolite tagplade blåtonet'),\n" +
                                "    (5.50,   8.50,   NULL, 'stk', 'Beslag',   'Universalbeslag højre'),\n" +
                                "    (5.50,   8.50,   NULL, 'stk', 'Beslag',   'Universalbeslag venstre'),\n" +
                                "    (2.50,   4.00,   NULL, 'stk', 'Skruer',   'Skruer 4,5x60 mm'),\n" +
                                "    (3.00,   4.75,   NULL, 'stk', 'Skruer',   'Skruer 4,5x50 mm'),\n" +
                                "    (45.00,  72.00,  3.00, 'stk', 'Træ',      'Vandbrædt 19x100 mm'),\n" +
                                "    (28.00,  44.00,  2.40, 'stk', 'Træ',      'Bræddebeklædning 22x100 mm trykimp.');\n" +
                                "\n" +
                                "-- products_parts_lists (which products + quantities go into each parts list)\n" +
                                "-- Parts list 1 (carport 3x5m with shed + roof)\n" +
                                "INSERT INTO test.products_parts_lists (product_id, parts_list_id, quantity) VALUES\n" +
                                "    (1,  1, 6),   -- 6 stolper\n" +
                                "    (2,  1, 10),  -- 10 spær\n" +
                                "    (3,  1, 2),   -- 2 remme\n" +
                                "    (4,  1, 4),   -- 4 stern brædder\n" +
                                "    (5,  1, 1),   -- 1 rulle hulbånd\n" +
                                "    (6,  1, 8),   -- 8 tagplader\n" +
                                "    (7,  1, 12),  -- 12 universalbeslag højre\n" +
                                "    (8,  1, 12),  -- 12 universalbeslag venstre\n" +
                                "    (9,  1, 100), -- 100 skruer 60mm\n" +
                                "    (10, 1, 100), -- 100 skruer 50mm\n" +
                                "    (11, 1, 4);   -- 4 vandbrædder\n" +
                                "\n" +
                                "-- Parts list 2 (carport 3x7.8m with shed)\n" +
                                "INSERT INTO test.products_parts_lists (product_id, parts_list_id, quantity) VALUES\n" +
                                "    (1,  2, 6),\n" +
                                "    (2,  2, 15),\n" +
                                "    (3,  2, 2),\n" +
                                "    (4,  2, 6),\n" +
                                "    (5,  2, 2),\n" +
                                "    (6,  2, 13),\n" +
                                "    (7,  2, 15),\n" +
                                "    (8,  2, 15),\n" +
                                "    (9,  2, 150),\n" +
                                "    (10, 2, 150),\n" +
                                "    (11, 2, 6);\n" +
                                "\n" +
                                "-- Parts list 3 (carport 4.5x7.8m with roof)\n" +
                                "INSERT INTO test.products_parts_lists (product_id, parts_list_id, quantity) VALUES\n" +
                                "    (1,  3, 6),\n" +
                                "    (2,  3, 15),\n" +
                                "    (3,  3, 2),\n" +
                                "    (4,  3, 6),\n" +
                                "    (5,  3, 2),\n" +
                                "    (6,  3, 18),\n" +
                                "    (7,  3, 15),\n" +
                                "    (8,  3, 15),\n" +
                                "    (9,  3, 200),\n" +
                                "    (10, 3, 200),\n" +
                                "    (11, 3, 6),\n" +
                                "    (12, 3, 24);\n" +
                                "\n" +
                                "-- Parts list 4 (carport 6x7.8m no addons)\n" +
                                "INSERT INTO test.products_parts_lists (product_id, parts_list_id, quantity) VALUES\n" +
                                "    (1,  4, 6),\n" +
                                "    (2,  4, 15),\n" +
                                "    (3,  4, 2),\n" +
                                "    (4,  4, 6),\n" +
                                "    (5,  4, 2),\n" +
                                "    (6,  4, 24),\n" +
                                "    (7,  4, 15),\n" +
                                "    (8,  4, 15),\n" +
                                "    (9,  4, 200),\n" +
                                "    (10, 4, 150),\n" +
                                "    (11, 4, 6);\n" +
                                "\n" +
                                "-- Parts list 5 (carport 3x5m with shed + roof)\n" +
                                "INSERT INTO test.products_parts_lists (product_id, parts_list_id, quantity) VALUES\n" +
                                "    (1,  5, 6),\n" +
                                "    (2,  5, 10),\n" +
                                "    (3,  5, 2),\n" +
                                "    (4,  5, 4),\n" +
                                "    (5,  5, 1),\n" +
                                "    (6,  5, 8),\n" +
                                "    (7,  5, 10),\n" +
                                "    (8,  5, 10),\n" +
                                "    (9,  5, 100),\n" +
                                "    (10, 5, 100),\n" +
                                "    (11, 5, 4),\n" +
                                "    (12, 5, 16);\n" +
                                "\n" +
                                "-- Parts list 6 (carport 4.5x5m no addons)\n" +
                                "INSERT INTO test.products_parts_lists (product_id, parts_list_id, quantity) VALUES\n" +
                                "    (1,  6, 6),\n" +
                                "    (2,  6, 10),\n" +
                                "    (3,  6, 2),\n" +
                                "    (4,  6, 4),\n" +
                                "    (5,  6, 1),\n" +
                                "    (6,  6, 12),\n" +
                                "    (7,  6, 10),\n" +
                                "    (8,  6, 10),\n" +
                                "    (9,  6, 100),\n" +
                                "    (10, 6, 100),\n" +
                                "    (11, 6, 4);\n" +
                                "\n" +
                                "-- inquiries (1-1 with carports via UNIQUE on carport_id)\n" +
                                "INSERT INTO test.inquiries (customer_id, remark, carport_id) VALUES\n" +
                                "    (1, 'Ønsker carport med skur til haveredskaber',        1),\n" +
                                "    (2, 'Skal passe til eksisterende indkørsel på 3 meter', 2),\n" +
                                "    (3, 'Vil gerne have mulighed for at tilkøbe belysning', 3),\n" +
                                "    (4, 'Ingen særlige bemærkninger',                       4),\n" +
                                "    (5, 'Carport skal matche husfarve - rød mur',           5),\n" +
                                "    (6, 'Ønsker montering inkluderet i prisen',             6);\n" +
                                "\n" +
                                "-- quotes (1-* with carports — multiple quotes per carport allowed)\n" +
                                "INSERT INTO test.quotes (quote_price, carport_id, customer_id, sales_rep_id) VALUES\n" +
                                "    (24999.00, 1, 1, 1),\n" +
                                "    (23500.00, 1, 1, 2), -- second quote on same carport (revised)\n" +
                                "    (28999.00, 2, 2, 1),\n" +
                                "    (32999.00, 3, 3, 3),\n" +
                                "    (30500.00, 3, 3, 2), -- second quote on same carport (revised)\n" +
                                "    (19999.00, 4, 4, 4),\n" +
                                "    (26999.00, 5, 5, 1),\n" +
                                "    (21999.00, 6, 6, 3);\n" +
                                "\n" +
                                "-- orders (1-1 with carports via UNIQUE on carport_id)\n" +
                                "INSERT INTO test.orders (customer_id, sales_rep_id, carport_id, order_price) VALUES\n" +
                                "    (1, 1, 1, 23500.00),\n" +
                                "    (2, 1, 2, 28999.00),\n" +
                                "    (4, 4, 4, 19999.00),\n" +
                                "    (5, 1, 5, 26999.00);\n");

                statement.execute("SELECT setval('test.customers_customer_id_seq', COALESCE((SELECT MAX(customer_id) + 1 FROM test.customers), 1), false)");
                statement.execute("SELECT setval('test.sales_reps_sales_rep_id_seq', COALESCE((SELECT MAX(sales_rep_id) + 1 FROM test.sales_reps), 1), false)");
                statement.execute("SELECT setval('test.sheds_shed_id_seq', COALESCE((SELECT MAX(shed_id) + 1 FROM test.sheds), 1), false)");
                statement.execute("SELECT setval('test.roofs_roof_id_seq', COALESCE((SELECT MAX(roof_id) + 1 FROM test.roofs), 1), false)");
                statement.execute("SELECT setval('test.addons_addon_id_seq', COALESCE((SELECT MAX(addon_id) + 1 FROM test.addons), 1), false)");
                statement.execute("SELECT setval('test.parts_lists_parts_list_id_seq', COALESCE((SELECT MAX(parts_list_id) + 1 FROM test.parts_lists), 1), false)");
                statement.execute("SELECT setval('test.products_product_id_seq', COALESCE((SELECT MAX(product_id) + 1 FROM test.products), 1), false)");
                statement.execute("SELECT setval('test.products_parts_lists_prod_parts_list_id_seq', COALESCE((SELECT MAX(prod_parts_list_id) + 1 FROM test.products_parts_lists), 1), false)");
                statement.execute("SELECT setval('test.carports_carport_id_seq', COALESCE((SELECT MAX(carport_id) + 1 FROM test.carports), 1), false)");
                statement.execute("SELECT setval('test.inquiries_inquiry_id_seq', COALESCE((SELECT MAX(inquiry_id) + 1 FROM test.inquiries), 1), false)");
                statement.execute("SELECT setval('test.quotes_quote_id_seq', COALESCE((SELECT MAX(quote_id) + 1 FROM test.quotes), 1), false)");
                statement.execute("SELECT setval('test.orders_order_id_seq', COALESCE((SELECT MAX(order_id) + 1 FROM test.orders), 1), false)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("Database connection failed");
        }
    }

    @Test
    void createCarport(){
            
        }
}
