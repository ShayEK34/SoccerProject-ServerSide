package ExternalSystems;

public class proxyTax implements TaxSystem {

    private boolean isConnect = false;

    public void setConnect(boolean connect) {
        isConnect = connect;
    }

    @Override
    public double getTaxRate(double revenueAmount) {
        Tax realTaxSystem = new Tax();
        return realTaxSystem.getTaxRate(revenueAmount);
    }
}



