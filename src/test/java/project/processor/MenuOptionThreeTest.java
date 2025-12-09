package project.processor;

import org.junit.jupiter.api.Test;
import project.common.Property;
import project.data.PropertyData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/*
constructorRejectsNull
*Checks IllegalArgumentException when PropertyData is null.

averageMarketValueNormalAndMemoized
*For ZIP 19104, two properties with market values 100 and 300.
*First call returns 200 (average).
*Second call returns the same value via memoization branch (memoMap.containsKey(zip)).

averageMarketValueEmptyOrInvalid
*Empty list for one ZIP → method returns 0.
*List with null, 0, and negative values → also returns 0.
*Covers branches where list is empty, and where all entries are invalid (value <= 0 and count == 0 / sum == 0).
 */

public class MenuOptionThreeTest {

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
    public void constructorRejectsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new MenuOptionThree(null));
    }

    @Test
    public void averageMarketValueNormalAndMemoizedWithRounding() throws Exception {
        FakePropertyData data = new FakePropertyData();
        // values: 100 and 101 -> avg 100.5 -> rounded to 101
        List<Property> list = Arrays.asList(
                new Property("19104", 100, 1000),
                new Property("19104", 101, 2000)
        );
        data.setProperties("19104", list);

        MenuOptionThree option = new MenuOptionThree(data);

        int first = option.findAverageResidentalMarketValue("19104");
        assertEquals(101, first);

        // second call hits memoization branch
        int second = option.findAverageResidentalMarketValue("19104");
        assertEquals(101, second);
    }

    @Test
    public void averageMarketValueEmptyOrInvalid() throws Exception {
        FakePropertyData data = new FakePropertyData();
        MenuOptionThree option = new MenuOptionThree(data);

        data.setProperties("00000", Collections.emptyList());
        assertEquals(0, option.findAverageResidentalMarketValue("00000"));

        List<Property> invalidList = Arrays.asList(
                null,
                new Property("19103", 0, 1000),
                new Property("19103", -5, 1000)
        );
        data.setProperties("19103", invalidList);
        assertEquals(0, option.findAverageResidentalMarketValue("19103"));
    }
}
