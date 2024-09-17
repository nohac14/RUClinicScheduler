package clinic;

public class Profile implements Comparable<Profile> {
    private String fname;
    private String lname;
    private Date dob;

    // Constructor, getters, and setters

    @Override
    public int compareTo(Profile other) {
        // Comparison logic for lname, fname, dob
    }

    @Override
    public String toString() {
        // Return formatted profile details
    }
}

