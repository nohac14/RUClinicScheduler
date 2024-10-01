package clinic;

/**
 * The Timeslot enum represents specific time slots for appointments in a clinic.
 * Each timeslot is defined by an hour and minute value, and the available time slots are:
 * - SLOT1: 9:00 AM
 * - SLOT2: 10:45 AM
 * - SLOT3: 11:15 AM
 * - SLOT4: 1:30 PM
 * - SLOT5: 3:00 PM
 * - SLOT6: 4:15 PM
 *
 * The enum provides methods to get a timeslot by number, retrieve the timeslot's number,
 * and format the timeslot as a string in "HH:MM AM/PM" format.
 *
 * @author studentName
 */
public enum Timeslot {
    SLOT1(9, 0),   // 9:00 AM
    SLOT2(10, 45), // 10:45 AM
    SLOT3(11, 15), // 11:15 AM
    SLOT4(13, 30), // 1:30 PM
    SLOT5(15, 0),  // 3:00 PM
    SLOT6(16, 15); // 4:15 PM

    private final int hour;
    private final int minute;

    /**
     * Constructs a Timeslot with the specified hour and minute.
     *
     * @param hour   the hour of the timeslot (24-hour format).
     * @param minute the minute of the timeslot.
     */
    Timeslot(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    /**
     * Gets a Timeslot enum by the provided timeslot number (1-6).
     *
     * @param timeSlot the timeslot number.
     * @return the Timeslot enum corresponding to the number, or null if invalid.
     */
    public static Timeslot getTimeslotByNumber(int timeSlot) {
        switch (timeSlot) {
            case 1:
                return Timeslot.SLOT1;
            case 2:
                return Timeslot.SLOT2;
            case 3:
                return Timeslot.SLOT3;
            case 4:
                return Timeslot.SLOT4;
            case 5:
                return Timeslot.SLOT5;
            case 6:
                return Timeslot.SLOT6;
            default:
                return null; // Return null if the timeslot is invalid
        }
    }

    /**
     * Gets the number corresponding to this timeslot (1-6).
     *
     * @return the timeslot number.
     */
    public int getSlotNumber() {
        switch (this) {
            case SLOT1: return 1;
            case SLOT2: return 2;
            case SLOT3: return 3;
            case SLOT4: return 4;
            case SLOT5: return 5;
            case SLOT6: return 6;
            default: return -1; // Invalid slot
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
     * Returns a string representation of the timeslot in "HH:MM AM/PM" format.
     *
     * @return a formatted string representing the timeslot.
     */
    @Override
    public String toString() {
        String period = (hour < 12) ? "AM" : "PM";
        int formattedHour = (hour > 12) ? hour - 12 : hour;
        return formattedHour + ":" + String.format("%02d", minute) + " " + period;
    }
}
