package clinic;

public enum Location {
    BRIDGEWATER("BRIDGEWATER", "Somerset", "08807"),
    EDISON("EDISON", "Middlesex", "08817"),
    PISCATAWAY("PISCATAWAY", "Middlesex", "08854"),
    PRINCETON("PRINCETON", "Mercer", "08542"),
    MORRISTOWN("MORRISTOWN", "Morris", "07960"),  // Added Morristown
    CLARK("CLARK", "Union", "07066");

    private final String city;
    private final String county;
    private final String zip;

    Location(String city, String county, String zip) {
        this.city = city;
        this.county = county;
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public String getCounty() {
        return county;
    }

    public String getZip() {
        return zip;
    }

    @Override
    public String toString() {
        return city + ", " + county + " " + zip;
    }

    /**
     * Simplified lookup by city only.
     * @param city the city name
     * @return the corresponding Location, or null if not found.
     */
    public static Location findByCity(String city) {
        for (Location loc : Location.values()) {
            if (loc.city.equalsIgnoreCase(city)) {
                return loc;
            }
        }
        return null;  // No match found for city
    }
}

