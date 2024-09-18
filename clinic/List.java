package clinic;

public class List {
    private Appointment[] appointments;  // Array to hold Appointment objects
    private int size;  // Number of appointments in the array
    private static final int INITIAL_CAPACITY = 4;  // Initial capacity of the array
    private static final int NOT_FOUND = -1;  // Constant for when an appointment is not found

    // Constructor to initialize the list
    public List() {
        this.appointments = new Appointment[INITIAL_CAPACITY];
        this.size = 0;
    }

    // Helper method to find the index of an appointment
    private int find(Appointment appointment) {
        for (int i = 0; i < size; i++) {
            if (appointments[i].equals(appointment)) {
                return i;
            }
        }
        return NOT_FOUND;  // If appointment is not found
    }

    // Helper method to grow the array by 4 when it's full
    private void grow() {
        Appointment[] newAppointments = new Appointment[appointments.length + 4];
        System.arraycopy(appointments, 0, newAppointments, 0, appointments.length);
        appointments = newAppointments;
    }

    // Check if the appointment exists in the list
    public boolean contains(Appointment appointment) {
        return find(appointment) != NOT_FOUND;
    }

    // Add a new appointment to the end of the array
    public void add(Appointment appointment) {
        if (size >= appointments.length) {
            grow();  // Grow the array if it's full
        }
        appointments[size++] = appointment;  // Add the appointment and increase size
    }

    // Remove an appointment from the array (replaces it with the last one)
    public void remove(Appointment appointment) {
        int index = find(appointment);
        if (index != NOT_FOUND) {
            appointments[index] = appointments[size - 1];  // Replace with the last appointment
            appointments[size - 1] = null;  // Null out the last spot
            size--;
        }
    }

    // Print appointments ordered by patient profile (last name, first name, DOB) and date/timeslot
    public void printByPatient() {
        sortByPatient();
        for (int i = 0; i < size; i++) {
            System.out.println(appointments[i]);
        }
    }

    // Print appointments ordered by county, then date/timeslot
    public void printByLocation() {
        sortByLocation();
        for (int i = 0; i < size; i++) {
            System.out.println(appointments[i]);
        }
    }

    // Print appointments ordered by date/timeslot, then provider name
    public void printByAppointment() {
        sortByAppointment();
        for (int i = 0; i < size; i++) {
            System.out.println(appointments[i]);
        }
    }

    // In-place sorting by patient profile (last name, first name, DOB), then date/timeslot
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

    // In-place sorting by county, then date/timeslot
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

    // In-place sorting by date/timeslot, then provider's last name
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

    // Helper method to swap two appointments in the array
    private void swap(int i, int j) {
        Appointment temp = appointments[i];
        appointments[i] = appointments[j];
        appointments[j] = temp;
    }
}
