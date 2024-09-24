package startup.domain.entities;

import startup.domain.enums.ComponentType;
import startup.domain.enums.ProfitMarginType;
import startup.domain.enums.ProjectStatusType;
import startup.domain.enums.RateTaxType;
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
    private final List<Component> components;


    public Project() {
      this.components = new ArrayList<>();
    }

    public Project(Long id, String projectName, double surface, Client client) {
        this();
        this.id = id;
        this.projectName = ValidationUtils.validateProjectName(projectName);
        this.surface = ValidationUtils.validateSurface(surface);
        this.client = client;
        this.projectStatus = ProjectStatusType.CREATED;
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

    public double getSurface() {return surface;}

    public void setSurface(double surface) {
        this.surface = ValidationUtils.validateSurface(surface);
    }

    public void addComponent(Component component) {
        if (component == null) {
            throw new IllegalArgumentException("Cannot add a null component.");
        }
        components.add(component);
        calculateTotalCost();
    }

    public void removeComponent(Component component) {
        components.remove(component);
        //updateTotalCost();
    }

    @Override
    public String toString() {
        return "Project{\n" +
                "  id=" + id + ",\n" +
                "  projectName='" + projectName + '\'' + ",\n" +
                "  profitMargin=" + profitMargin + ",\n" +
                "  totalCost=" + totalCost + ",\n" +
                "  projectStatus=" + projectStatus + ",\n" +
                "  surface=" + surface + ",\n" +
                "  client=" + client + ",\n" +
                "  components=" + components + "\n" +
                '}';
    }



    public double calculateTotalCost() {
        double materialCost = components.stream()
                .filter(c -> c.getComponentType() == ComponentType.MATERIAL)
                .mapToDouble(Component::calculateCost)
                .sum();

        double laborCost = components.stream()
                .filter(c -> c.getComponentType() == ComponentType.LABOR)
                .mapToDouble(Component::calculateCost)
                .sum();

        // Calculer la remise en fonction de la surface
        double discountMultiplier = 1 - (Math.floor(this.surface / 500) * 0.05); // Réduction de 5% pour chaque tranche de 500 m²

        if (discountMultiplier < 0) {
            discountMultiplier = 0;
        }

        if (components.stream().anyMatch(c -> c.getComponentType() == ComponentType.MATERIAL) &&
                components.stream().anyMatch(c -> c.getComponentType() == ComponentType.LABOR)) {
            totalCost = (materialCost + laborCost) * discountMultiplier * (1 + RateTaxType.TAX_COMBINED.getRate());
        } else if (components.stream().anyMatch(c -> c.getComponentType() == ComponentType.MATERIAL)) {
            totalCost = materialCost * discountMultiplier * (1 + RateTaxType.MATERIAL_TAX_ONLY.getRate());
        } else if (components.stream().anyMatch(c -> c.getComponentType() == ComponentType.LABOR)) {
            totalCost = laborCost * discountMultiplier * (1 + RateTaxType.LABOR_TAX_ONLY.getRate());
        }
        return totalCost;
    }


    private void initializeProfitMargin() {
        this.profitMargin = (client != null && client.isProfessional()) ?
                ProfitMarginType.COMPANY : ProfitMarginType.INDIVIDUAL;
    }



}
