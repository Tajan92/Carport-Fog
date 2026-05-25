package persistence;

import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class MapperTest {
    public static final ConnectionPool connectionPool = ConnectionPool.getInstance("postgres", "postgres", "jdbc:postgresql://localhost:5432/%s?currentSchema=test", "carport_db");

    @BeforeAll
    static void setupClass() throws DatabaseException {
        try (Connection connection = connectionPool.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.execute("DROP TABLE IF EXISTS test.products_parts_lists");
                statement.execute("DROP TABLE IF EXISTS test.orders");
                statement.execute("DROP TABLE IF EXISTS test.inquiries");
                statement.execute("DROP TABLE IF EXISTS test.quotes");
                statement.execute("DROP TABLE IF EXISTS test.carports");
                statement.execute("DROP TABLE IF EXISTS test.parts_lists");
                statement.execute("DROP TABLE IF EXISTS test.addons");
                statement.execute("DROP TABLE IF EXISTS test.products");
                statement.execute("DROP TABLE IF EXISTS test.sheds");
                statement.execute("DROP TABLE IF EXISTS test.roofs");
                statement.execute("DROP TABLE IF EXISTS test.zip_codes");
                statement.execute("DROP TABLE IF EXISTS test.customers");
                statement.execute("DROP TABLE IF EXISTS test.sales_reps");

                statement.execute("CREATE TABLE test.products AS (SELECT * from public.products) WITH NO DATA");
                statement.execute("CREATE TABLE test.products_parts_lists AS (SELECT * from public.products_parts_lists) WITH NO DATA");
                statement.execute("CREATE TABLE test.parts_lists AS (SELECT * from public.parts_lists) WITH NO DATA");
                statement.execute("CREATE TABLE test.roofs AS (SELECT * from public.roofs) WITH NO DATA");
                statement.execute("CREATE TABLE test.sheds AS (SELECT * from public.sheds) WITH NO DATA");
                statement.execute("CREATE TABLE test.addons AS (SELECT * from public.addons) WITH NO DATA");
                statement.execute("CREATE TABLE test.orders AS (SELECT * from public.orders) WITH NO DATA");
                statement.execute("CREATE TABLE test.inquiries AS (SELECT * from public.inquiries) WITH NO DATA");
                statement.execute("CREATE TABLE test.quotes AS (SELECT * from public.quotes) WITH NO DATA");
                statement.execute("CREATE TABLE test.zip_codes AS (SELECT * FROM public.zip_codes) WITH NO DATA");
                statement.execute("CREATE TABLE test.customers AS (SELECT * from public.customers) WITH NO DATA");
                statement.execute("CREATE TABLE test.sales_reps AS (SELECT * from public.sales_reps) WITH NO DATA");
                statement.execute("CREATE TABLE test.carports AS (SELECT * from public.carports) WITH NO DATA");
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
                statement.execute("DELETE FROM test.products_parts_lists");
                statement.execute("DELETE FROM test.orders");
                statement.execute("DELETE FROM test.inquiries");
                statement.execute("DELETE FROM test.quotes");
                statement.execute("DELETE FROM test.carports");
                statement.execute("DELETE FROM test.parts_lists");
                statement.execute("DELETE FROM test.addons");
                statement.execute("DELETE FROM test.products");
                statement.execute("DELETE FROM test.sheds");
                statement.execute("DELETE FROM test.roofs");
                statement.execute("DELETE FROM test.customers");
                statement.execute("DELETE FROM test.zip_codes");
                statement.execute("DELETE FROM test.sales_reps");

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
                statement.execute("DROP SEQUENCE IF EXISTS test.products_product_id_seq CASCADE;");

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

                statement.execute("CREATE SEQUENCE test.products_product_id_seq");
                statement.execute("ALTER TABLE test.products ALTER COLUMN product_id SET DEFAULT nextval('test.products_product_id_seq')");

                statement.execute("CREATE SEQUENCE test.carports_carport_id_seq");
                statement.execute("ALTER TABLE test.carports ALTER COLUMN carport_id SET DEFAULT nextval('test.carports_carport_id_seq')");

                // zip codes
                statement.execute("INSERT INTO test.zip_codes (zip_code, town) VALUES " +
                        "('2100', 'København Ø')," +
                        "('2200', 'København N')," +
                        "('8000', 'Aarhus C')," +
                        "('5000', 'Odense C')," +
                        "('9000', 'Aalborg')," +
                        "('3000', 'Helsingør')");

                // customers
                statement.execute("INSERT INTO test.customers (first_name, last_name, email, password, phone_number, address, zip_code) VALUES " +
                        "('Anders', 'Jensen', 'anders@email.dk', 'hashed_pw1', '12345678', 'Elmevej 4', '2100')," +
                        "('Mette', 'Nielsen', 'mette@email.dk', 'hashed_pw2', '23456789', 'Birkevej 12', '2200')," +
                        "('Lars', 'Hansen', 'lars@email.dk', 'hashed_pw3', '34567890', 'Kastanievej 7', '8000')," +
                        "('Sofie', 'Pedersen', 'sofie@email.dk', 'hashed_pw4', '45678901', 'Egevej 3', '5000')," +
                        "('Mikkel', 'Andersen', 'mikkel@email.dk', 'hashed_pw5', '56789012', 'Lindevej 22', '9000')," +
                        "('Camilla', 'Christensen', 'camilla@email.dk', 'hashed_pw6', '67890123', 'Fyrvej 8', '3000')");

                // sales_reps
                statement.execute("INSERT INTO test.sales_reps (first_name, last_name, email, password, phone_number) VALUES " +
                        "('Thomas', 'Møller', 'thomas@carport.dk', 'hashed_rep1', '11111111')," +
                        "('Hanne', 'Sørensen', 'hanne@carport.dk', 'hashed_rep2', '22222222')," +
                        "('Rasmus', 'Larsen', 'rasmus@carport.dk', 'hashed_rep3', '33333333')," +
                        "('Gitte', 'Madsen', 'gitte@carport.dk', 'hashed_rep4', '44444444')");

                // sheds
                statement.execute("INSERT INTO test.sheds (shed_width, shed_length, siding, floor) VALUES " +
                        "(240, 300, 'Trykimp. bræddebeklædning', true)," +
                        "(300, 300, 'Plastik panel', true)," +
                        "(300, 500, 'Trykimp. bræddebeklædning', true)," +
                        "(240, 240, 'Plastik panel', false)");

                // roofs
                statement.execute("INSERT INTO test.roofs (roof_slope, roof_material, roof_type) VALUES " +
                        "(15, 'Betontagsten - sort', 'Højt tag')," +
                        "(20, 'Betontagsten - sort', 'Højt tag')," +
                        "(25, 'Bitumentagpap', 'Højt tag')," +
                        "(15, 'Stålprofil', 'Fladt tag')");

                // addons
                statement.execute("INSERT INTO test.addons (shed_id, roof_id) VALUES " +
                        "(1, 1),(2, NULL),(NULL, 2),(3, 3),(4, NULL),(NULL, 4)");

                // parts_lists
                statement.execute("INSERT INTO test.parts_lists DEFAULT VALUES");
                statement.execute("INSERT INTO test.parts_lists DEFAULT VALUES");
                statement.execute("INSERT INTO test.parts_lists DEFAULT VALUES");
                statement.execute("INSERT INTO test.parts_lists DEFAULT VALUES");
                statement.execute("INSERT INTO test.parts_lists DEFAULT VALUES");
                statement.execute("INSERT INTO test.parts_lists DEFAULT VALUES");

                // carports
                statement.execute("INSERT INTO test.carports (carport_width, carport_height, carport_length, addon_id, price, parts_list_id) VALUES " +
                        "(300, 208, 500, 1, 24999.00, 1)," +
                        "(300, 208, 780, 2, 28999.00, 2)," +
                        "(450, 230, 780, 3, 32999.00, 3)," +
                        "(600, 230, 780, 4, 19999.00, 4)," +
                        "(300, 208, 500, 5, 26999.00, 5)," +
                        "(450, 230, 500, 6, 21999.00, 6)");

                // products
                statement.execute("INSERT INTO test.products (product_id, cost_price, retail_price, length, unit, product_group, description) VALUES " +
                        "(1, 95.00, 145.00, 360.00, 'Stk', 'Træ & Tagplader', '25x200 mm. trykimp. Brædt (Sternbræt)')," +
                        "(2, 140.00, 215.00, 540.00, 'Stk', 'Træ & Tagplader', '25x200 mm. trykimp. Brædt (Sternbræt)')," +
                        "(3, 60.00, 95.00, 360.00, 'Stk', 'Træ & Tagplader', '25x125mm. trykimp. Brædt')," +
                        "(4, 92.00, 142.00, 540.00, 'Stk', 'Træ & Tagplader', '25x125mm. trykimp. Brædt')," +
                        "(5, 40.00, 65.00, 420.00, 'Stk', 'Træ & Tagplader', '38x73 mm. Lægte ubh. (Lægte til dør)')," +
                        "(6, 45.00, 72.00, 270.00, 'Stk', 'Træ & Tagplader', '45x95 mm. Reglar ub.')," +
                        "(7, 40.00, 64.00, 240.00, 'Stk', 'Træ & Tagplader', '45x95 mm. Reglar ub.')," +
                        "(8, 125.00, 195.00, 300.00, 'Stk', 'Træ & Tagplader', '97x97 mm. trykimp. Stolpe')," +
                        "(9, 20.00, 32.00, 210.00, 'Stk', 'Træ & Tagplader', '19x100 mm. trykimp. Brædt')," +
                        "(10, 52.00, 82.00, 540.00, 'Stk', 'Træ & Tagplader', '19x100 mm. trykimp. Brædt')," +
                        "(11, 35.00, 55.00, 360.00, 'Stk', 'Træ & Tagplader', '19x100 mm. trykimp. Brædt')," +
                        "(12, 100.00, 155.00, 240.00, 'Stk', 'Træ & Tagplader', '45x195 mm. spærtræ ubh.')," +
                        "(13, 170.00, 260.00, 400.00, 'Stk', 'Træ & Tagplader', '45x195 mm. spærtræ ubh.')," +
                        "(14, 230.00, 355.00, 550.00, 'Stk', 'Træ & Tagplader', '45x195 mm. spærtræ ubh.')," +
                        "(15, 320.00, 495.00, 720.00, 'Stk', 'Træ & Tagplader', '45x195 mm. spærtræ ubh.')," +
                        "(16, 120.00, 185.00, 240.00, 'Stk', 'Træ & Tagplader', '45x220 mm. spærtræ ubh.')," +
                        "(17, 200.00, 310.00, 400.00, 'Stk', 'Træ & Tagplader', '45x220 mm. spærtræ ubh.')," +
                        "(18, 275.00, 425.00, 550.00, 'Stk', 'Træ & Tagplader', '45x220 mm. spærtræ ubh.')," +
                        "(19, 380.00, 590.00, 720.00, 'Stk', 'Træ & Tagplader', '45x220 mm. spærtræ ubh.')," +
                        "(20, 150.00, 230.00, 240.00, 'Stk', 'Træ & Tagplader', '45x245 mm. spærtræ ubh.')," +
                        "(21, 245.00, 380.00, 400.00, 'Stk', 'Træ & Tagplader', '45x245 mm. spærtræ ubh.')," +
                        "(22, 340.00, 525.00, 550.00, 'Stk', 'Træ & Tagplader', '45x245 mm. spærtræ ubh.')," +
                        "(23, 465.00, 720.00, 720.00, 'Stk', 'Træ & Tagplader', '45x245 mm. spærtræ ubh.')," +
                        "(24, 95.00, 149.00, 360.00, 'Stk', 'Træ & Tagplader', 'Plastmo Ecolite blåtonet')," +
                        "(25, 160.00, 249.00, 600.00, 'Stk', 'Træ & Tagplader', 'Plastmo Ecolite blåtonet')," +
                        "(26, 9.00, 14.50, 42.00, 'Stk', 'Træ & Tagplader', 'Betontagsten - koralrød')," +
                        "(27, 8.50, 13.50, 42.00, 'Stk', 'Træ & Tagplader', 'Betontagsten - sort')," +
                        "(28, 78.00, 119.00, 118.00, 'Stk', 'Træ & Tagplader', 'Eternittag B6 - grå')," +
                        "(29, 84.00, 129.00, 118.00, 'Stk', 'Træ & Tagplader', 'Eternittag B6 - sortblå')," +
                        "(30, 88.00, 135.00, 118.00, 'Stk', 'Træ & Tagplader', 'Eternittag B7 - sortblå')," +
                        "(31, 58.00, 89.00, 420.00, 'Stk', 'Træ & Tagplader', 'Beklædning: 19x125mm Klinkbeklædning trykimp.')," +
                        "(32, 95.00, 145.00, 420.00, 'Stk', 'Træ & Tagplader', 'Beklædning: 25x150mm Blokhusbrædder ubehandlet')," +
                        "(33, 38.00, 58.00, 360.00, 'Stk', 'Træ & Tagplader', 'Beklædning: 19x100mm Profilbrædt (1 på 2 beklædning)')," +
                        "(34, 220.00, 349.00, 244.00, 'Stk', 'Træ & Tagplader', 'Beklædning: 9x1220x2440mm Krydsfiner m/spor (Svalehale)')," +
                        "(35, 190.00, 289.00, NULL, 'pakke', 'Beslag & Skruer', 'plastmo bundskruer 200 stk.')," +
                        "(36, 85.00, 135.00, 1000.00, 'Rulle', 'Beslag & Skruer', 'hulbånd 1x20 mm. 10 mtr.')," +
                        "(37, 8.00, 14.50, 19.00, 'Stk', 'Beslag & Skruer', 'universal 190 mm højre')," +
                        "(38, 8.00, 14.50, 19.00, 'Stk', 'Beslag & Skruer', 'universal 190 mm venstre')," +
                        "(39, 60.00, 99.00, NULL, 'Pakke', 'Beslag & Skruer', '4,5 x 60 mm. skruer 200 stk.')," +
                        "(40, 75.00, 125.00, NULL, 'pakke', 'Beslag & Skruer', '4,0 x 50 mm. beslagskruer 250 stk.')," +
                        "(41, 4.50, 8.50, 12.00, 'Stk', 'Beslag & Skruer', 'bræddebolt 10 x 120 mm.')," +
                        "(42, 3.00, 6.00, NULL, 'Stk', 'Beslag & Skruer', 'firkantskiver 40x40x11mm')," +
                        "(43, 115.00, 189.00, NULL, 'pk.', 'Beslag & Skruer', '4,5 x 70 mm. Skruer 400 stk.')," +
                        "(44, 85.00, 139.00, NULL, 'pk.', 'Beslag & Skruer', '4,5 x 50 mm. Skruer 300 stk.')," +
                        "(45, 105.00, 165.00, NULL, 'Sæt', 'Beslag & Skruer', 'stalddørsgreb 50x75')," +
                        "(46, 48.00, 79.00, 39.00, 'Stk', 'Beslag & Skruer', 't hængsel 390 mm')," +
                        "(47, 2.20, 4.50, NULL, 'Stk', 'Beslag & Skruer', 'vinkelbeslag 35')," +
                        "(48, 180.00, 260.00, 240.00, 'Stk', 'Træ & Tagplader', '45x295 mm. spærtræ ubh.')," +
                        "(49, 275.00, 410.00, 400.00, 'Stk', 'Træ & Tagplader', '45x295 mm. spærtræ ubh.')," +
                        "(50, 370.00, 555.00, 550.00, 'Stk', 'Træ & Tagplader', '45x295 mm. spærtræ ubh.')," +
                        "(51, 495.00, 750.00, 720.00, 'Stk', 'Træ & Tagplader', '45x295 mm. spærtræ ubh.')," +
                        "(52, 20.00, 32.00, 210.00, 'Stk', 'Træ & Tagplader', 'Beklædning: 19x100 mm. trykimp. Brædt')");

                // products_parts_lists
                statement.execute("INSERT INTO test.products_parts_lists (product_id, parts_list_id, quantity) VALUES " +
                        "(1,1,6),(2,1,10),(3,1,2),(4,1,4),(5,1,1),(6,1,8),(7,1,12),(8,1,12),(9,1,100),(10,1,100),(11,1,4)");
                statement.execute("INSERT INTO test.products_parts_lists (product_id, parts_list_id, quantity) VALUES " +
                        "(1,2,6),(2,2,15),(3,2,2),(4,2,6),(5,2,2),(6,2,13),(7,2,15),(8,2,15),(9,2,150),(10,2,150),(11,2,6)");
                statement.execute("INSERT INTO test.products_parts_lists (product_id, parts_list_id, quantity) VALUES " +
                        "(1,3,6),(2,3,15),(3,3,2),(4,3,6),(5,3,2),(6,3,18),(7,3,15),(8,3,15),(9,3,200),(10,3,200),(11,3,6),(12,3,24)");
                statement.execute("INSERT INTO test.products_parts_lists (product_id, parts_list_id, quantity) VALUES " +
                        "(1,4,6),(2,4,15),(3,4,2),(4,4,6),(5,4,2),(6,4,24),(7,4,15),(8,4,15),(9,4,200),(10,4,150),(11,4,6)");
                statement.execute("INSERT INTO test.products_parts_lists (product_id, parts_list_id, quantity) VALUES " +
                        "(1,5,6),(2,5,10),(3,5,2),(4,5,4),(5,5,1),(6,5,8),(7,5,10),(8,5,10),(9,5,100),(10,5,100),(11,5,4),(12,5,16)");
                statement.execute("INSERT INTO test.products_parts_lists (product_id, parts_list_id, quantity) VALUES " +
                        "(1,6,6),(2,6,10),(3,6,2),(4,6,4),(5,6,1),(6,6,12),(7,6,10),(8,6,10),(9,6,100),(10,6,100),(11,6,4)");

                // inquiries
                statement.execute("INSERT INTO test.inquiries (customer_id, remark, carport_id) VALUES " +
                        "(1, 'Ønsker carport med skur til haveredskaber', 1)," +
                        "(2, 'Skal passe til eksisterende indkørsel på 3 meter', 2)," +
                        "(3, 'Vil gerne have mulighed for at tilkøbe belysning', 3)," +
                        "(4, 'Ingen særlige bemærkninger', 4)," +
                        "(5, 'Carport skal matche husfarve - rød mur', 5)," +
                        "(6, 'Ønsker montering inkluderet i prisen', 6)");

                // quotes
                statement.execute("INSERT INTO test.quotes (quote_price, carport_id, customer_id, sales_rep_id) VALUES " +
                        "(24999.00, 1, 1, 1),(23500.00, 1, 1, 2),(28999.00, 2, 2, 1)," +
                        "(32999.00, 3, 3, 3),(30500.00, 3, 3, 2),(19999.00, 4, 4, 4)," +
                        "(26999.00, 5, 5, 1),(21999.00, 6, 6, 3)");

                // orders
                statement.execute("INSERT INTO test.orders (customer_id, sales_rep_id, carport_id, order_price) VALUES " +
                        "(1, 1, 1, 23500.00),(2, 1, 2, 28999.00),(4, 4, 4, 19999.00),(5, 1, 5, 26999.00)");


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
}
