package ExternalSystems;

public class proxyAccounting implements AccountingSystem {
    private boolean isConnect = false;

    public void setConnect(boolean connect) {
        isConnect = connect;
    }

    @Override
    public boolean addPayment(String teamName, String date, double amount) {
        Accounting realAccountingSystem = new Accounting();
        return realAccountingSystem.addPayment(teamName,date,amount);
    }
}

