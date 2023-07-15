package carsharing.db;

import java.sql.*;

public class Database {
    private final String dbName;
    private static final String JDBC_DRIVER = "org.h2.Driver";
    private static final String DB_URL = "jdbc:h2:./src/carsharing/db/";
    private static final String USERNAME = "";
    private static final String PASSWORD = "";

    public Database(String dbName) {
        this.dbName = dbName;
    }

    public void initialize() {
        try {
            Class.forName(JDBC_DRIVER);
            Connection connection = getConnection();
            connection.setAutoCommit(true);

            String createTableQuery = "CREATE TABLE IF NOT EXISTS COMPANY (" +
                    "ID INT PRIMARY KEY AUTO_INCREMENT, " +
                    "NAME VARCHAR NOT NULL UNIQUE" +
                    ")";
            executeQuery(connection, createTableQuery);

            String createCarQuery = "CREATE TABLE IF NOT EXISTS CAR (" +
                    "ID INT PRIMARY KEY AUTO_INCREMENT, " +
                    "NAME VARCHAR NOT NULL UNIQUE, " +
                    "COMPANY_ID INT NOT NULL, " +
                    "CONSTRAINT fk_company FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY(ID)" +
                    ")";
            executeQuery(connection, createCarQuery);

            String createCustomerQuery = "CREATE TABLE IF NOT EXISTS CUSTOMER (" +
                    "ID INT PRIMARY KEY AUTO_INCREMENT, " +
                    "NAME VARCHAR NOT NULL UNIQUE, " +
                    "RENTED_CAR_ID INT, " +
                    "CONSTRAINT fk_car FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR(ID)" +
                    ")";
            executeQuery(connection, createCustomerQuery);

            connection.close();
        } catch (ClassNotFoundException e) {
            System.err.println("Error loading the JDBC driver: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error establishing a database connection: " + e.getMessage());
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL + dbName, USERNAME, PASSWORD);
    }

    private void executeQuery(Connection connection, String query) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute(query);
        }
    }
}