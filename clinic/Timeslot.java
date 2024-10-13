package clinic;

/**
 * The Timeslot class represents a specific time slot for appointments in a clinic.
 * Each timeslot is defined by an hour and minute value, and provides methods to get and compare timeslots.
 *
 * Implements Comparable interface to allow sorting and comparison of timeslots.
 *
 * This class provides constants for common timeslots and methods to retrieve timeslots by number.
 *
 * Format: "HH:MM AM/PM".
 *
 * @author Arjun/Jonas
 */
public class Timeslot implements Comparable<Timeslot> {
    private final int hour;
    private final int minute;

    // Predefined constants for common timeslots
    public static final Timeslot SLOT1 = new Timeslot(9, 0);   // 9:00 AM
    public static final Timeslot SLOT2 = new Timeslot(10, 45); // 10:45 AM
    public static final Timeslot SLOT3 = new Timeslot(11, 15); // 11:15 AM
    public static final Timeslot SLOT4 = new Timeslot(13, 30); // 1:30 PM
    public static final Timeslot SLOT5 = new Timeslot(15, 0);  // 3:00 PM
    public static final Timeslot SLOT6 = new Timeslot(16, 15); // 4:15 PM

    /**
     * Constructs a Timeslot with the specified hour and minute.
     *
     * @param hour   the hour of the timeslot (24-hour format).
     * @param minute the minute of the timeslot.
     */
    public Timeslot(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    /**
     * Gets a Timeslot object by the provided timeslot number (1-6).
     *
     * @param timeSlot the timeslot number.
     * @return the Timeslot corresponding to the number, or null if invalid.
     */
    public static Timeslot getTimeslotByNumber(int timeSlot) {
        switch (timeSlot) {
            case 1:
                return SLOT1;
            case 2:
                return SLOT2;
            case 3:
                return SLOT3;
            case 4:
                return SLOT4;
            case 5:
                return SLOT5;
            case 6:
                return SLOT6;
            default:
                return null; // Return null if the timeslot number is invalid
        }
    }

    /**
     * Gets the hour of the timeslot in 24-hour format.
     *
     * @return the hour of the timeslot.
     */
    public int getHour() {
        return hour;
    }

    /**
     * Gets the minute of the timeslot.
     *
     * @return the minute of the timeslot.
     */
    public int getMinute() {
        return minute;
    }

    /**
     * Compares this timeslot with another timeslot.
     * Comparison is based on the hour and minute of the timeslot.
     *
     * @param other the other timeslot to compare to.
     * @return a negative integer, zero, or a positive integer as this timeslot is earlier than, equal to, or later than the other timeslot.
     */
    @Override
    public int compareTo(Timeslot other) {
        if (this.hour != other.hour) {
            return this.hour - other.hour;
        }
        return this.minute - other.minute;
    }

    /**
     * Checks if this timeslot is equal to another object.
     * Two timeslots are considered equal if they have the same hour and minute.
     *
     * @param obj the object to compare with this timeslot.
     * @return true if the timeslots have the same hour and minute; false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Timeslot other = (Timeslot) obj;
        return this.hour == other.hour && this.minute == other.minute;
    }

    /**
     * Returns a string representation of the timeslot in "HH:MM AM/PM" format.
     *
     * @return a formatted string representing the timeslot.
     */
    @Override
    public String toString() {
        String period = (hour < 12) ? "AM" : "PM";
        int formattedHour = (hour == 0) ? 12 : (hour > 12) ? hour - 12 : hour;
        return String.format("%02d:%02d %s", formattedHour, minute, period);
    }

    /**
     * Returns the timeslot number based on predefined timeslots (1-6).
     *
     * @return the timeslot number corresponding to this timeslot, or -1 if it doesn't match any.
     */
    public int getSlotNumber() {
        if (this.equals(SLOT1)) return 1;
        if (this.equals(SLOT2)) return 2;
        if (this.equals(SLOT3)) return 3;
        if (this.equals(SLOT4)) return 4;
        if (this.equals(SLOT5)) return 5;
        if (this.equals(SLOT6)) return 6;
        return -1; // Invalid slot
    }
}