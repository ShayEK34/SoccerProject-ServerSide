package Domain.BudgetControl.unit;

import Domain.BudgetControl.ExpenseLog;
import Domain.BudgetControl.Transaction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class ExpenseLogTest {

    private Transaction trans1;
    private Transaction trans2;
    private Transaction trans3;
    private Transaction trans4;
    private Transaction trans5;
    private Transaction trans6;
    private ExpenseLog expanseLog;

    @Before
    public void initExpenseLog() {
        trans1 = new Transaction(2000, "Buy Player Gal", "Expense");
        trans2 = new Transaction(3000, "Buy Player David", "Expense");
        trans3 = new Transaction(4000, "Buy Player Messi number 10", "Expense");
        trans4 = new Transaction(5000, "Sell Player Ilay", "Income");
        trans5 = new Transaction(6000, "Sell Player Robert", "Income");
        trans6 = new Transaction(6000, "Buy Player Lionel Messi for barcelona", "Expense");
        expanseLog = new ExpenseLog();

    }

    @Test
    public void testAddExpanseTransaction(){
        Assert.assertTrue(this.expanseLog.addExpanseTransaction(trans1));
        Assert.assertTrue(this.expanseLog.addExpanseTransaction(trans2));
        Assert.assertTrue(this.expanseLog.addExpanseTransaction(trans3));
        Assert.assertFalse(this.expanseLog.addExpanseTransaction(trans4));
        Assert.assertFalse(this.expanseLog.addExpanseTransaction(trans5));
    }

    @Test
    public void testGetNumExpTransaction(){
        this.expanseLog.addExpanseTransaction(trans1);
        this.expanseLog.addExpanseTransaction(trans2);
        this.expanseLog.addExpanseTransaction(trans3);
        Assert.assertEquals(this.expanseLog.getNumExpTransaction(),3);
    }

    @Test
    public void testGetExpenseAmount(){
        this.expanseLog.addExpanseTransaction(trans1);
        this.expanseLog.addExpanseTransaction(trans2);
        this.expanseLog.addExpanseTransaction(trans3);
        Assert.assertEquals(this.expanseLog.getExpenseAmount(),9000,0.0001);
    }

    @Test
    public void testGetArrayExpense(){
        this.expanseLog.addExpanseTransaction(trans1);
        this.expanseLog.addExpanseTransaction(trans2);
        Assert.assertEquals(this.expanseLog.getExpenseTransaction().size(),2);
    }

    @Test
    public void testGetListExpanseByDescription(){
        this.expanseLog.addExpanseTransaction(trans1);
        this.expanseLog.addExpanseTransaction(trans3);
        this.expanseLog.addExpanseTransaction(trans2);
        this.expanseLog.addExpanseTransaction(trans6);
        ArrayList<Transaction> Transactions = this.expanseLog.getListExpanseByDescription("Messi");
        Assert.assertEquals(Transactions.get(0).getDescription(),"Buy Player Messi number 10");
        Assert.assertEquals(Transactions.get(1).getDescription(),"Buy Player Lionel Messi for barcelona");
    }

}

