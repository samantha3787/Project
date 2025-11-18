package project;

import project.data.DataLoader;
import project.data.ParkingViolationData;
import project.data.PropertyData;
import project.data.PopulationData; //update later

import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        // 1) check runtime arguments
        if (args.length != 4) {
            System.out.println("Usage: java project.Main <csv|json> "
                    + "<parking-file> <properties-file> <population-file>");
            return;
        }

        String parkingFormat = args[0];
        String parkingFile   = args[1];
        String propertyFile  = args[2];
        String populationFile = args[3];

        try {
            // 2) load all data in one place
            DataLoader loader = new DataLoader(
                    parkingFormat, parkingFile, propertyFile, populationFile);

            ParkingViolationData parkingData = loader.getParkingData();
            PropertyData propertyData = loader.getPropertyData();
            // PopulationData populationData = loader.getPopulationData(); // later

            // 3) for now just show that things loaded
            // later call processor / UI code
            System.out.println("Loaded parking violations: "
                    + parkingData.getViolations().size());
            System.out.println("Loaded properties: "
                    + propertyData.getProperties().size());
            // System.out.println("Total population: " + populationData.getTotalPopulation());

            // TODO create UI tier and main menu then pass data objects into it

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error reading input files: " + e.getMessage());
        }
    }
}