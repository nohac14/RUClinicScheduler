package clinic;

public class Appointment implements Comparable<Appointment> {
    private Date date;
    private Timeslot timeslot;
    private Profile patient;
    private Provider provider;

    // Constructor, getters, and setters

    @Override
    public boolean equals(Object obj) {
        // Logic for equality based on date, timeslot, and patient
    }

    @Override
    public String toString() {
        // Return formatted appointment details
    }

    @Override
    public int compareTo(Appointment other) {
        // Comparison logic based on date, timeslot, provider
    }
}
