package project.processor;

import org.junit.jupiter.api.Test;
import project.common.ParkingViolation;
import project.data.ParkingViolationData;
import project.data.PopulationData;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/*
constructorRejectsNull
*Ensures constructor throws IllegalArgumentException if either ParkingViolationData or PopulationData is null.

finesPerCapitaNormalCase
*Fake parking data: two PA violations in ZIP 19104 (30 + 20).
*Fake population data: ZIP 19104 has population 10.
*Asserts map has one entry and value is 5.0 (50/10), covering happy-path aggregation and division.

finesPerCapitaSkipsZeroPopAndZeroFine
*One ZIP with total fine 0, one ZIP with population 0.
*Asserts result map is empty, covering the fine <= 0 and pop <= 0 skip branches.
 */

public class MenuOptionTwoTest {

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

    static class FakeParkingViolationData extends ParkingViolationData {
        private Iterable<ParkingViolation> iterable = Collections.emptyList();

        void setIterable(Iterable<ParkingViolation> iterable) {
            this.iterable = iterable;
        }

        @Override
        public Iterable<ParkingViolation> paViolationsWithZipIterable() {
            return iterable;
        }
    }

    @Test
    public void constructorRejectsNull() throws Exception {
        FakeParkingViolationData p = new FakeParkingViolationData();
        FakePopulationData pop = new FakePopulationData();

        assertThrows(IllegalArgumentException.class,
                () -> new MenuOptionTwo(null, pop));
        assertThrows(IllegalArgumentException.class,
                () -> new MenuOptionTwo(p, null));
    }

    @Test
    public void finesPerCapitaAggregatesAndDivides() throws Exception {
        FakeParkingViolationData p = new FakeParkingViolationData();
        FakePopulationData pop = new FakePopulationData();

        // 19104 total fine = 5 + 10 = 15, pop = 4 => 3.75
        // 19103 total fine = 20, pop = 5 => 4.0
        List<ParkingViolation> list = Arrays.asList(
                new ParkingViolation("t1", 5, "d1", "v1", "PA", "id1", "19104"),
                new ParkingViolation("t2", 10, "d2", "v2", "PA", "id2", "19104"),
                new ParkingViolation("t3", 20, "d3", "v3", "PA", "id3", "19103")
        );
        p.setIterable(list);
        pop.put("19104", 4);
        pop.put("19103", 5);

        MenuOptionTwo option = new MenuOptionTwo(p, pop);
        Map<String, Double> result = option.findFinesPerCapita();

        assertEquals(2, result.size());
        assertEquals(3.75, result.get("19104"), 1e-9);
        assertEquals(4.0, result.get("19103"), 1e-9);
    }

    @Test
    public void finesPerCapitaSkipsZeroFineOrZeroPopulation() throws Exception {
        FakeParkingViolationData p = new FakeParkingViolationData();
        FakePopulationData pop = new FakePopulationData();

        List<ParkingViolation> list = Arrays.asList(
                new ParkingViolation("t1", 0, "d1", "v1", "PA", "id1", "00000"),
                new ParkingViolation("t2", 40, "d2", "v2", "PA", "id2", "11111")
        );
        p.setIterable(list);

        pop.put("00000", 100); // fine 0 -> skipped
        pop.put("11111", 0);   // pop 0 -> skipped

        MenuOptionTwo option = new MenuOptionTwo(p, pop);
        Map<String, Double> result = option.findFinesPerCapita();

        assertTrue(result.isEmpty());
    }
}
