package clinic;

import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Calendar;

public class testModular {
    private static final int MAX_APPOINTMENTS = 100;  // Adjust this as needed
    private Appointment[] appointments = new Appointment[MAX_APPOINTMENTS];
    private int appointmentCount = 0;  // Track the number of appointments

    public void run() {
        System.out.println("Scheduler is running.");
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            String input = scanner.nextLine().trim(); // User input + trims any whitespace
            if (input.isEmpty()) continue;
            StringTokenizer tokenizer = new StringTokenizer(input, ",");
            String command = tokenizer.nextToken();
            switch (command) {
                case "S":
                    handleScheduleCommand(tokenizer);
                    break;
                case "C":
                    handleCancelCommand(tokenizer);
                    break;
                case "R":
                    handleRescheduleCommand(tokenizer);
                    break;
                case "PA":
                    handlePrintAppointments();
                    break;
                case "PP":
                    handlePrintByPatient();
                    break;
                case "PL":
                    handlePrintByLocation();
                    break;
                case "PS":
                    handlePrintBillingStatements();
                    break;
                case "Q":
                    System.out.print("Scheduler is terminated.");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid command!");
                    break;
            }
        }
    }

    // Handler for scheduling a new appointment (S)
    private void handleScheduleCommand(StringTokenizer tokenizer) {
        String aptDate = tokenizer.nextToken();
        String timeSlot = tokenizer.nextToken();
        String fName = tokenizer.nextToken();
        String lName = tokenizer.nextToken();
        String dob = tokenizer.nextToken();
        String inputProvider = tokenizer.nextToken();

        Date date = parseDate(aptDate);
        Date dobDate = parseDate(dob);
        Date currentDate = getCurrentDate();

        if (!isValidAppointmentDate(date, currentDate)) return;
        Timeslot selectedTimeslot = parseTimeSlot(timeSlot);
        if (selectedTimeslot == null) return;
        if (!isValidDateOfBirth(dobDate, currentDate)) return;

        Profile patient = new Profile(fName, lName, dobDate);
        if (isDuplicateAppointment(date, selectedTimeslot, patient)) return;

        Provider provider = getValidProvider(inputProvider);
        if (provider == null) return;
        if (isProviderUnavailable(provider, date, selectedTimeslot)) return;

        finalizeAppointment(date, selectedTimeslot, patient, provider);
    }

    // Handler for canceling an appointment (C)
    private void handleCancelCommand(StringTokenizer tokenizer) {
        String aptDate = tokenizer.nextToken();
        String timeSlot = tokenizer.nextToken();
        String fName = tokenizer.nextToken();
        String lName = tokenizer.nextToken();
        String dob = tokenizer.nextToken();

        Date date = parseDate(aptDate);
        Timeslot selectedTimeslot = parseTimeSlot(timeSlot);
        Date dobDate = parseDate(dob);
        Profile patient = new Profile(fName, lName, dobDate);

        cancelAppointment(date, selectedTimeslot, patient);
    }

    // Search for the existing appointment by date, timeslot, and patient
    private Appointment findAppointment(Date date, Timeslot originalTimeslot, Profile patient) {
        Appointment targetAppointment = new Appointment(date, originalTimeslot, patient, null); // Null provider for comparison
        for (int i = 0; i < appointmentCount; i++) {
            if (appointments[i].compareTo(targetAppointment) == 0) {
                return appointments[i]; // Found the matching appointment
            }
        }
        return null; // Appointment not found
    }
    // Check if the new timeslot is valid
    private boolean isValidTimeslot(Timeslot newSelectedTimeslot, String newTimeSlot) {
        if (newSelectedTimeslot == null) {
            System.out.println(newTimeSlot + " is not a valid time slot.");
            return false; // Invalid timeslot
        }
        return true; // Timeslot is valid
    }
    // Check if the provider is available for the new timeslot
    private boolean isProviderAvailable(Provider provider, Date date, Timeslot newSelectedTimeslot) {
        for (int i = 0; i < appointmentCount; i++) {
            if (appointments[i].getProvider().equals(provider) &&
                    appointments[i].getTimeslot().equals(newSelectedTimeslot) &&
                    appointments[i].getDate().equals(date)) {
                return false; // Provider is unavailable
            }
        }
        return true; // Provider is available
    }

    // Handler for rescheduling an appointment (R)
    // Handler for rescheduling an appointment (R)
    private void handleRescheduleCommand(StringTokenizer tokenizer) {
        String aptDate = tokenizer.nextToken();       // Appointment date
        String oldTimeSlot = tokenizer.nextToken();   // Old timeslot
        String fName = tokenizer.nextToken();         // Patient first name
        String lName = tokenizer.nextToken();         // Patient last name
        String dob = tokenizer.nextToken();           // Patient DOB
        String newTimeSlot = tokenizer.nextToken();   // New timeslot

        // Parse date, timeslots, and patient info
        Date date = parseDate(aptDate);
        Timeslot originalTimeslot = Timeslot.getTimeslotByNumber(Integer.parseInt(oldTimeSlot));
        Timeslot newSelectedTimeslot = Timeslot.getTimeslotByNumber(Integer.parseInt(newTimeSlot));
        Date dobDate = parseDate(dob);
        Profile patient = new Profile(fName, lName, dobDate);

        // Step 1: Search for the existing appointment
        Appointment currentAppointment = findAppointment(date, originalTimeslot, patient);
        if (currentAppointment == null) {
            System.out.println(aptDate + " " + originalTimeslot + " " + fName + " " + lName + " " + dob + " does not exist.");
            return; // If no matching appointment was found, exit
        }

        // Step 2: Check if the new timeslot is valid
        if (newSelectedTimeslot == null) {
            System.out.println(newTimeSlot + " is not a valid time slot.");
            return; // Invalid timeslot, exit
        }

        // Step 3: Check if the new timeslot is available for the provider
        Provider provider = currentAppointment.getProvider();
        if (isProviderUnavailable(provider, date, newSelectedTimeslot)) {
            System.out.println(provider.toString() + " is not available at slot " + newSelectedTimeslot.getSlotNumber() + ".");
            return; // Exit if provider is unavailable
        }

        // Step 4: Reschedule the appointment
        currentAppointment.setTimeslot(newSelectedTimeslot);
        System.out.println("Rescheduled to " + currentAppointment.toString());
    }

    // Print all appointments sorted by date and provider (PA)
    private void handlePrintAppointments() {
        if (appointmentCount == 0) {
            System.out.println("The schedule calendar is empty.");
            return;
        }
        sortAppointmentsByDate();
        System.out.println("\n** Appointments ordered by date/time/provider **");
        printAppointments();
    }

    // Print all appointments sorted by patient name (PP)
    private void handlePrintByPatient() {
        if (appointmentCount == 0) {
            System.out.println("The schedule calendar is empty.");
            return;
        }
        sortAppointmentsByPatient();
        System.out.println("\n** Appointments ordered by patient/date/time **");

        printAppointments();
    }

    // Print all appointments sorted by county/location (PL)
    private void handlePrintByLocation() {
        if (appointmentCount == 0) {
            System.out.println("The schedule calendar is empty.");
            return;
        }
        sortAppointmentsByLocation();
        System.out.println("\n** Appointments ordered by county/date/time **");
        printAppointments();
    }

    // Print billing statements (PS)
    private void handlePrintBillingStatements() {
        if (appointmentCount == 0) {
            System.out.println("No appointments found to bill.");
            return;
        }
        sortAppointmentsByPatient();
        printBillingStatements();
    }

    // Helper methods
    private Date parseDate(String dateString) {
        String[] splitDate = dateString.split("/");
        int month = Integer.parseInt(splitDate[0]);
        int day = Integer.parseInt(splitDate[1]);
        int year = Integer.parseInt(splitDate[2]);
        return new Date(month, day, year);
    }

    private Profile parseProfile(String fName, String lName, String dob) {
        String[] splitDob = dob.split("/");
        int dobMonth = Integer.parseInt(splitDob[0]);
        int dobDay = Integer.parseInt(splitDob[1]);
        int dobYear = Integer.parseInt(splitDob[2]);
        Date dobDate = new Date(dobMonth, dobDay, dobYear);
        return new Profile(fName, lName, dobDate);
    }



    private Date getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;  // Month is 0-indexed
        int currentYear = calendar.get(Calendar.YEAR);
        return new Date(currentMonth, currentDay, currentYear);
    }

    private Timeslot parseTimeSlot(String timeSlot) {
        try {
            int timeSlotNumber = Integer.parseInt(timeSlot);
            Timeslot selectedTimeslot = Timeslot.getTimeslotByNumber(timeSlotNumber);
            if (selectedTimeslot == null) {
                System.out.println(timeSlot + " is not a valid time slot.");
            }
            return selectedTimeslot;
        } catch (NumberFormatException e) {
            System.out.println(timeSlot + " is not a valid time slot.");
            return null;
        }
    }

    private boolean isValidAppointmentDate(Date appointmentDate, Date currentDate) {
        if (!appointmentDate.isValid()) {
            System.out.println("Appointment date: " + appointmentDate + " is not a valid calendar date.");
            return false;
        } else if (appointmentDate.compareTo(currentDate) <= 0) {
            System.out.println("Appointment date: " + appointmentDate + " is today or a date before today.");
            return false;
        }

        Calendar appointmentCalendar = Calendar.getInstance();
        appointmentCalendar.set(appointmentDate.getYear(), appointmentDate.getMonth() - 1, appointmentDate.getDay());

        int dayOfWeek = appointmentCalendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
            System.out.println("Appointment date: " + appointmentDate + " is Saturday or Sunday.");
            return false;
        }

        Calendar sixMonthsFromNow = Calendar.getInstance();
        sixMonthsFromNow.add(Calendar.MONTH, 6);
        if (appointmentCalendar.after(sixMonthsFromNow)) {
            System.out.println("Appointment date: " + appointmentDate + " is not within six months.");
            return false;
        }

        return true;
    }

    private boolean isValidDateOfBirth(Date dobDate, Date currentDate) {
        if (!dobDate.isValid()) {
            System.out.println("Patient dob: " + dobDate + " is not a valid calendar date.");
            return false;
        } else if (dobDate.compareTo(currentDate) >= 0) {
            System.out.println("Patient dob: " + dobDate + " is today or a date after today.");
            return false;
        }
        return true;
    }

    private boolean isDuplicateAppointment(Date date, Timeslot timeslot, Profile patient) {
        Appointment newAppointment = new Appointment(date, timeslot, patient, null);
        for (int i = 0; i < appointmentCount; i++) {
            if (appointments[i].compareTo(newAppointment) == 0) {
                System.out.println(patient.getFname() + " " + patient.getLname() + " " + patient.getDob() + " has an existing appointment at the same time slot.");
                return true;
            }
        }
        return false;
    }

    private Provider getValidProvider(String providerName) {
        try {
            return Provider.valueOf(providerName.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println(providerName + " - provider doesn't exist.");
            return null;
        }
    }

    private boolean isProviderUnavailable(Provider provider, Date date, Timeslot timeslot) {
        for (int i = 0; i < appointmentCount; i++) {
            if (appointments[i].getProvider().equals(provider) &&
                    appointments[i].getTimeslot().equals(timeslot) &&
                    appointments[i].getDate().equals(date)) {
                System.out.println(provider.toString() + " is not available at slot " + timeslot.getSlotNumber() + ".");
                return true; // Provider is unavailable
            }
        }
        return false; // Provider is available
    }
    private void finalizeAppointment(Date date, Timeslot timeslot, Profile patient, Provider provider) {
        if (appointmentCount < MAX_APPOINTMENTS) {
            Appointment newAppointment = new Appointment(date, timeslot, patient, provider);
            appointments[appointmentCount++] = newAppointment;
            System.out.println(newAppointment.toString() + " booked.");
        } else {
            System.out.println("Appointment list is full.");
        }
    }

    private void cancelAppointment(Date date, Timeslot timeslot, Profile patient) {
        boolean appointmentFound = false;
        for (int i = 0; i < appointmentCount; i++) {
            Appointment currentAppointment = appointments[i];
            if (currentAppointment.getDate().equals(date) &&
                    currentAppointment.getTimeslot().equals(timeslot) &&
                    currentAppointment.getPatient().equals(patient)) {

                System.out.println(date + " " + timeslot.toString() + " " + patient.getFname() + " " + patient.getLname() + " " + patient.getDob() + " has been canceled.");
                for (int j = i; j < appointmentCount - 1; j++) {
                    appointments[j] = appointments[j + 1];
                }
                appointments[--appointmentCount] = null;
                appointmentFound = true;
                break;
            }
        }

        if (!appointmentFound) {
            System.out.println(date + " " + timeslot.toString() + " " + patient.getFname() + " " + patient.getLname() + " " + patient.getDob() + " does not exist.");
        }
    }

    // Reschedule the appointment to the new timeslot
    private void rescheduleAppointment(Appointment appointment, Timeslot newTimeslot) {
        appointment.setTimeslot(newTimeslot);
        System.out.println("Rescheduled to " + appointment.toString());
    }

    private void sortAppointmentsByDate() {
        for (int i = 0; i < appointmentCount - 1; i++) {
            for (int j = 0; j < appointmentCount - i - 1; j++) {
                if (compareAppointments(appointments[j], appointments[j + 1]) > 0) {
                    swapAppointments(j, j + 1);
                }
            }
        }
    }
    //helper for sortAppointmentsByDate
    private int compareAppointments(Appointment a1, Appointment a2) {
        int dateComparison = a1.getDate().compareTo(a2.getDate());
        if (dateComparison != 0) return dateComparison;

        int timeslotComparison = a1.getTimeslot().ordinal() - a2.getTimeslot().ordinal();
        if (timeslotComparison != 0) return timeslotComparison;

        return a1.getProvider().name().compareTo(a2.getProvider().name());
    }
    //helper for sortAppointmentsByDate
    private void swapAppointments(int index1, int index2) {
        Appointment temp = appointments[index1];
        appointments[index1] = appointments[index2];
        appointments[index2] = temp;
    }

    private void sortAppointmentsByPatient() {
        // Manual Bubble Sort to sort the appointments by patient last name, first name, DOB, appointment date, and time
        for (int i = 0; i < appointmentCount - 1; i++) {
            for (int j = 0; j < appointmentCount - i - 1; j++) {
                // Compare two consecutive appointments using the compareTo() method in Appointment class
                if (appointments[j].getPatient().compareTo(appointments[j + 1].getPatient()) > 0) {
                    // Swap appointments[j] and appointments[j + 1]
                    Appointment temp = appointments[j];
                    appointments[j] = appointments[j + 1];
                    appointments[j + 1] = temp;
                }
            }
        }
    }

    private void sortAppointmentsByLocation() {
        // Manual Bubble Sort to sort the appointments by county, date, and timeslot
        for (int i = 0; i < appointmentCount - 1; i++) {
            for (int j = 0; j < appointmentCount - i - 1; j++) {
                // Compare two consecutive appointments using the compareByCounty() method in Appointment class
                if (appointments[j].compareByCounty(appointments[j + 1]) > 0) {
                    // Swap appointments[j] and appointments[j + 1]
                    Appointment temp = appointments[j];
                    appointments[j] = appointments[j + 1];
                    appointments[j + 1] = temp;
                }
            }
        }
    }

    private void printAppointments() {
        for (int i = 0; i < appointmentCount; i++) {
            System.out.println(appointments[i].toString());
        }
        System.out.println("** end of list **");
    }

    private void printBillingStatements() {
        if (appointmentCount == 0) {
            System.out.println("No appointments found to bill.");
            return;
        }

        sortAppointmentsByPatient();
        System.out.println("\n** Billing statement ordered by patient **");

        int index = 1, totalAmountDue = 0;
        for (int i = 0; i < appointmentCount; i++) {
            Appointment currentAppointment = appointments[i];
            totalAmountDue += calculateCharge(currentAppointment.getProvider());

            if (isNewPatient(i, currentAppointment) || isLastAppointment(i)) {
                printPatientStatement(index++, currentAppointment.getPatient(), totalAmountDue);
                totalAmountDue = 0;
            }
        }

        System.out.println("** end of list **");
        resetAppointments();
    }

    private int calculateCharge(Provider provider) {
        switch (provider.getSpecialty()) {
            case FAMILY: return 250;
            case PEDIATRICIAN: return 300;
            case ALLERGIST: return 350;
            default: return 200;
        }
    }

    private boolean isNewPatient(int i, Appointment current) {
        return i < appointmentCount - 1 && !appointments[i + 1].getPatient().equals(current.getPatient());
    }

    private boolean isLastAppointment(int i) {
        return i == appointmentCount - 1;
    }

    private void printPatientStatement(int index, Profile patient, int totalAmountDue) {
        System.out.printf("(%d) %s %s %s [amount due: $%,.2f]\n",
                index, patient.getFname(), patient.getLname(), patient.getDob().toString(), (double) totalAmountDue);
    }

    private void resetAppointments() {
        for (int i = 0; i < appointmentCount; i++) {
            appointments[i] = null;
        }
        appointmentCount = 0;
    }

}
