package clinic;

public class Profile implements Comparable<Profile> {
    private String fname;  // First name of the patient
    private String lname;  // Last name of the patient
    private Date dob;      // Date of birth of the patient

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

    // New method to get the full name
    public String getFullName() {
        return fname + " " + lname;
    }

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

    @Override
    public String toString() {
        return fname + " " + lname + " " + dob.toString();
    }

    @Override
    public int compareTo(Profile other) {
        int lnameComparison = this.lname.compareTo(other.lname);
        if (lnameComparison != 0) {
            return Integer.compare(lnameComparison, 0);
        }

        int fnameComparison = this.fname.compareTo(other.fname);
        if (fnameComparison != 0) {
            return Integer.compare(fnameComparison, 0);
        }

        return Integer.compare(this.dob.compareTo(other.dob), 0);
    }

    public static void main(String[] args) {
        Profile p1 = new Profile("John", "Doe", new Date(12, 13, 1989));
        Profile p2 = new Profile("Alice", "Smith", new Date(8, 17, 1990));
        Profile p3 = new Profile("Jane", "Doe", new Date(6, 25, 1992));
        Profile p4 = new Profile("John", "Doe", new Date(12, 13, 1989));

        System.out.println("p1.compareTo(p2): " + p1.compareTo(p2) + " (Expected: -1)");
        System.out.println("p2.compareTo(p1): " + p2.compareTo(p1) + " (Expected: 1)");
        System.out.println("p3.compareTo(p1): " + p3.compareTo(p1) + " (Expected: -1)");
        System.out.println("p1.compareTo(p3): " + p1.compareTo(p3) + " (Expected: 1)");
        System.out.println("p1.compareTo(p4): " + p1.compareTo(p4) + " (Expected: 0)");
    }
}