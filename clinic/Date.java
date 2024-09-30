package clinic;

import java.util.Calendar;

public class Date implements Comparable<Date> {
    private int year;
    private int month;
    private int day;

    // Constants for leap year calculations
    public static final int QUADRENNIAL = 4;
    public static final int CENTENNIAL = 100;
    public static final int QUARTERCENTENNIAL = 400;

    // Constructor
    public Date(int month, int day, int year) {
        this.month = month;
        this.day = day;
        this.year = year;
    }

    // Getter methods
    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    // Method to check if the date is valid
    public boolean isValid() {
        // Check if year is before 1900
        if (year < 1900) {
            return false;
        }

        // Validate the month (1 = January, 12 = December)
        if (month < Calendar.JANUARY + 1 || month > Calendar.DECEMBER + 1) {
            return false;
        }

        // Validate days based on month
        switch (month) {
            case Calendar.JANUARY + 1: // January
            case Calendar.MARCH + 1:  // March
            case Calendar.MAY + 1:    // May
            case Calendar.JULY + 1:   // July
            case Calendar.AUGUST + 1: // August
            case Calendar.OCTOBER + 1:// October
            case Calendar.DECEMBER + 1:// December
                if (day < 1 || day > 31) {
                    return false;
                }
                break;
            case Calendar.APRIL + 1:  // April
            case Calendar.JUNE + 1:   // June
            case Calendar.SEPTEMBER + 1: // September
            case Calendar.NOVEMBER + 1:  // November
                if (day < 1 || day > 30) {
                    return false;
                }
                break;
            case Calendar.FEBRUARY + 1: // February
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

    // Method to determine if the year is a leap year
    private boolean isLeapYear() {
        if (year % QUADRENNIAL == 0) {
            if (year % CENTENNIAL == 0) {
                return year % QUARTERCENTENNIAL == 0;
            }
            return true;
        }
        return false;
    }

    // Override the equals() method
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Date other = (Date) obj;
        return this.year == other.year && this.month == other.month && this.day == other.day;
    }

    // Override the compareTo() method
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

    // Override the toString() method
    @Override
    public String toString() {
        return month + "/" + day + "/" + year; // Format MM/DD/YYYY
    }

    // Testbed main() method for testing the isValid() method
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

        // Test Case 6: Valid date (October 15, 2024)
        Date date6 = new Date(4, 1, 2030);
        System.out.println("Test Case 6: " + date6.isValid() + " (Expected: false)");
    }
}
