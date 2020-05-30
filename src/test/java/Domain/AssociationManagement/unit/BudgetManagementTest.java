package Domain.AssociationManagement.unit;

import Data.SystemDB.DataBaseInterface;
import Domain.AssociationManagement.BudgetManagement;
import Domain.AssociationManagement.League;
import Domain.AssociationManagement.Season;
import Domain.BudgetControl.Budget;
import Data.SystemDB.DB;
import Domain.User.AssociationUser;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import java.util.Vector;

public class BudgetManagementTest {

    private DataBaseInterface database = DB.getInstance();
    private Vector<AssociationUser> employees;
    private Budget budget;
    private BudgetManagement bm;
    private Season season;
    private League laliga;
    private AssociationUser au;


    @Before
    public void initObjects(){
        database.resetDB();

        season = new Season(2020);
        bm = new BudgetManagement(season,100000);
        au = new AssociationUser("gal","buzaglo","galb","galbb", "ceo","18/07/91","gal@gmail.com");
        bm.addEmployee(au);
        au.setSalary(14000);
    }

    /**
     * tests the constructor
     */
    @Test (expected = IllegalArgumentException.class)
    public void testConstructor(){
        BudgetManagement bmm = new BudgetManagement(null,0);
    }


    /**
     * tests a valid employees set/get
     *
     */
    @Test
    public void testSetGetEmployees(){
        Vector<AssociationUser> tmp = new Vector<>();
        tmp.add(au);
        bm.setEmployees(tmp);
        Vector<AssociationUser> tmp1 = new Vector<>();
        tmp1.add(au);
        Assert.assertEquals(tmp1,bm.getEmployees());
    }

    /**
     * tests a valid Budget set/get
     *
     */
    @Test
    public void testSetGetBudget(){
        Budget bm11 = new Budget(1000000,season);
        bm.setBudget(bm11);
        Assert.assertEquals(bm11,bm.getBudget());
    }

    /**
     * tests a valid adding employees
     *
     */
    @Test
    public void testAddEmployee(){
        AssociationUser au1 = new AssociationUser("dfgsdfg","buzafdsgdfglo","fdggds","gdfgg", "ceoo","18/07/91","galgfdg@gmail.com");
        Assert.assertFalse(bm.addEmployee(null));
        Assert.assertTrue(bm.addEmployee(au1));
    }


//    /**
//     * tests 0 and negative
//     * test checks that the income does add money to budget
//     */
//    @Test
//    public void testTotoAnnualIncome(){
//        Assert.assertFalse(bm.totoAnnualIncome(-500));
//        Assert.assertFalse(bm.totoAnnualIncome(0));
//        Assert.assertTrue(bm.totoAnnualIncome(50000));
//        Assert.assertEquals(bm.getBudget().getSeasonAmountBudget(),1050000,0);
//    }
//
//    /**
//     * test checks that player can be transferred to a team which can buy him
//     */
//    @Test
//    public void testIsPlayerTransfer(){
//        Assert.assertTrue(bm.isPlayerTransferLegal(realMadrid, 10000));
//        Assert.assertFalse(bm.isPlayerTransferLegal(barca, 100000));
//        Assert.assertFalse(bm.isPlayerTransferLegal(barca, -100000));
//        Assert.assertFalse(bm.isPlayerTransferLegal(null, 100000));
//        Assert.assertFalse(bm.isPlayerTransferLegal(barca, 0));
//    }
//
//    /**
//     * tests that association pays to its employees
//     */
//    @Test
//    public void testPaySalaries(){
//        bm.paySalariesToEmployees();
//        double tmp = 986000.0;
//        Assert.assertEquals(bm.getBudget().getSeasonAmountBudget(),tmp,0);
//    }
//
//    /**
//     * tests null
//     * tests a team with valid budget
//     * tests a team with invalid budget
//     */
//    @Test
//    public void testTeamBudgetOK(){
//        Assert.assertFalse(bm.isTeamBudgetOK(null));
//        Assert.assertTrue(bm.isTeamBudgetOK(barca)); //has 10K
//        Assert.assertFalse(bm.isTeamBudgetOK(atletico)); // has -9K
//        atletico.getTeamSeasonBudget().addIncomeTransaction(new Transaction(9000,"tickets income","Income"));
//        Assert.assertTrue(bm.isTeamBudgetOK(atletico));//has 0
//        Assert.assertEquals(atletico.getTeamSeasonBudget().createQuarterlyReport().getQuarterTotalAmount() ,0,0);
//    }
//
//    /**
//     * tests null
//     * tests a team with valid budget
//     * tests a team with invalid budget
//     */
//    @Test
//    public void testAllTeamBudgetOK(){
//        bm.registerTeamToLeague(barca,laliga);
//        bm.registerTeamToLeague(realMadrid,laliga);
//        bm.getBudget().getSeason().addLeagueToSeasonAndSeasonToLeague(laliga);
//        Assert.assertTrue(bm.isAllTeamsBudgetOK());
//        barca.getTeamSeasonBudget().addExpenseTransaction(new Transaction(19000,"racism report","Expense"));
//        Assert.assertFalse(bm.isAllTeamsBudgetOK());
//        Assert.assertEquals(laliga.getTeams().get(0).getTeamSeasonBudget().getSeason(),bm.getBudget().getSeason());
//    }


    @After
    public void resetDB(){
        DB.getInstance().resetDB();
    }





}

