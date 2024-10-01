package clinic;

/**
 * The Visit class represents a single visit in a linked list of visits.
 * Each visit contains a reference to an appointment and a reference to the next visit in the linked list.
 * This class is used to manage the visits for a patient in a chronological order.
 *
 * Modifications to the appointment object are not allowed after initialization, but the next visit can be set.
 *
 * The toString() method provides a string representation of the visit by converting the appointment to a string.
 *
 * @author studentName
 */
public class Visit {
    private Appointment appointment; // A reference to an appointment object
    private Visit next; // A reference to the next visit in the linked list

    /**
     * Constructs a Visit object with the specified appointment.
     * Initially, the next visit in the linked list is set to null.
     *
     * @param appointment the appointment associated with this visit.
     */
    public Visit(Appointment appointment) {
        this.appointment = appointment;
        this.next = null; // Next is initially null
    }

    /**
     * Gets the appointment associated with this visit.
     *
     * @return the appointment associated with this visit.
     */
    public Appointment getAppointment() {
        return appointment;
    }

    /**
     * Gets the next visit in the linked list.
     *
     * @return the next visit in the linked list, or null if there is no next visit.
     */
    public Visit getNext() {
        return next;
    }

    /**
     * Sets the next visit in the linked list.
     * This method is used to link another visit after this one.
     *
     * @param next the next visit to link in the list.
     */
    public void setNext(Visit next) {
        this.next = next;
    }

    /**
     * Returns a string representation of the visit by converting the associated appointment to a string.
     *
     * @return a string representation of the visit.
     */
    @Override
    public String toString() {
        return appointment.toString(); // Convert appointment to string for easy representation
    }
}
