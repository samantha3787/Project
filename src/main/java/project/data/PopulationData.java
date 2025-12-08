package project.data;

import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PopulationData {

    private final Map<String, Integer> populationByZipCode = new HashMap<>();

    public PopulationData(String filename) throws IOException {
        fileLoader(filename);
    }

    private void fileLoader(String filename) throws IOException {
        File file = new File(filename);

        try(Scanner scanner = new Scanner(file)) {
            while(scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if(line.isEmpty()) {
                    continue;
                }

                String[] lineParts = line.split("\\s+");
                if (lineParts.length < 2) {
                    continue;
                }

                String pureZipCode = lineParts[0];
                String purePopulationValue = lineParts[1];

                String zipCode = processZipCode(pureZipCode);
                if (zipCode == null){
                    continue;
                }

                int population;
                try {
                    population = Integer.parseInt(purePopulationValue);
                } catch (NumberFormatException e) {
                    continue;
                }

                if(population<0) {
                    continue;
                }

                populationByZipCode.put(zipCode, population);

            }
        } catch (FileNotFoundException e) {
            throw new IOException("Could not open population file", e);
        }
    }

    private String processZipCode(String zip) {
        if(zip == null){
            return null;
        }
        zip = zip.trim();
        if(zip.isEmpty()) {
            return null;
        }
        if(zip.length() >= 5) {
            return zip.substring(0,5);
        } else {
            return zip;
        }
    }

    public int getPopulation(String zipCode) {
        if(zipCode == null) {
            return 0;
        }
        Integer pop = populationByZipCode.get(zipCode);
        return (pop==null) ? 0 : pop;
    }

    public Map<String, Integer> getAllPopulationData() {
        return populationByZipCode;
    }

    public long getTotalPopulation() {
        return populationByZipCode.values().stream()
                                            .mapToLong(v -> v)
                                            .reduce(0L, (a,b) -> a+b);
    }

}