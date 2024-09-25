package startup.domain.entities;

import startup.domain.enums.ComponentType;
import startup.domain.enums.LaborType;
import startup.domain.enums.ProductivityLevelType;
import startup.domain.enums.RateTaxType;

public class Labor extends Component {
    private LaborType laborType;
    private double workHours;
    private ProductivityLevelType productivityLevel;

    public Labor(Long id, String componentName, double transportCost, LaborType type, double workHours, ProductivityLevelType productivity, Project project) {
        super(id, ComponentType.LABOR, componentName, RateTaxType.LABOR_TAX_ONLY, transportCost, project);
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
        return "Labor{\n" +
                "  id=" + getId() + ",\n" +
                "  componentName='" + getComponentName() + '\'' + ",\n" +
                "  cost=" + getCost() + ",\n" +
                "  transportCost=" + getTransportCost() + ",\n" +
                "  taxType=" + getTaxType() + ",\n" +
                "  type=" + laborType + ",\n" +
                "  workHours=" + workHours + ",\n" +
                "  productivityLevel=" + productivityLevel + "\n" +
                '}';
    }

}
