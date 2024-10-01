package clinic;

/**
 * The MedicalRecord class represents a collection of patients in a medical record system.
 * It maintains a dynamically resizable array to store Patient objects and allows adding, retrieving,
 * and printing patients.
 *
 * The class uses an initial capacity of 4 and grows the array as needed when more patients are added.
 *
 * @author Jonas Lazebnik, Arjun Anand
 */
public class MedicalRecord {
    private Patient[] patients;  // Array to hold Patient objects
    private int size;  // Number of Patient objects in the array
    private static final int INITIAL_CAPACITY = 4;  // Initial capacity of the array

    /**
     * Constructor to initialize the MedicalRecord with an initial capacity.
     */
    public MedicalRecord() {
        this.patients = new Patient[INITIAL_CAPACITY];
        this.size = 0;
    }

    /**
     * Adds a new patient to the medical record.
     * If the array is full, the method grows the array by increasing its capacity by 4.
     *
     * @param patient the patient to be added to the medical record.
     */
    public void add(Patient patient) {
        if (size >= patients.length) {
            grow();  // Grow the array if it's full
        }
        patients[size++] = patient;  // Add the patient and increment the size
    }

    /**
     * Grows the internal patients array by increasing its capacity by 4.
     * This method is called when the array reaches its current capacity.
     */
    private void grow() {
        Patient[] newPatients = new Patient[patients.length + 4];  // Increase capacity by 4
        System.arraycopy(patients, 0, newPatients, 0, patients.length);  // Copy existing patients to new array
        patients = newPatients;
    }

    /**
     * Prints all the patients currently stored in the medical record.
     * This method is mainly used for testing purposes.
     */
    public void printPatients() {
        for (int i = 0; i < size; i++) {
            System.out.println(patients[i]);
        }
    }

    /**
     * Returns the current number of patients in the medical record.
     *
     * @return the number of patients in the medical record.
     */
    public int getSize() {
        return size;
    }

    /**
     * Retrieves the array of Patient objects currently in the medical record.
     * This method is primarily for testing purposes.
     *
     * @return the array of patients.
     */
    public Patient[] getPatients() {
        return patients;
    }

    /**
     * Testbed main method to test the functionality of the MedicalRecord class.
     *
     * @param args the command-line arguments.
     */
    public static void main(String[] args) {
        // Create some sample patients
        Profile profile1 = new Profile("John", "Doe", new Date(12, 13, 1989));
        Profile profile2 = new Profile("Jane", "Doe", new Date(6, 25, 1992));
        Profile profile3 = new Profile("Alice", "Smith", new Date(8, 17, 1990));

        Patient patient1 = new Patient(profile1);
        Patient patient2 = new Patient(profile2);
        Patient patient3 = new Patient(profile3);

        // Create the MedicalRecord (Bag)
        MedicalRecord record = new MedicalRecord();

        // Add patients to the medical record
        record.add(patient1);
        record.add(patient2);
        record.add(patient3);

        // Print the patients
        System.out.println("Patients in the medical record:");
        record.printPatients();
        System.out.println("Number of patients: " + record.getSize() + " (Expected: 3)");

        // Add more patients to test array growth
        Profile profile4 = new Profile("Bob", "Jones", new Date(1, 15, 1985));
        Patient patient4 = new Patient(profile4);
        record.add(patient4);

        // Print all patients again after adding the fourth patient
        System.out.println("\nAfter adding another patient:");
        record.printPatients();
        System.out.println("Number of patients: " + record.getSize() + " (Expected: 4)");
    }
}
