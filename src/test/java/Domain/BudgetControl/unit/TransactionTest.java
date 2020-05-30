package Domain.BudgetControl.unit;

import Domain.BudgetControl.Transaction;
import junit.framework.Assert;
import org.junit.Test;

import java.time.LocalDate;

public class TransactionTest {

    @Test (expected = IllegalArgumentException.class)
    public void TestTransactionConstructorNullDesc(){
        Transaction tran1 = new Transaction(1000,null,"Income");
    }

    @Test (expected = IllegalArgumentException.class)
    public void TestTransactionConstructorNullStatus(){
        Transaction tran2 = new Transaction(1000,"Buy Player",null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void TestTransactionConstructorNegativeAmount(){
        Transaction tran3 = new Transaction(-1000,"Buy coach","Expense");
    }

    @Test (expected = IllegalArgumentException.class)
    public void TestTransactionConstructorZeroAmount(){
        Transaction tran18 = new Transaction(0,"Buy coach","Expense");
    }

    @Test (expected = IllegalArgumentException.class)
    public void TestTransactionConstructorIncorrectStatus(){
        Transaction tran4 = new Transaction(1000,"Buy coach","Buy");
    }

    @Test
    public void TestGetTranAmount(){
        Transaction tran5 = new Transaction(2000, "Sell Player Ronaldo","Income");
        Transaction tran6 = new Transaction(3000, "Buy Player Messi","Expense");
        Assert.assertEquals(tran5.getAmount(),2000.0);
        Assert.assertEquals(tran6.getAmount(),3000.0);
    }

    @Test
    public void TestGetTranDesc(){
        Transaction tran7 = new Transaction(2000, "Sell Player Ronaldo CR7","Income");
        Transaction tran8 = new Transaction(3000, "Buy Player Messi 10","Expense");
        Assert.assertEquals(tran7.getDescription(),"Sell Player Ronaldo CR7");
        Assert.assertEquals(tran8.getDescription(),"Buy Player Messi 10");
    }

    @Test
    public void TestGetTranStatus(){
        Transaction tran9 = new Transaction(7000, "Sell Player Nymar","Income");
        Transaction tran10 = new Transaction(5000, "Buy Player Zlatan","Expense");
        Assert.assertEquals(tran9.getActionStatus(),"Income");
        Assert.assertEquals(tran10.getActionStatus(),"Expense");
    }

    @Test
    public void TestGetTransactionDate(){
        Transaction tran11 = new Transaction(7000, "Sell Player Pogaba","Income");
        Transaction tran12= new Transaction(5000, "Buy Player Aguero","Expense");
        Assert.assertEquals(tran11.getTransactionDate().toString(), LocalDate.now().toString());
        Assert.assertEquals(tran12.getTransactionDate().toString(), LocalDate.now().toString());
    }

    @Test
    public void TestSetAmount(){
        Transaction tran13 = new Transaction(7000, "Sell Player Zehavi","Income");
        tran13.setAmount(5000);
        Assert.assertEquals(tran13.getAmount(),5000.0);
    }

    @Test
    public void TestSetDesc(){
        Transaction tran14 = new Transaction(7000, "Sell Player Raul","Income");
        tran14.setDescription("Sell Player Raul num 7");
        Assert.assertEquals(tran14.getDescription(),"Sell Player Raul num 7");
    }

    @Test
    public void TestGetIncomeFinalStatus(){
        Transaction tran15 = new Transaction(7000, "Sell Player Suarez","Income");
        Assert.assertEquals(tran15.getIncome(),"Income");
    }

    @Test
    public void TestGetExpanseFinalStatus(){
        Transaction tran16 = new Transaction(9000, "Sell Player Salah","Income");
        Assert.assertEquals(tran16.getExpense(),"Expense");
    }






}

