package project.data;

import java.io.File;
import java.io.IOException;

public class DataLoader {

    public enum ParkingFormat {
        CSV, JSON;

        public static ParkingFormat convertString(String s) {
            if(s == null) {
                throw new IllegalArgumentException("Parking format should not be null.");
            }
            switch(s.toLowerCase()) {
                case "csv":
                    return CSV;
                case "json":
                    return JSON;
                default:
                    throw new IllegalArgumentException("Invalid parking data format has been provided.");
            }
        }
    }

    private static DataLoader instance;

    private final ParkingViolationData parkingData;
    private final PropertyData propertyData;
    private final PopulationData populationData;

    private DataLoader(String parkingFormatName,
                      String parkingFile,
                      String propertyFile,
                      String populationFile) throws IOException {

        if(parkingFormatName == null || parkingFile == null || propertyFile == null || populationFile == null) {
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

        ParkingFormat parkingFormat = ParkingFormat.convertString(parkingFormatName);

        if (parkingFormat == ParkingFormat.CSV) {
            this.parkingData = ParkingViolationData.fromCsvFile(parkingFile);
        } else {
            this.parkingData = ParkingViolationData.fromJsonFile(parkingFile);
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