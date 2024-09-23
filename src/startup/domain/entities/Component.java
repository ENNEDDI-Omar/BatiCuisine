package startup.domain.entities;

import startup.domain.enums.RateTaxType;

import java.util.ArrayList;
import java.util.List;

abstract class Component
{
    private long id;
    private String componentName;
    private double cost;
    private double transportCost;
    private RateTaxType taxType;
    private Project project;
    private List<Material> materials = new ArrayList<>();
    private List<Labor> labors = new ArrayList<>();


    public Component(long id, String componentName, double transportCost) {
        this.id = id;
        this.componentName = componentName;
        this.transportCost = transportCost;

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
    }

    public void addLabors(Labor labor) {
        this.labors.add(labor);
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
