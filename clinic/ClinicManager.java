package clinic;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ClinicManager {

    // List to store appointments
    private List<Appointment> appointments;
    // List to store providers (doctors and technicians)
    private List<Provider> providers;
    // List to store technicians
    private List<Technician> technicians;
    // Index to keep track of the current technician for circular assignment
    private int currentTechnicianIndex;

    // Constructor: Initialize lists and load providers
    public ClinicManager() {
        this.appointments = new List<>();
        this.providers = new List<>();
        this.technicians = new List<>();
        this.currentTechnicianIndex = 0;  // Initialize technician index for rotation
        loadProviders();  // Load providers from the file
    }

    // Method to load providers from the file 'providers.txt'
    private void loadProviders() {
        try {
            File file = new File("providers.txt");
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] tokens = line.split("\\s+");
                String type = tokens[0]; // D for Doctor, T for Technician
                String firstName = tokens[1];
                String lastName = tokens[2];
                Date dob = new Date(Integer.parseInt(tokens[3].split("/")[0]),
                        Integer.parseInt(tokens[3].split("/")[1]),
                        Integer.parseInt(tokens[3].split("/")[2]));
                Location location = Location.findByCity(tokens[4]);

                if (type.equals("D")) {
                    // Add doctor to providers list
                    Specialty specialty = Specialty.valueOf(tokens[5].toUpperCase());
                    String npi = tokens[6];
                    providers.add(new Doctor(new Profile(firstName, lastName, dob), specialty, npi, location));
                } else if (type.equals("T")) {
                    // Add technician to technician list
                    int rate = Integer.parseInt(tokens[5]);
                    Technician technician = new Technician(new Profile(firstName, lastName, dob), rate, location);
                    technicians.add(technician);
                    providers.add(technician); // Technicians are also part of the provider list
                }
            }

            // Sort providers by profile (last name, first name, DOB)
            Sort.provider(providers);

            // Print the list of providers in the correct format
            System.out.println("Providers loaded to the list.");
            for (Provider provider : providers) {
                if (provider instanceof Doctor) {
                    // Format for doctors
                    System.out.println(String.format("[%s, %s][%s, #%s]",
                            provider.getProfile().toString(),
                            provider.getLocation().toString(),
                            ((Doctor) provider).getSpecialty().name(),
                            ((Doctor) provider).getNPI()));
                } else if (provider instanceof Technician) {
                    // Format for technicians
                    Technician tech = (Technician) provider;
                    System.out.println(String.format("[%s, %s][rate: $%.2f]",
                            tech.getProfile().toString(),
                            tech.getLocation().toString(),
                            (double) tech.getRatePerVisit()));
                }
            }

            // Display the rotation list of technicians in reverse order
            System.out.println("\nRotation list for the technicians.");
            for (int i = technicians.size() - 1; i >= 0; i--) {
                Technician tech = technicians.get(i);
                System.out.print(tech.getProfile().getFullName() + " (" + tech.getLocation().getCity() + ")");
                if (i > 0) {
                    System.out.print(" --> ");
                }
            }
            System.out.println();

        } catch (FileNotFoundException e) {
            System.out.println("Error: 'providers.txt' file not found.");
        }
    }

    // Method to get the next technician in rotation
    private Technician getNextTechnician() {
        Technician technician = technicians.get(currentTechnicianIndex);
        // Update the index for circular rotation
        currentTechnicianIndex = (currentTechnicianIndex + 1) % technicians.size();
        return technician;
    }

    // Main run method to process commands from user input
    public void run() {
        System.out.println("Clinic Manager is running...");
        Scanner sc = new Scanner(System.in);

        while (true) {
            String input = sc.nextLine().trim().toUpperCase();
            if (input.equalsIgnoreCase("Q")) {
                System.out.println("Clinic Manager terminated");
                break;
            }
            processCommand(input);
        }
    }


    // Method to process each command entered by the user
    private void processCommand(String command) {
        // Command processing logic here
    }
}