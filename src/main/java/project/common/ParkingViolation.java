package project.common;

import java.util.Objects;

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

    public String getTimestamp() {
        return timestamp;
    }

    public int getFine() {
        return fine;
    }

    public String getDescription() {
        return description;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public String getState() {
        return state;
    }

    public String getViolationId() {
        return violationId;
    }

    public String getZipCode() {
        return zipCode;
    }

    @Override
    public String toString() {
        return "Parking Violation: " + "timestamp = " + timestamp + '\'' + "fine = " + fine + '\'' +
                "description = " + description + '\'' + "vehicleId = " + vehicleId + '\'' +
                "state = " + state + '\'' + "violationId" + violationId + '\'' + "zipCode" + zipCode;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ParkingViolation)) {
            return false;
        }
        ParkingViolation p = (ParkingViolation) o;

        return fine == p.fine && Objects.equals(timestamp, p.timestamp) &&
                Objects.equals(description, p.description) && Objects.equals(vehicleId, p.vehicleId)
                && Objects.equals(state, p.state) && Objects.equals(violationId, p.violationId) &&
                Objects.equals(zipCode, p.zipCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, fine, description, vehicleId, state, violationId, zipCode);
    }

}
