package clinic;

public enum Specialty {
    FAMILY(250),
    PEDIATRICIAN(300),
    ALLERGIST(350);

    private final int charge;

    // Constructor
    Specialty(int charge) {
        this.charge = charge;
    }

    // Getter for the charge
    public int getCharge() {
        return charge;
    }

    // Override toString() to return the specialty name
    @Override
    public String toString() {
        return name(); // Returns the enum name, e.g., FAMILY, PEDIATRICIAN, etc.
    }
}
