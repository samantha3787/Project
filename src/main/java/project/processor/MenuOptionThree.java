package project.processor;
import project.data.PropertyData;
import project.common.Property;
import project.data.PopulationData;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class MenuOptionThree {

    private final PropertyData propertyData;
    private final Map<String, Integer> memoMap = new HashMap<>();

    public MenuOptionThree(PropertyData propertyData, PopulationData populationData) {
        this.propertyData = propertyData;
    }

    public int findAverageResidentalMarketValue(String zip) {

        if(memoMap.containsKey(zip)) {
            return memoMap.get(zip);
        }

        List<Property> list = propertyData.getPropertiesByZipCode(zip);

        if (list == null || list.isEmpty()) {
            return 0;
        }

        int sum = 0;
        int count = 0;

        for (Property p : list) {
            if (p == null) {
                continue;
            }
            int value = p.getMarketValue();
            if (value <= 0) {
                continue;
            }

            sum += value;
            count++;
        }

        if (count == 0) {
            return 0;
        }

        double avg = (double) sum / count;
        int result = (int) Math.round(avg);
        memoMap.put(zip, result);
        return result;
    }


}
