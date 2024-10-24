package clinic;

public class Radiology {

    // Enum to represent different types of imaging services
    public enum ImagingService {
        CATSCAN,
        ULTRASOUND,
        XRAY
    }

    private String roomNumber;  // Room number of the radiology room
    private ImagingService serviceType;  // Type of imaging service offered in this room

    /**
     * Constructs a Radiology room with the specified room number and imaging service type.
     *
     * @param roomNumber the number of the radiology room.
     * @param serviceType the type of imaging service offered in this room.
     */
    public Radiology(String roomNumber, ImagingService serviceType) {
        this.roomNumber = roomNumber;
        this.serviceType = serviceType;
    }

    /**
     * Gets the room number of this radiology room.
     *
     * @return the room number.
     */
    public String getRoomNumber() {
        return roomNumber;
    }

    /**
     * Gets the type of imaging service offered in this room.
     *
     * @return the type of imaging service.
     */
    public ImagingService getRoomType() {
        return serviceType;
    }

    /**
     * Sets the room number for this radiology room.
     *
     * @param roomNumber the new room number.
     */
    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    /**
     * Sets the imaging service type for this room.
     *
     * @param serviceType the new imaging service type.
     */
    public void setServiceType(ImagingService serviceType) {
        this.serviceType = serviceType;
    }

    /**
     * Returns a string representation of the radiology room and its imaging service.
     *
     * @return a formatted string with the room number and imaging service type.
     */
    @Override
    public String toString() {
        return String.format("Room: %s, Service: %s", roomNumber, serviceType);
    }
}
