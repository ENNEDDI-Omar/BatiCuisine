package startup.domain.entities;

import startup.domain.enums.QuotesStatusType;
import startup.exceptions.ProjectNotFoundException;
import startup.utils.ValidationUtils;

import java.time.LocalDate;

public class Quotes
{
    private Long id;
    private double estimatedAmount;
    private QuotesStatusType status;
    private LocalDate issueDate;
    private LocalDate expirationDate;
    private Project project;

    public Quotes(Long id, LocalDate issueDate, LocalDate expirationDate, Project project)
    {
        this.id = id;
        this.status = QuotesStatusType.REQUESTED;
        setIssueDate(issueDate);
        setExpirationDate(expirationDate);
        setProject(project);
        this.estimatedAmount = calculateEstimatedAmount();

    }

    public Quotes(){}

    public double calculateEstimatedAmount() {
        if (project == null) {
            throw new ProjectNotFoundException("Project must not be null to calculate estimated amount.");
        }
        return project.getTotalCost() * (1 + project.getProfitMargin().getMargin());
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getEstimatedAmount() {
        return estimatedAmount;
    }

    public void setEstimatedAmount(double estimatedAmount) {
        this.estimatedAmount = ValidationUtils.validateEstimatedAmount(estimatedAmount);
    }

    public QuotesStatusType getStatus() {
        return status;
    }

    public void setStatus(QuotesStatusType status) {
        this.status = status;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = ValidationUtils.validateIssueDate(issueDate, this.expirationDate);
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = ValidationUtils.validateExpirationDate(expirationDate, this.issueDate);
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Override
    public String toString() {
        return "Quotes{" +
                "id=" + id +
                ", estimatedAmount=" + estimatedAmount +
                ", status=" + status +
                ", issueDate=" + issueDate +
                ", expirationDate=" + expirationDate +
                ", project=" + project.getProjectName() +
                '}';
    }
}
