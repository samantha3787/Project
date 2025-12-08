package project.processor;

import project.common.Property;
import project.data.PropertyData;
import java.util.List;

public class MenuOptionFour {

    private final PropertyData data;

    public MenuOptionFour(PropertyData data) {
        this.data = data;
    }

    public int findAverageResidentialTotalLivableArea(String zip) {
        List<Property> list = data.getPropertiesByZipCode(zip);
        if (list == null || list.isEmpty()) {
            return 0;
        }

        int sum = 0;
        int count = 0;

        for (Property p : list) {
            if (p == null) {
                continue;
            }
            int area = p.getTotalLivableArea();

            if (area <= 0) {
                continue;
            }

            sum += area;
            count++;
        }

        if (count == 0) {
            return 0;
        }
        double avg = (double) sum / count;
        return (int) Math.round(avg);
    }

}
