package project.processor;

import org.junit.jupiter.api.Test;
import project.common.Property;
import project.data.PropertyData;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/*
constructorRejectsNull
*Ensures constructor throws IllegalArgumentException when PropertyData is null.

averageLivableAreaNormalCase
*ZIP 19104: areas 1000 and 3000 → average 2000.
*Covers normal loop behavior and rounding.

averageLivableAreaEmptyOrInvalid
*Empty list for one ZIP → returns 0.
*List with null, 0, and negative areas → returns 0.
*Exercises branches where all properties are skipped (area <= 0 and count == 0 / sum == 0).
 */

public class MenuOptionFourTest {

    static class FakePropertyData extends MenuOptionThreeTest.FakePropertyData {
        FakePropertyData() throws IOException {
            super();
        }
    }

    @Test
    public void constructorRejectsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new MenuOptionFour(null));
    }

    @Test
    public void averageLivableAreaNormalCaseWithRounding() throws Exception {
        FakePropertyData data = new FakePropertyData();
        // areas: 1001 and 1002 -> avg 1001.5 -> round to 1002
        data.setProperties("19104", Arrays.asList(
                new Property("19104", 100, 1001),
                new Property("19104", 200, 1002)
        ));
        MenuOptionFour option = new MenuOptionFour(data);

        assertEquals(1002, option.findAverageResidentialTotalLivableArea("19104"));
    }

    @Test
    public void averageLivableAreaEmptyOrInvalid() throws Exception {
        FakePropertyData data = new FakePropertyData();
        MenuOptionFour option = new MenuOptionFour(data);

        data.setProperties("00000", Collections.emptyList());
        assertEquals(0, option.findAverageResidentialTotalLivableArea("00000"));

        data.setProperties("19103", Arrays.asList(
                null,
                new Property("19103", 100, 0),
                new Property("19103", 100, -10)
        ));
        assertEquals(0, option.findAverageResidentialTotalLivableArea("19103"));
    }
}
