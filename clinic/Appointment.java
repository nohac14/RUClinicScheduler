package clinic;

/**
 * Represents an appointment in the clinic.
 * Contains details such as the date, timeslot, patient, and provider.
 * Implements Comparable interface to allow comparison between appointments.
 *
 * @author Jonas Lazebnik, Arjun Anand
 */
public class Appointment implements Comparable<Appointment> {
    protected Date date;
    protected Timeslot timeslot;
    protected Person patient;
    protected Person provider;

    /**
     * Constructs an Appointment with the specified date, timeslot, patient, and provider.
     *
     * @param date     the date of the appointment.
     * @param timeslot the timeslot for the appointment.
     * @param patient  the patient for the appointment.
     * @param provider the provider for the appointment.
     */
    public Appointment(Date date, Timeslot timeslot, Person patient, Person provider) {
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
    public Person getPatient() {
        return patient;
    }

    /**
     * Gets the provider of the appointment.
     *
     * @return the provider of the appointment.
     */
    public Person getProvider() {
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
        if (provider instanceof Provider) {  // Ensure provider is of type Provider
            Provider prov = (Provider) provider;
            return String.format("%s %s %s [%s, %s, %s %s, %s]",
                    date.toString(),
                    timeslot.toString(),
                    patient.toString(),
                    prov.getName(),
                    prov.getLocation().getCity(),
                    prov.getLocation().getCounty(),
                    prov.getLocation().getZip(),
                    prov.getSpecialty());
        } else {
            return String.format("%s %s %s [Provider information not available]",
                    date.toString(),
                    timeslot.toString(),
                    patient.toString());
        }
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
        if (this.provider instanceof Provider && other.provider instanceof Provider) {
            Provider thisProvider = (Provider) this.provider;
            Provider otherProvider = (Provider) other.provider;

            String thisCounty = thisProvider.getLocation().getCounty();
            String otherCounty = otherProvider.getLocation().getCounty();

            int countyComparison = thisCounty.compareTo(otherCounty);
            if (countyComparison != 0) {
                return countyComparison;
            }

            int dateComparison = this.date.compareTo(other.date);
            if (dateComparison != 0) {
                return dateComparison;
            }

            return this.timeslot.compareTo(other.timeslot);
        } else {
            throw new IllegalArgumentException("One or both providers are not of type Provider.");
        }
    }
}