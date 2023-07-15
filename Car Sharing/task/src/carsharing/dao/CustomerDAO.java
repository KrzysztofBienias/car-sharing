package carsharing.dao;

import carsharing.db.Database;
import carsharing.abstracts.BaseDAO;
import carsharing.models.Car;
import carsharing.models.Customer;
import carsharing.utils.StatementBinder;
import carsharing.utils.StatementMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO extends BaseDAO<Customer> {

    public CustomerDAO(Database database) {
        super(database);
    }

    public boolean createCustomer(String name) {
        String insertQuery = "INSERT INTO CUSTOMER (NAME, RENTED_CAR_ID) VALUES (?, NULL)";

        Customer customer = new Customer();
        customer.setName(name);

        StatementBinder<Customer> binder = (statement, customerObj) ->
                statement.setString(1, customerObj.getName());

        return executeUpdate(insertQuery, binder, customer);
    }

    public List<String> getAllCustomers() {
        String query = "SELECT ID, NAME, RENTED_CAR_ID FROM CUSTOMER ORDER BY ID";

        StatementMapper<Customer> mapper = resultSet -> {
            Customer customer = new Customer();

            customer.setId(resultSet.getInt("ID"));
            customer.setName(resultSet.getString("NAME"));

            int rentedCarId = resultSet.getInt("RENTED_CAR_ID");
            if (rentedCarId > 0) {
                CarDAO carDAO = new CarDAO(database);
                Car rentedCar = carDAO.getCarById(rentedCarId);
                customer.setRentedCar(rentedCar);
            }

            return customer;
        };

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            List<String> customers = new ArrayList<>();

            while (resultSet.next()) {
                customers.add(resultSet.getString("NAME"));
            }

            return customers;
        } catch (SQLException e) {
            System.err.println("Error retrieving customers: " + e.getMessage());
            return List.of();
        }
    }
}

