package Domain.BudgetControl.it;

import Domain.AssociationManagement.Season;
import Domain.BudgetControl.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BudgetIT {

    private Season season;
    private Transaction trans1;
    private Transaction trans2;
    private Transaction trans3;
    private Transaction trans4;
    private Transaction trans5;
    private Transaction trans6;
    private Transaction trans7;
    private Budget budget;

    @Before
    public void initBudget(){
        trans1 = new Transaction(2000, "Buy Player Gal", "Expense");
        trans2 = new Transaction(3000, "Buy Player David", "Expense");
        trans3 = new Transaction(4000, "Buy Player Messi number 10", "Expense");
        trans4 = new Transaction(7000, "Buy Player Lionel Messi for barcelona", "Expense");
        trans5 = new Transaction(5000, "Sell Player Ilay", "Income");
        trans6 = new Transaction(6000, "Sell Player Robert", "Income");
        trans7 = new Transaction(18000, "Sell Player Raul", "Income");
        this.season = new Season(2020);
        this.budget = new Budget(1000000,this.season);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testConstructorNegativeBudget(){
        Budget budget1 = new Budget(-1000,this.season);
    }

//    @Test (expected = IllegalArgumentException.class)
//    public void testConstructorZeroBudget(){
//        Budget budget1 = new Budget(0,this.season);
//    }

//    @Test (expected = IllegalArgumentException.class)
////    public void testConstructorNullSeason(){
////        Budget budget1 = new Budget(10000,null);
////    }

    @Test
    public void testAddExpenseTransaction(){
        Assert.assertTrue(this.budget.addExpenseTransaction(trans1));
        Assert.assertTrue(this.budget.addExpenseTransaction(trans2));
        Assert.assertTrue(this.budget.addExpenseTransaction(trans3));
        Assert.assertTrue(this.budget.addExpenseTransaction(trans4));
        Assert.assertFalse(this.budget.addExpenseTransaction(trans5));
    }

    @Test
    public void testAddIncomeTransaction(){
        Assert.assertTrue(this.budget.addIncomeTransaction(trans5));
        Assert.assertTrue(this.budget.addIncomeTransaction(trans6));
        Assert.assertTrue(this.budget.addIncomeTransaction(trans7));
        Assert.assertFalse(this.budget.addIncomeTransaction(trans1));
    }

    @Test (expected = IllegalArgumentException.class)
    public void addNullExpanseTran(){
        this.budget.addExpenseTransaction(null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void addNullIncomeTran(){
        this.budget.addIncomeTransaction(null);
    }

    @Test
    public void testQuartelyReport(){
        this.budget.addExpenseTransaction(trans1);
        this.budget.addExpenseTransaction(trans2);
        this.budget.addExpenseTransaction(trans3);
        this.budget.addExpenseTransaction(trans4);
        this.budget.addIncomeTransaction(trans5);
        this.budget.addIncomeTransaction(trans6);
        this.budget.addIncomeTransaction(trans7);
        QuartelyReport report = this.budget.createQuarterlyReport();
        double profits = report.getQuarterTotalAmount();
        Assert.assertEquals(profits,13000,0.00001);
    }

    @Test
    public void testSetSeasonAmountBudget(){
        this.budget.setSeasonAmountBudget(2000000);
        Assert.assertEquals(this.budget.getSeasonAmountBudget(),2000000,0.000001);
    }

    @Test
    public void testSetSeason(){
        Season season2 = new Season(2021);
        this.budget.setSeason(season2);
        Assert.assertEquals(this.budget.getSeason().getYear(),2021);
    }

    @Test
    public void testSetExpenses(){
        ExpenseLog newExpanseLog = new ExpenseLog();
        Transaction tran8 = new Transaction(5000,"Buy Pogba", "Expense");
        newExpanseLog.addExpanseTransaction(tran8);
        this.budget.setExpenses(newExpanseLog);
        ExpenseLog checkExpanseLog = this.budget.getExpenses();
        Assert.assertEquals(checkExpanseLog.getNumExpTransaction(),1);
        Assert.assertEquals(checkExpanseLog.getExpenseTransaction().get(0).getAmount(),5000,0.00001);
        Assert.assertEquals(checkExpanseLog.getExpenseTransaction().get(0).getDescription(),"Buy Pogba");
    }

    @Test
    public void testSetIncomes(){
        IncomeLog newIncomeLog = new IncomeLog();
        Transaction tran9 = new Transaction(9000,"Sell Materazi", "Income");
        newIncomeLog.addIncomeTransaction(tran9);
        this.budget.setIncomes(newIncomeLog);
        IncomeLog checkIncomeLog = this.budget.getIncomes();
        Assert.assertEquals(checkIncomeLog.getNumIncTransaction(),1);
        Assert.assertEquals(checkIncomeLog.getIncomeTransaction().get(0).getAmount(),9000,0.00001);
        Assert.assertEquals(checkIncomeLog.getIncomeTransaction().get(0).getDescription(),"Sell Materazi");
    }

}

