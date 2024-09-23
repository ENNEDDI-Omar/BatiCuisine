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
    List<Component> components;

    public Project() {

    }

    public Project(Long id, String projectName, double surface, Client client) {
        this.id = id;
        this.projectName = ValidationUtils.validateProjectName(projectName);
        this.surface = ValidationUtils.validateSurface(surface);
        this.client = client;
        this.components = new ArrayList<>();
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

    public List<Component> getComponents() {
        return components;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }

    public double getSurface() {

        return surface;
    }

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


    private void updateTotalCost() {
        this.totalCost = calculateTotalCost();
    }

    private void initializeProfitMargin() {
        if (this.client != null) {
            this.profitMargin = client.isProfessional() ? ProfitMarginType.COMPANY : ProfitMarginType.INDIVIDUAL;
        } else {
            this.profitMargin = ProfitMarginType.INDIVIDUAL;
        }
    }



}
