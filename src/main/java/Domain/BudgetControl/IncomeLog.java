package Domain.BudgetControl;

import java.util.ArrayList;

/**
 * This class represent the income transactions log of the team.
 */
public class IncomeLog {

    private ArrayList<Transaction> IncomeTransaction;

    /**
     * This constructor create new array list that represent the income transaction log of the team.
     */
    public IncomeLog() {
        IncomeTransaction = new ArrayList <Transaction> ();
    }

    /**
     * This function return the array of the transaction income log.
     * @return - The array of the transaction income log.
     */
    public ArrayList<Transaction> getIncomeTransaction() {
        return IncomeTransaction;
    }

    /**
     * This function return the total number of the transactions in the transaction income log.
     * @return - The total number of the transactions in the transaction income log.
     */
    public int getNumIncTransaction (){
        return this.IncomeTransaction.size();
    }

    /**
     * This function return the total amount of the transaction income log.
     * @return - The total amount of the transaction income log.
     */
    public double getIncomeAmount(){
        double incAmount=0;
        for(int i = 0; i< this.getNumIncTransaction();i++){
            incAmount = incAmount + this.IncomeTransaction.get(i).getAmount();
        }
        return incAmount;
    }

    public boolean addIncomeTransaction(Transaction transaction){
        if(transaction.getActionStatus().equals(transaction.getIncome())){
            this.IncomeTransaction.add(transaction);
            return true;
        }
        else{
            return false;
        }
    }

    public ArrayList<Transaction> getListExpanseByDescription(String description){
        ArrayList <Transaction> incTransactions = new ArrayList <Transaction> ();
        for (int i = 0; i < this.IncomeTransaction.size(); i++){
            if(this.IncomeTransaction.get(i).getDescription().contains(description)){
                incTransactions.add(this.IncomeTransaction.get(i));
            }
        }
        return incTransactions;
    }

}
