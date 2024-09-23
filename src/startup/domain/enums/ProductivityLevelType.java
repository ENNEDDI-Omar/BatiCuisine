package startup.domain.enums;


public enum ProductivityLevelType {
    NORMAL(0.79),
    HIGH(0.96);

    private final double coefficient; // Coefficient de productivité


    ProductivityLevelType(double coefficient) {
        this.coefficient = coefficient;
    }


    public double getCoefficient() {
        return coefficient;
    }

    @Override
    public String toString()
    {
        return name() + "{ Productivity Coefficient: " + coefficient + "}";
    }
}
