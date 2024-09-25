package startup.domain.enums;

public enum QualityCoefficientType {
    STANDARD(1.1),
    PREMIUM(1.4);

    private final double qualityCoefficientValue;

    QualityCoefficientType(double value) {
        this.qualityCoefficientValue = value;
    }

    public double getQuality() {
        return qualityCoefficientValue;
    }

    public static QualityCoefficientType getDefault() {
        return STANDARD;
    }

    @Override
    public String toString() {
        return name() + "{coefficient=" + qualityCoefficientValue + "}";
    }
}