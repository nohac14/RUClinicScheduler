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




//package clinic;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.util.Scanner;
//
///**
// * ClinicManager class manages the clinic's appointments, providers, and technicians.
// * It processes commands to schedule, cancel, reschedule, and display appointments.
// */
//public class ClinicManager {
//
//    // List to store appointments
//    private List<Appointment> appointments;
//    // List to store providers (doctors and technicians)
//    private List<Provider> providers;
//    // List to store technicians
//    private List<Technician> technicians;
//    // Index to keep track of the current technician for circular assignment
//    private int currentTechnicianIndex;
//
//    // Constructor: Initialize lists and load providers
//    public ClinicManager() {
//        this.appointments = new List<>();
//        this.providers = new List<>();
//        this.technicians = new List<>();
//        this.currentTechnicianIndex = 0;  // Initialize technician index for rotation
//        loadProviders();  // Load providers from the file
//    }
//
//    // Method to load providers from the file 'providers.txt'
//    private void loadProviders() {
//        try {
//            File file = new File("providers.txt");
//            Scanner scanner = new Scanner(file);
//
//            while (scanner.hasNextLine()) {
//                String line = scanner.nextLine().trim();
//                if (line.isEmpty()) continue;
//
//                String[] tokens = line.split("\\s+");
//                String type = tokens[0]; // D for Doctor, T for Technician
//                String firstName = tokens[1];
//                String lastName = tokens[2];
//                Date dob = new Date(Integer.parseInt(tokens[3].split("/")[0]),
//                        Integer.parseInt(tokens[3].split("/")[1]),
//                        Integer.parseInt(tokens[3].split("/")[2]));
//                Location location = Location.findByCity(tokens[4]);
//
//                if (type.equals("D")) {
//                    // Add doctor to providers list
//                    Specialty specialty = Specialty.valueOf(tokens[5].toUpperCase());
//                    String npi = tokens[6];
//                    providers.add(new Doctor(new Profile(firstName, lastName, dob), specialty, npi, location));
//                } else if (type.equals("T")) {
//                    // Add technician to technician list
//                    int rate = Integer.parseInt(tokens[5]);
//                    Technician technician = new Technician(new Profile(firstName, lastName, dob), rate, location);
//                    technicians.add(technician);
//                    providers.add(technician); // Technicians are also part of the provider list
//                }
//            }
//
//            // Sort providers by profile (last name, first name, DOB)
//            Sort.provider(providers);
//
//            // Print the list of providers in the correct format
//            System.out.println("Providers loaded to the list.");
//            for (Provider provider : providers) {
//                if (provider instanceof Doctor) {
//                    // Format for doctors
//                    System.out.println(String.format("[%s, %s][%s, #%s]",
//                            provider.getProfile().toString(),
//                            provider.getLocation().toString(),
//                            ((Doctor) provider).getSpecialty().name(),
//                            ((Doctor) provider).getNPI()));
//                } else if (provider instanceof Technician) {
//                    // Format for technicians
//                    Technician tech = (Technician) provider;
//                    System.out.println(String.format("[%s, %s][rate: $%.2f]",
//                            tech.getProfile().toString(),
//                            tech.getLocation().toString(),
//                            (double) tech.getRatePerVisit()));
//                }
//            }
//
//            // Display the rotation list of technicians in reverse order
//            System.out.println("\nRotation list for the technicians.");
//            for (int i = technicians.size() - 1; i >= 0; i--) {
//                Technician tech = technicians.get(i);
//                System.out.print(tech.getProfile().getFullName() + " (" + tech.getLocation().getCity() + ")");
//                if (i > 0) {
//                    System.out.print(" --> ");
//                }
//            }
//            System.out.println();
//
//        } catch (FileNotFoundException e) {
//            System.out.println("Error: 'providers.txt' file not found.");
//        }
//    }
//
//    // Method to get the next technician in rotation
//    private Technician getNextTechnician() {
//        Technician technician = technicians.get(currentTechnicianIndex);
//        // Update the index for circular rotation
//        currentTechnicianIndex = (currentTechnicianIndex + 1) % technicians.size();
//        return technician;
//    }
//
//    // Main run method to process commands from user input
//    public void run() {
//        System.out.println("Clinic Manager is running...");
//        Scanner sc = new Scanner(System.in);
//
//        while (true) {
//            String input = sc.nextLine().trim().toUpperCase();
//            if (input.equalsIgnoreCase("Q")) {
//                System.out.println("Clinic Manager terminated");
//                break;
//            }
//            processCommand(input);
//        }
//    }
//
//    // Method to process each command entered by the user
//    private void processCommand(String command) {
//        String[] tokens = command.split(",");
//        String commandType = tokens[0].toUpperCase();
//
//        switch (commandType) {
//            case "D":  // Scheduling a doctor's appointment
//                scheduleDoctorAppointment(tokens);
//                break;
//            case "T":  // Scheduling an imaging appointment
//                scheduleImagingAppointment(tokens);
//                break;
//            case "C":  // Cancel an appointment
//                cancelAppointment(tokens);
//                break;
//            case "R":  // Reschedule an appointment
//                rescheduleAppointment(tokens);
//                break;
//            case "PO":  // Display office appointments
//                displayOfficeAppointments();
//                break;
//            case "PI":  // Display imaging appointments
//                displayImagingAppointments();
//                break;
//            case "PC":  // Display provider credit amounts
//                displayProviderCredit();
//                break;
//            case "PA":  // Display all appointments by date
//                displayAppointments();
//                break;
//            default:
//                System.out.println("Invalid command!");
//        }
//    }
//
//    // Schedule a doctor's appointment (D command)
//    private void scheduleDoctorAppointment(String[] tokens) {
//        try {
//            String dateStr = tokens[1];
//            int timeslotNum = Integer.parseInt(tokens[2]);
//            Timeslot timeslot = Timeslot.getTimeslotByNumber(timeslotNum);
//            String firstName = tokens[3];
//            String lastName = tokens[4];
//            Date dob = parseDate(tokens[5]);
//            String npi = tokens[6];
//
//            if (timeslot == null || !dob.isValid()) {
//                System.out.println("Invalid timeslot or date of birth.");
//                return;
//            }
//
//            // Find the doctor by NPI
//            Doctor doctor = findDoctorByNPI(npi);
//            if (doctor == null) {
//                System.out.println("Doctor with NPI " + npi + " not found.");
//                return;
//            }
//
//            Patient patient = new Patient(new Profile(firstName, lastName, dob));
//            Appointment appointment = new Appointment(new Date(dateStr), timeslot, patient, doctor);
//
//            // Check for conflicts and book the appointment
//            if (checkConflicts(appointment)) {
//                System.out.println("Appointment conflicts with an existing one.");
//            } else {
//                appointments.add(appointment);
//                System.out.println("Appointment scheduled for Dr. " + doctor.getProfile().getFullName());
//            }
//
//        } catch (Exception e) {
//            System.out.println("Error processing appointment: " + e.getMessage());
//        }
//    }
//
//    // Schedule an imaging appointment (T command)
//    private void scheduleImagingAppointment(String[] tokens) {
//        try {
//            String dateStr = tokens[1];
//            int timeslotNum = Integer.parseInt(tokens[2]);
//            Timeslot timeslot = Timeslot.getTimeslotByNumber(timeslotNum);
//            String firstName = tokens[3];
//            String lastName = tokens[4];
//            Date dob = parseDate(tokens[5]);
//            String imagingService = tokens[6].toLowerCase();
//
//            if (timeslot == null || !dob.isValid()) {
//                System.out.println("Invalid timeslot or date of birth.");
//                return;
//            }
//
//            // Parse dateStr (MM/DD/YYYY) to create a Date object
//            Date apptDate = parseDate(dateStr); // Use the parseDate helper method to convert the string into Date
//
//            // Get the next technician in the circular rotation
//            Technician technician = getNextTechnician();
//            Radiology.ImagingService serviceType = Radiology.ImagingService.valueOf(imagingService.toUpperCase());
//
//            // Check if technician is available for the requested service and time
//            if (technician.isAvailable(serviceType, apptDate, timeslot)) {
//                Imaging imagingAppointment = new Imaging(apptDate, timeslot, new Patient(new Profile(firstName, lastName, dob)), technician, serviceType);
//                appointments.add(imagingAppointment);
//                System.out.println("Imaging appointment scheduled with technician " + technician.getProfile().getFullName());
//            } else {
//                System.out.println("No available technician for the requested service and time.");
//            }
//
//        } catch (Exception e) {
//            System.out.println("Error scheduling imaging appointment: " + e.getMessage());
//        }
//    }
//
//
//    // Cancel an appointment (C command)
//    private void cancelAppointment(String[] tokens) {
//        try {
//            Date date = parseDate(tokens[1]);
//            Timeslot timeslot = Timeslot.getTimeslotByNumber(Integer.parseInt(tokens[2]));
//            String firstName = tokens[3];
//            String lastName = tokens[4];
//            Date dob = parseDate(tokens[5]);
//
//            for (Appointment appt : appointments) {
//                if (appt.getDate().equals(date) && appt.getTimeslot().equals(timeslot) &&
//                        appt.getPatient().getProfile().getFname().equals(firstName) &&
//                        appt.getPatient().getProfile().getLname().equals(lastName) &&
//                        appt.getPatient().getProfile().getDob().equals(dob)) {
//                    appointments.remove(appt);
//                    System.out.println("Appointment cancelled for " + firstName + " " + lastName);
//                    return;
//                }
//            }
//            System.out.println("Appointment not found.");
//
//        } catch (Exception e) {
//            System.out.println("Error cancelling appointment: " + e.getMessage());
//        }
//    }
//
//    // Reschedule an appointment (R command)
//    private void rescheduleAppointment(String[] tokens) {
//        try {
//            cancelAppointment(tokens); // First cancel the existing appointment
//            tokens[2] = tokens[6]; // Set the new timeslot
//            scheduleDoctorAppointment(tokens); // Then schedule the new one
//        } catch (Exception e) {
//            System.out.println("Error rescheduling appointment: " + e.getMessage());
//        }
//    }
//
//    // Display office appointments (PO command)
//    private void displayOfficeAppointments() {
//        if (appointments.isEmpty()) {
//            System.out.println("No office appointments.");
//            return;
//        }
//
//        System.out.println("Office appointments:");
//        for (Appointment appt : appointments) {
//            if (!(appt instanceof Imaging)) {
//                System.out.println(appt);
//            }
//        }
//    }
//
//    // Display imaging appointments (PI command)
//    private void displayImagingAppointments() {
//        if (appointments.isEmpty()) {
//            System.out.println("No imaging appointments.");
//            return;
//        }
//
//        System.out.println("Imaging appointments:");
//        for (Appointment appt : appointments) {
//            if (appt instanceof Imaging) {
//                System.out.println(appt);
//            }
//        }
//    }
//
//    // Display provider credit amounts (PC command)
//    private void displayProviderCredit() {
//        // Display each provider's expected credit based on appointments
//        for (Provider provider : providers) {
//            int totalCredit = 0;
//            for (Appointment appt : appointments) {
//                if (appt.getProvider().equals(provider)) {
//                    totalCredit += provider.rate();
//                }
//            }
//            System.out.println(provider + " [credit: $" + totalCredit + "]");
//        }
//    }
//
//    // Display all appointments sorted by date (PA command)
//    private void displayAppointments() {
//        Sort.appointment(appointments, 'd');
//        System.out.println(appointments);
//    }
//
//    // Helper method to check for conflicting appointments
//    private boolean checkConflicts(Appointment newAppt) {
//        for (Appointment appt : appointments) {
//            if (appt.getDate().equals(newAppt.getDate()) &&
//                    appt.getTimeslot().equals(newAppt.getTimeslot()) &&
//                    appt.getProvider().equals(newAppt.getProvider())) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    // Helper method to find a doctor by their NPI
//    private Doctor findDoctorByNPI(String npi) {
//        for (Provider provider : providers) {
//            if (provider instanceof Doctor && ((Doctor) provider).getNPI().equals(npi)) {
//                return (Doctor) provider;
//            }
//        }
//        return null;
//    }
//
//    // Helper method to parse a date string in MM/DD/YYYY format
//    private Date parseDate(String dateStr) {
//        String[] parts = dateStr.split("/");
//        int month = Integer.parseInt(parts[0]);
//        int day = Integer.parseInt(parts[1]);
//        int year = Integer.parseInt(parts[2]);
//        return new Date(month, day, year);
//    }
//
//}
