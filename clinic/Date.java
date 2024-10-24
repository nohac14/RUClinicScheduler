package clinic;

import java.util.Calendar;

/**
 * Represents a Date with year, month, and day.
 * This class provides methods to validate the date and determine leap years.
 * It implements the Comparable interface to allow comparison between Date objects.
 */
public class Date implements Comparable<Date> {
    private int year;
    private int month;
    private int day;

    // Constants for leap year calculations
    public static final int QUADRENNIAL = 4;
    public static final int CENTENNIAL = 100;
    public static final int QUARTERCENTENNIAL = 400;

    /**
     * Constructs a Date object with the specified month, day, and year.
     *
     * @param month the month of the date (1 = January, 12 = December).
     * @param day   the day of the date.
     * @param year  the year of the date.
     */
    public Date(int month, int day, int year) {
        this.month = month;
        this.day = day;
        this.year = year;
    }

    /**
     * Gets the year of the date.
     *
     * @return the year of the date.
     */
    public int getYear() {
        return year;
    }

    /**
     * Gets the month of the date.
     *
     * @return the month of the date.
     */
    public int getMonth() {
        return month;
    }

    /**
     * Gets the day of the date.
     *
     * @return the day of the date.
     */
    public int getDay() {
        return day;
    }

    /**
     * Checks if the date is valid according to the month, day, and year.
     * A date is valid if the year is after 1900, the month is between January and December,
     * and the day is valid for the given month, considering leap years for February.
     *
     * @return true if the date is valid, false otherwise.
     */
    public boolean isValid() {
        // Check if year is before 1900 or in the future
        if (year < 1900 || year > Calendar.getInstance().get(Calendar.YEAR)) {
            return false;
        }

        // Validate the month (1 = January, 12 = December)
        if (month < 1 || month > 12) {
            return false;
        }

        // Validate days based on month
        switch (month) {
            case 1: // January
            case 3: // March
            case 5: // May
            case 7: // July
            case 8: // August
            case 10: // October
            case 12: // December
                if (day < 1 || day > 31) {
                    return false;
                }
                break;
            case 4:  // April
            case 6:  // June
            case 9:  // September
            case 11: // November
                if (day < 1 || day > 30) {
                    return false;
                }
                break;
            case 2: // February
                if (isLeapYear()) {
                    if (day < 1 || day > 29) {
                        return false;
                    }
                } else {
                    if (day < 1 || day > 28) {
                        return false;
                    }
                }
                break;
            default:
                return false;
        }

        return true;
    }

    /**
     * Determines if the year is a leap year.
     *
     * @return true if the year is a leap year, false otherwise.
     */
    private boolean isLeapYear() {
        if (year % QUADRENNIAL == 0) {
            if (year % CENTENNIAL == 0) {
                return year % QUARTERCENTENNIAL == 0;
            }
            return true;
        }
        return false;
    }

    /**
     * Compares this date with another date based on year, month, and day.
     *
     * @param other the other date to compare with this date.
     * @return a negative integer, zero, or a positive integer as this date is less than, equal to, or greater than the specified date.
     */
    @Override
    public int compareTo(Date other) {
        if (this.year != other.year) {
            return Integer.compare(this.year, other.year);  // Compare years first
        } else if (this.month != other.month) {
            return Integer.compare(this.month, other.month);  // Compare months if years are the same
        } else {
            return Integer.compare(this.day, other.day);  // Compare days if months are the same
        }
    }

    /**
     * Returns a string representation of the date in MM/DD/YYYY format.
     *
     * @return a string representing the date in MM/DD/YYYY format.
     */
    @Override
    public String toString() {
        return month + "/" + day + "/" + year;
    }

    /**
     * A testbed main() method to test the isValid() method with different date scenarios.
     *
     * @param args the command-line arguments.
     */
    public static void main(String[] args) {
        // Test Case 1: Invalid date (year before 1900)
        Date date1 = new Date(11, 21, 800);
        System.out.println("Test Case 1: " + date1.isValid() + " (Expected: false)");

        // Test Case 2: Invalid date (non-leap year February 29)
        Date date2 = new Date(2, 29, 2018);
        System.out.println("Test Case 2: " + date2.isValid() + " (Expected: false)");

        // Test Case 3: Valid leap year date (February 29, 2020)
        Date date3 = new Date(2, 29, 2020);
        System.out.println("Test Case 3: " + date3.isValid() + " (Expected: true)");

        // Test Case 4: Invalid date (month outside valid range)
        Date date4 = new Date(13, 1, 2024);
        System.out.println("Test Case 4: " + date4.isValid() + " (Expected: false)");

        // Test Case 5: Valid date (October 15, 2024)
        Date date5 = new Date(10, 15, 2024);
        System.out.println("Test Case 5: " + date5.isValid() + " (Expected: true)");
    }
}
