package clinic;

public class Doctor extends Provider {
    private Specialty specialty;  // Doctor's medical specialty
    private String npi;  // National Provider Identification (NPI)

    public Doctor(Profile profile, Specialty specialty, String npi, Location location) {
        super(profile, location, specialty);  // Call the superclass constructor
        this.specialty = specialty;
        this.npi = npi;
    }

    public Specialty getSpecialty() {
        return specialty;
    }

    @Override
    public String getNPI() {
        return npi;
    }

    @Override
    public int rate() {
        switch (specialty) {
            case FAMILY:
                return 250;
            case PEDIATRICIAN:
                return 300;
            case ALLERGIST:
                return 350;
            default:
                return 200;  // Default rate for any undefined specialty
        }
    }

    @Override
    public String toString() {
        return String.format("[%s, %s, %s %s, %s, NPI: %s]",
                this.getProfile().getFullName(),
                getLocation().getCity(),
                getLocation().getCounty(),
                getLocation().getZip(),
                specialty.name(),
                npi);
    }
}