package clinic;

public class MedicalRecord {
    private Patient[] patients;  // Array to hold Patient objects
    private int size;  // Number of Patient objects in the array
    private static final int INITIAL_CAPACITY = 4;  // Initial capacity of the array

    // Constructor to initialize the MedicalRecord
    public MedicalRecord() {
        this.patients = new Patient[INITIAL_CAPACITY];
        this.size = 0;
    }

    // Method to add a patient to the medical record (bag)
    public void add(Patient patient) {
        if (size >= patients.length) {
            grow();  // Grow the array if it's full
        }
        patients[size++] = patient;  // Add the patient and increment the size
    }

    // Helper method to grow the array by increasing its capacity by 4
    private void grow() {
        Patient[] newPatients = new Patient[patients.length + 4];  // Increase capacity by 4
        System.arraycopy(patients, 0, newPatients, 0, patients.length);  // Copy existing patients to new array
        patients = newPatients;
    }

    // Method to print all the patients in the medical record (for testing purposes)
    public void printPatients() {
        for (int i = 0; i < size; i++) {
            System.out.println(patients[i]);
        }
    }

    // Method to retrieve the size of the medical record (number of patients)
    public int getSize() {
        return size;
    }

    // Getter for patients array (for potential testing)
    public Patient[] getPatients() {
        return patients;
    }

    // Testbed main() method to test the MedicalRecord class
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
