package project.processor;

import org.junit.jupiter.api.Test;
import project.data.PopulationData;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/*
constructorRejectsNull
*Verifies the constructor throws IllegalArgumentException when PopulationData is null.
*Covers the constructor’s defensive null check.

findTotalPopulationUsesData
*Uses a fake PopulationData with two ZIPs (10 + 20).
*Asserts findTotalPopulation() returns 30, proving it just delegates to the data object.
 */

public class MenuOptionOneTest {

    // Stub data class – all data is in memory
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

    @Test
    public void constructorRejectsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new MenuOptionOne(null));
    }

    @Test
    public void findTotalPopulationUsesAllEntries() throws Exception {
        FakePopulationData data = new FakePopulationData();
        data.put("19104", 10);
        data.put("19103", 20);
        data.put("19102", 0); // zero should just be included in sum

        MenuOptionOne option = new MenuOptionOne(data);
        assertEquals(30L, option.findTotalPopulation());
    }
}
