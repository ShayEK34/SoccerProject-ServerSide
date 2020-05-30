package ExternalSystems;

public class Accounting implements AccountingSystem {

    public Accounting() {
    }

    @Override
    public boolean addPayment(String teamName, String date, double amount) {
        if (amount>0) {
            return true;
        }
        return false;
    }
}

