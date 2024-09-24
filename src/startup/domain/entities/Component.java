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
    private List<Material> materials;
    private List<Labor> labors;


    public Component(long id, ComponentType type, String componentName, double transportCost, Project project) {
        this();
        this.id = id;
        this.componentType = type;
        this.componentName = componentName;
        this.transportCost = transportCost;
        this.project = project;
        this.cost = calculateCost();

    }

    public Component() {
        this.materials = new ArrayList<>();
        this.labors = new ArrayList<>();
    }

    public abstract double calculateCost();

    // Getters and setters for each field
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ComponentType getComponentType() {
        return componentType;
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
        if (cost < 0) {
            throw new IllegalArgumentException("Cost cannot be negative.");
        }
        this.cost = cost;
    }

    public double getTransportCost() {
        return transportCost;
    }

    public void setTransportCost(double transportCost) {
        if (transportCost < 0) {
            throw new IllegalArgumentException("Transport cost cannot be negative.");
        }
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

    public List<Material> getMaterials() { return materials; }

    public void setMaterials(List<Material> materials) {
        this.materials = materials;
    }

    public List<Labor> getLabors() {
        return labors;
    }

    public void setLabors(List<Labor> labors) {
        this.labors = labors;
    }

    public void addMaterial(Material material) {
        this.materials.add(material);
        calculateCost();
    }

    public void addLabors(Labor labor) { this.labors.add(labor);
    calculateCost();}

    @Override
    public String toString() {
        return "Component{\n" +
                "  id=" + id + ",\n" +
                "  componentName='" + componentName + '\'' + ",\n" +
                "  cost=" + cost + ",\n" +
                "  transportCost=" + transportCost + ",\n" +
                "  taxType=" + taxType + ",\n" +
                "  project=" + project + "\n" +
                '}';
    }





}
