import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Calendar;

/**
 * The Scheduler class manages appointments in a clinic by allowing scheduling,
 * rescheduling, and canceling appointments. It also supports printing schedules
 * sorted by different criteria and generating billing statements.
 *
 * The scheduler can handle a maximum of 100 appointments at a time.
 *
 * @author Jonas Lazebnik, Arjun Anand
 */
public class Scheduler {
    private static final int MAX_APPOINTMENTS = 100;
    private Appointment[] appointments = new Appointment[MAX_APPOINTMENTS];
    private int appointmentCount = 0; // Track the number of appointments

    /**
     * Starts the scheduler, allowing the user to input commands to manage appointments.
     * The scheduler processes commands to schedule, cancel, and reschedule appointments,
     * and to print schedules and billing statements.
     */
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

    /**
     * Handles the "S" command to schedule a new appointment.
     *
     * @param tokenizer the tokenizer containing the appointment details.
     */
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

    /**
     * Handles the "C" command to cancel an existing appointment.
     *
     * @param tokenizer the tokenizer containing the appointment details.
     */
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

    /**
     * Handles the "R" command to reschedule an existing appointment.
     *
     * @param tokenizer the tokenizer containing the rescheduling details.
     */
    private void handleRescheduleCommand(StringTokenizer tokenizer) {
        String aptDate = tokenizer.nextToken();
        String oldTimeSlot = tokenizer.nextToken();
        String fName = tokenizer.nextToken();
        String lName = tokenizer.nextToken();
        String dob = tokenizer.nextToken();
        String newTimeSlot = tokenizer.nextToken();

        Date date = parseDate(aptDate);
        Timeslot originalTimeslot = Timeslot.getTimeslotByNumber(Integer.parseInt(oldTimeSlot));
        Timeslot newSelectedTimeslot = Timeslot.getTimeslotByNumber(Integer.parseInt(newTimeSlot));
        Date dobDate = parseDate(dob);
        Profile patient = new Profile(fName, lName, dobDate);

        Appointment currentAppointment = findAppointment(date, originalTimeslot, patient);
        if (currentAppointment == null) {
            System.out.println(aptDate + " " + originalTimeslot.toString() + " " + fName + " " + lName + " " + dob + " does not exist.");
            return;
        }

        if (newSelectedTimeslot == null) {
            System.out.println(newTimeSlot + " is not a valid time slot.");
            return;
        }

        if (originalTimeslot.equals(newSelectedTimeslot)) {
            System.out.println("New timeslot is the same as the original timeslot. No changes made.");
            return;
        }

        Provider provider = currentAppointment.getProvider();
        if (!isProviderAvailable(provider, date, newSelectedTimeslot)) {
            System.out.println(provider.toString() + " is not available at slot " + newSelectedTimeslot.getSlotNumber() + ".");
            return;
        }

        currentAppointment.setTimeslot(newSelectedTimeslot);
        System.out.println("Rescheduled to " + currentAppointment.toString());
    }

    /**
     * Finds an appointment based on the provided date, timeslot, and patient profile.
     *
     * @param date the date of the appointment.
     * @param originalTimeslot the original timeslot of the appointment.
     * @param patient the patient profile.
     * @return the Appointment if found, otherwise null.
     */
    private Appointment findAppointment(Date date, Timeslot originalTimeslot, Profile patient) {
        Appointment targetAppointment = new Appointment(date, originalTimeslot, patient, null); // Null provider for comparison
        for (int i = 0; i < appointmentCount; i++) {
            if (appointments[i].compareTo(targetAppointment) == 0) {
                return appointments[i]; // Found the matching appointment
            }
        }
        return null; // Appointment not found
    }

    /**
     * Checks if the selected timeslot is valid.
     *
     * @param newSelectedTimeslot the new selected timeslot.
     * @param newTimeSlot the new timeslot string input.
     * @return true if the timeslot is valid, false otherwise.
     */
    private boolean isValidTimeslot(Timeslot newSelectedTimeslot, String newTimeSlot) {
        if (newSelectedTimeslot == null) {
            System.out.println(newTimeSlot + " is not a valid time slot.");
            return false;
        }
        return true;
    }

    /**
     * Checks if the provider is available for the new timeslot.
     *
     * @param provider the provider to check.
     * @param date the date of the appointment.
     * @param newSelectedTimeslot the new selected timeslot.
     * @return true if the provider is available, false otherwise.
     */
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

    /**
     * Prints all appointments sorted by date and provider.
     */
    private void handlePrintAppointments() {
        if (appointmentCount == 0) {
            System.out.println("The schedule calendar is empty.");
            return;
        }
        sortAppointmentsByDate();
        System.out.println("\n** Appointments ordered by date/time/provider **");
        printAppointments();
    }

    /**
     * Prints all appointments sorted by patient name.
     */
    private void handlePrintByPatient() {
        if (appointmentCount == 0) {
            System.out.println("The schedule calendar is empty.");
            return;
        }
        sortAppointmentsByPatient();
        System.out.println("\n** Appointments ordered by patient/date/time **");
        printAppointments();
    }

    /**
     * Prints all appointments sorted by location (county).
     */
    private void handlePrintByLocation() {
        if (appointmentCount == 0) {
            System.out.println("The schedule calendar is empty.");
            return;
        }
        sortAppointmentsByLocation();
        System.out.println("\n** Appointments ordered by county/date/time **");
        printAppointments();
    }

    /**
     * Prints billing statements for all patients based on their appointments.
     */
    private void handlePrintBillingStatements() {
        if (appointmentCount == 0) {
            System.out.println("No appointments found to bill.");
            return;
        }
        sortAppointmentsByPatient();
        printBillingStatements();
    }

    /**
     * Parses a date from a string in the format "MM/DD/YYYY".
     *
     * @param dateString the date string to parse.
     * @return the parsed Date object.
     */
    private Date parseDate(String dateString) {
        String[] splitDate = dateString.split("/");
        int month = Integer.parseInt(splitDate[0]);
        int day = Integer.parseInt(splitDate[1]);
        int year = Integer.parseInt(splitDate[2]);
        return new Date(month, day, year);
    }

    /**
     * Parses the profile of a patient from the first name, last name, and DOB.
     *
     * @param fName the patient's first name.
     * @param lName the patient's last name.
     * @param dob the patient's date of birth.
     * @return the Profile object representing the patient.
     */
    private Profile parseProfile(String fName, String lName, String dob) {
        String[] splitDob = dob.split("/");
        int dobMonth = Integer.parseInt(splitDob[0]);
        int dobDay = Integer.parseInt(splitDob[1]);
        int dobYear = Integer.parseInt(splitDob[2]);
        Date dobDate = new Date(dobMonth, dobDay, dobYear);
        return new Profile(fName, lName, dobDate);
    }

    /**
     * Retrieves the current date.
     *
     * @return the current Date object.
     */
    private Date getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH) + 1; // Month is 0-indexed
        int currentYear = calendar.get(Calendar.YEAR);
        return new Date(currentMonth, currentDay, currentYear);
    }

    /**
     * Parses a time slot from the input string.
     *
     * @param timeSlot the time slot string input.
     * @return the parsed Timeslot object, or null if invalid.
     */
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

    /**
     * Checks if the appointment date is valid.
     *
     * @param appointmentDate the appointment date.
     * @param currentDate the current date.
     * @return true if the appointment date is valid, false otherwise.
     */
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

    /**
     * Checks if the patient's date of birth is valid.
     *
     * @param dobDate the patient's date of birth.
     * @param currentDate the current date.
     * @return true if the date of birth is valid, false otherwise.
     */
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

    /**
     * Checks if there is a duplicate appointment.
     *
     * @param date the date of the appointment.
     * @param timeslot the timeslot of the appointment.
     * @param patient the patient profile.
     * @return true if a duplicate appointment exists, false otherwise.
     */
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

    /**
     * Validates and returns the provider based on the input string.
     *
     * @param providerName the name of the provider.
     * @return the Provider object if valid, null otherwise.
     */
    private Provider getValidProvider(String providerName) {
        try {
            return Provider.valueOf(providerName.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println(providerName + " - provider doesn't exist.");
            return null;
        }
    }

    /**
     * Checks if the provider is unavailable for the selected timeslot and date.
     *
     * @param provider the provider.
     * @param date the date of the appointment.
     * @param timeslot the timeslot of the appointment.
     * @return true if the provider is unavailable, false otherwise.
     */
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

    /**
     * Finalizes and adds the appointment to the schedule.
     *
     * @param date the date of the appointment.
     * @param timeslot the timeslot of the appointment.
     * @param patient the patient profile.
     * @param provider the provider.
     */
    private void finalizeAppointment(Date date, Timeslot timeslot, Profile patient, Provider provider) {
        if (appointmentCount < MAX_APPOINTMENTS) {
            Appointment newAppointment = new Appointment(date, timeslot, patient, provider);
            appointments[appointmentCount++] = newAppointment;
            System.out.println(newAppointment.toString() + " booked.");
        } else {
            System.out.println("Appointment list is full.");
        }
    }

    /**
     * Cancels an appointment based on the date, timeslot, and patient profile.
     *
     * @param date the date of the appointment.
     * @param timeslot the timeslot of the appointment.
     * @param patient the patient profile.
     */
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

    /**
     * Reschedules an appointment to a new timeslot.
     *
     * @param appointment the appointment to be rescheduled.
     * @param newTimeslot the new timeslot.
     */
    private void rescheduleAppointment(Appointment appointment, Timeslot newTimeslot) {
        appointment.setTimeslot(newTimeslot);
        System.out.println("Rescheduled to " + appointment.toString());
    }

    /**
     * Sorts appointments by date and provider.
     */
    private void sortAppointmentsByDate() {
        for (int i = 0; i < appointmentCount - 1; i++) {
            for (int j = 0; j < appointmentCount - i - 1; j++) {
                if (compareAppointments(appointments[j], appointments[j + 1]) > 0) {
                    swapAppointments(j, j + 1);
                }
            }
        }
    }

    /**
     * Compares two appointments by date and timeslot.
     *
     * @param a1 the first appointment.
     * @param a2 the second appointment.
     * @return the comparison result.
     */
    private int compareAppointments(Appointment a1, Appointment a2) {
        int dateComparison = a1.getDate().compareTo(a2.getDate());
        if (dateComparison != 0) return dateComparison;

        int timeslotComparison = a1.getTimeslot().ordinal() - a2.getTimeslot().ordinal();
        if (timeslotComparison != 0) return timeslotComparison;

        return a1.getProvider().name().compareTo(a2.getProvider().name());
    }

    /**
     * Swaps two appointments in the array.
     *
     * @param index1 the index of the first appointment.
     * @param index2 the index of the second appointment.
     */
    private void swapAppointments(int index1, int index2) {
        Appointment temp = appointments[index1];
        appointments[index1] = appointments[index2];
        appointments[index2] = temp;
    }

    /**
     * Sorts appointments by patient profile.
     */
    private void sortAppointmentsByPatient() {
        for (int i = 0; i < appointmentCount - 1; i++) {
            for (int j = 0; j < appointmentCount - i - 1; j++) {
                if (appointments[j].getPatient().compareTo(appointments[j + 1].getPatient()) > 0) {
                    swapAppointments(j, j + 1);
                }
            }
        }
    }

    /**
     * Sorts appointments by location (county).
     */
    private void sortAppointmentsByLocation() {
        for (int i = 0; i < appointmentCount - 1; i++) {
            for (int j = 0; j < appointmentCount - i - 1; j++) {
                if (appointments[j].compareByCounty(appointments[j + 1]) > 0) {
                    swapAppointments(j, j + 1);
                }
            }
        }
    }

    /**
     * Prints all appointments in the current schedule.
     */
    private void printAppointments() {
        for (int i = 0; i < appointmentCount; i++) {
            System.out.println(appointments[i].toString());
        }
        System.out.println("** end of list **");
    }

    /**
     * Prints billing statements for all patients in the schedule.
     */
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

    /**
     * Calculates the charge based on the provider's specialty.
     *
     * @param provider the provider.
     * @return the calculated charge.
     */
    private int calculateCharge(Provider provider) {
        switch (provider.getSpecialty()) {
            case Specialty.FAMILY: return 250;
            case Specialty.PEDIATRICIAN: return 300;
            case Specialty.ALLERGIST: return 350;
            default: return 200;
        }
    }

    /**
     * Checks if the current appointment is for a new patient.
     *
     * @param i the index of the current appointment.
     * @param current the current appointment.
     * @return true if the patient is new, false otherwise.
     */
    private boolean isNewPatient(int i, Appointment current) {
        return i < appointmentCount - 1 && !appointments[i + 1].getPatient().equals(current.getPatient());
    }

    /**
     * Checks if the current appointment is the last one in the schedule.
     *
     * @param i the index of the current appointment.
     * @return true if it is the last appointment, false otherwise.
     */
    private boolean isLastAppointment(int i) {
        return i == appointmentCount - 1;
    }

    /**
     * Prints a billing statement for a patient.
     *
     * @param index the index of the patient.
     * @param patient the patient's profile.
     * @param totalAmountDue the total amount due for the patient's appointments.
     */
    private void printPatientStatement(int index, Profile patient, int totalAmountDue) {
        System.out.printf("(%d) %s %s %s [amount due: $%,.2f]\n",
                index, patient.getFname(), patient.getLname(), patient.getDob().toString(), (double) totalAmountDue);
    }

    /**
     * Resets the appointment list by clearing all appointments.
     */
    private void resetAppointments() {
        for (int i = 0; i < appointmentCount; i++) {
            appointments[i] = null;
        }
        appointmentCount = 0;
    }
}
