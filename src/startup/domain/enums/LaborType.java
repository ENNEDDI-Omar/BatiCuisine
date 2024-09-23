package startup.domain.enums;

public enum LaborType
{
    WORKER(18),
    SPECIALIST(26);

    private final int payementRate;

    LaborType(int payementRate)
    {
        this.payementRate = payementRate;
    }

    public int getPayementRate() {
            return payementRate;
    }

    @Override
    public String toString()
    {
        return name() + "{ Payement Rate = " + payementRate + "}" ;
    }
}
