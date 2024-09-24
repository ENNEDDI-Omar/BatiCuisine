package startup.domain.entities;

import startup.domain.enums.ComponentType;
import startup.domain.enums.QualityCoefficientType;

public class Material extends Component {
    private double unitPrice;
    private double quantity;
    private QualityCoefficientType qualityCoefficient;

    public Material(long id, ComponentType type, String componentName, double transportCost, double unitPrice, double quantity, QualityCoefficientType quality, Project project) {
        super(id, type, componentName, transportCost, project);
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.qualityCoefficient = quality;
        setCost(calculateCost());
    }

    public Material() {
        super();
    }

    public double calculateCost() {
        return (unitPrice * quantity * qualityCoefficient.getQuality()) + getTransportCost();
    }

    // Getters and setters
    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
        super.setCost(calculateCost());
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
        super.setCost(calculateCost());
    }

    public QualityCoefficientType getQualityCoefficient() {
        return qualityCoefficient;
    }

    public void setQualityCoefficient(QualityCoefficientType qualityCoefficient) {
        this.qualityCoefficient = qualityCoefficient;
        super.setCost(calculateCost());
    }

    @Override
    public String toString() {
        return "Material{\n" +
                "  id=" + getId() + ",\n" +
                "  componentName='" + getComponentName() + '\'' + ",\n" +
                "  cost=" + getCost() + ",\n" +
                "  transportCost=" + getTransportCost() + ",\n" +
                "  taxType=" + getTaxType() + ",\n" +
                "  unitPrice=" + unitPrice + ",\n" +
                "  quantity=" + quantity + ",\n" +
                "  qualityCoefficient=" + qualityCoefficient + "\n" +
                '}';
    }

}
