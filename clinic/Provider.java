package clinic;

/**
 * The Provider enum represents healthcare providers along with their associated location and specialty.
 * Each provider is associated with a specific Location and a Specialty.
 * The enum also provides methods to retrieve the location and specialty of the provider.
 *
 * @author Jonas Lazebnik, Arjun Anand
 */
public enum Provider {
    PATEL(Location.BRIDGEWATER, Specialty.FAMILY),
    LIM(Location.BRIDGEWATER, Specialty.PEDIATRICIAN),
    ZIMNES(Location.CLARK, Specialty.FAMILY),
    HARPER(Location.CLARK, Specialty.FAMILY),
    KAUR(Location.PRINCETON, Specialty.ALLERGIST),
    TAYLOR(Location.PISCATAWAY, Specialty.PEDIATRICIAN),
    RAMESH(Location.MORRISTOWN, Specialty.ALLERGIST),
    CERAVOLO(Location.EDISON, Specialty.PEDIATRICIAN);

    private final Location location;
    private final Specialty specialty;

    /**
     * Constructs a Provider enum instance with the specified location and specialty.
     *
     * @param location  the location where the provider operates.
     * @param specialty the specialty of the provider.
     */
    Provider(Location location, Specialty specialty) {
        this.location = location;
        this.specialty = specialty;
    }

    /**
     * Returns a string representation of the provider in the format:
     * "[Provider Name, City, County ZIP, Specialty]".
     *
     * @return a formatted string representing the provider.
     */
    @Override
    public String toString() {
        return String.format("[%s, %s, %s %s, %s]",
                this.name(), // Provider's name
                location.getCity(), // City where the provider operates
                location.getCounty(), // County
                location.getZip(), // ZIP code
                specialty.name() // Provider's specialty
        );
    }

    /**
     * Gets the location where the provider operates.
     *
     * @return the location of the provider.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Gets the specialty of the provider.
     *
     * @return the specialty of the provider.
     */
    public Specialty getSpecialty() {
        return specialty;
    }
}
