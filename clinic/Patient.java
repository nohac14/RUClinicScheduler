package clinic;

/**
 * The Patient class represents a patient in the clinic system.
 * It stores the patient's profile and a linked list of completed visits (appointments).
 * The class supports calculating total charges for the patient's visits.
 * It also implements the Comparable interface for comparing patients by profile.
 *
 * @author Jonas Lazebnik, Arjun Anand
 */
public class Patient implements Comparable<Patient> {
    private Profile profile;  // Profile of the patient (first name, last name, date of birth)
    private Visit visits;     // Head of the linked list of visits (completed appointments)

    /**
     * Constructs a new Patient with the specified profile.
     * Initially, the patient has no completed visits.
     *
     * @param profile the profile of the patient.
     */
    public Patient(Profile profile) {
        this.profile = profile;
        this.visits = null;  // Initially, there are no completed visits
    }

    /**
     * Compares this Patient object with another object for equality based on their profiles.
     *
     * @param obj the object to compare with this patient.
     * @return true if the profiles are equal, false otherwise.
     */
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

    /**
     * Returns a string representation of the patient's profile.
     *
     * @return a string representation of the patient's profile.
     */
    @Override
    public String toString() {
        return profile.toString();  // Display the patient's profile details
    }

    /**
     * Compares this Patient object with another Patient object by their profiles.
     *
     * @param other the other patient to compare with.
     * @return a negative integer, zero, or a positive integer as this patient's profile is less than, equal to, or greater than the specified patient's profile.
     */
    @Override
    public int compareTo(Patient other) {
        return this.profile.compareTo(other.profile);  // Delegate to Profile's compareTo method
    }

    /**
     * Calculates the total charge for the patient's completed visits by traversing the linked list of visits.
     *
     * @return the total charge for the patient's visits.
     */
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

    /**
     * Adds a new visit (appointment) to the patient's linked list of visits.
     *
     * @param appointment the appointment to add as a visit.
     */
    public void addVisit(Appointment appointment) {
        Visit newVisit = new Visit(appointment);
        newVisit.setNext(visits);  // Insert the new visit at the beginning of the list
        visits = newVisit;         // Update the head to the new visit
    }

    /**
     * Gets the profile of the patient.
     *
     * @return the patient's profile.
     */
    public Profile getProfile() {
        return profile;
    }

    /**
     * Gets the linked list of visits (appointments) for the patient.
     *
     * @return the head of the linked list of visits.
     */
    public Visit getVisits() {
        return visits;
    }

    /**
     * Testbed main method to test the functionality of the Patient class, specifically the charge calculation.
     *
     * @param args the command-line arguments.
     */
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
