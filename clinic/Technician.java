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