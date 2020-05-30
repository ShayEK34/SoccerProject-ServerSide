package Domain.BudgetControl;
import Domain.AssociationManagement.Season;

/**
 * This class represent the season budget of the team.
 */

public class Budget {

    private double seasonAmountBudget;
    private ExpenseLog expenses;
    private IncomeLog incomes;
    //private QuartelyReport report;
    private Season season;

    /**
     * This constructor get a budget value and season and create a new team budget.
     * @param seasonAmountBudget - The budget amount value.
     * @param season - The season value for budget.
     */
    public Budget(double seasonAmountBudget, Season season) {
        if(seasonAmountBudget<0){
            throw new IllegalArgumentException("Budget can't be negative");
        }
        this.seasonAmountBudget = seasonAmountBudget;
        this.expenses = new ExpenseLog();
        this.incomes = new IncomeLog();
        this.season = season;
    }

    /**
     *This function get buying transaction and add it to expenses log.
     * @param tran - The transaction buying value.
     */
    public boolean addExpenseTransaction (Transaction tran){
        if(tran == null ){
            throw new IllegalArgumentException("Parameters can't be null");
        }
        if(this.expenses.addExpanseTransaction(tran)){
            this.seasonAmountBudget =this.seasonAmountBudget -tran.getAmount();
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * This function get selling transaction and add it to incomes log.
     * @param tran - The transaction selling value.
     */
    public boolean addIncomeTransaction (Transaction tran){
        if(tran == null ){
            throw new IllegalArgumentException("Parameters can't be null");
        }
        if(this.incomes.addIncomeTransaction(tran) && tran.getAmount()>0){
            this.seasonAmountBudget =this.seasonAmountBudget +tran.getAmount();
            return true;
        }
        else
            return false;
    }

    /**
     * This function create quarter report and print the amount profits of the team at this quarter of the year
     */
    public QuartelyReport createQuarterlyReport(){
        QuartelyReport report = new QuartelyReport(this.expenses,this.incomes);
        return report;
    }

    /**
     * This function get amount for the current season budget and set it.
     * @param seasonAmountBudget - The given season budget.
     */
    public void setSeasonAmountBudget(double seasonAmountBudget) {
        this.seasonAmountBudget = seasonAmountBudget;
    }

    /**
     * This function get a season and set it for the season budget.
     * @param season - The given season budget.
     */
    public void setSeason(Season season) {
        this.season = season;
    }

    public double getSeasonAmountBudget() {
        return seasonAmountBudget;
    }

    public ExpenseLog getExpenses() {
        return expenses;
    }

    public IncomeLog getIncomes() {
        return incomes;
    }


    public Season getSeason() {
        return season;
    }

    /**
     * This function get a log of expanses and set it.
     * @param expenses - The given log of expanses.
     */
    public void setExpenses(ExpenseLog expenses) {
        this.expenses = expenses;
    }

    /**
     * This function get a log of incomes and set it.
     * @param incomes - The given log of incomes.
     */
    public void setIncomes(IncomeLog incomes) {
        this.incomes = incomes;
    }

}

