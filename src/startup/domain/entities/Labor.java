package startup.domain.entities;

import startup.domain.enums.LaborType;
import startup.domain.enums.ProductivityLevelType;

public class Labor extends Component {
    private LaborType laborType;
    private double workHours;
    private ProductivityLevelType productivityLevel;

    public Labor(long id, String componentType, String componentName, double transportCost, LaborType type, double workHours, ProductivityLevelType productivity, Project project) {
        super(id, componentType, componentName, transportCost, project);
        this.laborType = type;
        this.workHours = workHours;
        this.productivityLevel = productivity;
        setCost(calculateCost());
    }

    public Labor() {
        super();
    }

    public double calculateCost() {
        return (laborType.getPayementRate() * workHours * productivityLevel.getCoefficient()) + getTransportCost();
    }


    public LaborType getLaborType() {
        return laborType;
    }

    public void setType(LaborType type) {
        this.laborType = type;
        super.setCost(calculateCost());
    }

    public double getWorkHours() {
        return workHours;
    }

    public void setWorkHours(double workHours) {
        this.workHours = workHours;
        super.setCost(calculateCost());
    }

    public ProductivityLevelType getProductivityLevel() {
        return productivityLevel;
    }

    public void setProductivityLevel(ProductivityLevelType productivityLevel) {
        this.productivityLevel = productivityLevel;
        super.setCost(calculateCost());
    }

    @Override
    public String toString() {
        return "Labor{" +
                "id=" + getId() +
                ", componentName='" + getComponentName() + '\'' +
                ", cost=" + getCost() +
                ", transportCost=" + getTransportCost() +
                ", taxType=" + getTaxType() +
                ", type=" + getLaborType() +
                ", workHours=" + workHours +
                ", productivityLevel=" + getProductivityLevel() +
                '}';
    }
}
