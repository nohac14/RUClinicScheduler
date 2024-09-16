package src;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        boolean running = true;
        Scanner scanner = new Scanner(System.in);

        System.out.println("Hello. Welcome to simple chat! type bitch.");
        while (running) {
            System.out.print(">> ");
            String input = scanner.nextLine(); // Read the input as a string
            if (input.equals("Q")) {
                System.out.print("Scheduler is terminated.");
                running = false;
            }
        }
    }
}