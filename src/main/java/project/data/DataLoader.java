package project.data;

import java.io.IOException;

public class DataLoader {

    private final ParkingViolationData parkingData;
    private final PropertyData propertyData;
    private final PopulationData populationData;

    public DataLoader(String parkingFormat,
                      String parkingFile,
                      String propertyFile,
                      String populationFile) throws IOException {

        /*
        arg0 = csv or json
        arg1 = parking.csv or parking.json
        arg2 = properties.csv
        arg3 = population.txt
         */

        if ("csv".equals(parkingFormat)) {
            this.parkingData = ParkingViolationData.fromCsvFile(parkingFile);
        } else if ("json".equals(parkingFormat)) {
            this.parkingData = ParkingViolationData.fromJsonFile(parkingFile);
        } else {
            throw new IllegalArgumentException(
                    "First argument must be \"csv\" or \"json\"; got: " + parkingFormat);
        }

        this.propertyData = new PropertyData(propertyFile);
        this.populationData = new PopulationData(populationFile); // uses population.txt
    }

    public ParkingViolationData getParkingData() {
        return parkingData;
    }

    public PropertyData getPropertyData() {
        return propertyData;
    }

    public PopulationData getPopulationData() {
        return populationData;
    }
}