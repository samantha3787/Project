package project.common;

public class ParkingViolation {

    private final String timestamp;
    private final int fine;
    private final String description;
    private final String vehicleId;
    private final String state;
    private final String violationId;
    private final String zipCode;

    public ParkingViolation(String timestamp, int fine, String description, String vehicleId,
                            String state, String violationId, String zipCode) {
        this.timestamp = timestamp;
        this.fine = fine;
        this.description = description;
        this.vehicleId = vehicleId;
        this.state = state;
        this.violationId = violationId;
        this.zipCode = zipCode;
    }



}
