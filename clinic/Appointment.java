package clinic;

public class Appointment implements Comparable<Appointment> {
    private Date date;
    private Timeslot timeslot;
    private Profile patient;
    private Provider provider;

    // Constructor, getters, and setters
    public Appointment (Date date, Timeslot timeslot, Profile patient, Provider provider) {
        this.date = date;
        this.timeslot = timeslot;
        this.patient = patient;
        this.provider = provider;
    }

    public Date getDate() {
        return date;
    }

    public Timeslot getTimeslot() {
        return timeslot;
    }

    public Profile getPatient() {
        return patient;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setTimeslot(Timeslot newTimeslot) {
        this.timeslot = newTimeslot;
    }

    @Override
    public boolean equals(Object obj) {
        // Logic for equality based on date, timeslot, and patient
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Appointment other = (Appointment) obj;

        // Check if date, timeslot, and patient are the same
        return this.date.equals(other.date) &&
                this.timeslot.equals(other.timeslot) &&
                this.patient.equals(other.patient);
    }

    @Override
    public String toString() {
        // Return formatted appointment details
        // Format: MM/DD/YYYY HH:MM AM/PM FirstName LastName DOB [PROVIDER, LOCATION, COUNTY ZIP, SPECIALTY]
        return String.format("%s %s %s [%s, %s, %s %s, %s]",
                date.toString(), // Date
                timeslot.toString(), // Time in the correct format (based on Timeslot enum)
                patient.toString(), // Patient details
                provider.name(), // Provider's name (from Provider enum)
                provider.getLocation().getCity(), // City where the provider operates
                provider.getLocation().getCounty(), // County
                provider.getLocation().getZip(), // ZIP code
                provider.getSpecialty().name() // Provider's specialty
        );
    }

    @Override
    public int compareTo(Appointment other) {
        // Comparison logic based on date, timeslot, provider
        int patientComparison = this.patient.compareTo(other.patient);
        if (patientComparison != 0) {
            return patientComparison;
        }
        // First, compare by date
        int dateComparison = this.date.compareTo(other.date);
        if (dateComparison != 0) {
            return dateComparison;
        }

        // If dates are the same, compare by timeslot
        int timeslotComparison = this.timeslot.compareTo(other.timeslot);
        if (timeslotComparison != 0) {
            return timeslotComparison;
        }

        // If both date and timeslot are the same, compare by patient (for consistency in ordering)
        return this.patient.compareTo(other.patient);
    }

    public int compareByCounty(Appointment other) { //Used for PL
        // First, compare by provider's county
        String thisCounty = this.provider.getLocation().getCounty();
        String otherCounty = other.provider.getLocation().getCounty();

        int countyComparison = thisCounty.compareTo(otherCounty);
        if (countyComparison != 0) {
            return countyComparison;
        }

        // If counties are the same, compare by appointment date
        int dateComparison = this.date.compareTo(other.date);
        if (dateComparison != 0) {
            return dateComparison;
        }

        // If appointment dates are the same, compare by timeslot
        return this.timeslot.compareTo(other.timeslot);
    }

}
