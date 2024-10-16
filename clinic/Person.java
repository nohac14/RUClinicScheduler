package clinic;

public class Person implements Comparable<Person> {
    protected Profile profile;

    public Person(Profile profile) {
        this.profile = profile;
    }

    public Profile getProfile() {
        return profile;
    }

    @Override
    public int compareTo(Person other) {
        return this.profile.compareTo(other.getProfile());
    }

    @Override
    public String toString() {
        return profile.toString();
    }

    // Method to return the full name of the person
    public String getName() {
        return profile.getFname() + " " + profile.getLname();
    }
}