package project.processor;
import project.data.PropertyData;
import project.common.Property;
import project.data.PopulationData;
import java.util.List;

public class MenuOptionFive {

    private final PropertyData propertyData;
    private final PopulationData populationData;

    public MenuOptionFive(PropertyData propertyData, PopulationData populationData) {
        this.propertyData = propertyData;
        this.populationData = populationData;
    }

    public int findResidentalMarketValuePerCapita(String zip) {
        int pop = populationData.getPopulation(zip);
        if (pop == 0) {
            return 0;
        }

        List<Property> list = propertyData.getPropertiesByZipCode(zip);

        if (list == null || list.isEmpty()) {
            return 0;
        }




    }


}
