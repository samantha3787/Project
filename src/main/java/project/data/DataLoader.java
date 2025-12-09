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

        if(parkingFormat == null || parkingFile == null || propertyFile == null || populationFile == null) {
            throw new IllegalArgumentException("File format and names cannot be null.");
        }

        File parking = new File(parkingFile);
        File property = new File(propertyFile);
        File population = new File(populationFile);

        if(!parking.isFile()) {
            throw new IOException("Parking file not found.");
        }

        if(!property.isFile()) {
            throw new IOException("Property file not found.");
        }

        if(!population.isFile()) {
            throw new IOException("Population file not found.");
        }

        if ("csv".equals(parkingFormat)) {
            this.parkingData = ParkingViolationData.fromCsvFile(parkingFile);
        } else if ("json".equals(parkingFormat)) {
            this.parkingData = ParkingViolationData.fromJsonFile(parkingFile);
        } else {
            throw new IllegalArgumentException("Invalid parking data has been provided.");
        }

        this.propertyData = new PropertyData(propertyFile);
        this.populationData = new PopulationData(populationFile);
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