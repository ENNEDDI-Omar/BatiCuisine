package startup.domain.enums;

public enum QualityCoefficientType
{
    STANDARD(1.1),
    PREMIUM(1.4);

    private final double qualityCoefficientValue;

    QualityCoefficientType(double value)
    {
        qualityCoefficientValue = value;
    }

    public double getQuality() {
        return qualityCoefficientValue;
    }

    @Override
    public String toString() {
        return name() + "{coefficient=" + qualityCoefficientValue + "}";
    }

}
