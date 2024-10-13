package clinic;

/**
 * The Imaging class extends the Appointment class to hold additional data specific to imaging appointments.
 * Adds a Radiology room to the existing appointment details.
 *
 * Inherits date, timeslot, patient, and provider from Appointment.
 *
 * @author studentName
 */
public class Imaging extends Appointment {
    private Radiology room;

    /**
     * Constructs an Imaging appointment with the specified date, timeslot, patient, provider, and room.
     *
     * @param date     the date of the appointment.
     * @param timeslot the timeslot for the appointment.
     * @param patient  the patient for the appointment.
     * @param provider the provider for the appointment.
     * @param room     the radiology room for the imaging appointment.
     */
    public Imaging(Date date, Timeslot timeslot, Person patient, Person provider, Radiology room) {
        super(date, timeslot, patient, provider);
        this.room = room;
    }

    /**
     * Gets the radiology room for this imaging appointment.
     *
     * @return the radiology room.
     */
    public Radiology getRoom() {
        return room;
    }

    /**
     * Sets the radiology room for this imaging appointment.
     *
     * @param room the new radiology room.
     */
    public void setRoom(Radiology room) {
        this.room = room;
    }

    /**
     * Returns a string representation of the imaging appointment details.
     * Includes the room in addition to the usual appointment details.
     *
     * @return a formatted string representing the imaging appointment details.
     */
    @Override
    public String toString() {
        return String.format("%s, Room: %s", super.toString(), room.toString());
    }
}