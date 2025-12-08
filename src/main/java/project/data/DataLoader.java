package project.data;

import java.io.File;
import java.io.IOException;

public class DataLoader {

    private static DataLoader instance;

    private final ParkingViolationData parkingData;
    private final PropertyData propertyData;
    private final PopulationData populationData;

    private DataLoader(String parkingFormat,
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

    public static void initialize(String parkingFormat,
                                  String parkingFile,
                                  String propertyFile,
                                  String populationFile) throws IOException {
        if(instance != null) {
            throw new IllegalStateException("DataLoader was already initialized.");
        }
        instance = new DataLoader(parkingFormat, parkingFile, propertyFile, populationFile);
    }

    public static DataLoader getInstance() {
        if(instance == null) {
            throw new IllegalStateException("DataLoader has not been initialized.");
        }
        return instance;
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