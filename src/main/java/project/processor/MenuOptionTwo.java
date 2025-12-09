package project.processor;

import project.data.ParkingViolationData;
import project.data.PopulationData;
import project.common.ParkingViolation;

import java.util.HashMap;
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
        Map<String, Long> finesByZipcode = new HashMap<>();

        for(ParkingViolation v : parkingData.paViolationsWithZipIterable()) {
            String zip = v.getZipCode();
            int fine = v.getFine();

            Long current = finesByZipcode.get(zip);
            if(current == null) {
                current = 0;
            }
            current += fine;
            finesByZipcode.put(zip, current);
        }

        Map<String, Double> finesPerCapita = new HashMap<>();

        for(Map.Entry<String, Long> entry : finesByZipcode.entrySet()) {
            String zip = entry.getKey();
            Long fine = entry.getValue();

            int pop = populationData.getPopulation(zip);
            if(fine <= 0L || pop <= 0L) {
                continue;
            }

            double result = (double) fine / (double) pop;

            finesPerCapita.put(zip, result);
        }

        return finesPerCapita;
    }


}
