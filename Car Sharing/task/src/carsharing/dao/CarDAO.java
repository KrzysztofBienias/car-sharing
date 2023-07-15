package carsharing.dao;

import carsharing.db.Database;
import carsharing.abstracts.BaseDAO;
import carsharing.models.Car;
import carsharing.models.Company;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarDAO extends BaseDAO<Car> {

    public CarDAO(Database carDAO) {
        super(carDAO);
    }

    public boolean createCar(String name, String companyName) {
        String companyIdQuery = "SELECT ID FROM COMPANY WHERE NAME = ?";
        String insertQuery = "INSERT INTO CAR (NAME, COMPANY_ID) VALUES (?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement companyIdStatement = connection.prepareStatement(companyIdQuery);
             PreparedStatement carsStatement = connection.prepareStatement(insertQuery)) {

            companyIdStatement.setString(1, companyName);
            ResultSet resultSet = companyIdStatement.executeQuery();

            if (resultSet.next()) {
                int companyId = resultSet.getInt("ID");
                carsStatement.setString(1, name);
                carsStatement.setInt(2, companyId);
                carsStatement.executeUpdate();

                return true;
            } else {
                System.out.println("Invalid company name.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error establishing a database connection: " + e.getMessage());
            return false;
        }
    }

    public List<String> getCompanyCars(String companyName) {
        String companyIdQuery = "SELECT ID FROM COMPANY WHERE NAME = ?";
        String carsQuery = "SELECT CAR.NAME " +
                "FROM CAR " +
                "LEFT JOIN CUSTOMER ON CAR.ID = CUSTOMER.RENTED_CAR_ID " +
                "WHERE CAR.COMPANY_ID = ? AND CUSTOMER.RENTED_CAR_ID IS NULL " +
                "ORDER BY CAR.ID";

        try (Connection connection = getConnection();
             PreparedStatement companyIdStatement = connection.prepareStatement(companyIdQuery);
             PreparedStatement carsStatement = connection.prepareStatement(carsQuery)) {

            companyIdStatement.setString(1, companyName);
            ResultSet resultSet = companyIdStatement.executeQuery();

            if (resultSet.next()) {
                int companyId = resultSet.getInt("ID");
                carsStatement.setInt(1, companyId);
                ResultSet carsResultSet = carsStatement.executeQuery();

                List<String> cars = new ArrayList<>();

                while (carsResultSet.next()) {
                    cars.add(carsResultSet.getString("NAME"));
                }


                return cars;
            } else {
                System.out.println("Invalid company name.");
                return new ArrayList<>();
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving cars: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<String> getRentedCarForCustomer(String customerName) {
    String rentedCarQuery = "SELECT CAR.NAME, COMPANY.NAME " +
            "FROM CAR " +
            "JOIN COMPANY ON CAR.COMPANY_ID = COMPANY.ID " +
            "JOIN CUSTOMER ON CAR.ID = CUSTOMER.RENTED_CAR_ID " +
            "WHERE CUSTOMER.NAME = ?";

    try (Connection connection = getConnection();
         PreparedStatement statement = connection.prepareStatement(rentedCarQuery)) {

        statement.setString(1, customerName);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            String carName = resultSet.getString(1);
            String companyName = resultSet.getString(2);

            List<String> rentedCar = new ArrayList<>();
            rentedCar.add(carName);
            rentedCar.add(companyName);

            return rentedCar;
        }

        return new ArrayList<>();
    } catch (SQLException e) {
        System.err.println("Error retrieving rented car: " + e.getMessage());
        return new ArrayList<>();
    }
}

    public boolean returnRentedCar(String carName, String customerName) {
        String updateQuery = "UPDATE CUSTOMER SET RENTED_CAR_ID = NULL " +
                "WHERE NAME = ? AND RENTED_CAR_ID IN " +
                "(SELECT ID FROM CAR WHERE NAME = ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(updateQuery)) {

            statement.setString(1, customerName);
            statement.setString(2, carName);

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("Error returning rented car: " + e.getMessage());
            return false;
        }
    }

    public boolean rentCar(String carName, String customerName) {
        String updateQuery = "UPDATE CUSTOMER SET RENTED_CAR_ID = ? WHERE NAME = ? AND RENTED_CAR_ID IS NULL";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(updateQuery)) {

            statement.setInt(1, getCarIdByName(carName));
            statement.setString(2, customerName);

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("Error updating car rental status: " + e.getMessage());
            return false;
        }
    }

    public int getCarIdByName(String carName) {
        String query = "SELECT ID FROM CAR WHERE NAME = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, carName);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("ID");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving car ID: " + e.getMessage());
        }

        return -1;
    }

    public Car getCarById(int carId) {
        String query = "SELECT * FROM CAR WHERE ID = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, carId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("ID");
                    String name = resultSet.getString("NAME");
                    int companyId = resultSet.getInt("COMPANY_ID");

                    CompanyDAO companyDAO = new CompanyDAO(database);
                    Company company = companyDAO.getCompanyById(companyId);

                    Car car = new Car();
                    car.setId(id);
                    car.setName(name);
                    car.setCompany(company);

                    return car;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving car by ID: " + e.getMessage());
        }

        return null;
    }
}
