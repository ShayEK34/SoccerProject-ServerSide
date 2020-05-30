package Domain.BudgetControl;
import java.util.ArrayList; // import just the ArrayList class

/**
 * This class represent the expanse transactions log of the team.
 */
public class ExpenseLog {

    private ArrayList <Transaction> expenseTransaction;

    /**
     * This constructor create new array list that represent the expanse transaction log of the team.
     */
    public ExpenseLog() {
        this.expenseTransaction = new ArrayList <Transaction> ();
    }

    /**
     * This function return the array of the transaction expanse log.
     * @return - The array of the transaction expanse log.
     */
    public ArrayList<Transaction> getExpenseTransaction() {
        return expenseTransaction;
    }

    /**
     * This function return the total number of the transactions in the transaction expanse log.
     * @return - The total number of the transactions in the transaction expanse log.
     */
    public int getNumExpTransaction (){
        return this.expenseTransaction.size();
    }

    /**
     * This function return the total amount of the transaction expanse log.
     * @return - The total amount of the transaction expanse log.
     */
    public double getExpenseAmount(){
        double expAmount=0;
        for(int i = 0; i< this.getNumExpTransaction();i++){
            expAmount = expAmount + this.expenseTransaction.get(i).getAmount();
        }
        return expAmount;
    }

    public boolean addExpanseTransaction(Transaction transaction){
        if(transaction.getActionStatus().equals(transaction.getExpense())){
            this.expenseTransaction.add(transaction);
            return true;
        }
        else{
            return false;
        }
    }

    public ArrayList<Transaction> getListExpanseByDescription(String description){
        ArrayList <Transaction> expTransactions = new ArrayList <Transaction> ();
        for (int i = 0; i < this.expenseTransaction.size(); i++){
            if(this.expenseTransaction.get(i).getDescription().contains(description)){
                expTransactions.add(this.expenseTransaction.get(i));
            }
        }
        return expTransactions;
    }
}

