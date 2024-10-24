package clinic;

public class Profile implements Comparable<Profile> {
    private String fname;  // First name of the provider
    private String lname;  // Last name of the provider
    private Date dob;      // Date of birth of the provider

    public Profile(String fname, String lname, Date dob) {
        this.fname = fname;
        this.lname = lname;
        this.dob = dob;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public Date getDob() {
        return dob;
    }

    // Method to get the full name
    public String getFullName() {
        return fname + " " + lname;
    }

    // Compare profiles based on last name, first name, then DOB
    @Override
    public int compareTo(Profile other) {
        int lnameComparison = this.lname.compareTo(other.lname);
        if (lnameComparison != 0) {
            return lnameComparison;  // Sort by last name
        }

        int fnameComparison = this.fname.compareTo(other.fname);
        if (fnameComparison != 0) {
            return fnameComparison;  // Sort by first name if last names are equal
        }

        return this.dob.compareTo(other.dob);  // Sort by DOB if first and last names are the same
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", fname, lname, dob.toString());
    }
}
