package startup.domain.entities;

import startup.domain.enums.LaborType;
import startup.domain.enums.ProductivityLevelType;

public class Labor extends Component {
    private LaborType laborType;
    private double workHours;
    private ProductivityLevelType productivityLevel;

    public Labor(long id, String componentName, double transportCost, LaborType type, double workHours, ProductivityLevelType productivityLevel) {
        super(id, componentName, transportCost);
        this.laborType = type;
        this.workHours = workHours;
        this.productivityLevel = productivityLevel;
    }

    public Labor() {
        super();
    }

    // Getters and setters
    public LaborType getLaborType() {
        return laborType;
    }

    public void setType(LaborType type) {
        this.laborType = type;
    }

    public double getWorkHours() {
        return workHours;
    }

    public void setWorkHours(double workHours) {
        this.workHours = workHours;
    }

    public ProductivityLevelType getProductivityLevel() {
        return productivityLevel;
    }

    public void setProductivityLevel(ProductivityLevelType productivityLevel) {
        this.productivityLevel = productivityLevel;
    }

    @Override
    public String toString() {
        return "Labor{" +
                "id=" + getId() +
                ", componentName='" + getComponentName() + '\'' +
                ", cost=" + getCost() +
                ", transportCost=" + getTransportCost() +
                ", taxType=" + getTaxType() +
                ", type=" + laborType +
                ", workHours=" + workHours +
                ", productivityLevel=" + productivityLevel +
                '}';
    }
}
