package clinic;

/**
 * The Imaging class extends the Appointment class to hold additional data for imaging appointments.
 * This includes the radiology room and the type of imaging service (e.g., XRAY, CATSCAN, ULTRASOUND).
 */
public class Imaging extends Appointment {
    private Radiology room;

    /**
     * Constructs an Imaging appointment with the specified date, timeslot, patient, provider, and room.
     *
     * @param date     the date of the appointment.
     * @param timeslot the timeslot for the appointment.
     * @param patient  the patient for the appointment.
     * @param provider the technician handling the imaging.
     * @param room     the radiology room used for the imaging service.
     */
    public Imaging(Date date, Timeslot timeslot, Person patient, Technician provider, Radiology room) {
        super(date, timeslot, patient, provider);
        this.room = room;
    }

    /**
     * Gets the radiology room associated with this imaging appointment.
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

    @Override
    public String toString() {
        return String.format("%s %s [%s, %s]",
                super.toString(),
                room.toString(),
                ((Technician) provider).getProfile().getFullName(),
                room.getRoomType());
    }
}
