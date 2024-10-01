package clinic;

/**
 * The Profile class represents the profile of a patient, which includes the patient's first name, last name, and date of birth.
 * It implements the Comparable interface, allowing profiles to be compared based on last name, first name, and date of birth.
 *
 * @author Jonas Lazebnik, Arjun Anand
 */
public class Profile implements Comparable<Profile> {
    private String fname;  // First name of the patient
    private String lname;  // Last name of the patient
    private Date dob;      // Date of birth of the patient

    /**
     * Constructs a Profile object with the specified first name, last name, and date of birth.
     *
     * @param fname the first name of the patient.
     * @param lname the last name of the patient.
     * @param dob   the date of birth of the patient.
     */
    public Profile(String fname, String lname, Date dob) {
        this.fname = fname;
        this.lname = lname;
        this.dob = dob;
    }

    /**
     * Gets the first name of the patient.
     *
     * @return the first name of the patient.
     */
    public String getFname() {
        return fname;
    }

    /**
     * Gets the last name of the patient.
     *
     * @return the last name of the patient.
     */
    public String getLname() {
        return lname;
    }

    /**
     * Gets the date of birth of the patient.
     *
     * @return the date of birth of the patient.
     */
    public Date getDob() {
        return dob;
    }

    /**
     * Compares this Profile object with another object for equality based on the first name, last name, and date of birth.
     *
     * @param obj the object to compare with this profile.
     * @return true if the first name, last name, and date of birth are equal; false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Profile other = (Profile) obj;
        return fname.equals(other.fname) && lname.equals(other.lname) && dob.equals(other.dob);
    }

    /**
     * Returns a string representation of the Profile in the format "FirstName LastName DOB".
     *
     * @return a formatted string representing the profile.
     */
    @Override
    public String toString() {
        return fname + " " + lname + " " + dob.toString();  // Format: FirstName LastName DOB
    }

    /**
     * Compares this Profile object with another Profile object for ordering.
     * Comparison is done based on last name, first name, and date of birth, in that order.
     *
     * @param other the other profile to compare to.
     * @return a negative integer, zero, or a positive integer as this profile is less than, equal to, or greater than the specified profile.
     */
    @Override
    public int compareTo(Profile other) {
        // Step 1: Compare last names
        int lnameComparison = this.lname.compareTo(other.lname);
        if (lnameComparison != 0) {
            return Integer.compare(lnameComparison, 0);  // Normalize result to -1, 0, or 1
        }

        // Step 2: Compare first names if last names are the same
        int fnameComparison = this.fname.compareTo(other.fname);
        if (fnameComparison != 0) {
            return Integer.compare(fnameComparison, 0);  // Normalize result to -1, 0, or 1
        }

        // Step 3: Compare dates if both first and last names are the same
        return Integer.compare(this.dob.compareTo(other.dob), 0);  // Compare dates directly
    }

    /**
     * Testbed main method to test the functionality of the Profile class, specifically the compareTo method.
     *
     * @param args the command-line arguments.
     */
    public static void main(String[] args) {
        // Create profiles for test cases
        Profile p1 = new Profile("John", "Doe", new Date(12, 13, 1989));   // Profile 1
        Profile p2 = new Profile("Alice", "Smith", new Date(8, 17, 1990));  // Profile 2
        Profile p3 = new Profile("Jane", "Doe", new Date(6, 25, 1992));     // Profile 3
        Profile p4 = new Profile("John", "Doe", new Date(12, 13, 1989));    // Same as Profile 1

        // Test cases for Profile class compareTo() method
        System.out.println("p1.compareTo(p2): " + p1.compareTo(p2) + " (Expected: -1)");  // Case 1
        System.out.println("p2.compareTo(p1): " + p2.compareTo(p1) + " (Expected: 1)");   // Case 2
        System.out.println("p3.compareTo(p1): " + p3.compareTo(p1) + " (Expected: -1)");  // Case 3
        System.out.println("p1.compareTo(p3): " + p1.compareTo(p3) + " (Expected: 1)");   // Case 4
        System.out.println("p3.compareTo(p1): " + p3.compareTo(p1) + " (Expected: -1)");  // Case 5
        System.out.println("p1.compareTo(p3): " + p1.compareTo(p3) + " (Expected: 1)");   // Case 6
        System.out.println("p1.compareTo(p4): " + p1.compareTo(p4) + " (Expected: 0)");   // Case 7
    }
}
