package clinic;

public class Visit {
    private Appointment appointment; // A reference to an appointment object
    private Visit next; // A reference to the next visit in the linked list

    // Constructor
    public Visit(Appointment appointment) {
        this.appointment = appointment;
        this.next = null; // Next is initially null
    }

    // Getters for the instance variables (no setters, as modifications to instance variables are not allowed)
    public Appointment getAppointment() {
        return appointment;
    }

    public Visit getNext() {
        return next;
    }

    // Method to set the next visit in the linked list (allowed since we are not changing the instance variable itself)
    public void setNext(Visit next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return appointment.toString(); // Convert appointment to string for easy representation
    }
}
