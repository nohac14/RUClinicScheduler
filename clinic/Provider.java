package clinic;

public abstract class Provider extends Person {
    private Location location;
    private Specialty specialty;

    public Provider(Profile profile, Location location, Specialty specialty) {
        super(profile);
        this.location = location;
        this.specialty = specialty;
    }

    public Location getLocation() {
        return location;
    }

    public Specialty getSpecialty() {
        return specialty;
    }

    // Abstract method for getting the NPI, will be implemented in the Doctor class
    public abstract String getNPI();

    public abstract int rate();

    @Override
    public String toString() {
        return String.format("[%s, %s, %s %s, %s]",
                this.getProfile().getFullName(),
                getLocation().getCity(),
                getLocation().getCounty(),
                getLocation().getZip(),
                specialty.name());
    }

    // Method to return a list of all providers
    public static List<Provider> getAllProviders() {
        List<Provider> providers = new List<>();

        providers.add(new Doctor(new Profile("John", "Patel", new Date(12, 25, 1970)),
                Specialty.FAMILY, "12345", Location.BRIDGEWATER));
        providers.add(new Doctor(new Profile("Emily", "Lim", new Date(3, 4, 1985)),
                Specialty.PEDIATRICIAN, "23456", Location.BRIDGEWATER));
        providers.add(new Doctor(new Profile("Paul", "Zimnes", new Date(7, 15, 1980)),
                Specialty.FAMILY, "34567", Location.CLARK));
        providers.add(new Doctor(new Profile("Sarah", "Harper", new Date(5, 10, 1978)),
                Specialty.FAMILY, "45678", Location.CLARK));
        providers.add(new Doctor(new Profile("Raj", "Kaur", new Date(9, 19, 1982)),
                Specialty.ALLERGIST, "56789", Location.PRINCETON));
        providers.add(new Doctor(new Profile("Lucas", "Taylor", new Date(6, 7, 1984)),
                Specialty.PEDIATRICIAN, "67890", Location.PISCATAWAY));
        providers.add(new Doctor(new Profile("Amit", "Ramesh", new Date(11, 14, 1979)),
                Specialty.ALLERGIST, "78901", Location.MORRISTOWN));
        providers.add(new Doctor(new Profile("Sophia", "Ceravolo", new Date(2, 20, 1983)),
                Specialty.PEDIATRICIAN, "89012", Location.EDISON));

        return providers;
    }
}