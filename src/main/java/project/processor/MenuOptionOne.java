package project.processor;

import project.data.PopulationData;

public class MenuOptionOne {

    private final PopulationData data;

    public MenuOptionOne(PopulationData data) {
        if(data == null) {
            throw new IllegalArgumentException("Population data cannot be null.");
        }
        this.data = data;
    }

    public long findTotalPopulation() {
        return data.getTotalPopulation();
    }

}