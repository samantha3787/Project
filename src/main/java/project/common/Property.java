package project.common;

import java.util.Objects;

public class Property {

    private final String zipCode;
    private final int marketValue;
    private final int totalLivableArea;

    public Property(String zipCode, int marketValue, int totalLivableArea) {
        this.zipCode = zipCode;
        this.marketValue = marketValue;
        this.totalLivableArea = totalLivableArea;
    }

    public String getZipCode() {
        return zipCode;
    }

    public int getMarketValue() {
        return marketValue;
    }

    public int getTotalLivableArea() {
        return totalLivableArea;
    }

    @Override
    public String toString() {
        return "Property: " + "zipCode = " + zipCode + '\'' + "marketValue = " +
                marketValue + '\'' + "totalLivableArea = " + totalLivableArea;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Property)) {
            return false;
        }
        Property p = (Property) o;
        return Objects.equals(zipCode, p.zipCode) && marketValue == p.marketValue &&
                totalLivableArea == p.totalLivableArea;
    }

    @Override
    public int hashCode() {
        return Objects.hash(zipCode, marketValue, totalLivableArea);
    }


}
