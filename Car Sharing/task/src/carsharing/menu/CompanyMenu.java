package carsharing.menu;

import carsharing.db.Database;
import carsharing.abstracts.Menu;
import carsharing.dao.CarDAO;

import java.util.List;

public class CompanyMenu extends Menu {
    private final String name;
    private final CarDAO carDAO;

    public CompanyMenu(String name, Database database) {
        this.name = name;
        this.carDAO = new CarDAO(database);
    }

    @Override
    protected void displayMenu() {
        boolean exit = false;

        while (!exit) {
            System.out.println("'" + name + "' company:");
            System.out.println("1. Car list");
            System.out.println("2. Create a car");
            System.out.println("0. Back");

            int choice = getUserChoice();

            switch (choice) {
                case 1 -> displayCarList();
                case 2 -> createCar();
                case 0 -> exit = true;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void displayCarList() {
        List<String> cars = carDAO.getCompanyCars(name);

        if (cars.isEmpty()) {
            System.out.println("The car list is empty!");
            return;
        }

        System.out.println(name + " cars:");
        for (int i = 0; i < cars.size(); i++) {
            System.out.println((i + 1) + ". " + cars.get(i));
        }
    }

    private void createCar() {
        System.out.println("Enter the car name: ");
        String carName = getUserInput();

        if (carDAO.createCar(carName, name)) {
            System.out.println("The car was created!");
        } else {
            System.out.println("Failed to create the car. Please try again.");
        }
    }
}
