package features.console;

import java.util.Scanner;

public class Main {

    public static void main (String args []) {

        System.out.print("welcome to MIRF console interface" + System.lineSeparator());
        Scanner keyboard = new Scanner(System.in);
        boolean exit = false;
        while (!exit) {
            String input = keyboard.nextLine();
            if (input == null)
                continue;

            if (input.equals("q"))
                exit = true;
            else
                CommandParser.tryExecute(input, null);

        }
        keyboard.close();
    }
}
