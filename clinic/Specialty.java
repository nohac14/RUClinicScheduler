package clinic;

/**
 * The Specialty enum represents different medical specialties and their associated charges.
 * Each specialty has a fixed charge for the services provided, which can be accessed through the getCharge() method.
 *
 * The available specialties are:
 * - FAMILY: Charge of $250
 * - PEDIATRICIAN: Charge of $300
 * - ALLERGIST: Charge of $350
 *
 * The toString() method returns the name of the specialty.
 *
 * @author studentName
 */
public enum Specialty {
    FAMILY(250),
    PEDIATRICIAN(300),
    ALLERGIST(350);

    private final int charge;

    /**
     * Constructs a Specialty enum instance with the specified charge.
     *
     * @param charge the charge associated with the specialty.
     */
    Specialty(int charge) {
        this.charge = charge;
    }

    /**
     * Gets the charge for the specialty.
     *
     * @return the charge for the specialty.
     */
    public int getCharge() {
        return charge;
    }

    /**
     * Returns the name of the specialty as a string.
     *
     * @return the name of the specialty.
     */
    @Override
    public String toString() {
        return name(); // Returns the enum name, e.g., FAMILY, PEDIATRICIAN, etc.
    }
}
