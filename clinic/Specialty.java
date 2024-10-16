package clinic;

public enum Specialty {
    FAMILY(250),
    PEDIATRICIAN(300),
    ALLERGIST(350),
    TECHNICIAN(200); // Added technician with a base rate

    private final int charge;

    Specialty(int charge) {
        this.charge = charge;
    }

    public int getCharge() {
        return charge;
    }

    @Override
    public String toString() {
        return name(); // Returns the enum name
    }
}