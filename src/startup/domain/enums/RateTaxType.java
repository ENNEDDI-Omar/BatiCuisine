package startup.domain.enums;

public enum RateTaxType
{
    MATERIAL_TAX_ONLY(0.05),
    LABOR_TAX_ONLY(0.09),
    TAX_COMBINED(0.14);

    private final double rate;

    RateTaxType(double rate)
    {
        this.rate = rate;
    }

    public double getRate() {
            return rate;
    }
}