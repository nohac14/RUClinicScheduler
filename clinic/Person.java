package clinic;

public class Person implements Comparable<Person> {
    protected Profile profile;

    /**
     * Constructs a Person with the specified profile.
     *
     * @param profile the profile associated with this person.
     */
    public Person(Profile profile) {
        this.profile = profile;
    }

    /**
     * Gets the profile of the person.
     *
     * @return the profile.
     */
    public Profile getProfile() {
        return profile;
    }

    /**
     * Compares this Person with another Person based on their profile.
     *
     * @param other the other Person to compare to.
     * @return a negative integer, zero, or a positive integer as this profile is less than, equal to, or greater than the other profile.
     */
    @Override
    public int compareTo(Person other) {
        return this.profile.compareTo(other.getProfile());
    }

    /**
     * Returns a string representation of the person's profile.
     *
     * @return a string representation of the person.
     */
    @Override
    public String toString() {
        return profile.toString();
    }
}