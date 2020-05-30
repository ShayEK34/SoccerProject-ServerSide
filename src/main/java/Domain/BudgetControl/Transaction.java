package Domain.BudgetControl;

import java.security.InvalidParameterException;
import java.time.LocalDate;

/**
 * This class represent transaction of buying or selling in the team actions
 */
public class Transaction {

    private final String expense = "Expense" ;
    private final String income = "Income";
    private double amount;
    private String description;
    private String actionStatus;
    private LocalDate transactionDate;


    /**
     * The constructor get an amount of the transaction action, description, and status of the action (Income or Expense)
     * and create new transaction with the current date.
     * @param amount - The amount of the transaction action
     * @param description - The description of the transaction action
     * @param actionStatus - The status of the transaction action
     */
    public Transaction(double amount, String description, String actionStatus) {
        if(description == null || actionStatus == null){
            throw new IllegalArgumentException("Parameters can't be null");
        }
        if(amount<=0){
            throw new IllegalArgumentException("amount can't be negative or zero!");
        }
        if(actionStatus.equals(expense) || actionStatus.equals(income)) {
            this.amount = amount;
            this.description = description;
            this.actionStatus = actionStatus;
            this.transactionDate = LocalDate.now();
        }
        else
            throw new InvalidParameterException("The transaction status should be Income or Expense!");
    }

    /**
     * This function return the Income action status of the transaction.
     * @return - The Income action status of the transaction.
     */
    public String getExpense() {
        return expense;
    }

    /**
     * This function return the Expense action status of the transaction.
     * @return - The Expense action status of the transaction.
     */
    public String getIncome() {
        return income;
    }

    /**
     * This function return the amount of the transaction.
     * @return - The amount of the transaction.
     */
    public double getAmount() {
        return amount;
    }

    /**
     * This function return the description of the transaction.
     * @return - The description of the transaction.
     */
    public String getDescription() {
        return description;
    }

    /**
     * This function return the status action of the transaction.
     * @return - The status action of the transaction.
     */
    public String getActionStatus() {
        return actionStatus;
    }

    /**
     * This function return the date of the transaction.
     * @return - The date of the transaction.
     */
    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }
}
