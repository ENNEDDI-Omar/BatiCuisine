package startup.domain.entities;

import startup.domain.enums.ComponentType;
import startup.domain.enums.QualityCoefficientType;
import startup.domain.enums.RateTaxType;

public class Material extends Component {
    private double unitPrice;
    private double quantity;
    private QualityCoefficientType qualityCoefficient;

    public Material(long id, String componentName, double transportCost, double unitPrice, double quantity, QualityCoefficientType qualityCoefficient, Project project) {
        super(id, ComponentType.MATERIAL, componentName, RateTaxType.MATERIAL_TAX_ONLY, transportCost, project);
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.qualityCoefficient = (qualityCoefficient != null) ? qualityCoefficient : QualityCoefficientType.STANDARD;
        setCost(calculateCost());
    }

    public Material() {
        super(0L, ComponentType.MATERIAL, "", RateTaxType.MATERIAL_TAX_ONLY, 0.0, null);
        this.unitPrice = 0.0;
        this.quantity = 0.0;
        this.qualityCoefficient = QualityCoefficientType.STANDARD;
    }

    @Override
    public double calculateCost() {
        if (qualityCoefficient == null) {
            qualityCoefficient = QualityCoefficientType.STANDARD;
        }
        return (unitPrice * quantity * qualityCoefficient.getQuality()) + getTransportCost();
    }

    // Getters et setters
    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
        setCost(calculateCost());
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
        setCost(calculateCost());
    }

    public QualityCoefficientType getQualityCoefficient() {
        return qualityCoefficient;
    }

    public void setQualityCoefficient(QualityCoefficientType qualityCoefficient) {
        this.qualityCoefficient = (qualityCoefficient != null) ? qualityCoefficient : QualityCoefficientType.STANDARD;
        setCost(calculateCost());
    }

    @Override
    public String toString() {
        return "Material{" +
                "id=" + getId() +
                ", componentName='" + getComponentName() + '\'' +
                ", cost=" + getCost() +
                ", transportCost=" + getTransportCost() +
                ", taxType=" + getTaxType() +
                ", unitPrice=" + unitPrice +
                ", quantity=" + quantity +
                ", qualityCoefficient=" + qualityCoefficient +
                '}';
    }
}