package clinic;

import java.io.File;
import java.io.FileNotFoundException;

import java.util.Scanner;

public class Scheduler {
    // Instance variables for managing appointments and commands

    public void run() {
        System.out.println("Scheduler is running.");
        Scanner scanner = new Scanner(System.in);

        boolean running = true;
        String command;

        while (running) {
            System.out.print(">> "); //Input line for user
            String input = scanner.nextLine(); // Read the input as a string
            if (input.equals("S")) {

            } else if (input.equals("C")) {

            } else if (input.equals("R")) {

            } else if (input.equals("PA")) {

            } else if (input.equals("PP")) {

            } else if (input.equals("PL")) {

            } else if (input.equals("PS")) {

            } else if (input.equals("Q")) {
                System.out.print("Scheduler is terminated.");
                running = false;
            } else {
                System.out.println("Invalid command!");
            }
        }
    }
}