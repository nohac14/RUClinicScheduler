package clinic;

public class Technician extends Provider {
    private int ratePerVisit;

    public Technician(Profile profile, int ratePerVisit, Location location) {
        super(profile, location, Specialty.TECHNICIAN);  // Using the TECHNICIAN specialty
        this.ratePerVisit = ratePerVisit;
    }

    @Override
    public int rate() {
        return ratePerVisit;
    }

    public int getRatePerVisit() {
        return ratePerVisit;
    }

    public void setRatePerVisit(int ratePerVisit) {
        this.ratePerVisit = ratePerVisit;
    }

    /**
     * Checks if the technician is available for a given imaging service, date, and timeslot.
     *
     * @param serviceType the type of imaging service (e.g., XRAY, CATSCAN, ULTRASOUND).
     * @param date the date of the appointment.
     * @param timeslot the timeslot of the appointment.
     * @return true if the technician is available, false otherwise.
     */
    public boolean isAvailable(Radiology.ImagingService serviceType, Date date, Timeslot timeslot) {
        // Logic to check if the technician is available based on service type, date, and timeslot
        // (Placeholder: availability logic needs to be defined based on actual scheduling)
        return true;  // Placeholder for availability check
    }

    @Override
    public String toString() {
        return String.format("[%s, %s, %s %s, $%d per visit]",
                this.getProfile().getFullName(), // Name of the technician
                getLocation().getCity(),
                getLocation().getCounty(),
                getLocation().getZip(),
                ratePerVisit);
    }
}
