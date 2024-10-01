package clinic;

/**
 * Represents an appointment in the clinic.
 * Contains details such as the date, timeslot, patient, and provider.
 * Implements Comparable interface to allow comparison between appointments.
 *
 * @author Jonas Lazebnik, Arjun Anand
 */
public class Appointment implements Comparable<Appointment> {
    private Date date;
    private Timeslot timeslot;
    private Profile patient;
    private Provider provider;

    /**
     * Constructs an Appointment with the specified date, timeslot, patient, and provider.
     *
     * @param date     the date of the appointment.
     * @param timeslot the timeslot for the appointment.
     * @param patient  the patient for the appointment.
     * @param provider the provider for the appointment.
     */
    public Appointment(Date date, Timeslot timeslot, Profile patient, Provider provider) {
        this.date = date;
        this.timeslot = timeslot;
        this.patient = patient;
        this.provider = provider;
    }

    /**
     * Gets the date of the appointment.
     *
     * @return the date of the appointment.
     */
    public Date getDate() {
        return date;
    }

    /**
     * Gets the timeslot of the appointment.
     *
     * @return the timeslot of the appointment.
     */
    public Timeslot getTimeslot() {
        return timeslot;
    }

    /**
     * Gets the patient of the appointment.
     *
     * @return the patient of the appointment.
     */
    public Profile getPatient() {
        return patient;
    }

    /**
     * Gets the provider of the appointment.
     *
     * @return the provider of the appointment.
     */
    public Provider getProvider() {
        return provider;
    }

    /**
     * Sets the timeslot for the appointment.
     *
     * @param newTimeslot the new timeslot for the appointment.
     */
    public void setTimeslot(Timeslot newTimeslot) {
        this.timeslot = newTimeslot;
    }

    /**
     * Compares this appointment to another appointment for equality based on date, timeslot, and patient.
     *
     * @param obj the object to compare with this appointment.
     * @return true if the date, timeslot, and patient are the same; false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Appointment other = (Appointment) obj;

        return this.date.equals(other.date) &&
                this.timeslot.equals(other.timeslot) &&
                this.patient.equals(other.patient);
    }

    /**
     * Returns a string representation of the appointment details.
     * Format: MM/DD/YYYY HH:MM AM/PM FirstName LastName DOB [PROVIDER, LOCATION, COUNTY ZIP, SPECIALTY]
     *
     * @return a formatted string representing the appointment details.
     */
    @Override
    public String toString() {
        return String.format("%s %s %s [%s, %s, %s %s, %s]",
                date.toString(),
                timeslot.toString(),
                patient.toString(),
                provider.name(),
                provider.getLocation().getCity(),
                provider.getLocation().getCounty(),
                provider.getLocation().getZip(),
                provider.getSpecialty().name()
        );
    }

    /**
     * Compares this appointment with another appointment based on patient, date, and timeslot.
     *
     * @param other the other appointment to compare to.
     * @return a negative integer, zero, or a positive integer as this appointment is less than, equal to, or greater than the specified appointment.
     */
    @Override
    public int compareTo(Appointment other) {
        int patientComparison = this.patient.compareTo(other.patient);
        if (patientComparison != 0) {
            return patientComparison;
        }

        int dateComparison = this.date.compareTo(other.date);
        if (dateComparison != 0) {
            return dateComparison;
        }

        return this.timeslot.compareTo(other.timeslot);
    }

    /**
     * Compares this appointment with another appointment based on the provider's county, date, and timeslot.
     *
     * @param other the other appointment to compare to.
     * @return a negative integer, zero, or a positive integer as this appointment's county is less than, equal to, or greater than the specified appointment's county.
     */
    public int compareByCounty(Appointment other) {
        String thisCounty = this.provider.getLocation().getCounty();
        String otherCounty = other.provider.getLocation().getCounty();

        int countyComparison = thisCounty.compareTo(otherCounty);
        if (countyComparison != 0) {
            return countyComparison;
        }

        int dateComparison = this.date.compareTo(other.date);
        if (dateComparison != 0) {
            return dateComparison;
        }

        return this.timeslot.compareTo(other.timeslot);
    }
}
