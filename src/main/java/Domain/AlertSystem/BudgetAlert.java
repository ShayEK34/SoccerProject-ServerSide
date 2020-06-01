package Domain.AlertSystem;

public class BudgetAlert extends A_AlertPop {

    public BudgetAlert(String content) {
        super(content, "Budget");
    }

    @Override
    public String showAlert() {
        String str= super.showAlert();
        return str;
    }
}
