package project.processor;
import project.data.PropertyData;
import project.common.Property;
import project.data.PopulationData;
import java.util.List;

public class MenuOptionFive {

    private final PropertyData propertyData;
    private final PopulationData populationData;

    public MenuOptionFive(PropertyData propertyData, PopulationData populationData) {
        if(propertyData == null || populationData == null) {
            throw new IllegalArgumentException("Property data and population data cannot be null.");
        }
        this.propertyData = propertyData;
        this.populationData = populationData;
    }

    public int findResidentialMarketValuePerCapita(String zip) {
        int pop = populationData.getPopulation(zip);
        if (pop <= 0) {
            return 0;
        }

        List<Property> list = propertyData.getPropertiesByZipCode(zip);

        if (list == null || list.isEmpty()) {
            return 0;
        }

        long sum = 0L;
        int count = 0;

        for (Property p : list) {
            if (p == null) {
                continue;
            }
            int value = p.getMarketValue();
            if (value <= 0) {
                continue;
            }

            sum += (long) value;
            count++;
        }

        if (count == 0 || sum == 0L) {
            return 0;
        }

        double avg = (double) sum / (double) pop;

        return (int) Math.round(avg);
    }


}
