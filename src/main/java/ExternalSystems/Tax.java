package ExternalSystems;

public class Tax implements TaxSystem {

    public Tax() {
    }

    @Override
    public double getTaxRate(double revenueAmount) {
        return 0.17;
    }

}

