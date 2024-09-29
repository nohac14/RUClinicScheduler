package clinic;

import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Calendar;


public class Scheduler {
    // Instance variables for managing appointments and commands
    private static final int MAX_APPOINTMENTS = 100;  // Adjust this as needed
    private Appointment[] appointments = new Appointment[MAX_APPOINTMENTS];
    private int appointmentCount = 0;  // Track the number of appointments

    public void run() {
        System.out.println("Scheduler is running.");
        Scanner scanner = new Scanner(System.in);

        boolean running = true;

        while (running) {
            System.out.print(">> "); //Input line for user
            String input = scanner.nextLine().trim(); // User input + trims any whitespace

            if (input.isEmpty()) {  //If the user presses enter (or nothing)
                continue;
            }

            //parsing the input
            StringTokenizer tokenizer = new StringTokenizer(input, ",");
            String command = tokenizer.nextToken();                          // "S"

            if (command.equals("S")) {
                //parsing the rest of the input
                String aptDate = tokenizer.nextToken();                          // "9/31/2024"
                String timeSlot = tokenizer.nextToken();                         // "1"
                Timeslot selectedTimeslot = null;
                String fName = tokenizer.nextToken();                            // "John"
                String lName = tokenizer.nextToken();                            // "Doe"
                String dob = tokenizer.nextToken();                              // "12/13/1989"
                String inputProvider = tokenizer.nextToken();                    // "PATEL"

                // create the appointment date
                String[] splitDate = aptDate.split("/");
                int aMonth = Integer.parseInt(splitDate[0]);
                int aDay = Integer.parseInt(splitDate[1]);
                int aYear = Integer.parseInt(splitDate[2]);
                Date date = new Date(aMonth, aDay, aYear);         // Create a Date object using the constructor

                Calendar calendar = Calendar.getInstance();
                int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
                int currentMonth = calendar.get(Calendar.MONTH) + 1;  // Month is 0-indexed, so add 1
                int currentYear = calendar.get(Calendar.YEAR);
                Date currentDate = new Date(currentMonth, currentDay, currentYear);  // date object for today's date

                calendar.add(Calendar.DAY_OF_MONTH, -1); // Subtract 1 day from today's date
                int yesterDay = calendar.get(Calendar.DAY_OF_MONTH);
                int yesterMonth = calendar.get(Calendar.MONTH) + 1;  // Again, month is 0-indexed
                int yesterYear = calendar.get(Calendar.YEAR);
                Date yesterDate = new Date(yesterMonth, yesterDay, yesterYear); //date obj for yesterday

                Calendar appointmentCalendar = Calendar.getInstance();
                appointmentCalendar.set(aYear, aMonth - 1, aDay);  // Subtract 1 from the month because Calendar months are 0-based
                int dayOfWeek = appointmentCalendar.get(Calendar.DAY_OF_WEEK); // Gets the date of the Week
                //System.out.println("Day of the week for the appointment: " + dayOfWeek); // (Error check with Calander class)

                Calendar sixMonthsFromNow = Calendar.getInstance();
                sixMonthsFromNow.add(Calendar.MONTH, 6);  // Add 6 months

                String[] splitDob = dob.split("/");   // Splits the dob
                int dobMonth = Integer.parseInt(splitDob[0]);
                int dobDay = Integer.parseInt(splitDob[1]);
                int dobYear = Integer.parseInt(splitDob[2]);
                Date dobDate = new Date(dobMonth, dobDay, dobYear);

                // Create the patient profile
                Profile patient = new Profile(fName, lName, dobDate);

                //Error Checks for the appointment date
                if (!date.isValid()) { //valid APT date?
                    System.out.println("Appointment date: " + aptDate + " is not a valid calendar date.");
                    continue;
                } else if (date.equals(currentDate) || date.equals(yesterDate)) {  //yesterday?
                    System.out.println("Appointment date: " + aptDate + " is today or a date before today.");
                    continue;
                } else if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {  //weekend?
                    System.out.println("Appointment date: " + aptDate + " is Saturday or Sunday.");
                    continue;
                } else if (appointmentCalendar.after(sixMonthsFromNow)) {   //Within 6 months?
                    System.out.println("Appointment date: " + aptDate + " is not within six months.");
                    continue;
                }

                //Checks for the timeSlot
                try { //Attempts to build the timeSlot
                    int timeSlotNumber = Integer.parseInt(timeSlot);
                    selectedTimeslot = Timeslot.getTimeslotByNumber(timeSlotNumber);
                    if (selectedTimeslot == null) {
                        System.out.println(timeSlot + " is not a valid time slot.");
                        continue;
                    }
                } catch (NumberFormatException e) { // error catcher for non-numerics
                    System.out.println(timeSlot + " is not a valid time slot.");
                    continue;
                }

                //Checks for dob
                if (!dobDate.isValid()) { //dob valid
                    System.out.println("Patient dob: " + dob + " is not a valid calendar date.");
                    continue;
                } else if (dobDate.compareTo(currentDate) >= 0) { //dob not today or in the future
                    System.out.println("Patient dob: " + dob + " is today or a date after today.");
                    continue;
                }

                // Check for duplicate appointments
                Appointment newAppointment = new Appointment(date, selectedTimeslot, patient, null);  // No provider yet (order of the checks)
                boolean duplicateFound = false;
                for (int i = 0; i < appointmentCount; i++) {
                    if (appointments[i].compareTo(newAppointment) == 0) {
                        System.out.println(fName + " " + lName + " " + dob + " has an existing appointment at the same time slot.");
                        duplicateFound = true;
                        break;  // Exit the loop as soon as we find a duplicate
                    }
                }

                if (duplicateFound) {
                    continue;  // Don't proceed with provider check if a duplicate is found
                }

                // Provider check after appointment validation
                Provider provider;
                try {
                    provider = Provider.valueOf(inputProvider.toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.out.println(inputProvider + " - provider doesn't exist.");
                    continue;
                }

                boolean providerTaken = false;
                for (int i = 0; i < appointmentCount; i++) {
                    if (appointments[i].getProvider().equals(provider) &&
                            appointments[i].getTimeslot().equals(selectedTimeslot) &&
                            appointments[i].getDate().equals(date)) {
                        System.out.println(provider.toString() + " is not available at slot " + timeSlot + ".");
                        // [PATEL, BRIDGEWATER, Somerset 08807, FAMILY] is not available at slot 1.
                        providerTaken = true;
                        break;
                    }
                }

                if (providerTaken) {
                    continue; // Skip if provider is already booked
                }

                // Finalize the appointment (provider confirmed
                newAppointment = new Appointment(date, selectedTimeslot, patient, provider);

                // Add the appointment if there's no error
                if (appointmentCount < MAX_APPOINTMENTS) {
                    appointments[appointmentCount++] = newAppointment;
                    System.out.println(newAppointment.toString() + " booked.");
                    // 1/17/2025 4:15 PM Duke Ellington 1/20/2003 [KAUR, PRINCETON, Mercer 08542, ALLERGIST] booked.
                } else {
                    System.out.println("Appointment list is full."); //Edge case
                }

            } else if (command.equals("C")) {
                // Parsing the input data
                String aptDate = tokenizer.nextToken();      // e.g., "9/31/2024"
                String timeSlot = tokenizer.nextToken();     // e.g., "1"
                String fName = tokenizer.nextToken();        // e.g., "John"
                String lName = tokenizer.nextToken();        // e.g., "Doe"
                String dob = tokenizer.nextToken();          // e.g., "12/13/1989"

                // Parse the date and create a Date object for the appointment
                String[] splitDate = aptDate.split("/");
                int aMonth = Integer.parseInt(splitDate[0]);
                int aDay = Integer.parseInt(splitDate[1]);
                int aYear = Integer.parseInt(splitDate[2]);
                Date date = new Date(aMonth, aDay, aYear);

                // Convert the timeslot string to a Timeslot object
                Timeslot selectedTimeslot = Timeslot.getTimeslotByNumber(Integer.parseInt(timeSlot));

                // Parse the date of birth and create a Date object for the patient's DOB
                String[] splitDob = dob.split("/");
                int dobMonth = Integer.parseInt(splitDob[0]);
                int dobDay = Integer.parseInt(splitDob[1]);
                int dobYear = Integer.parseInt(splitDob[2]);
                Date dobDate = new Date(dobMonth, dobDay, dobYear);

                // Create a Profile object for the patient
                Profile patient = new Profile(fName, lName, dobDate);

                String formattedTimeslot = selectedTimeslot.toString(); // Assuming toString() method returns in HH:MM AM/PM format

                // Search for the matching appointment
                boolean appointmentFound = false;
                for (int i = 0; i < appointmentCount; i++) {
                    Appointment currentAppointment = appointments[i];

                    // Check if the current appointment matches the cancel request (same date, timeslot, and patient)
                    if (currentAppointment.getDate().equals(date) &&
                            currentAppointment.getTimeslot().equals(selectedTimeslot) &&
                            currentAppointment.getPatient().equals(patient)) {

                        System.out.println(aptDate + " " + formattedTimeslot + " " + fName + " " + lName + " " + dob + " has been canceled.");

                        // Shift the appointments array to remove the canceled appointment
                        for (int j = i; j < appointmentCount - 1; j++) {
                            appointments[j] = appointments[j + 1];
                        }
                        appointments[--appointmentCount] = null;  // Decrease appointment count and set the last slot to null
                        appointmentFound = true;
                        break;
                    }
                }

                // If no matching appointment was found
                if (!appointmentFound) {
                    System.out.println(aptDate + " " + formattedTimeslot + " " + fName + " " + lName + " " + dob + " does not exist.");
                }
            } else if (command.equals("R")) {
                // Parsing the input data
                String aptDate = tokenizer.nextToken();      // e.g., "9/30/2024"
                String oldTimeSlot = tokenizer.nextToken();  // e.g., "1"
                String fName = tokenizer.nextToken();        // e.g., "John"
                String lName = tokenizer.nextToken();        // e.g., "Doe"
                String dob = tokenizer.nextToken();          // e.g., "12/13/1989"
                String newTimeSlot = tokenizer.nextToken();  // New timeslot number (e.g., "2")

                // new Date
                String[] splitDate = aptDate.split("/");
                int aMonth = Integer.parseInt(splitDate[0]);
                int aDay = Integer.parseInt(splitDate[1]);
                int aYear = Integer.parseInt(splitDate[2]);
                Date date = new Date(aMonth, aDay, aYear);

                Timeslot originalTimeslot = Timeslot.getTimeslotByNumber(Integer.parseInt(oldTimeSlot));
                Timeslot newSelectedTimeslot = Timeslot.getTimeslotByNumber(Integer.parseInt(newTimeSlot));

                // dob
                String[] splitDob = dob.split("/");
                int dobMonth = Integer.parseInt(splitDob[0]);
                int dobDay = Integer.parseInt(splitDob[1]);
                int dobYear = Integer.parseInt(splitDob[2]);
                Date dobDate = new Date(dobMonth, dobDay, dobYear);

                Profile patient = new Profile(fName, lName, dobDate);

                // Step 1: Search for the existing appointment
                Appointment targetAppointment = new Appointment(date, originalTimeslot, patient, null); // Null provider for comparison
                boolean appointmentFound = false;
                Appointment currentAppointment = null;

                for (int i = 0; i < appointmentCount; i++) {
                    if (appointments[i].compareTo(targetAppointment) == 0) {
                        appointmentFound = true;
                        currentAppointment = appointments[i];  // store data (order of checks)
                        break;
                    }
                }

                if (!appointmentFound) {
                    // If no matching appointment was found, print the error and skip the timeslot check
                    System.out.println(aptDate + " " + originalTimeslot + " " + fName + " " + lName + " " + dob + " does not exist.");
                    continue;
                }

                // Step 2: Check if the new timeslot is valid
                if (newSelectedTimeslot == null) {
                    System.out.println(newTimeSlot + " is not a valid time slot.");
                    continue;
                }

                // Step 3: Check if the new timeslot is available for the provider
                Provider provider = currentAppointment.getProvider();
                boolean isAvailable = true; // Assume available until checked

                // Iterate through appointments to see if the provider is booked at the new timeslot on the same day
                for (int j = 0; j < appointmentCount; j++) {
                    if (appointments[j].getDate().equals(date) &&
                            appointments[j].getTimeslot().equals(newSelectedTimeslot) &&
                            appointments[j].getProvider().equals(provider)) {
                        isAvailable = false;
                        break;
                    }
                }

                if (isAvailable) {
                    currentAppointment.setTimeslot(newSelectedTimeslot);
                    System.out.println("Rescheduled to " + currentAppointment.toString());
                    //Rescheduled to 12/11/2024 10:45 AM Jane Doe 5/1/1996 [TAYLOR, PISCATAWAY, Middlesex 08854, PEDIATRICIAN]
                } else {
                    System.out.println(provider.toString() + " is not available at slot " + newTimeSlot + ".");
                }
            } else if (command.equals("PA")) {

            } else if (command.equals("PP")) {

            } else if (command.equals("PL")) {

            } else if (command.equals("PS")) {

            } else if (command.equals("Q")) {
                System.out.print("Scheduler is terminated.");
                running = false;
            } else {
                System.out.println("Invalid command!");
            }
        }
    }
}