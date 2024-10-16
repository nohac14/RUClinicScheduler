package clinic;

import java.util.Scanner;
import java.util.StringTokenizer;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * The ClinicManager class manages appointments in the clinic. It handles scheduling
 * office and imaging appointments, rescheduling, canceling, and printing various views of
 * the appointments and provider information.
 *
 * It also supports loading providers from a text file and managing the circular list of technicians.
 *
 * @author Jonas Lazebnik
 */
public class ClinicManager {
    private List<Appointment> appointments = new List<>();  // List to store all appointments
    private List<Provider> providers = new List<>();  // List to store all providers
    private List<Technician> technicianQueue = new List<>();  // Circular list of technicians

    /**
     * Starts the Clinic Manager, loading the providers and running the main command loop.
     */
    public void run() {
        System.out.println("Clinic Manager is running.");
        loadProvidersFromFile();
        printProviders();

        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) continue;

            StringTokenizer tokenizer = new StringTokenizer(input, ",");
            String command = tokenizer.nextToken().toUpperCase();
            switch (command) {
                case "D":
                    handleOfficeAppointment(tokenizer);
                    break;
                case "T":
                    handleImagingAppointment(tokenizer);
                    break;
                case "C":
                    handleCancelCommand(tokenizer);
                    break;
                case "R":
                    handleRescheduleCommand(tokenizer);
                    break;
                case "PO":
                    handlePrintOfficeAppointments();
                    break;
                case "PI":
                    handlePrintImagingAppointments();
                    break;
                case "PC":
                    handlePrintCredits();
                    break;
                case "Q":
                    System.out.println("Clinic Manager terminated");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid command!");
                    break;
            }
        }
    }

    /**
     * Loads the providers from the "providers.txt" file at startup.
     */
    private void loadProvidersFromFile() {
        try {
            Scanner fileScanner = new Scanner(new File("providers.txt"));
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (line.isEmpty()) continue;

                // Parse provider info and create appropriate Provider objects (Doctor or Technician)
                StringTokenizer tokenizer = new StringTokenizer(line, ",");
                String type = tokenizer.nextToken();
                String fName = tokenizer.nextToken();
                String lName = tokenizer.nextToken();
                String dob = tokenizer.nextToken();
                String specialtyOrRate = tokenizer.nextToken();
                String npiOrLocation = tokenizer.nextToken();

                Profile profile = new Profile(fName, lName, parseDate(dob));
                if (type.equals("DOCTOR")) {
                    Specialty specialty = Specialty.valueOf(specialtyOrRate.toUpperCase());
                    Doctor doctor = new Doctor(profile, specialty, npiOrLocation);
                    providers.add(doctor);
                } else if (type.equals("TECHNICIAN")) {
                    int rate = Integer.parseInt(specialtyOrRate);
                    Technician technician = new Technician(profile, rate, npiOrLocation);
                    providers.add(technician);
                    technicianQueue.add(technician); // Add to custom circular queue for technicians
                }
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: providers.txt not found.");
        }
    }

    /**
     * Handles the "D" command to schedule a new office appointment.
     *
     * @param tokenizer the tokenizer containing the appointment details.
     */
    private void handleOfficeAppointment(StringTokenizer tokenizer) {
        try {
            String aptDate = tokenizer.nextToken();
            String timeSlot = tokenizer.nextToken();
            String fName = tokenizer.nextToken();
            String lName = tokenizer.nextToken();
            String dob = tokenizer.nextToken();
            String npi = tokenizer.nextToken();

            Date date = parseDate(aptDate);
            Timeslot timeslot = parseTimeSlot(timeSlot);
            Profile patient = new Profile(fName, lName, parseDate(dob));

            // Check for valid NPI and provider availability
            Provider provider = findProviderByNPI(npi);
            if (provider == null || !isProviderAvailable(provider, date, timeslot)) {
                return;
            }

            // Create the appointment and add it to the list
            Appointment appointment = new Appointment(date, timeslot, patient, provider);
            appointments.add(appointment);
            System.out.println("Office appointment booked for " + patient + " with provider " + provider);
        } catch (Exception e) {
            System.out.println("Error scheduling office appointment: " + e.getMessage());
        }
    }

    /**
     * Handles the "T" command to schedule a new imaging appointment.
     *
     * @param tokenizer the tokenizer containing the appointment details.
     */
    private void handleImagingAppointment(StringTokenizer tokenizer) {
        try {
            String aptDate = tokenizer.nextToken();
            String timeSlot = tokenizer.nextToken();
            String fName = tokenizer.nextToken();
            String lName = tokenizer.nextToken();
            String dob = tokenizer.nextToken();
            String roomType = tokenizer.nextToken().toUpperCase();

            Date date = parseDate(aptDate);
            Timeslot timeslot = parseTimeSlot(timeSlot);
            Profile patient = new Profile(fName, lName, parseDate(dob));

            // Assign next available technician for the requested room type (e.g., XRAY, ULTRASOUND, CATSCAN)
            Radiology.ImagingService imagingService = Radiology.ImagingService.valueOf(roomType);
            Provider technician = assignNextAvailableTechnician(imagingService, date, timeslot);
            if (technician == null) {
                System.out.println("No available technician for " + roomType + " at the requested time.");
                return;
            }

            // Create and add the imaging appointment
            Imaging imagingAppointment = new Imaging(date, timeslot, patient, technician, imagingService);
            appointments.add(imagingAppointment);
            System.out.println("Imaging appointment booked for " + patient + " for " + roomType);
        } catch (Exception e) {
            System.out.println("Error scheduling imaging appointment: " + e.getMessage());
        }
    }

    /**
     * Assigns the next available technician for the given imaging service, date, and timeslot.
     * Uses a circular list to rotate between technicians.
     *
     * @param imagingService the type of imaging service.
     * @param date the date of the appointment.
     * @param timeslot the timeslot of the appointment.
     * @return the assigned Technician, or null if none are available.
     */
    private Provider assignNextAvailableTechnician(Radiology.ImagingService imagingService, Date date, Timeslot timeslot) {
        for (int i = 0; i < technicianQueue.size(); i++) {
            Technician technician = technicianQueue.get(i);

            // Check if the technician is available for the requested timeslot
            if (technician.isAvailable(imagingService, date, timeslot)) {
                return technician;
            }

            // Move to the next technician (circular behavior)
            Technician temp = technicianQueue.get(0);
            technicianQueue.remove(0);
            technicianQueue.add(temp);
        }
        return null;  // No technicians are available
    }

    /**
     * Handles the "C" command to cancel an existing appointment.
     *
     * @param tokenizer the tokenizer containing the appointment details.
     */
    private void handleCancelCommand(StringTokenizer tokenizer) {
        try {
            String aptDate = tokenizer.nextToken();
            String timeSlot = tokenizer.nextToken();
            String fName = tokenizer.nextToken();
            String lName = tokenizer.nextToken();
            String dob = tokenizer.nextToken();

            Date date = parseDate(aptDate);
            Timeslot timeslot = parseTimeSlot(timeSlot);
            Profile patient = new Profile(fName, lName, parseDate(dob));

            for (int i = 0; i < appointments.size(); i++) {
                Appointment appointment = appointments.get(i);
                if (appointment.getDate().equals(date) &&
                        appointment.getTimeslot().equals(timeslot) &&
                        appointment.getPatient().equals(patient)) {
                    appointments.remove(i);
                    System.out.println("Appointment for " + patient + " on " + date + " at " + timeslot + " canceled.");
                    return;
                }
            }
            System.out.println("No matching appointment found to cancel.");
        } catch (Exception e) {
            System.out.println("Error canceling appointment: " + e.getMessage());
        }
    }

    /**
     * Handles the "R" command to reschedule an existing appointment.
     *
     * @param tokenizer the tokenizer containing the reschedule details.
     */
    private void handleRescheduleCommand(StringTokenizer tokenizer) {
        try {
            String aptDate = tokenizer.nextToken();
            String oldTimeSlot = tokenizer.nextToken();
            String fName = tokenizer.nextToken();
            String lName = tokenizer.nextToken();
            String dob = tokenizer.nextToken();
            String newTimeSlot = tokenizer.nextToken();

            Date date = parseDate(aptDate);
            Timeslot oldTimeslot = parseTimeSlot(oldTimeSlot);
            Timeslot newTimeslot = parseTimeSlot(newTimeSlot);
            Profile patient = new Profile(fName, lName, parseDate(dob));

            // Find the appointment to reschedule
            Appointment appointmentToReschedule = null;
            for (int i = 0; i < appointments.size(); i++) {
                Appointment appointment = appointments.get(i);
                if (appointment.getDate().equals(date) &&
                        appointment.getTimeslot().equals(oldTimeslot) &&
                        appointment.getPatient().equals(patient)) {
                    appointmentToReschedule = appointment;
                    break;
                }
            }

            if (appointmentToReschedule == null) {
                System.out.println("No matching appointment found to reschedule.");
                return;
            }

            // Check if the provider is available for the new timeslot
            Provider provider = appointmentToReschedule.getProvider();
            if (!isProviderAvailable(provider, date, newTimeslot)) {
                System.out.println("Provider is not available at the new timeslot.");
                return;
            }

            // Reschedule the appointment
            appointmentToReschedule.setTimeslot(newTimeslot);
            System.out.println("Appointment rescheduled to " + newTimeslot);
        } catch (Exception e) {
            System.out.println("Error rescheduling appointment: " + e.getMessage());
        }
    }

    /**
     * Handles printing office appointments sorted by location.
     */
    private void handlePrintOfficeAppointments() {
        Sort.appointment(appointments, 'l');
        System.out.println("\n** Office appointments ordered by county/date/time **");
        for (int i = 0; i < appointments.size(); i++) {
            Appointment appointment = appointments.get(i);
            if (!(appointment instanceof Imaging)) {
                System.out.println(appointment);
            }
        }
    }

    /**
     * Handles printing imaging appointments sorted by location.
     */
    private void handlePrintImagingAppointments() {
        Sort.appointment(appointments, 'l');
        System.out.println("\n** Imaging appointments ordered by county/date/time **");
        for (int i = 0; i < appointments.size(); i++) {
            Appointment appointment = appointments.get(i);
            if (appointment instanceof Imaging) {
                System.out.println(appointment);
            }
        }
    }

    /**
     * Handles printing the expected credits for each provider, sorted by profile.
     */
    private void handlePrintCredits() {
        Sort.provider(providers);
        System.out.println("\n** Provider credits **");
        for (int i = 0; i < providers.size(); i++) {
            Provider provider = providers.get(i);
            int totalCredits = 0;
            for (int j = 0; j < appointments.size(); j++) {
                Appointment appointment = appointments.get(j);
                if (appointment.getProvider().equals(provider)) {
                    totalCredits += provider.rate();
                }
            }
            System.out.printf("%s: $%,d\n", provider.getProfile().toString(), totalCredits);
        }
    }

    /**
     * Helper method to find a provider by NPI.
     *
     * @param npi the NPI of the provider.
     * @return the provider object if found, otherwise null.
     */
    private Provider findProviderByNPI(String npi) {
        for (int i = 0; i < providers.size(); i++) {
            Provider provider = providers.get(i);
            if (provider.getNPI().equals(npi)) {
                return provider;
            }
        }
        System.out.println("Provider with NPI " + npi + " not found.");
        return null;
    }

    /**
     * Helper method to check if a provider is available for the given date and timeslot.
     *
     * @param provider the provider to check.
     * @param date the date of the appointment.
     * @param timeslot the timeslot of the appointment.
     * @return true if the provider is available, otherwise false.
     */
    private boolean isProviderAvailable(Provider provider, Date date, Timeslot timeslot) {
        for (int i = 0; i < appointments.size(); i++) {
            Appointment appointment = appointments.get(i);
            if (appointment.getProvider().equals(provider) &&
                appointment.getDate().equals(date) &&
                appointment.getTimeslot().equals(timeslot)) {
                return false;  // Provider is not available
            }
        }
        return true;  // Provider is available
    }

    /**
     * Parses a date from a string in "MM/DD/YYYY" format.
     *
     * @param dateString the date string.
     * @return the Date object.
     */
    private Date parseDate(String dateString) {
        String[] parts = dateString.split("/");
        return new Date(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
    }

    /**
     * Parses a time slot from the input string.
     *
     * @param timeSlot the time slot string input.
     * @return the parsed Timeslot object, or null if invalid.
     */
    private Timeslot parseTimeSlot(String timeSlot) {
        try {
            return Timeslot.getTimeslotByNumber(Integer.parseInt(timeSlot));
        } catch (NumberFormatException e) {
            System.out.println(timeSlot + " is not a valid time slot.");
            return null;
        }
    }

    /**
     * Prints all providers sorted by profile.
     */
    private void printProviders() {
        Sort.provider(providers);
        System.out.println("\n** Providers ordered by profile **");
        for (int i = 0; i < providers.size(); i++) {
            System.out.println(providers.get(i));
        }
    }
}
