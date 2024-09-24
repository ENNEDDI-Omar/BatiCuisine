package startup.domain.entities;

import startup.domain.enums.ProfitMarginType;
import startup.domain.enums.ProjectStatusType;
import startup.utils.ValidationUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class Project
{
    private Long id;
    private String projectName;
    private ProfitMarginType profitMargin;
    private double totalCost;
    private ProjectStatusType projectStatus;
    private double surface;
    private Client client;
    private List<Material> materials;
    private List<Labor> labors;

    public Project() {

    }

    public Project(Long id, String projectName, double surface, Client client) {
        this.id = id;
        this.projectName = ValidationUtils.validateProjectName(projectName);
        this.surface = ValidationUtils.validateSurface(surface);
        this.client = client;
        this.projectStatus = ProjectStatusType.CREATED;
        this.materials = new ArrayList<>();
        this.labors = new ArrayList<>();
        initializeProfitMargin();
        this.totalCost = calculateTotalCost();
    }



    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = ValidationUtils.validateProjectName(projectName);
    }

    public ProfitMarginType getProfitMargin() {
        return profitMargin;
    }

    public void setProfitMargin(ProfitMarginType profitMargin) {
        this.profitMargin = profitMargin;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = ValidationUtils.validateTotalCost(totalCost);
    }

    public ProjectStatusType getStatus() {
        return projectStatus;
    }

    public void setStatus(ProjectStatusType status) {
        this.projectStatus = status;
    }

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

    public void addLabors(Labor labor) { this.labors.add(labor);}

    public double getSurface() {return surface;}

    public void setSurface(double surface) {
        this.surface = ValidationUtils.validateSurface(surface);
    }

    public void addComponent(Component component) {
        if (component == null) {
            throw new IllegalArgumentException("Cannot add a null component.");
        }
        components.add(component);
        updateTotalCost();
    }

    public void removeComponent(Component component) {
        components.remove(component);
        updateTotalCost();
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", projectName='" + projectName + '\'' +
                ", profitMargin=" + profitMargin +
                ", totalCost=" + totalCost +
                ", projectStatus=" + projectStatus +
                ", surface=" + surface +
                ", client=" + client +
                '}';
    }


    public double calculateTotalCost() {
        double componentCosts = components.stream().mapToDouble(Component::getCost).sum();
        this.totalCost = componentCosts;
        return this.totalCost;
    }


    public void updateTotalCost() {
        double materialCost = materials.stream().mapToDouble(Material::calculateCost).sum();
        double laborCost = labors.stream().mapToDouble(Labor::calculateCost).sum();

        if (!materials.isEmpty() && !labors.isEmpty()) {
            totalCost = (materialCost + laborCost) * (1 + TaxRateType.TAX_COMBINED.getRate());
        } else {
            totalCost = materialCost + laborCost; // Taxation individuelle déjà appliquée dans chaque calcul
        }
    }


    private void initializeProfitMargin() {
        if (this.client != null) {
            this.profitMargin = client.isProfessional() ? ProfitMarginType.COMPANY : ProfitMarginType.INDIVIDUAL;
        } else {
            this.profitMargin = ProfitMarginType.INDIVIDUAL;
        }
    }



}
