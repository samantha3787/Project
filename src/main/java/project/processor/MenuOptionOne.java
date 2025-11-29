package project.processor;

import project.data.PopulationData;

public class MenuOptionOne {

    private final PopulationData data;

    public MenuOptionOne(PopulationData data) {
        this.data = data;
    }

    public long findTotalPopulation() {
        return data.getTotalPopulation();
    }

}