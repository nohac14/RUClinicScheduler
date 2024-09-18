package clinic;

public enum Location {
    BRIDGEWATER("Bridgewater", "Somerset", "08807"),
    EDISON("Edison", "Middlesex", "08817"),
    PISCATAWAY("Piscataway", "Middlesex", "08854"),
    PRINCETON("Princeton", "Mercer", "08542"),
    MORRISTOWN("Morristown", "Morris", "07960"),
    CLARK("Clark", "Union", "07066");

    private final String city;
    private final String county;
    private final String zip;

    // Constructor
    Location(String city, String county, String zip) {
        this.city = city;
        this.county = county;
        this.zip = zip;
    }

    // Getters
    public String getCity() {
        return city;
    }

    public String getCounty() {
        return county;
    }

    public String getZip() {
        return zip;
    }

    // Override toString() for a formatted location string
    @Override
    public String toString() {
        return city + ", " + county + " " + zip;
    }
}
