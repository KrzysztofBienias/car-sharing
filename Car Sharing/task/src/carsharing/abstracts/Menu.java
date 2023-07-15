package carsharing.abstracts;

import java.util.Scanner;

public abstract class Menu {
    protected abstract void displayMenu();

    protected static int getUserChoice() {
        Scanner scanner = new Scanner(System.in);

        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    };

    protected static String getUserInput() {
        Scanner scanner = new Scanner(System.in);

        return scanner.nextLine();
    }
}
