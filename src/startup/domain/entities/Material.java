package startup.domain.entities;

import startup.domain.enums.RateTaxType;
import startup.domain.enums.QualityCoefficientType;

public class Material extends Component {
    private double unitPrice;
    private double quantity;
    private QualityCoefficientType qualityCoefficient;

    public Material(long id, String componentName, double cost, double transportCost, RateTaxType taxType,
                    double unitPrice, double quantity, QualityCoefficientType qualityCoefficient) {
        super(id, componentName, cost, transportCost, taxType);
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.qualityCoefficient = qualityCoefficient;
    }

    // Getters and setters
    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public QualityCoefficientType getQualityCoefficient() {
        return qualityCoefficient;
    }

    public void setQualityCoefficient(QualityCoefficientType qualityCoefficient) {
        this.qualityCoefficient = qualityCoefficient;
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
