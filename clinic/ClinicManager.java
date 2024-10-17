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
                String type = tokenizer.nextToken();  // Either "DOCTOR" or "TECHNICIAN"
                String fName = tokenizer.nextToken(); // First name
                String lName = tokenizer.nextToken(); // Last name
                String dob = tokenizer.nextToken();   // Date of birth in MM/DD/YYYY format
                String specialtyOrRate = tokenizer.nextToken(); // Specialty for Doctor or Rate for Technician
                String npiOrLocation = tokenizer.nextToken();   // NPI for Doctor or Location for Technician

                // Create the profile for the provider
                Profile profile = new Profile(fName, lName, parseDate(dob));

                if (type.equals("DOCTOR")) {
                    // Parse the doctor's specialty
                    Specialty specialty = Specialty.valueOf(specialtyOrRate.toUpperCase());

                    // Use npiOrLocation as the NPI string for the doctor
                    String npi = npiOrLocation;

                    // For now, we can assign the location and technician as null, or use logic to assign them
                    Location location = null;  // You can assign a default or parsed location if needed
                    Technician assignedTechnician = null;  // Assign a technician if available

                    // Create the Doctor object
                    Doctor doctor = new Doctor(profile, specialty, npi, location, assignedTechnician);
                    providers.add(doctor);

                } else if (type.equals("TECHNICIAN")) {
                    // Parse the rate for the technician
                    int rate = Integer.parseInt(specialtyOrRate);

                    // Parse the location from the npiOrLocation field
                    Location location = parseLocation(npiOrLocation);

                    // Create the Technician object
                    Technician technician = new Technician(profile, rate, location);
                    providers.add(technician);

                    // Add the technician to the circular queue
                    technicianQueue.add(technician);
                }
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: providers.txt not found.");
        } catch (Exception e) {
            System.out.println("Error parsing provider data: " + e.getMessage());
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

            // Parse date and timeslot
            Date date = parseDate(aptDate);
            Timeslot timeslot = parseTimeSlot(timeSlot);

            // Create Profile for the patient and wrap it in a Patient object (which is a Person)
            Profile patientProfile = new Profile(fName, lName, parseDate(dob));
            Patient patient = new Patient(patientProfile);  // Assuming Patient extends Person

            // Find the provider by NPI
            Provider provider = findProviderByNPI(npi);
            if (provider == null || !isProviderAvailable(provider, date, timeslot)) {
                System.out.println("Error: Invalid provider or provider not available.");
                return;
            }

            // Create and add the appointment with the correct types for patient and provider (both are Person)
            Appointment appointment = new Appointment(date, timeslot, patient, provider);
            appointments.add(appointment);

            System.out.println("Office appointment booked for " + patient + " with provider " + provider);
        } catch (Exception e) {
            System.out.println("Error scheduling office appointment: " + e.getMessage());
        }
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
                        appointment.getPatient().getProfile().equals(patient)) {
                    appointmentToReschedule = appointment;
                    break;
                }
            }

            if (appointmentToReschedule == null) {
                System.out.println("No matching appointment found to reschedule.");
                return;
            }

            // Ensure that the provider is actually a Provider instance
            Provider provider = null;
            if (appointmentToReschedule.getProvider() instanceof Provider) {
                provider = (Provider) appointmentToReschedule.getProvider();
            } else {
                System.out.println("Error: The provider for this appointment is not valid.");
                return;
            }

            // Check if the provider is available for the new timeslot
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
            if (provider.getNpi().equals(npi)) {
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
     * @param date     the date of the appointment.
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

    private Technician assignNextAvailableTechnician(Radiology.ImagingService imagingService, Date date, Timeslot timeslot) {
        for (int i = 0; i < technicianQueue.size(); i++) {
            Technician technician = technicianQueue.get(0);

            // Check if the technician is available for the requested timeslot
            if (technician.isAvailable(imagingService, date, timeslot)) {
                // Move the technician to the end of the queue (circular rotation)
                technicianQueue.remove(0);
                technicianQueue.add(technician);
                return technician;  // Return the assigned technician
            }

            // If not available, rotate the technician to the end of the queue
            technicianQueue.remove(0);
            technicianQueue.add(technician);
        }
        return null;  // No technicians are available
    }
    /**
     * Handles the "D" command to schedule a new office appointment.
     *
     * @param tokenizer the tokenizer containing the appointment details.
     */
    private void handleOfficeAppointment(StringTokenizer tokenizer) {
        try {
            // Parse input values
            String aptDate = tokenizer.nextToken();
            String timeSlot = tokenizer.nextToken();
            String fName = tokenizer.nextToken();
            String lName = tokenizer.nextToken();
            String dob = tokenizer.nextToken();
            String npi = tokenizer.nextToken();

            // Validate date and timeslot
            Date date = validateDate(aptDate);
            Timeslot timeslot = validateTimeSlot(timeSlot);
            if (date == null || timeslot == null) return; // Invalid date or timeslot

            // Validate patient profile
            Profile patientProfile = new Profile(fName, lName, validateDate(dob));

            // Validate provider by NPI
            Provider provider = findProviderByNPI(npi);
            if (provider == null || !isProviderAvailable(provider, date, timeslot)) {
                System.out.println("Error: Invalid provider or provider not available.");
                return;
            }

            // Check for duplicate appointments
            if (appointmentExists(patientProfile, date, timeslot)) {
                System.out.println("Error: Duplicate appointment already exists.");
                return;
            }

            // Create and add the new appointment
            Appointment appointment = new Appointment(date, timeslot, new Person(patientProfile), provider);
            appointments.add(appointment);
            System.out.println("Office appointment booked successfully!");
        } catch (Exception e) {
            System.out.println("Error: Invalid input for office appointment.");
        }
    }

    /**
     * Handles the "T" command to schedule a new imaging appointment.
     *
     * @param tokenizer the tokenizer containing the appointment details.
     */
    private void handleImagingAppointment(StringTokenizer tokenizer) {
        try {
            // Parse input values
            String aptDate = tokenizer.nextToken();
            String timeSlot = tokenizer.nextToken();
            String fName = tokenizer.nextToken();
            String lName = tokenizer.nextToken();
            String dob = tokenizer.nextToken();
            String roomType = tokenizer.nextToken().toUpperCase();

            // Validate date and timeslot
            Date date = validateDate(aptDate);
            Timeslot timeslot = validateTimeSlot(timeSlot);
            if (date == null || timeslot == null) return; // Invalid date or timeslot

            // Validate patient profile
            Profile patientProfile = new Profile(fName, lName, validateDate(dob));

            // Validate room type (imaging service)
            Radiology.ImagingService imagingService;
            try {
                imagingService = Radiology.ImagingService.valueOf(roomType);
            } catch (IllegalArgumentException e) {
                System.out.println("Error: Invalid imaging service type.");
                return;
            }

            // Assign next available technician for the requested room type
            Technician technician = assignNextAvailableTechnician(imagingService, date, timeslot);
            if (technician == null) {
                System.out.println("Error: No available technician for the requested time.");
                return;
            }

            // Create and add the imaging appointment
            Imaging imagingAppointment = new Imaging(date, timeslot, new Person(patientProfile), technician, new Radiology(imagingService));
            appointments.add(imagingAppointment);
            System.out.println("Imaging appointment booked successfully!");
        } catch (Exception e) {
            System.out.println("Error: Invalid input for imaging appointment.");
        }
    }

    /**
     * Validates the date string and ensures it conforms to the expected format.
     *
     * @param dateString the input date string.
     * @return the parsed Date object, or null if invalid.
     */
    private Date validateDate(String dateString) {
        try {
            // Use appropriate date parsing logic (e.g., MM/DD/YYYY)
            return parseDate(dateString);
        } catch (Exception e) {
            System.out.println("Error: Invalid date format.");
            return null;
        }
    }

    /**
     * Validates the time slot input and ensures it corresponds to a valid timeslot.
     *
     * @param timeSlot the input timeslot number.
     * @return the corresponding Timeslot object, or null if invalid.
     */
    private Timeslot validateTimeSlot(String timeSlot) {
        try {
            int slotNumber = Integer.parseInt(timeSlot);
            Timeslot timeslot = Timeslot.getTimeslotByNumber(slotNumber);
            if (timeslot == null) {
                System.out.println("Error: Invalid timeslot.");
            }
            return timeslot;
        } catch (NumberFormatException e) {
            System.out.println("Error: Timeslot is not a valid number.");
            return null;
        }
    }

    /**
     * Checks if an appointment with the same patient, date, and timeslot already exists.
     *
     * @param profile the patient's profile.
     * @param date the appointment date.
     * @param timeslot the appointment timeslot.
     * @return true if an appointment exists, false otherwise.
     */
    private boolean appointmentExists(Profile profile, Date date, Timeslot timeslot) {
        for (Appointment appointment : appointments) {
            if (appointment.getPatient().getProfile().equals(profile) &&
                    appointment.getDate().equals(date) &&
                    appointment.getTimeslot().equals(timeslot)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Validates and finds a provider by NPI.
     *
     * @param npi the NPI number.
     * @return the corresponding Provider, or null if not found or invalid.
     */
    private Provider findProviderByNPI(String npi) {
        if (!npi.matches("\\d+")) {  // NPI should be numeric
            System.out.println("Error: NPI must be numeric.");
            return null;
        }
        for (Provider provider : providers) {
            if (provider.getNpi().equals(npi)) {
                return provider;
            }
        }
        System.out.println("Error: Provider with NPI " + npi + " not found.");
        return null;
    }

    /**
     * Parses a location string in the format "city, county, zip" and returns a Location object.
     *
     * @param locationString the string containing location details.
     * @return the Location object, or null if the format is invalid.
     */
    private Location parseLocation(String locationString) {
        try {
            StringTokenizer tokenizer = new StringTokenizer(locationString, ",");
            String city = tokenizer.nextToken().trim();
            String county = tokenizer.nextToken().trim();
            String zip = tokenizer.nextToken().trim();

            return new Location(city, county, zip);
        } catch (Exception e) {
            System.out.println("Error: Invalid location format.");
            return null;  // Return null if the location string is invalid
        }
    }

}