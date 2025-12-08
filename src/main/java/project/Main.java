package project;

import project.data.DataLoader;
import project.data.ParkingViolationData;
import project.data.PropertyData;
import project.data.PopulationData;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        // 1) check runtime arguments
        if (args.length != 4) {
            System.out.println("The number of run-time arguments is incorrect. " +
                    "You must provide parking violations file format, parking violations file name, " +
                    "property values file name, and population file name.");
            return;
        }

        String parkingFormat = args[0];
        String parkingFile   = args[1];
        String propertyFile  = args[2];
        String populationFile = args[3];

        try {
            DataLoader.initialize(parkingFormat, parkingFile, propertyFile, populationFile);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            return;
        } catch (IOException e) {
            System.out.println("Error reading input files: " + e.getMessage());
            return;
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }

        DataLoader loader = DataLoader.getInstance();

        ParkingViolationData parkingData = loader.getParkingData();
        PropertyData propertyData = loader.getPropertyData();
        PopulationData populationData = loader.getPopulationData();
    }
}