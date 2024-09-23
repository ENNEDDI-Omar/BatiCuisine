package startup.domain.entities;

import startup.domain.enums.RateTaxType;

abstract class Component
{
    private long id;
    private String componentName;
    private double cost;
    private double transportCost;
    private RateTaxType taxType;


    public Component(long id, String componentName, double transportCost) {
        this.id = id;
        this.componentName = componentName;
        this.transportCost = transportCost;

    }

    // Getters and setters for each field
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getTransportCost() {
        return transportCost;
    }

    public void setTransportCost(double transportCost) {
        this.transportCost = transportCost;
    }

    public RateTaxType getTaxType() {
        return taxType;
    }

    public void setTaxType(RateTaxType taxType) {
        this.taxType = taxType;
    }

    @Override
    public String toString() {
        return "Component{" +
                "id=" + id +
                ", componentName='" + componentName + '\'' +
                ", cost=" + cost +
                ", transportCost=" + transportCost +
                ", taxType=" + taxType +
                '}';
    }
}
