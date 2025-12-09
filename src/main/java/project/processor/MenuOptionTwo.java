package project.processor;

import project.data.ParkingViolationData;
import project.data.PopulationData;
import project.common.ParkingViolation;

import java.util.Map;

public class MenuOptionTwo {

    private final ParkingViolationData parkingData;
    private final PopulationData populationData;

    public MenuOptionTwo(ParkingViolationData parkingData, PopulationData populationData) {
        if(parkingData == null || populationData == null) {
            throw new IllegalArgumentException("Parking data and population data cannot be null.");
        }
        this.parkingData = parkingData;
        this.populationData = populationData;
    }

    public Map<String, Double> findFinesPerCapita() {

    }


}
