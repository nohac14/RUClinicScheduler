package clinic;

/**
 * The Location enum represents various locations with their associated city, county, and zip code.
 * Each location has a city name, a county, and a zip code.
 *
 * @author Jonas Lazebnik, Arjun Anand
 */
public enum Location {
    BRIDGEWATER("BRIDGEWATER", "Somerset", "08807"),
    EDISON("EDISON", "Middlesex", "08817"),
    PISCATAWAY("PISCATAWAY", "Middlesex", "08854"),
    PRINCETON("PRINCETON", "Mercer", "08542"),
    MORRISTOWN("MORRISTOWN", "Morris", "07960"),
    CLARK("CLARK", "Union", "07066");

    private final String city;
    private final String county;
    private final String zip;

    /**
     * Constructs a Location enum instance with the specified city, county, and zip code.
     *
     * @param city   the name of the city.
     * @param county the name of the county.
     * @param zip    the zip code of the location.
     */
    Location(String city, String county, String zip) {
        this.city = city;
        this.county = county;
        this.zip = zip;
    }

    /**
     * Gets the city of the location.
     *
     * @return the city name.
     */
    public String getCity() {
        return city;
    }

    /**
     * Gets the county of the location.
     *
     * @return the county name.
     */
    public String getCounty() {
        return county;
    }

    /**
     * Gets the zip code of the location.
     *
     * @return the zip code.
     */
    public String getZip() {
        return zip;
    }

    /**
     * Returns a string representation of the location in the format "City, County Zip".
     *
     * @return a formatted string representing the location.
     */
    @Override
    public String toString() {
        return city + ", " + county + " " + zip;
    }
}
