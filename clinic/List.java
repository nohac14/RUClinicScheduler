package clinic;

/**
 * A list that holds and manages Appointment objects. The list supports dynamic resizing,
 * searching, sorting, and basic operations like adding and removing appointments.
 *
 * @author Jonas Lazebnik, Arjun Anand
 */
public class List {
    private Appointment[] appointments;  // Array to hold Appointment objects
    private int size;  // Number of appointments in the array
    private static final int INITIAL_CAPACITY = 4;  // Initial capacity of the array
    private static final int NOT_FOUND = -1;  // Constant for when an appointment is not found

    /**
     * Constructor to initialize the list with an initial capacity of appointments.
     */
    public List() {
        this.appointments = new Appointment[INITIAL_CAPACITY];
        this.size = 0;
    }

    /**
     * Finds the index of a given appointment in the list.
     *
     * @param appointment the appointment to search for.
     * @return the index of the appointment if found, or -1 if not found.
     */
    private int find(Appointment appointment) {
        for (int i = 0; i < size; i++) {
            if (appointments[i].equals(appointment)) {
                return i;
            }
        }
        return NOT_FOUND;  // If appointment is not found
    }

    /**
     * Expands the size of the appointments array by 4 when it's full.
     */
    private void grow() {
        Appointment[] newAppointments = new Appointment[appointments.length + 4];
        System.arraycopy(appointments, 0, newAppointments, 0, appointments.length);
        appointments = newAppointments;
    }

    /**
     * Checks if a given appointment exists in the list.
     *
     * @param appointment the appointment to check.
     * @return true if the appointment exists in the list, false otherwise.
     */
    public boolean contains(Appointment appointment) {
        return find(appointment) != NOT_FOUND;
    }

    /**
     * Adds a new appointment to the list. Expands the list if the array is full.
     *
     * @param appointment the appointment to add.
     */
    public void add(Appointment appointment) {
        if (size >= appointments.length) {
            grow();  // Grow the array if it's full
        }
        appointments[size++] = appointment;  // Add the appointment and increase size
    }

    /**
     * Removes a specified appointment from the list, replacing it with the last appointment
     * in the list to maintain contiguous data.
     *
     * @param appointment the appointment to remove.
     */
    public void remove(Appointment appointment) {
        int index = find(appointment);
        if (index != NOT_FOUND) {
            appointments[index] = appointments[size - 1];  // Replace with the last appointment
            appointments[size - 1] = null;  // Null out the last spot
            size--;
        }
    }

    /**
     * Prints the appointments sorted by patient profile (last name, first name, DOB),
     * followed by date and timeslot.
     */
    public void printByPatient() {
        sortByPatient();
        for (int i = 0; i < size; i++) {
            System.out.println(appointments[i]);
        }
    }

    /**
     * Prints the appointments sorted by the provider's location (county),
     * followed by date and timeslot.
     */
    public void printByLocation() {
        sortByLocation();
        for (int i = 0; i < size; i++) {
            System.out.println(appointments[i]);
        }
    }

    /**
     * Prints the appointments sorted by date and timeslot, followed by the provider's name.
     */
    public void printByAppointment() {
        sortByAppointment();
        for (int i = 0; i < size; i++) {
            System.out.println(appointments[i]);
        }
    }

    /**
     * Sorts the appointments in place by patient profile (last name, first name, DOB),
     * followed by date and timeslot.
     */
    private void sortByPatient() {
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - i - 1; j++) {
                if (appointments[j].getPatient().compareTo(appointments[j + 1].getPatient()) > 0 ||
                        (appointments[j].getPatient().compareTo(appointments[j + 1].getPatient()) == 0 &&
                                appointments[j].getDate().compareTo(appointments[j + 1].getDate()) > 0)) {
                    swap(j, j + 1);  // Swap the appointments
                }
            }
        }
    }

    /**
     * Sorts the appointments in place by provider's county, followed by date and timeslot.
     */
    private void sortByLocation() {
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - i - 1; j++) {
                if (appointments[j].getProvider().getLocation().getCounty().compareTo(
                        appointments[j + 1].getProvider().getLocation().getCounty()) > 0 ||
                        (appointments[j].getProvider().getLocation().getCounty().compareTo(
                                appointments[j + 1].getProvider().getLocation().getCounty()) == 0 &&
                                appointments[j].getDate().compareTo(appointments[j + 1].getDate()) > 0)) {
                    swap(j, j + 1);  // Swap the appointments
                }
            }
        }
    }

    /**
     * Sorts the appointments in place by date and timeslot, followed by the provider's last name.
     */
    private void sortByAppointment() {
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - i - 1; j++) {
                if (appointments[j].getDate().compareTo(appointments[j + 1].getDate()) > 0 ||
                        (appointments[j].getDate().compareTo(appointments[j + 1].getDate()) == 0 &&
                                appointments[j].getProvider().name().compareTo(appointments[j + 1].getProvider().name()) > 0)) {
                    swap(j, j + 1);  // Swap the appointments
                }
            }
        }
    }

    /**
     * Helper method to swap two appointments in the appointments array.
     *
     * @param i index of the first appointment.
     * @param j index of the second appointment.
     */
    private void swap(int i, int j) {
        Appointment temp = appointments[i];
        appointments[i] = appointments[j];
        appointments[j] = temp;
    }
}
