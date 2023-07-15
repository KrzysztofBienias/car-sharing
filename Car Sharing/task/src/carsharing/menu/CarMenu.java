package carsharing.menu;

import carsharing.abstracts.Menu;
import carsharing.dao.CarDAO;
import carsharing.dao.CompanyDAO;
import carsharing.db.Database;
import carsharing.models.Company;

import java.util.List;

public class CarMenu extends Menu {
    private static String customerName = null;
    private static CarDAO carDAO = null;
    private static CompanyDAO companyDAO = null;

    public CarMenu(String customerName, Database database) {
        this.customerName = customerName;
        this.carDAO = new CarDAO(database);
        this.companyDAO = new CompanyDAO(database);
    }

    @Override
    protected void displayMenu() {
        boolean exit = false;

        while (!exit) {
            System.out.println("1. Rent a car");
            System.out.println("2. Return a rented car");
            System.out.println("3. My rented car");
            System.out.println("0. Back");

            int choice = getUserChoice();

            switch (choice) {
                case 1 -> rentCar();
                case 2 -> returnCar();
                case 3 -> rentedCar();
                case 0 -> exit = true;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void rentCar() {
        List<Company> companies = companyDAO.getAllCompanies();
        if (companies.isEmpty()) {
            System.out.println("The company list is empty!");
            return;
        }

        List<String> rentedCar = carDAO.getRentedCarForCustomer(customerName);
        if (!rentedCar.isEmpty() && !rentedCar.get(0).isEmpty()) {
            System.out.println("You've already rented a car!");
            return;
        }

        String companyName = chooseRentingCompany(companies);
        if (companyName == null) {
            return;
        }

        List<String> companyCars = carDAO.getCompanyCars(companyName);
        if (companyCars.isEmpty()) {
            System.out.println("No available cars in the '" + companyName + "' company.");
            return;
        }

        String carName = chooseRentingCar(companyCars);
        if (carName == null) {
            return;
        }

        if (carDAO.rentCar(carName, customerName)) {
            System.out.println("You rented '" + carName + "'");
        } else {
            System.out.println("Failed to rent the car. Please try again.");
        }
    }

    private static void returnCar() {
        List<String> rentedCar = carDAO.getRentedCarForCustomer(customerName);
        if (rentedCar.isEmpty() || rentedCar.get(0).isEmpty()) {
            System.out.println("You didn't rent a car!");
            return;
        }

        String carName = rentedCar.get(0);
        if (carName.equals("")) {
            System.out.println("You've already returned a rented car!");
            return;
        }

        if (carDAO.returnRentedCar(carName, customerName)) {
            System.out.println("You've returned a rented car!");
        } else {
            System.out.println("Failed to return the car. Please try again.");
        }
    }

    private static void rentedCar() {
        List<String> car = carDAO.getRentedCarForCustomer(customerName);

        if (car.isEmpty()) {
            System.out.println("You didn't rent a car!");
            return;
        }

        System.out.println("Your rented car:");
        System.out.println(car.get(0));
        System.out.println("Company:");
        System.out.println(car.get(1));
    }

    private static String chooseRentingCar(List<String> availableCarArray) {
        System.out.println("Choose a car:");
        for (int i = 0; i < availableCarArray.size(); i++) {
            System.out.println((i + 1) + ". " + availableCarArray.get(i));
        }
        System.out.println("0. Back");

        int carChoice = getUserChoice();
        if (carChoice == 0) {
            return null;
        }

        if (carChoice < 1 || carChoice > availableCarArray.size()) {
            System.out.println("Invalid choice. Please try again.");
            return null;
        }

        return availableCarArray.get(carChoice - 1);
    }

    private static String chooseRentingCompany(List<Company> companies) {
        System.out.println("Choose a company:");
        for (int i = 0; i < companies.size(); i++) {
            System.out.println((i + 1) + ". " + companies.get(i).getName());
        }
        System.out.println("0. Back");

        int companyChoice = getUserChoice();
        if (companyChoice == 0) {
            return null;
        }

        if (companyChoice < 1 || companyChoice > companies.size()) {
            System.out.println("Invalid choice. Please try again.");
            return null;
        }

        return companies.get(companyChoice - 1).getName();
    }
}
