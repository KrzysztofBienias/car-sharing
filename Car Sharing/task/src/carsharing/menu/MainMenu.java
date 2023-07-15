package carsharing.menu;

import carsharing.db.Database;
import carsharing.abstracts.Menu;

public class MainMenu extends Menu {
    private final Database database;

    public MainMenu(Database database) {
        this.database = database;
    }

    @Override
    public void displayMenu() {
        boolean exit = false;

        while (!exit) {
            System.out.println("1. Log in as a manager");
            System.out.println("2. Log in as a customer");
            System.out.println("3. Create a customer");
            System.out.println("0. Exit");

            int choice = getUserChoice();

            switch (choice) {
                case 1 -> {
                    ManagerMenu managerMenu = new ManagerMenu(database);
                    managerMenu.displayMenu();
                }
                case 2 -> {
                    CustomerMenu customerMenu = new CustomerMenu(database);
                    customerMenu.chooseCustomer();
                }
                case 3 -> {
                    CustomerMenu customerMenu = new CustomerMenu(database);
                    customerMenu.createCustomer();
                }
                case 0 -> exit = true;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
