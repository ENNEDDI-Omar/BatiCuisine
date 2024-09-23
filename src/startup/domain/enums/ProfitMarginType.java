package startup.domain.enums;

public enum ProfitMarginType {
  INDIVIDUAL(0.22),
  COMPANY(0.32);

  private final double margin;

  ProfitMarginType(double margin) {
    this.margin = margin;
  }

  public double getMargin() {
    return margin;
  }
}

