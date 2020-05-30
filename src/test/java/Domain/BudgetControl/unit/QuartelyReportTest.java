package Domain.BudgetControl.unit;

import Domain.BudgetControl.ExpenseLog;
import Domain.BudgetControl.IncomeLog;
import Domain.BudgetControl.QuartelyReport;
import Domain.BudgetControl.Transaction;
import org.junit.Before;
import org.junit.Test;
import junit.framework.Assert;

import java.time.LocalDate;

public class QuartelyReportTest {

    private Transaction trans1;
    private Transaction trans2;
    private Transaction trans3;
    private Transaction trans4;
    private Transaction trans5;
    private Transaction trans6;
    private Transaction trans7;
    private ExpenseLog expanseLog;
    private IncomeLog incomeLog;
    private QuartelyReport report;


    @Before
    public void initQuartelyReport() {
        trans1 = new Transaction(2000, "Buy Player Gal", "Expense");
        trans2 = new Transaction(3000, "Buy Player David", "Expense");
        trans3 = new Transaction(4000, "Buy Player Messi number 10", "Expense");
        trans4 = new Transaction(5000, "Sell Player Ilay", "Income");
        trans5 = new Transaction(6000, "Sell Player Robert", "Income");
        trans6 = new Transaction(7000, "Buy Player Lionel Messi for barcelona", "Expense");
        trans7 = new Transaction(18000, "Sell Player Raul", "Income");
        expanseLog = new ExpenseLog();
        incomeLog = new IncomeLog();
        this.expanseLog.addExpanseTransaction(trans1);
        this.expanseLog.addExpanseTransaction(trans2);
        this.expanseLog.addExpanseTransaction(trans3);
        this.expanseLog.addExpanseTransaction(trans6);
        this.incomeLog.addIncomeTransaction(trans4);
        this.incomeLog.addIncomeTransaction(trans5);
        this.incomeLog.addIncomeTransaction(trans7);
        report = new QuartelyReport(expanseLog,incomeLog);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testQuartelyReportNull1(){
        QuartelyReport q1 = new QuartelyReport(null,incomeLog);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testQuartelyReportNull2(){
        QuartelyReport q2 = new QuartelyReport(expanseLog,null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testQuartelyReportNull3(){
        QuartelyReport q3 = new QuartelyReport(null,null);
    }

    @Test
    public void testGetQuarterTotalAmount(){
        this.report.getQuarterIncome().getIncomeTransaction().get(0).setTransactionDate(LocalDate.of(2019,7,1));
        this.report.getQuarterIncome().getIncomeTransaction().get(1).setTransactionDate(LocalDate.of(2020,3,1));
        this.report.getQuarterIncome().getIncomeTransaction().get(2).setTransactionDate(LocalDate.of(2020,4,1));
        this.report.getQuarterExpanse().getExpenseTransaction().get(0).setTransactionDate(LocalDate.of(2019,7,2));
        this.report.getQuarterExpanse().getExpenseTransaction().get(1).setTransactionDate(LocalDate.of(2020,3,1));
        this.report.getQuarterExpanse().getExpenseTransaction().get(2).setTransactionDate(LocalDate.of(2020,3,2));
        this.report.getQuarterExpanse().getExpenseTransaction().get(3).setTransactionDate(LocalDate.of(2020,4,3));
        double amount = this.report.getQuarterTotalAmount();
        Assert.assertEquals(amount,10000,0.0001);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testMonthsBetweenDatesNull1(){
        this.report.monthsBetweenDates(LocalDate.now(),null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testMonthsBetweenDatesNull2(){
        this.report.monthsBetweenDates(null,LocalDate.now());
    }
}

