package Domain.BudgetControl;
import java.time.LocalDate;
import java.time.Period;

/**
 * This class represent the quarterly report of the team
 */
public class QuartelyReport {

    private ExpenseLog quarterExpanse;
    private IncomeLog quarterIncome;

    /**
     * This constructor get one expanse log and one income log and create the quarterly report of the team.
     * @param quarterExpanse - The given expanse log.
     * @param quarterIncome - The given income log.
     */
    public QuartelyReport(ExpenseLog quarterExpanse, IncomeLog quarterIncome) {
        if(quarterExpanse == null || quarterIncome == null){
            throw new IllegalArgumentException("Parameters can't be null");
        }
        this.quarterExpanse = quarterExpanse;
        this.quarterIncome = quarterIncome;
    }

    /**
     * This function calculate the total amount profits of the team in the current quarter of the year.
     * @return - The total amount profits of the team in the current quarter of the year.
     */
    public double getQuarterTotalAmount() {
        double totalQuarterAmount = 0;
        for( int i = 0; i< this.quarterIncome.getIncomeTransaction().size();i++){
            if(monthsBetweenDates(LocalDate.now(),this.quarterIncome.getIncomeTransaction().get(i).getTransactionDate())<=3){
                totalQuarterAmount = totalQuarterAmount +this.quarterIncome.getIncomeTransaction().get(i).getAmount();
            }
        }
        for( int i = 0; i< this.quarterExpanse.getExpenseTransaction().size();i++){
            if(monthsBetweenDates(LocalDate.now(),this.quarterExpanse.getExpenseTransaction().get(i).getTransactionDate())<=3){
                totalQuarterAmount = totalQuarterAmount -this.quarterExpanse.getExpenseTransaction().get(i).getAmount();
            }
        }
        return totalQuarterAmount;
    }

    /**
     * This function get two dates and return the number of months between the two dates.
     * @param firstDate - First given date.
     * @param secondDate - Second given date.
     * @return
     */
    public int monthsBetweenDates(LocalDate firstDate, LocalDate secondDate) {
        if (firstDate == null || secondDate == null){
            throw new IllegalArgumentException("Parameters can't be null");
        }
        Period period = Period.between(firstDate, secondDate);
        return period.getMonths()*(-1);
    }

    public ExpenseLog getQuarterExpanse() {
        return quarterExpanse;
    }

    public IncomeLog getQuarterIncome() {
        return quarterIncome;
    }
}
