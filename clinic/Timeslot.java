package clinic;

public enum Timeslot {
    SLOT1(9, 0),   // 9:00 AM
    SLOT2(10, 45), // 10:45 AM
    SLOT3(11, 15), // 11:15 AM
    SLOT4(13, 30), // 1:30 PM
    SLOT5(15, 0),  // 3:00 PM
    SLOT6(16, 15); // 4:15 PM

    private final int hour;
    private final int minute;

    // Constructor
    Timeslot(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

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


    // Getter for hour
    public int getHour() {
        return hour;
    }

    // Getter for minute
    public int getMinute() {
        return minute;
    }

    // Override toString() to return the time in HH:MM AM/PM format
    @Override
    public String toString() {
        String period = (hour < 12) ? "AM" : "PM";
        int formattedHour = (hour > 12) ? hour - 12 : hour;
        return formattedHour + ":" + String.format("%02d", minute) + " " + period;
    }
}
