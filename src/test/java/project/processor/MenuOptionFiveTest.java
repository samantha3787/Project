package project.processor;

import org.junit.jupiter.api.Test;
import project.common.Property;
import project.data.PopulationData;
import project.data.PropertyData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/*
constructorRejectsNull
*Verifies IllegalArgumentException thrown if either PropertyData or PopulationData is null.

marketValuePerCapitaNormalCase
*ZIP 19104: two properties with market values 100000 and 300000 (total 400000), population 200.
*Asserts result is 2000 (400000 / 200), hitting standard path.

marketValuePerCapitaZeroPopOrNoProperties
*Case 1: population 0 → returns 0.
*Case 2: empty property list → returns 0.
*Covers the pop <= 0 and list == null || list.isEmpty() branches.

marketValuePerCapitaAllInvalidProperties
*Properties are null, 0, and negative values.
*Population positive, but all properties skipped → sum == 0 / count == 0, returns 0.
 */


public class MenuOptionFiveTest {

    static class FakePopulationData extends PopulationData {

        private final Map<String, Integer> map = new HashMap<>();

        FakePopulationData() throws IOException {
            super(createEmptyTempFile());
        }

        private static String createEmptyTempFile() throws IOException {
            File f = File.createTempFile("pop", ".txt");
            return f.getAbsolutePath();
        }

        void put(String zip, int pop) {
            map.put(zip, pop);
        }

        @Override
        public int getPopulation(String zipCode) {
            return map.getOrDefault(zipCode, 0);
        }

        @Override
        public Map<String, Integer> getAllPopulationData() {
            return map;
        }

        @Override
        public long getTotalPopulation() {
            return map.values().stream().mapToLong(v -> v).sum();
        }
    }

    static class FakePropertyData extends PropertyData {
        private final Map<String, List<Property>> byZip = new HashMap<>();

        FakePropertyData() throws IOException {
            super(createHeaderOnlyFile());
        }

        private static String createHeaderOnlyFile() throws IOException {
            File f = File.createTempFile("props", ".csv");
            try (PrintWriter out = new PrintWriter(new FileWriter(f))) {
                out.println("zip_code,market_value,total_livable_area");
            }
            return f.getAbsolutePath();
        }

        void setProperties(String zip, List<Property> list) {
            byZip.put(zip, list);
        }

        @Override
        public List<Property> getPropertiesByZipCode(String zip) {
            List<Property> list = byZip.get(zip);
            return list == null ? Collections.emptyList() : list;
        }
    }

    @Test
    public void constructorRejectsNull() throws Exception {
        FakePropertyData prop = new FakePropertyData();
        FakePopulationData pop = new FakePopulationData();

        assertThrows(IllegalArgumentException.class,
                () -> new MenuOptionFive(null, pop));
        assertThrows(IllegalArgumentException.class,
                () -> new MenuOptionFive(prop, null));
    }

    @Test
    public void marketValuePerCapitaNormalCaseWithRounding() throws Exception {
        FakePropertyData prop = new FakePropertyData();
        FakePopulationData pop = new FakePopulationData();

        // total market value = 1 + 2 = 3, pop = 2 -> 1.5 -> round to 2
        prop.setProperties("19104", Arrays.asList(
                new Property("19104", 1, 1000),
                new Property("19104", 2, 2000)
        ));
        pop.put("19104", 2);

        MenuOptionFive option = new MenuOptionFive(prop, pop);
        assertEquals(2, option.findResidentialMarketValuePerCapita("19104"));
    }

    @Test
    public void marketValuePerCapitaZeroPopOrNoProperties() throws Exception {
        FakePropertyData prop = new FakePropertyData();
        FakePopulationData pop = new FakePopulationData();
        MenuOptionFive option = new MenuOptionFive(prop, pop);

        prop.setProperties("19100", Arrays.asList(
                new Property("19100", 100000, 1000)
        ));
        pop.put("19100", 0);
        assertEquals(0, option.findResidentialMarketValuePerCapita("19100"));

        prop.setProperties("19101", Collections.emptyList());
        pop.put("19101", 100);
        assertEquals(0, option.findResidentialMarketValuePerCapita("19101"));
    }

    @Test
    public void marketValuePerCapitaAllInvalidProperties() throws Exception {
        FakePropertyData prop = new FakePropertyData();
        FakePopulationData pop = new FakePopulationData();
        MenuOptionFive option = new MenuOptionFive(prop, pop);

        prop.setProperties("19102", Arrays.asList(
                null,
                new Property("19102", 0, 1000),
                new Property("19102", -5, 2000)
        ));
        pop.put("19102", 100);

        assertEquals(0, option.findResidentialMarketValuePerCapita("19102"));
    }
}
