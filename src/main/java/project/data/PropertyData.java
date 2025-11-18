package project.data;

import project.common.Property;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class PropertyData {

    private final List<Property> properties = new ArrayList<>();
    private final Map<String, List<Property>> propertiesByZipCode = new HashMap<>();

    public PropertyData(String filename) throws IOException {
        readFile(filename);
    }

    private void readFile(String filename) throws IOException {
        File file = new File(filename);
        try (Scanner scanner = new Scanner(file)) {
            if(!(scanner.hasNext())) {
                throw new IOException("Properties file is empty.");
            }

            String firstLine = scanner.nextLine();
            String[] firstLineArray = firstLine.split(",", -1);

            int zipCodeIndex = findIndex(firstLineArray, "zip_code");
            int marketValueIndex = findIndex(firstLineArray, "market_value");
            int totalLivableAreaIndex = findIndex(firstLineArray, "total_livable_area");

            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if(line.trim().isEmpty()) {
                    continue;
                }

                String[] lineArray = line.split(",", -1);
                if(lineArray.length != firstLineArray.length) {
                    continue;
                }

                String pureZipCode = lineArray[zipCodeIndex];
                String pureMarketValue = lineArray[marketValueIndex];
                String pureTotalLivableArea = lineArray[totalLivableAreaIndex];

                String zipCode = processZipCode(pureZipCode);
                int marketValue = Integer.parseInt(pureMarketValue);
                int totalLivableArea = Integer.parseInt(pureTotalLivableArea);

                Property property = new Property(zipCode, marketValue, totalLivableArea);
                properties.add(property);

                if(zipCode != null) {
                    List<Property> l = propertiesByZipCode.get(zipCode);
                    if(l == null) {
                        l = new ArrayList<>();
                        propertiesByZipCode.put(zipCode, l);
                    }
                    l.add(property);
                }

            }

        }
    }

    private int findIndex(String[] array, String target) {
        for(int i=0; i< array.length; i++) {
            if(target.equals(array[i].trim())) {
                return i;
            }
        }
        throw new IllegalArgumentException("Data type not found in first line of properties file.");
    }

    private String processZipCode(String zip) {
        if(zip == null) {
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

    private Integer parsePositiveIntOrNull(String s) {
        if (s == null) return null;
        s = s.trim();
        if (s.isEmpty()) return null;
        try {
            double asDouble = Double.parseDouble(s);
            if (asDouble <= 0) return null;
            // data often has ".0", so round to nearest int
            int value = (int) Math.round(asDouble);
            return value > 0 ? value : null;
        } catch (NumberFormatException e) {
            return null; //"dog", etc...
        }
    }

    public List<Property> getProperties() {
        return properties;
    }

    public List<Property> getPropertiesByZipCode(String zip) {
        if(zip == null || zip.isEmpty()) {
            return Collections.EMPTY_LIST;
        }

        List<Property> list = propertiesByZipCode.get(zip);
        if(list == null) {
            return Collections.EMPTY_LIST;
        }

        return list;
    }

}