package clinic;

public class Date implements Comparable<Date> {
    private int year;
    private int month;
    private int day;

    // Constructor, getters, and setters

    public boolean isValid() {
        // Implement date validation logic (leap year, valid days per month, etc.)
    }

    @Override
    public int compareTo(Date other) {
        // Compare based on year, month, day
    }

    @Override
    public String toString() {
        // Return formatted date string
    }
}

