package clinic;

public class Profile implements Comparable<Profile> {
    private String fname;  // First name of the patient
    private String lname;  // Last name of the patient
    private Date dob;      // Date of birth of the patient

    // Constructor
    public Profile(String fname, String lname, Date dob) {
        this.fname = fname;
        this.lname = lname;
        this.dob = dob;
    }

    // Getters
    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public Date getDob() {
        return dob;
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
        Profile other = (Profile) obj;
        return fname.equals(other.fname) && lname.equals(other.lname) && dob.equals(other.dob);
    }

    // Override toString() method
    @Override
    public String toString() {
        return fname + " " + lname + " " + dob.toString();  // Format: FirstName LastName DOB
    }

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
