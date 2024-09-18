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
        return String.format("%02d:%02d %s", formattedHour, minute, period);
    }
}
