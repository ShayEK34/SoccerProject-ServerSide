package Domain.BudgetControl.unit;

import Domain.BudgetControl.IncomeLog;
import Domain.BudgetControl.Transaction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class IncomeLogTest {

    private Transaction trans1;
    private Transaction trans2;
    private Transaction trans3;
    private Transaction trans4;
    private Transaction trans5;
    private Transaction trans6;
    private IncomeLog incomeLog;

    @Before
    public void initExpenseLog() {
        trans1 = new Transaction(2000, "Sell Player Gal", "Income");
        trans2 = new Transaction(3000, "Buy Player David", "Expense");
        trans3 = new Transaction(4000, "Sell Player Ronaldo number 7", "Income");
        trans4 = new Transaction(5000, "Sell Player Ilay", "Income");
        trans5 = new Transaction(6000, "Sell Player Robert", "Expense");
        trans6 = new Transaction(6000, "Sell Player Cristiano Ronaldo for Real Madrid", "Income");
        incomeLog = new IncomeLog();
    }

    @Test
    public void testAddExpanseTransaction(){
        Assert.assertTrue(this.incomeLog.addIncomeTransaction(trans1));
        Assert.assertFalse(this.incomeLog.addIncomeTransaction(trans2));
        Assert.assertTrue(this.incomeLog.addIncomeTransaction(trans3));
        Assert.assertTrue(this.incomeLog.addIncomeTransaction(trans4));
        Assert.assertFalse(this.incomeLog.addIncomeTransaction(trans5));
    }

    @Test
    public void testGetNumExpTransaction(){
        this.incomeLog.addIncomeTransaction(trans1);
        this.incomeLog.addIncomeTransaction(trans3);
        this.incomeLog.addIncomeTransaction(trans4);
        Assert.assertEquals(this.incomeLog.getNumIncTransaction(),3);
    }

    @Test
    public void testGetExpenseAmount(){
        this.incomeLog.addIncomeTransaction(trans1);
        this.incomeLog.addIncomeTransaction(trans3);
        this.incomeLog.addIncomeTransaction(trans4);
        Assert.assertEquals(this.incomeLog.getIncomeAmount(),11000,0.0001);
    }

    @Test
    public void testGetArrayExpense(){
        this.incomeLog.addIncomeTransaction(trans1);
        this.incomeLog.addIncomeTransaction(trans3);
        Assert.assertEquals(this.incomeLog.getIncomeTransaction().size(),2);
    }

    @Test
    public void testGetListExpanseByDescription(){
        this.incomeLog.addIncomeTransaction(trans1);
        this.incomeLog.addIncomeTransaction(trans3);
        this.incomeLog.addIncomeTransaction(trans2);
        this.incomeLog.addIncomeTransaction(trans4);
        this.incomeLog.addIncomeTransaction(trans5);
        this.incomeLog.addIncomeTransaction(trans6);
        ArrayList<Transaction> Transactions = this.incomeLog.getListExpanseByDescription("Ronaldo");
        Assert.assertEquals(Transactions.get(0).getDescription(),"Sell Player Ronaldo number 7");
        Assert.assertEquals(Transactions.get(1).getDescription(),"Sell Player Cristiano Ronaldo for Real Madrid");
    }
}

