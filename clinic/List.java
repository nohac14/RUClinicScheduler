package clinic;

public class List {
    private Appointment[] appointments;
    private int size;

    private static final int NOT_FOUND = -1;

    public List() {
        // Initialize with capacity of 4
    }

    private int find(Appointment appointment) {
        // Implement search logic
    }

    private void grow() {
        // Increase capacity by 4
    }

    public boolean contains(Appointment appointment) {
        // Check if appointment exists
    }

    public void add(Appointment appointment) {
        // Add appointment to list
    }

    public void remove(Appointment appointment) {
        // Remove appointment from list
    }

    public void printByPatient() {
        // Print ordered by patient
    }

    public void printByLocation() {
        // Print ordered by location
    }

    public void printByAppointment() {
        // Print ordered by appointment details
    }
}

