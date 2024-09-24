package startup.domain.entities;

import startup.domain.enums.ComponentType;
import startup.domain.enums.RateTaxType;

import java.util.ArrayList;
import java.util.List;

public abstract class Component
{
    private long id;
    private ComponentType componentType;
    private String componentName;
    private double cost;
    private double transportCost;
    private RateTaxType taxType;
    private Project project;


    public Component(long id, String type, String componentName, double transportCost, Project project) {
        this.id = id;
        this.componentType = ComponentType.valueOf(type);
        this.componentName = componentName;
        this.transportCost = transportCost;
        this.project = project;
        this.cost = calculateCost();

    }

    public Component()
    {}

    // Getters and setters for each field
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getComponentType() {
        return componentType.toString();
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

    public Project getProject() { return project; }

    public void setProject(Project project) { this.project = project; }



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

    public abstract double calculateCost();
}
