package carsharing.menu;

import carsharing.db.Database;
import carsharing.abstracts.Menu;
import carsharing.dao.CompanyDAO;
import carsharing.models.Company;

import java.util.List;

public class ManagerMenu extends Menu {
    private final Database database;
    private final CompanyDAO companyDAO;

    public ManagerMenu(Database database) {
        this.database = database;
        this.companyDAO = new CompanyDAO(database);
    }

    @Override
    protected void displayMenu() {
        boolean exit = false;

        while (!exit) {
            System.out.println("1. Company list");
            System.out.println("2. Create a company");
            System.out.println("0. Back");

            int choice = getUserChoice();

            switch (choice) {
                case 1 -> chooseCompany();
                case 2 -> createCompany();
                case 0 -> exit = true;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void chooseCompany() {
        List<Company> companies = companyDAO.getAllCompanies();

        if (companies.isEmpty()) {
            System.out.println("The company list is empty!");
            return;
        }

        System.out.println("Choose the company:");
        for (int i = 0; i < companies.size(); i++) {
            System.out.println((i + 1) + ". " + companies.get(i).getName());
        }
        System.out.println("0. Back");

        int choice = getUserChoice();

        if (choice == 0) {
            return;
        }

        if (choice >= 1 && choice <= companies.size()) {
            String companyName = companies.get(choice - 1).getName();
            CompanyMenu company = new CompanyMenu(companyName, database);
            company.displayMenu();
        } else {
            System.out.println("Invalid choice. Please try again.");
        }
    }

    private void createCompany() {
        System.out.println("Enter the company name: ");
        String companyName = getUserInput();

        if (companyDAO.createCompany(companyName)) {
            System.out.println("The company was created!");
        } else {
            System.out.println("Failed to create the company. Please try again.");
        }
    }
}