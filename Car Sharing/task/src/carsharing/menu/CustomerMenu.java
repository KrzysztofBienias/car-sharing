package carsharing.menu;

import carsharing.abstracts.Menu;
import carsharing.db.Database;
import carsharing.dao.CustomerDAO;

import java.util.List;

public class CustomerMenu extends Menu {
    private final Database database;
    private final CustomerDAO customerDAO;

    public CustomerMenu(Database database) {
        this.database = database;
        this.customerDAO = new CustomerDAO(database);
    }

    @Override
    protected void displayMenu() {
        List<String> customers = customerDAO.getAllCustomers();
        boolean exit = false;

        if (customers.isEmpty()) {
            System.out.println("The customer list is empty!");
            return;
        }

        while (!exit) {

            int choice = getUserChoice();

            switch (choice) {
                case 1 -> chooseCustomer();
                case 2 -> createCustomer();
                case 0 -> exit = true;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public void chooseCustomer() {
        List<String> customers = customerDAO.getAllCustomers();

        if (customers.isEmpty()) {
            System.out.println("The customer list is empty!");
            return;
        }

        System.out.println("Choose a customer:");
        for (int i = 0; i < customers.size(); i++) {
            System.out.println((i + 1) + ". " + customers.get(i));
        }
        System.out.println("0. Back");

        int choice = getUserChoice();

        if (choice == 0) {
            return;
        }

        if (choice >= 1 && choice <= customers.size()) {
            String customerName = customers.get(choice - 1);

            CarMenu carMenu = new CarMenu(customerName, database);
            carMenu.displayMenu();
        } else {
            System.out.println("Invalid choice. Please try again.");
        }
    }

    public void createCustomer() {
        System.out.println("Enter the customer name:");
        String name = getUserInput();
        customerDAO.createCustomer(name);
    }
}
