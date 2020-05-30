package Domain.AlertSystem;

public class BudgetAlert extends A_AlertPop {

    public BudgetAlert(String content) {
        super(content, "Budget");
    }

    @Override
    public String showAlert() {
        String str= "Budget: "+ super.showAlert();
        return str;
    }
}
