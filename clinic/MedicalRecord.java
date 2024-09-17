package clinic;

public class MedicalRecord {
    private Patient[] patients; // Array to hold patient objects
    private int size; // Number of patient objects currently in the array

    private static final int INITIAL_CAPACITY = 4; // Initial capacity of the array

    // Constructor
    public MedicalRecord() {
        this.patients = new Patient[INITIAL_CAPACITY]; // Initialize array with initial capacity
        this.size = 0; // Initially, there are no patients
    }

    // Method to add a patient to the array
    public void add(Patient patient) {
        if (size >= patients.length) {
            grow(); // Increase capacity if the array is full
        }
        patients[size++] = patient; // Add patient and increment size
    }

    // Helper method to increase the array capacity
    private void grow() {
        Patient[] newPatients = new Patient[patients.length + INITIAL_CAPACITY]; // New array with increased capacity
        System.arraycopy(patients, 0, newPatients, 0, patients.length); // Copy old elements to the new array
        patients = newPatients; // Assign the new array back to the patients variable
    }

    // Additional method to print the list of patients (for demonstration purposes)
    public void printPatients() {
        for (int i = 0; i < size; i++) {
            System.out.println(patients[i]); // Print each patient in the array
        }
    }
}
