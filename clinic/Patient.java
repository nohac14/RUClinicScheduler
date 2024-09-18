package clinic;

public class Patient implements Comparable<Patient> {
    private Profile profile;  // Profile of the patient (first name, last name, date of birth)
    private Visit visits;     // Head of the linked list of visits (completed appointments)

    // Constructor
    public Patient(Profile profile) {
        this.profile = profile;
        this.visits = null;  // Initially, there are no completed visits
    }

    // Override equals() method
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Patient other = (Patient) obj;
        return profile.equals(other.profile);  // Compare profiles for equality
    }

    // Override toString() method
    @Override
    public String toString() {
        return profile.toString();  // Display the patient's profile details
    }

    // Override compareTo() method to compare two patients by profile
    @Override
    public int compareTo(Patient other) {
        return this.profile.compareTo(other.profile);  // Delegate to Profile's compareTo method
    }

    // Method to calculate the total charge by traversing the linked list of visits
    public int charge() {
        int totalCharge = 0;
        Visit currentVisit = visits;  // Start from the head of the linked list

        // Traverse the linked list of visits
        while (currentVisit != null) {
            totalCharge += currentVisit.getAppointment().getProvider().getSpecialty().getCharge();
            currentVisit = currentVisit.getNext();  // Move to the next visit in the list
        }

        return totalCharge;
    }

    // Method to add a visit to the linked list of visits
    public void addVisit(Appointment appointment) {
        Visit newVisit = new Visit(appointment);
        newVisit.setNext(visits);  // Insert the new visit at the beginning of the list
        visits = newVisit;         // Update the head to the new visit
    }

    // Getters (if needed for other parts of the code)
    public Profile getProfile() {
        return profile;
    }

    public Visit getVisits() {
        return visits;
    }

    // Testbed main() method for testing the charge() method
    public static void main(String[] args) {
        // Create some sample data
        Profile profile = new Profile("John", "Doe", new Date(12, 13, 1989));
        Patient patient = new Patient(profile);

        // Create appointments with different providers
        Appointment appointment1 = new Appointment(new Date(10, 30, 2024), Timeslot.SLOT2, profile, Provider.PATEL);
        Appointment appointment2 = new Appointment(new Date(11, 5, 2024), Timeslot.SLOT4, profile, Provider.LIM);
        Appointment appointment3 = new Appointment(new Date(12, 1, 2024), Timeslot.SLOT6, profile, Provider.KAUR);

        // Add visits (appointments) to the patient
        patient.addVisit(appointment1);
        patient.addVisit(appointment2);
        patient.addVisit(appointment3);

        // Test charge calculation
        System.out.println("Total charge for the patient: $" + patient.charge() + " (Expected: $900)");
    }
}
