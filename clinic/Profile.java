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

    // Override compareTo() method
    @Override
    public int compareTo(Profile other) {
        // Compare by last name
        int lnameComparison = this.lname.compareTo(other.lname);
        if (lnameComparison != 0) {
            return lnameComparison;
        }

        // If last names are the same, compare by first name
        int fnameComparison = this.fname.compareTo(other.fname);
        if (fnameComparison != 0) {
            return fnameComparison;
        }

        // If both first and last names are the same, compare by date of birth
        return this.dob.compareTo(other.dob);
    }

    // Testbed main() method for testing the compareTo() method
    public static void main(String[] args) {
        // Create Date objects for testing
        Date dob1 = new Date(12, 13, 1989);
        Date dob2 = new Date(6, 15, 1990);
        Date dob3 = new Date(12, 13, 1989);  // Same DOB as dob1

        // Create Profile objects for testing
        Profile p1 = new Profile("John", "Doe", dob1);
        Profile p2 = new Profile("John", "Doe", dob2);   // Different DOB
        Profile p3 = new Profile("Alice", "Smith", dob1); // Different name
        Profile p4 = new Profile("John", "Doe", dob3);   // Same as p1
        Profile p5 = new Profile("Jane", "Doe", dob1);   // Different first name

        // Test equals method
        System.out.println("Test equals():");
        System.out.println("p1.equals(p4): " + p1.equals(p4) + " (Expected: true)");  // Same profile
        System.out.println("p1.equals(p2): " + p1.equals(p2) + " (Expected: false)"); // Different DOB

        // Test compareTo method
        System.out.println("\nTest compareTo():");
        System.out.println("p1.compareTo(p4): " + p1.compareTo(p4) + " (Expected: 0)"); // Same profiles
        System.out.println("p1.compareTo(p2): " + p1.compareTo(p2) + " (Expected: < 0)"); // Same name, earlier DOB
        System.out.println("p1.compareTo(p5): " + p1.compareTo(p5) + " (Expected: > 0)"); // Same last name, p1's first name comes later
        System.out.println("p3.compareTo(p1): " + p3.compareTo(p1) + " (Expected: > 0)"); // p3's last name comes later
    }
}
