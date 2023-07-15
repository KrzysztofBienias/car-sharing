package carsharing;

import carsharing.db.Database;
import carsharing.menu.MainMenu;

public class Main {
    private static Database database;

    public static void main(String[] args) {
        String name = "carsharing";

        if (args.length > 0 && args[0].equals("-databaseFileName")) {
            name = args[1];
        }

        database = new Database(name);
        database.initialize();

        MainMenu mainMenu = new MainMenu(database);
        mainMenu.displayMenu();
    }
}