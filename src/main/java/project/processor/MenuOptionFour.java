package project.processor;

import project.common.Property;
import project.data.PropertyData;
import java.util.List;

public class MenuOptionFour {

    private final PropertyData data;

    public MenuOptionFour(PropertyData data) {
        if(data == null) {
            throw new IllegalArgumentException("Property data cannot be null.");
        }
        this.data = data;
    }

    public int findAverageResidentialTotalLivableArea(String zip) {
        List<Property> list = data.getPropertiesByZipCode(zip);
        if (list == null || list.isEmpty()) {
            return 0;
        }

        long sum = 0L;
        int count = 0;

        for (Property p : list) {
            if (p == null) {
                continue;
            }
            int area = p.getTotalLivableArea();

            if (area <= 0) {
                continue;
            }

            sum += (long) area;
            count++;
        }

        if (count == 0 || sum == 0L) {
            return 0;
        }
        double avg = (double) sum / (double) count;
        return (int) Math.round(avg);
    }

}
