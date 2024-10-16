package clinic;

public class Doctor extends Provider {
    private Specialty specialty;  // Doctor's medical specialty
    private String npi;  // National Provider Identification (NPI)
    private Technician technician;  // Associated technician

    public Doctor(Profile profile, Specialty specialty, String npi, Location location, Technician technician) {
        super(profile, location, specialty);
        this.specialty = specialty;
        this.npi = npi;
        this.technician = technician;  // Assign the technician during doctor creation
    }

    public Specialty getSpecialty() {
        return specialty;
    }

    public String getNpi() {
        return npi;
    }

    public Technician getTechnician() {
        return technician;
    }

    public void setTechnician(Technician technician) {
        this.technician = technician;
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
                return 200;
        }
    }

    @Override
    public String toString() {
        return String.format("[%s, %s, %s %s, %s, NPI: %s, Technician: %s]",
                this.getProfile().getFullName(),
                getLocation().getCity(),
                getLocation().getCounty(),
                getLocation().getZip(),
                specialty.name(),
                npi,
                technician != null ? technician.getProfile().getFullName() : "None"
        );
    }
}