package Domain.AssociationManagement;

import Domain.BudgetControl.Budget;
import Domain.BudgetControl.Transaction;
import Domain.ClubManagement.TeamInfo;
import Domain.User.AssociationUser;

import java.util.Vector;

/**
 * this class represents the Association football budget and the actions that can be made.
 */
public class BudgetManagement {

    private Vector<AssociationUser> employees;
    private Budget budget;

    /**
     * constructor for budget
     * @param s - the season which the budget refers to
     */
//    public BudgetManagement(Season s){
//        if(s == null){
//            throw new IllegalArgumentException("Season must be initialized");
//        }
//        employees = new Vector<>();
//        budget = new Budget(1000000,s);
//    }

    public BudgetManagement(Season s,double budgetAmount){
        if(s == null){
            throw new IllegalArgumentException("Season must be initialized");
        }
        employees = new Vector<>();
        budget = new Budget(budgetAmount,s);
    }

    /**
     * check if a team has enough budget to register to a certain league
     * @param team
     * @param league
     * @return
     */
    public boolean registerTeamToLeague(TeamInfo team, League league){
        boolean ans = false;
        if (team!=null && league!=null){
            if(team.getTeamSeasonBudget().getSeasonAmountBudget()>=10000 && league.getTeams().size()<league.getNumOfTeams()){
                boolean tmp = league.addTeam(team);
                if (tmp) {
                    team.getTeamSeasonBudget().addExpenseTransaction(new Transaction(10000,"Registration fee","Expense"));
                    budget.addIncomeTransaction(new Transaction(10000,"Registration fee of " + team.getTeamName(), "Income"));
                    ans = true;
                }
            }
        }
        return ans;
    }

//    /**
//     * add extra money from the Toto Organization
//     * @param money
//     */
//    public boolean totoAnnualIncome(double money){
//        if(money>0){
//            budget.addIncomeTransaction(new Transaction(money,"Toto annual income fee", "Income"));
//            return true;
//        }
//        return false;
//    }

//    /**
//     * check if the buying team has enough money to buy a certain player.
//     * @param to
//     * @param transferFee
//     * @return
//     */
//    public boolean isPlayerTransferLegal(TeamInfo to, double transferFee){
//        boolean ans = false;
//        if(to!=null && transferFee > 0){
//            if(to.getTeamSeasonBudget().getSeasonAmountBudget()-transferFee >= 0){
//                ans = true;
//            }
//        }
//        return ans;
//    }

//    /**
//     * pay salaries to association employees and add the expenses to the budget
//     */
//    public void paySalariesToEmployees(){
//        for (int i = 0; i < employees.size(); i++) {
//            budget.addExpenseTransaction(new Transaction(employees.get(i).getSalary(), "Salary to: " + employees.get(i).getFirstName() + " "+ employees.get(i).getLastName(),"Expense"));
//        }
//    }
//
//    /**
//     * check if teams budget is bigger than 0
//     * @param team
//     * @return
//     */
//    public boolean isTeamBudgetOK(TeamInfo team){
//        boolean ans = false;
//        if(team != null){
//            QuartelyReport rep = team.getTeamSeasonBudget().createQuarterlyReport();
//            if(rep.getQuarterTotalAmount()>=0){
//                ans = true;
//            }
//        }
//        return ans;
//    }
//
//
//    /**
//     * go over all teams in all leagues and check if they have enough money to get the season started
//     * if the team doesn't have enough funds the func will print it
//     * otherwise this season will be set to the teams budget
//     * @return
//     */
//    public boolean isAllTeamsBudgetOK(){
//        boolean ans = true;
//        for (int i = 0; i < budget.getSeason().getAllLeagues().size(); i++) {
//            League league = budget.getSeason().getAllLeagues().get(i);
//            for (int j = 0; j < league.getNumOfTeams(); j++) {
//                if(!isTeamBudgetOK(league.getTeams().get(j))){
//                    return false;
//                }
//                else{
//                    league.getTeams().get(j).getTeamSeasonBudget().setSeason(budget.getSeason());
//                }
//            }
//        }
//        return ans;
//    }

    public Vector<AssociationUser> getEmployees() {
        return employees;
    }

    public void setEmployees(Vector<AssociationUser> employees) {
        this.employees = employees;
    }

    public Budget getBudget() {
        return budget;
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
    }

    public boolean addEmployee(AssociationUser associationUser){
        if(associationUser != null){
            employees.add(associationUser);
            return true;
        }
        return false;
    }
}

